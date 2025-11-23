// Personal Finance Manager - Custom JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);

    // Transaction form enhancements
    initializeTransactionForm();

    // Dashboard interactions
    initializeDashboard();

    // Form validation enhancements
    initializeFormValidation();
});

// Transaction Form Functions
function initializeTransactionForm() {
    const transactionTypeSelect = document.getElementById('type');
    const categorySelect = document.getElementById('categoryId');

    if (transactionTypeSelect && categorySelect) {
        transactionTypeSelect.addEventListener('change', function() {
            updateCategoryOptions(this.value);
        });

        // Initialize category options based on default type
        updateCategoryOptions(transactionTypeSelect.value);
    }
}

function updateCategoryOptions(transactionType) {
    const categorySelect = document.getElementById('categoryId');
    if (!categorySelect) return;

    // This would typically be populated via AJAX call to the server
    // For now, we'll show/hide based on data attributes
    const options = categorySelect.querySelectorAll('option');
    options.forEach(option => {
        if (option.dataset.type && option.dataset.type !== transactionType.toLowerCase()) {
            option.style.display = 'none';
        } else {
            option.style.display = 'block';
        }
    });

    // Clear selection if hidden option was selected
    if (categorySelect.selectedOptions[0] && categorySelect.selectedOptions[0].style.display === 'none') {
        categorySelect.selectedIndex = 0;
    }
}

// Dashboard Functions
function initializeDashboard() {
    // Animate numbers on dashboard
    animateValue('balance-amount', 0, parseFloat(document.getElementById('balance-amount')?.textContent.replace(/[^0-9.-]+/g, '') || 0), 1000);
    animateValue('income-amount', 0, parseFloat(document.getElementById('income-amount')?.textContent.replace(/[^0-9.-]+/g, '') || 0), 1000);
    animateValue('expense-amount', 0, parseFloat(document.getElementById('expense-amount')?.textContent.replace(/[^0-9.-]+/g, '') || 0), 1000);

    // Initialize chart data loading
    loadDashboardCharts();
}

function animateValue(id, start, end, duration) {
    const element = document.getElementById(id);
    if (!element) return;

    const range = end - start;
    const increment = range / (duration / 16);
    let current = start;

    const timer = setInterval(function() {
        current += increment;
        if ((increment > 0 && current >= end) || (increment < 0 && current <= end)) {
            current = end;
            clearInterval(timer);
        }
        element.textContent = '$' + current.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }, 16);
}

function loadDashboardCharts() {
    // This would typically load chart data via AJAX
    console.log('Loading dashboard charts...');

    // Example of how you might load spending by category chart
    fetch('/api/spending-by-category')
        .then(response => response.json())
        .then(data => {
            renderSpendingChart(data);
        })
        .catch(error => {
            console.log('Could not load chart data:', error);
        });
}

function renderSpendingChart(data) {
    // This would render a chart using a library like Chart.js
    console.log('Rendering spending chart with data:', data);
}

// Form Validation
function initializeFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');

    Array.from(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }

            form.classList.add('was-validated');
        }, false);
    });

    // Password strength indicator
    const passwordInputs = document.querySelectorAll('input[type="password"][id*="password"]');
    passwordInputs.forEach(input => {
        input.addEventListener('input', function() {
            checkPasswordStrength(this.value, this.id + '-strength');
        });
    });
}

function checkPasswordStrength(password, strengthId) {
    const strengthElement = document.getElementById(strengthId);
    if (!strengthElement) return;

    let strength = 0;
    let feedback = '';

    if (password.length >= 8) strength++;
    if (password.length >= 12) strength++;
    if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
    if (/\d/.test(password)) strength++;
    if (/[^a-zA-Z\d]/.test(password)) strength++;

    const strengthLevels = ['Very Weak', 'Weak', 'Fair', 'Good', 'Strong'];
    const strengthColors = ['#dc3545', '#fd7e14', '#ffc107', '#28a745', '#007bff'];

    strengthElement.textContent = strengthLevels[strength] || 'Very Weak';
    strengthElement.style.color = strengthColors[strength] || '#dc3545';
}

// Utility Functions
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

function formatDate(date) {
    return new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    }).format(new Date(date));
}

function showAlert(message, type = 'info') {
    const alertContainer = document.querySelector('.container');
    if (!alertContainer) return;

    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    alertContainer.insertBefore(alertDiv, alertContainer.firstChild);

    // Auto-hide after 5 seconds
    setTimeout(() => {
        const alert = new bootstrap.Alert(alertDiv);
        alert.close();
    }, 5000);
}

// Table Functions
function confirmDelete(itemId, itemName) {
    return confirm(`Are you sure you want to delete "${itemName}"? This action cannot be undone.`);
}

function sortTable(tableId, column) {
    const table = document.getElementById(tableId);
    if (!table) return;

    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr'));

    const sortedRows = rows.sort((a, b) => {
        const aValue = a.cells[column].textContent.trim();
        const bValue = b.cells[column].textContent.trim();

        // Try to sort as numbers first
        const aNum = parseFloat(aValue.replace(/[^0-9.-]+/g, ''));
        const bNum = parseFloat(bValue.replace(/[^0-9.-]+/g, ''));

        if (!isNaN(aNum) && !isNaN(bNum)) {
            return aNum - bNum;
        }

        return aValue.localeCompare(bValue);
    });

    tbody.innerHTML = '';
    sortedRows.forEach(row => tbody.appendChild(row));
}

// AJAX Functions
function fetchWithFallback(url, options = {}) {
    return fetch(url, options)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .catch(error => {
            console.error('Fetch error:', error);
            showAlert('An error occurred while fetching data. Please try again.', 'danger');
            throw error;
        });
}

// Local Storage Functions
function saveToLocalStorage(key, data) {
    try {
        localStorage.setItem(key, JSON.stringify(data));
    } catch (error) {
        console.error('Error saving to localStorage:', error);
    }
}

function loadFromLocalStorage(key) {
    try {
        const data = localStorage.getItem(key);
        return data ? JSON.parse(data) : null;
    } catch (error) {
        console.error('Error loading from localStorage:', error);
        return null;
    }
}

// Theme Functions (for future dark mode implementation)
function toggleTheme() {
    const body = document.body;
    const currentTheme = body.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';

    body.setAttribute('data-theme', newTheme);
    saveToLocalStorage('theme', newTheme);
}

function loadTheme() {
    const savedTheme = loadFromLocalStorage('theme') || 'light';
    document.body.setAttribute('data-theme', savedTheme);
}

// Initialize theme on page load
loadTheme();

// Export functions for use in other scripts
window.FinanceManager = {
    formatCurrency,
    formatDate,
    showAlert,
    confirmDelete,
    sortTable,
    fetchWithFallback,
    saveToLocalStorage,
    loadFromLocalStorage,
    toggleTheme
};