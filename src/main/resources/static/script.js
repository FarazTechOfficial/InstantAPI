document.addEventListener('DOMContentLoaded', function () {

    const SUPPORTED_TYPES = ['String', 'Integer', 'Long', 'Double', 'Float', 'Boolean', 'LocalDate'];

    let currentFields = [];
    let currentServiceName = '';

    // Tab switching
    const tabs = document.querySelectorAll('.tab');
    const tabContents = document.querySelectorAll('.tab-content');
    const manualSection = document.getElementById('manual-section');
    const previewSection = document.getElementById('preview-section');

    tabs.forEach(function (tab) {
        tab.addEventListener('click', function () {
            tabs.forEach(function (t) { t.classList.remove('active'); });
            tab.classList.add('active');
            tabContents.forEach(function (c) { c.classList.remove('active'); });
            document.getElementById(tab.dataset.tab + '-tab').classList.add('active');

            if (tab.dataset.tab === 'manual') {
                manualSection.classList.remove('hidden');
                previewSection.classList.add('hidden');
                document.getElementById('explain-section').classList.add('hidden');
                if (document.getElementById('manual-fields').children.length <= 1) {
                    addFieldRow();
                }
            }
        });
    });

    // AI generate
    document.getElementById('ai-generate-btn').addEventListener('click', function () {
        var prompt = document.getElementById('ai-prompt').value.trim();
        if (!prompt) return;

        show('ai-loading');
        hide('ai-error');

        fetch('/api/ai/understand', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ prompt: prompt })
        })
        .then(function (res) {
            if (!res.ok) throw new Error('AI request failed');
            return res.json();
        })
        .then(function (data) {
            hide('ai-loading');
            currentServiceName = data.serviceName || '';
            currentFields = (data.parameters || []).map(function (p) {
                return { name: p.name, dataType: p.dataType };
            });
            showPreview();
        })
        .catch(function (err) {
            hide('ai-loading');
            showError('ai-error', 'Something went wrong. Make sure the AI service is configured.');
        });
    });

    // Manual add field
    document.getElementById('add-field-btn').addEventListener('click', function () {
        addFieldRow();
    });

    // Preview add field
    document.getElementById('preview-add-field-btn').addEventListener('click', function () {
        addPreviewFieldRow();
    });

    // Generate project
    document.getElementById('generate-btn').addEventListener('click', function () {
        var serviceName, fields;

        if (!previewSection.classList.contains('hidden')) {
            serviceName = currentServiceName;
            fields = collectPreviewFields();
        } else {
            serviceName = document.getElementById('service-name').value.trim();
            fields = collectManualFields();
        }

        if (!serviceName) { showError('gen-error', 'Service name is required.'); return; }
        if (!fields || fields.length === 0) { showError('gen-error', 'Add at least one field.'); return; }

        hide('gen-error');
        show('gen-loading');

        fetch('/api/generate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ serviceName: serviceName, parameters: fields })
        })
        .then(function (res) {
            if (!res.ok) throw new Error('Generation failed');
            return res.blob();
        })
        .then(function (blob) {
            hide('gen-loading');
            var url = URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.href = url;
            a.download = serviceName + 'API.zip';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
            showExplanation(serviceName, fields);
        })
        .catch(function (err) {
            hide('gen-loading');
            showError('gen-error', 'Something went wrong while generating the project.');
        });
    });

    // AI explanation
    document.getElementById('explain-btn').addEventListener('click', function () {
        var question = document.getElementById('explain-input').value.trim();
        if (!question) return;

        show('explain-loading');
        hide('explain-result');

        var context = 'API for ' + currentServiceName + ' with fields: ' +
            currentFields.map(function (f) { return f.name + ' (' + f.dataType + ')'; }).join(', ');

        fetch('/api/ai/explain', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ prompt: question, serviceName: currentServiceName, fields: currentFields })
        })
        .then(function (res) {
            if (!res.ok) throw new Error('Explain failed');
            return res.json();
        })
        .then(function (data) {
            hide('explain-loading');
            var el = document.getElementById('explain-result');
            el.textContent = data.answer || data.explanation || 'No answer available.';
            el.classList.remove('hidden');
        })
        .catch(function (err) {
            hide('explain-loading');
            showError('gen-error', 'Could not get an explanation.');
        });
    });

    function showPreview() {
        tabs.forEach(function (t) { t.classList.remove('active'); });
        tabContents.forEach(function (c) { c.classList.remove('active'); });
        document.querySelector('[data-tab="manual"]').classList.add('active');
        document.getElementById('manual-tab').classList.add('active');

        manualSection.classList.add('hidden');
        previewSection.classList.remove('hidden');

        document.getElementById('preview-service-name').textContent = currentServiceName;

        var container = document.getElementById('preview-fields');
        container.innerHTML = '';

        currentFields.forEach(function (f, i) {
            var row = document.createElement('div');
            row.className = 'preview-field';

            var check = document.createElement('span');
            check.className = 'preview-check';
            check.textContent = '\u2713';

            var select = document.createElement('select');
            select.className = 'preview-type';
            SUPPORTED_TYPES.forEach(function (t) {
                var opt = document.createElement('option');
                opt.value = t;
                opt.textContent = t;
                if (t === f.dataType) opt.selected = true;
                select.appendChild(opt);
            });

            var nameInput = document.createElement('input');
            nameInput.type = 'text';
            nameInput.className = 'preview-name-text';
            nameInput.value = f.name || '';

            var removeBtn = document.createElement('button');
            removeBtn.className = 'remove-btn';
            removeBtn.textContent = '\u00d7';
            removeBtn.addEventListener('click', function () {
                row.remove();
            });

            row.appendChild(check);
            row.appendChild(select);
            row.appendChild(nameInput);
            row.appendChild(removeBtn);
            container.appendChild(row);
        });
    }

    function showExplanation(name, fields) {
        currentServiceName = name;
        currentFields = fields;
        document.getElementById('explain-section').classList.remove('hidden');
    }

    function addFieldRow() {
        var container = document.getElementById('manual-fields');
        var row = buildFieldRow();
        container.appendChild(row);
    }

    function addPreviewFieldRow() {
        var container = document.getElementById('preview-fields');
        var row = document.createElement('div');
        row.className = 'preview-field';

        var check = document.createElement('span');
        check.className = 'preview-check';
        check.textContent = '\u2713';

        var select = document.createElement('select');
        select.className = 'preview-type';
        SUPPORTED_TYPES.forEach(function (t) {
            var opt = document.createElement('option');
            opt.value = t;
            opt.textContent = t;
            select.appendChild(opt);
        });

        var nameInput = document.createElement('input');
        nameInput.type = 'text';
        nameInput.className = 'preview-name-text';
        nameInput.placeholder = 'fieldName';

        var removeBtn = document.createElement('button');
        removeBtn.className = 'remove-btn';
        removeBtn.textContent = '\u00d7';
        removeBtn.addEventListener('click', function () {
            row.remove();
        });

        row.appendChild(check);
        row.appendChild(select);
        row.appendChild(nameInput);
        row.appendChild(removeBtn);
        container.appendChild(row);
    }

    function buildFieldRow() {
        var row = document.createElement('div');
        row.className = 'field-row';

        var select = document.createElement('select');
        SUPPORTED_TYPES.forEach(function (t) {
            var opt = document.createElement('option');
            opt.value = t;
            opt.textContent = t;
            select.appendChild(opt);
        });

        var input = document.createElement('input');
        input.type = 'text';
        input.placeholder = 'fieldName';

        var removeBtn = document.createElement('button');
        removeBtn.className = 'remove-btn';
        removeBtn.textContent = '\u00d7';
        removeBtn.addEventListener('click', function () {
            row.remove();
        });

        row.appendChild(select);
        row.appendChild(input);
        row.appendChild(removeBtn);
        return row;
    }

    function collectManualFields() {
        var rows = document.querySelectorAll('#manual-fields .field-row:not(.header-row)');
        var fields = [];
        rows.forEach(function (row) {
            var select = row.querySelector('select');
            var input = row.querySelector('input');
            if (input && input.value.trim()) {
                fields.push({ name: input.value.trim(), dataType: select.value });
            }
        });
        return fields;
    }

    function collectPreviewFields() {
        var fields = [];
        var rows = document.querySelectorAll('#preview-fields .preview-field');
        rows.forEach(function (row) {
            var typeEl = row.querySelector('.preview-type');
            var nameEl = row.querySelector('.preview-name-text');
            if (nameEl && nameEl.value.trim()) {
                fields.push({ name: nameEl.value.trim(), dataType: typeEl.value });
            }
        });
        return fields;
    }

    function show(id) { document.getElementById(id).classList.remove('hidden'); }
    function hide(id) { document.getElementById(id).classList.add('hidden'); }
    function showError(id, msg) {
        var el = document.getElementById(id);
        el.textContent = msg;
        el.classList.remove('hidden');
    }
});
