#!/bin/bash
# One-time setup for InstantAPI on an Oracle Cloud Always Free VM (Oracle Linux 8/9).
# Run as:  sudo bash deploy/oracle.sh
# After it finishes, you still need to open port 8080 in the Oracle console security list.

set -e

APP_DIR=/opt/instantapi
REPO_URL=https://github.com/FarazTechOfficial/InstantAPI.git
GROQ_KEY="${GROQ_API_KEY:-REPLACE_WITH_YOUR_KEY}"

echo "==> Installing Java 17, Maven and git"
sudo dnf install -q -y git maven java-17-openjdk

echo "==> Cloning the project"
sudo mkdir -p "$APP_DIR"
if [ ! -d "$APP_DIR/InstantAPI" ]; then
  sudo git clone "$REPO_URL" "$APP_DIR/InstantAPI"
else
  sudo git -C "$APP_DIR/InstantAPI" pull
fi

echo "==> Building the jar"
cd "$APP_DIR/InstantAPI"
sudo mvn -q -DskipTests -f instantapi/pom.xml package

echo "==> Writing systemd service"
sudo tee /etc/systemd/system/instantapi.service > /dev/null <<EOF
[Unit]
Description=InstantAPI Spring Boot app
After=network.target

[Service]
WorkingDirectory=$APP_DIR/InstantAPI/instantapi
ExecStart=/usr/bin/java -jar $APP_DIR/InstantAPI/instantapi/target/instantapi-1.0.0.jar --server.port=8080
Environment=GROQ_API_KEY=$GROQ_KEY
Restart=on-failure
User=opc

[Install]
WantedBy=multi-user.target
EOF

echo "==> Opening port 8080 in the firewall"
sudo firewall-cmd --permanent --add-port=8080/tcp 2>/dev/null || true
sudo firewall-cmd --reload 2>/dev/null || true

echo "==> Enabling and starting the service"
sudo systemctl daemon-reload
sudo systemctl enable instantapi
sudo systemctl start instantapi
sleep 5
sudo systemctl status instantapi --no-pager

echo ""
echo "==============================================================="
echo " Done. Next steps:"
echo "  1. In the Oracle console, open INGRESS TCP 8080 from 0.0.0.0/0"
echo "     (Virtual Cloud Networks > your VCN > Security Lists > default)"
echo "  2. Your app will be live at:  http://<PUBLIC_IP>:8080"
echo "  3. If you left GROQ_API_KEY as REPLACE_WITH_YOUR_KEY:"
echo "        sudo vi /etc/systemd/system/instantapi.service"
echo "        sudo systemctl daemon-reload && sudo systemctl restart instantapi"
echo "==============================================================="