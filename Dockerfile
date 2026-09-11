FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/instantapi-1.0.0.jar app.jar
EXPOSE 8080
CMD java -jar app.jar --server.port=${PORT:-8080}
