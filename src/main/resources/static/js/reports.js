document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, initializing charts...');

    // Modern color palettes
    const expenseColors = [
        '#ef4444', '#f97316', '#f59e0b', '#eab308', '#84cc16',
        '#22c55e', '#10b981', '#14b8a6', '#06b6d4', '#0ea5e9',
        '#3b82f6', '#6366f1', '#8b5cf6', '#a855f7', '#d946ef',
        '#ec4899', '#f43f5e', '#fb7185', '#fda4af', '#fecaca'
    ];

    const incomeColors = [
        '#10b981', '#059669', '#047857', '#065f46', '#064e3b',
        '#22c55e', '#16a34a', '#15803d', '#166534', '#14532d',
        '#84cc16', '#65a30d', '#4d7c0f', '#365314', '#1a2e05',
        '#34d399', '#6ee7b7', '#9ca3af', '#d1d5db', '#e5e7eb'
    ];

    // Enhanced chart configuration
    const getChartConfig = (type) => ({
        responsive: true,
        maintainAspectRatio: false,
        cutout: '60%',
        plugins: {
            legend: {
                position: 'right',
                align: 'center',
                labels: {
                    padding: 15,
                    usePointStyle: true,
                    pointStyle: 'circle',
                    font: {
                        size: 12,
                        weight: '500',
                        family: 'Inter'
                    },
                    color: '#64748b',
                    generateLabels: function(chart) {
                        const data = chart.data;
                        if (data.labels.length && data.datasets.length) {
                            const dataset = data.datasets[0];
                            const total = dataset.data.reduce((a, b) => a + b, 0);

                            return data.labels.map((label, i) => {
                                const value = dataset.data[i];
                                const percentage = ((value / total) * 100).toFixed(1);
                                const formattedValue = new Intl.NumberFormat('en-US', {
                                    style: 'currency',
                                    currency: 'USD'
                                }).format(value);

                                return {
                                    text: `${label} (${percentage}%)`,
                                    fillStyle: dataset.backgroundColor[i],
                                    strokeStyle: dataset.backgroundColor[i],
                                    lineWidth: 0,
                                    pointStyle: 'circle',
                                    hidden: false,
                                    index: i
                                };
                            });
                        }
                        return [];
                    }
                }
            },
            tooltip: {
                backgroundColor: 'rgba(15, 23, 42, 0.95)',
                titleColor: '#f8fafc',
                bodyColor: '#f8fafc',
                borderColor: 'rgba(148, 163, 184, 0.2)',
                borderWidth: 1,
                cornerRadius: 12,
                padding: 16,
                titleFont: {
                    size: 14,
                    weight: '600'
                },
                bodyFont: {
                    size: 13,
                    weight: '500'
                },
                callbacks: {
                    label: function(context) {
                        const label = context.label || '';
                        const value = new Intl.NumberFormat('en-US', {
                            style: 'currency',
                            currency: 'USD'
                        }).format(context.parsed);
                        const total = context.dataset.data.reduce((a, b) => a + b, 0);
                        const percentage = ((context.parsed / total) * 100).toFixed(1);
                        return `${label}: ${value} (${percentage}%)`;
                    }
                }
            }
        },
        animation: {
            animateScale: true,
            animateRotate: true,
            duration: 1500,
            easing: 'easeOutCubic'
        },
        interaction: {
            intersect: false,
            mode: 'index'
        },
        onHover: (event, activeElements, chart) => {
            event.native.target.style.cursor = activeElements.length > 0 ? 'pointer' : 'default';
        }
    });

    // Initialize charts with enhanced functionality
    window.initializeCharts = function(expenseData, incomeData) {
        console.log('Initializing charts with data:', { expenseData, incomeData });

        // Coerce values to numbers and validate data
        const normalizeValues = (arr) => {
            if (!arr || !Array.isArray(arr)) return [];
            return arr.map(v => {
                const num = Number(v);
                return isNaN(num) ? 0 : Math.abs(num); // Ensure positive values for charts
            }).filter(v => v > 0); // Remove zero values
        };

        const normalizedExpenseValues = normalizeValues(expenseData?.values);
        const normalizedIncomeValues = normalizeValues(incomeData?.values);

        // Filter out labels with zero values
        const filterDataWithLabels = (labels, values) => {
            const filtered = { labels: [], values: [] };
            if (!labels || !values) return filtered;

            for (let i = 0; i < Math.min(labels.length, values.length); i++) {
                if (values[i] > 0) {
                    filtered.labels.push(labels[i]);
                    filtered.values.push(values[i]);
                }
            }
            return filtered;
        };

        const filteredExpenseData = filterDataWithLabels(expenseData?.labels, normalizedExpenseValues);
        const filteredIncomeData = filterDataWithLabels(incomeData?.labels, normalizedIncomeValues);

        // Initialize expense chart
        const expenseCanvas = document.getElementById('expenseChart');
        if (expenseCanvas) {
            console.log('Creating expense chart with filtered data:', filteredExpenseData);

            if (filteredExpenseData.labels.length > 0 && filteredExpenseData.values.length > 0) {
                const ctx = expenseCanvas.getContext('2d');
                const expenseChart = new Chart(ctx, {
                    type: 'doughnut',
                    data: {
                        labels: filteredExpenseData.labels,
                        datasets: [{
                            data: filteredExpenseData.values,
                            backgroundColor: expenseColors.slice(0, filteredExpenseData.labels.length),
                            borderWidth: 2,
                            borderColor: '#ffffff',
                            hoverBorderWidth: 4,
                            hoverBorderColor: '#ffffff',
                            hoverBackgroundColor: expenseColors.slice(0, filteredExpenseData.labels.length).map(color => color + 'CC')
                        }]
                    },
                    options: getChartConfig('expense')
                });

                // Add click handler for drill-down functionality
                expenseChart.onClick = (event, elements) => {
                    if (elements.length > 0) {
                        const index = elements[0].index;
                        const category = filteredExpenseData.labels[index];
                        console.log(`Clicked on expense category: ${category}`);
                        // Could add navigation to detailed view here
                    }
                };

                console.log('Expense chart created successfully');
            } else {
                console.log('No expense data available');
                showNoDataMessage(expenseCanvas, 'No Expense Data', 'No expense transactions found for this period');
            }
        }

        // Initialize income chart
        const incomeCanvas = document.getElementById('incomeChart');
        if (incomeCanvas) {
            console.log('Creating income chart with filtered data:', filteredIncomeData);

            if (filteredIncomeData.labels.length > 0 && filteredIncomeData.values.length > 0) {
                const ctx = incomeCanvas.getContext('2d');
                const incomeChart = new Chart(ctx, {
                    type: 'doughnut',
                    data: {
                        labels: filteredIncomeData.labels,
                        datasets: [{
                            data: filteredIncomeData.values,
                            backgroundColor: incomeColors.slice(0, filteredIncomeData.labels.length),
                            borderWidth: 2,
                            borderColor: '#ffffff',
                            hoverBorderWidth: 4,
                            hoverBorderColor: '#ffffff',
                            hoverBackgroundColor: incomeColors.slice(0, filteredIncomeData.labels.length).map(color => color + 'CC')
                        }]
                    },
                    options: getChartConfig('income')
                });

                // Add click handler for drill-down functionality
                incomeChart.onClick = (event, elements) => {
                    if (elements.length > 0) {
                        const index = elements[0].index;
                        const category = filteredIncomeData.labels[index];
                        console.log(`Clicked on income category: ${category}`);
                        // Could add navigation to detailed view here
                    }
                };

                console.log('Income chart created successfully');
            } else {
                console.log('No income data available');
                showNoDataMessage(incomeCanvas, 'No Income Data', 'No income transactions found for this period');
            }
        }
    };

    function showNoDataMessage(canvas, title, message) {
        const container = canvas.parentElement;
        container.innerHTML = `
            <div class="no-data-state">
                <div class="no-data-icon">
                    <i class="fas fa-chart-pie"></i>
                </div>
                <div class="no-data-title">${title}</div>
                <div class="no-data-text">${message}</div>
            </div>
        `;
    }

    // Add loading state functionality
    window.showLoadingState = function() {
        const charts = document.querySelectorAll('.chart-container');
        charts.forEach(container => {
            container.innerHTML = `
                <div class="loading-state">
                    <div class="loading-spinner">
                        <i class="fas fa-spinner fa-spin"></i>
                    </div>
                    <div class="loading-text">Loading chart data...</div>
                </div>
            `;
        });
    };

    // Add chart resize handler for responsiveness
    window.addEventListener('resize', debounce(() => {
        Chart.helpers.each(Chart.instances, (instance) => {
            instance.resize();
        });
    }, 300));

    // Debounce utility function
    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    // Add print functionality
    window.printReport = function() {
        window.print();
    };

    // Add chart data export functionality
    window.exportChartData = function(chartType) {
        const chartCanvas = document.getElementById(`${chartType}Chart`);
        if (!chartCanvas) return;

        const chart = Chart.getChart(chartCanvas);
        if (!chart) return;

        const data = chart.data;
        let csv = 'Category,Amount\n';

        data.labels.forEach((label, index) => {
            const amount = data.datasets[0].data[index];
            csv += `"${label}","${amount}"\n`;
        });

        const blob = new Blob([csv], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${chartType}-chart-data.csv`;
        a.click();
        window.URL.revokeObjectURL(url);
    };
});
