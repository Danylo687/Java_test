const contentDiv = document.getElementById('content');

function showProducts() {
    contentDiv.innerHTML = `
        <h1>Продукти</h1>
        <div id="product-list">
            </div>
        <button onclick="showAddProductForm()">Додати продукт</button>
        <div id="add-product-form" style="display:none;">
            </div>
    `;
    fetchProducts();
    // Assuming the form is already present in products.html or loaded differently
    const addProductForm = document.getElementById('add-product-form');
    if (addProductForm) {
        addProductForm.addEventListener('submit', handleAddProduct);
    }
}

function showCustomers() {
    fetch('customers.html')
        .then(response => response.text())
        .then(html => {
            contentDiv.innerHTML = html;
            fetchCustomers();
            document.getElementById('add-customer-form').addEventListener('submit', handleAddCustomer);
        });
}

function showCategories() {
    fetch('categories.html')
        .then(response => response.text())
        .then(html => {
            contentDiv.innerHTML = html;
            fetchCategories();
            document.getElementById('add-category-form').addEventListener('submit', handleAddCategory);
        });
}

function showAddProductForm() {
    const addProductForm = document.getElementById('add-product-form');
    addProductForm.style.display = 'block';
    fetchCategoriesForDropdown(); // Populate dropdown when form is shown
}

function showAddCustomerForm() {
    document.getElementById('add-customer-form').style.display = 'block';
}

function showAddCategoryForm() {
    document.getElementById('add-category-form').style.display = 'block';
}

// Функції для продуктів
async function fetchProducts() {
    try {
        const response = await fetch('/api/products');
        const products = await response.json();
        let productListHTML = '<ul>';
        products.forEach(product => {
            productListHTML += `<li>${product.name} (${product.category ? product.category.name : 'N/A'}) - ${product.price} грн <button onclick="handleDeleteProduct(${product.id})">Видалити</button></li>`;
        });
        productListHTML += '</ul>';
        document.getElementById('product-list').innerHTML = productListHTML;
    } catch (error) {
        console.error(`Помилка завантаження продуктів: ${error.message}`);
    }
}

async function handleAddProduct(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);

    const categoryId = parseInt(formData.get('category'), 10);
    const productData = {
        name: formData.get('name'),
        price: parseFloat(formData.get('price')),
        producer: formData.get('producer'),
        CountryOfOrigin: formData.get('CountryOfOrigin'),
        weight: parseFloat(formData.get('weight')),
        description: formData.get('description'),
        category: { id: categoryId } // Send category as an object with only the ID
    };

    try {
        const response = await fetch('/api/products', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(productData),
        });

        if (response.ok) {
            fetchProducts();
            form.reset();
            form.style.display = 'none';
        } else {
            console.error('Помилка при додаванні продукту:', response.statusText);
            const errorText = await response.text();
            console.error('Помилка від сервера:', errorText);
            alert(`Помилка при додаванні продукту: ${response.statusText}`);
        }
    } catch (error) {
        console.error('Помилка:', error);
    }
}

async function handleDeleteProduct(id) {
    if (confirm('Ви впевнені, що хочете видалити цей продукт?')) {
        try {
            const response = await fetch(`/api/products/${id}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                fetchProducts();
            } else {
                console.error('Помилка при видаленні продукту:', response.statusText);
            }
        } catch (error) {
            console.error('Помилка:', error);
        }
    }
}


// Функції для клієнтів
async function fetchCustomers() {
    try {
        const response = await fetch('/api/customers');
        const customers = await response.json();
        let customerListHTML = '<ul>';
        customers.forEach(customer => {
            customerListHTML += `<li>${customer.name} (${customer.email}) <button onclick="handleDeleteCustomer(${customer.id})">Видалити</button></li>`;
        });
        customerListHTML += '</ul>';
        document.getElementById('customer-list').innerHTML = customerListHTML;
    } catch (error) {
        console.error('Помилка при завантаженні клієнтів:', error);
    }
}

async function handleAddCustomer(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const customerData = Object.fromEntries(formData.entries());

    try {
        const response = await fetch('/api/customers', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(customerData),
        });

        if (response.ok) {
            fetchCustomers();
            form.reset();
            form.style.display = 'none';
        } else {
            console.error('Помилка при додаванні клієнта:', response.statusText);
        }
    } catch (error) {
        console.error('Помилка:', error);
    }
}

async function handleDeleteCustomer(id) {
    if (confirm('Ви впевнені, що хочете видалити цього клієнта?')) {
        try {
            const response = await fetch(`/api/customers/${id}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                fetchCustomers();
            } else {
                console.error('Помилка при видаленні клієнта:', response.statusText);
            }
        } catch (error) {
            console.error('Помилка:', error);
        }
    }
}

// Функції для категорій
async function fetchCategories() {
    try {
        const response = await fetch('/api/categories'); // Припустимо, що у вас є такий ендпоінт
        const categories = await response.json();
        let categoryListHTML = '<ul>';
        categories.forEach(category => {
            categoryListHTML += `<li>${category.name} <button onclick="handleDeleteCategory(${category.id})">Видалити</button></li>`;
        });
        categoryListHTML += '</ul>';
        document.getElementById('category-list').innerHTML = categoryListHTML;
    } catch (error) {
        console.error('Помилка при завантаженні категорій:', error);
    }
}

async function fetchCategoriesForDropdown() {
    try {
        const response = await fetch('/api/categories'); // Assuming this endpoint exists
        const categories = await response.json();
        const categorySelect = document.getElementById('category');

        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.id;
            option.textContent = category.name;
            categorySelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error fetching categories:', error);
    }
}

async function handleAddCategory(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const categoryData = Object.fromEntries(formData.entries());

    try {
        const response = await fetch('/api/categories', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(categoryData),
        });

        if (response.ok) {
            fetchCategories();
            form.reset();
            form.style.display = 'none';
        } else {
            console.error('Помилка при додаванні категорії:', response.statusText);
        }
    } catch (error) {
        console.error('Помилка:', error);
    }
}

async function handleDeleteCategory(id) {
    if (confirm('Ви впевнені, що хочете видалити цю категорію?')) {
        try {
            const response = await fetch(`/api/categories/${id}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                fetchCategories();
            } else {
                console.error('Помилка при видаленні категорії:', response.statusText);
            }
        } catch (error) {
            console.error('Помилка:', error);
        }
    }
}

// Завантаження початкового контенту
window.onload = showProducts;