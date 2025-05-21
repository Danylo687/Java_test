const contentDiv = document.getElementById('content');

function showProducts() {
    fetch('products.html')
        .then(response => response.text())
        .then(html => {
            contentDiv.innerHTML = html;
            fetchProducts();
            document.getElementById('add-product-form').addEventListener('submit', handleAddProduct);
        });
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

// =====================================================================================================================

// НОВА ФУНКЦІЯ: Ініціалізація сторінки продуктів з фільтром
function showProductsWithFilter() {
    fetch('filter.html')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text();
        })
        .then(html => {
            contentDiv.innerHTML = html;

            // Завантажуємо категорії для випадаючого списку фільтрації
            fetchCategoriesForProductFilterDropdown();
            // Завантажуємо категорії для форми додавання/редагування
            fetchCategoriesForProductFormDropdownNew();


            // Отримуємо посилання на саму форму (НЕ на div-контейнер) і додаємо слухача
            const productAddEditForm = document.getElementById('product-add-edit-form');
            if (productAddEditForm) {
                productAddEditForm.addEventListener('submit', handleAddProductNew); // Використовуємо нову функцію
            } else {
                console.error("Елемент 'product-add-edit-form' не знайдено в products.html. Перевірте ID форми.");
            }

            // Початкове завантаження продуктів (без фільтра)
            fetchProductsNew();
        })
        .catch(error => {
            console.error('Помилка завантаження products.html:', error);
            contentDiv.innerHTML = `<p>Не вдалося завантажити сторінку продуктів. Будь ласка, спробуйте ще раз.</p>`;
        });
}

// НОВА ФУНКЦІЯ: для показу форми додавання продукту (якщо потрібно)
function showAddProductFormNew() {
    const addProductFormContainer = document.getElementById('add-product-form');
    if (addProductFormContainer) {
        addProductFormContainer.style.display = 'block';
        fetchCategoriesForProductFormDropdownNew(); // Заповнюємо випадаючий список при показі форми
    } else {
        console.error("Елемент 'add-product-form' не знайдено. Перевірте, чи є він у products.html.");
    }
}

// НОВА ФУНКЦІЯ: для скасування додавання/редагування продукту (якщо потрібно)
function cancelAddProductNew() {
    const addProductFormContainer = document.getElementById('add-product-form');
    const productAddEditForm = document.getElementById('product-add-edit-form');
    if (addProductFormContainer && productAddEditForm) {
        addProductFormContainer.style.display = 'none';
        productAddEditForm.reset(); // Очищуємо поля форми
        const categorySelect = document.getElementById('category');
        if (categorySelect) categorySelect.value = '';
    }
}


// НОВА ФУНКЦІЯ: Завантаження категорій для випадаючого списку ФІЛЬТРАЦІЇ продуктів
async function fetchCategoriesForProductFilterDropdown() {
    try {
        const response = await fetch('/api/categories');
        const categories = await response.json();
        const categoryFilterSelect = document.getElementById('category-filter');

        // Очищаємо попередні опції, крім "Усі категорії"
        categoryFilterSelect.innerHTML = '<option value="">Усі категорії</option>';

        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.id;
            option.textContent = category.name;
            categoryFilterSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Помилка завантаження категорій для фільтра:', error);
    }
}

// НОВА ФУНКЦІЯ: Завантаження категорій для випадаючого списку у ФОРМІ ДОДАВАННЯ/РЕДАГУВАННЯ продукту
async function fetchCategoriesForProductFormDropdownNew() {
    try {
        const response = await fetch('/api/categories');
        const categories = await response.json();
        const categorySelect = document.getElementById('category'); // Це select у формі додавання

        // Очищаємо попередні опції, крім placeholder
        categorySelect.innerHTML = '<option value="">-- Виберіть категорію --</option>';

        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.id;
            option.textContent = category.name;
            categorySelect.appendChild(option);
        });
    } catch (error) {
        console.error('Помилка завантаження категорій для форми продукту:', error);
    }
}

// НОВА ФУНКЦІЯ: Завантаження продуктів з можливістю фільтрації за категорією
async function fetchProductsNew(categoryId = null) {
    try {
        let url = '/api/products';
        if (categoryId) {
            url = `/api/products/category/${categoryId}`; // Припустимо, що у вас є такий ендпоінт
        }

        const response = await fetch(url);
        if (!response.ok) {
            // Обробка HTTP помилок (наприклад, 404 Not Found)
            const errorText = await response.text();
            throw new Error(`Помилка завантаження продуктів: ${response.status} ${response.statusText} - ${errorText}`);
        }
        const products = await response.json();
        let productListHTML = '<ul>';
        if (products.length === 0) {
            productListHTML += '<p>Немає продуктів у цій категорії або список порожній.</p>';
        } else {
            products.forEach(product => {
                // Використовуйте product.categoryName, якщо ви його додали в DTO або в геттер Product
                productListHTML += `<li>${product.name} (${product.categoryName ? product.categoryName : 'Без категорії'}) - ${product.price} грн <button onclick="handleDeleteProductNew(${product.id})">Видалити</button></li>`; // Використовуємо нову функцію видалення
            });
        }
        productListHTML += '</ul>';
        const productListDiv = document.getElementById('product-list');
        if (productListDiv) {
            productListDiv.innerHTML = productListHTML;
        } else {
            console.error("Елемент 'product-list' не знайдено в products.html. Перевірте ID.");
        }
    } catch (error) {
        console.error(`Помилка завантаження продуктів: ${error.message}`);
        const productListDiv = document.getElementById('product-list');
        if (productListDiv) {
            productListDiv.innerHTML = `<p>Не вдалося завантажити продукти: ${error.message}</p>`;
        }
    }
}

// НОВА ФУНКЦІЯ: Обробка зміни вибору категорії у фільтрі
function filterProductsByCategory() {
    const categoryFilterSelect = document.getElementById('category-filter');
    const selectedCategoryId = categoryFilterSelect.value;

    if (selectedCategoryId === "") {
        fetchProductsNew(); // Завантажуємо всі продукти
    } else {
        fetchProductsNew(selectedCategoryId); // Завантажуємо продукти за вибраною категорією
    }
}


// НОВА ФУНКЦІЯ: Обробник додавання/редагування продукту (копія handleAddProduct)
async function handleAddProductNew(event) {
    event.preventDefault(); // Запобігаємо стандартній відправці форми

    const form = event.target;
    const formData = new FormData(form);
    const productData = {};

    // Збираємо дані з форми
    for (const [key, value] of formData.entries()) {
        productData[key] = value;
    }

    // Перетворюємо ціну та вагу в числові значення
    productData.price = parseFloat(productData.price);
    productData.weight = parseFloat(productData.weight);

    // Отримуємо ID категорії, оскільки бекенд очікує Category ID, а не об'єкт Category
    const categoryId = productData.category; // Це буде ID категорії
    if (categoryId) {
        productData.category = { id: parseInt(categoryId) }; // Створюємо об'єкт з ID для бекенду
    } else {
        productData.category = null; // Або встановлюємо null, якщо категорія необов'язкова
    }


    try {
        const response = await fetch('/api/products', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(productData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Помилка додавання/редагування продукту: ${response.status} ${response.statusText} - ${errorText}`);
        }

        // Очищаємо форму після успішного додавання/редагування
        form.reset();
        cancelAddProductNew(); // Приховати форму

        // Оновлюємо список продуктів
        fetchProductsNew();
        alert('Продукт успішно збережено!');
    } catch (error) {
        console.error('Помилка додавання/редагування продукту:', error);
        alert('Не вдалося зберегти продукт: ' + error.message);
    }
}

// НОВА ФУНКЦІЯ: Обробник видалення продукту (копія handleDeleteProduct)
async function handleDeleteProductNew(productId) {
    if (!confirm('Ви впевнені, що хочете видалити цей продукт?')) {
        return;
    }

    try {
        const response = await fetch(`/api/products/${productId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Помилка видалення продукту: ${response.status} ${response.statusText} - ${errorText}`);
        }

        alert('Продукт успішно видалено!');
        fetchProductsNew(); // Оновлюємо список продуктів
    } catch (error) {
        console.error('Помилка видалення продукту:', error);
        alert('Не вдалося видалити продукт: ' + error.message);
    }
}


// Завантаження початкового контенту (замініть showProducts на showProductsWithFilter)
window.onload = showProductsWithFilter;
// =====================================================================================================================

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