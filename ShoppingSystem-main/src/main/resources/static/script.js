const contentDiv = document.getElementById('content');

function showProducts() {
    fetch('products.html')
        .then(response => response.text())
        .then(html => {
            contentDiv.innerHTML = html;
            fetchProductsNew();
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
    fetch('products.html')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text();
        })
        .then(html => {
            contentDiv.innerHTML = html;

            fetchCategoriesForProductFilterDropdown();
            fetchCategoriesForProductFormDropdownNew();

            const productAddEditForm = document.getElementById('product-add-edit-form');
            if (productAddEditForm) {
                productAddEditForm.addEventListener('submit', handleAddProductNew);
            } else {
                console.error("Елемент 'product-add-edit-form' не знайдено в products.html. Перевірте ID форми.");
            }

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
    // Отримаємо доступ до самої форми, а не лише до контейнера
    const productAddEditForm = document.getElementById('product-add-edit-form');

    if (addProductFormContainer && productAddEditForm) {
        addProductFormContainer.style.display = 'block';
        productAddEditForm.style.display = 'block'; // Переконаємось, що сама форма також видима

        // **ВИДАЛЕНО: document.getElementById('product-add-edit-form').reset();**
        // Цей рядок очищав форму, навіть коли ви відкривали її для редагування.

        // Цю логіку краще перенести в handleEditProductNew для встановлення заголовка
        // document.getElementById('product-add-edit-form').querySelector('h2').textContent = 'Додати новий продукт';

        // Очищаємо ID та поле для зображень тільки якщо це точно нова форма
        // Якщо id вже встановлено (тобто, це редагування), ми не хочемо його очищати
        if (document.getElementById('id').value === '') { // Перевіряємо, чи ID пустий
            document.getElementById('product-add-edit-form').reset(); // Очищаємо форму тільки для нового продукту
            document.getElementById('id').value = '';
            const imageUrlsInput = document.getElementById('imageUrls');
            if (imageUrlsInput) imageUrlsInput.value = '';
            document.getElementById('product-add-edit-form').querySelector('h2').textContent = 'Додати новий продукт';
        }

        fetchCategoriesForProductFormDropdownNew(); // Завжди завантажуємо категорії
    } else {
        console.error("Елемент 'add-product-form' або 'product-add-edit-form' не знайдено. Перевірте, чи є він у products.html.");
    }
}


// НОВА ФУНКЦІЯ: Завантаження продукту для редагування
async function handleEditProductNew(productId) {
    try {
        const response = await fetch(`/api/products/dto/${productId}`);
        if (!response.ok) {
            throw new Error(`Помилка завантаження продукту для редагування: ${response.status} ${response.statusText}`);
        }
        const product = await response.json();

        // Заповнюємо форму даними продукту
        document.getElementById('id').value = product.id;
        document.getElementById('name').value = product.name;
        document.getElementById('price').value = product.price;
        document.getElementById('producer').value = product.producer;
        document.getElementById('countryOfOrigin').value = product.countryOfOrigin || ''; // Додано || '' для уникнення undefined
        document.getElementById('weight').value = product.weight;
        document.getElementById('description').value = product.description;

        // Встановлюємо категорію
        const categorySelect = document.getElementById('category');
        // Заповнюємо список категорій перед вибором, якщо він ще не заповнений
        await fetchCategoriesForProductFormDropdownNew(); // Важливо, щоб опції були доступні
        if (product.categoryId) {
            categorySelect.value = product.categoryId;
        } else {
            categorySelect.value = ''; // Якщо категорія null, скидаємо вибір
        }

        // Заповнюємо поле imageUrls
        const imageUrlsInput = document.getElementById('imageUrls');
        if (product.imageUrls && product.imageUrls.length > 0) {
            imageUrlsInput.value = product.imageUrls.join(', '); // З'єднуємо URLи комою
        } else {
            imageUrlsInput.value = '';
        }

        // Змінюємо заголовок форми
        document.getElementById('product-add-edit-form').querySelector('h2').textContent = `Редагувати продукт: ${product.name}`;

        showAddProductFormNew(); // Показуємо форму. Тепер вона не буде скидатися автоматично.
    } catch (error) {
        console.error('Помилка при завантаженні продукту для редагування:', error);
        alert('Не вдалося завантажити продукт для редагування: ' + error.message);
    }
}

// НОВА ФУНКЦІЯ: для скасування додавання/редагування продукту (якщо потрібно)
function cancelAddProductNew() {
    const addProductFormContainer = document.getElementById('add-product-form');
    const productAddEditForm = document.getElementById('product-add-edit-form');
    if (addProductFormContainer && productAddEditForm) {
        addProductFormContainer.style.display = 'none';
        productAddEditForm.reset();
        const categorySelect = document.getElementById('category');
        if (categorySelect) categorySelect.value = '';
        const imageUrlsInput = document.getElementById('imageUrls');
        if (imageUrlsInput) imageUrlsInput.value = '';
        document.getElementById('id').value = '';
    }
}


// НОВА ФУНКЦІЯ: Завантаження категорій для випадаючого списку ФІЛЬТРАЦІЇ продуктів
async function fetchCategoriesForProductFilterDropdown() {
    try {
        const response = await fetch('/api/categories');
        const categories = await response.json();
        const categoryFilterSelect = document.getElementById('category-filter');

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
        const categorySelect = document.getElementById('category');

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
        let url = '/api/products/dto';
        if (categoryId) {
            url = `/api/products/category/${categoryId}`;
        }

        const response = await fetch(url);
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Помилка завантаження продуктів: ${response.status} ${response.statusText} - ${errorText}`);
        }
        const products = await response.json();
        let productListHTML = '<ul>';
        if (products.length === 0) {
            productListHTML += '<p>Немає продуктів у цій категорії або список порожній.</p>';
        } else {
            products.forEach(product => {
                const imageUrl = (product.imageUrls && product.imageUrls.length > 0) ? product.imageUrls[0] : 'https://via.placeholder.com/50x50?text=No+Image';
                productListHTML += `
                    <li>
                        <img src="${imageUrl}" alt="${product.name}" style="width:50px; height:50px; object-fit:cover; margin-right:10px;">
                        ${product.name} (${product.categoryName ? product.categoryName : 'Без категорії'}) - ${product.price} грн
                        <button onclick="handleEditProductNew(${product.id})">Редагувати</button>
                        <button onclick="handleDeleteProductNew(${product.id})">Видалити</button>
                    </li>`;
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
        fetchProductsNew();
    } else {
        fetchProductsNew(selectedCategoryId);
    }
}

// НОВА ФУНКЦІЯ: Обробник додавання/редагування продукту
async function handleAddProductNew(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);
    const productData = {};

    for (const [key, value] of formData.entries()) {
        productData[key] = value;
    }

    productData.price = parseFloat(productData.price);
    productData.weight = parseFloat(productData.weight);

    const categoryId = productData.category;
    if (categoryId) {
        productData.categoryId = parseInt(categoryId);
        delete productData.category;
    } else {
        productData.categoryId = null;
        delete productData.category;
    }

    const imageUrlsInput = document.getElementById('imageUrls').value;
    if (imageUrlsInput) {
        productData.imageUrls = imageUrlsInput.split(',').map(url => url.trim()).filter(url => url !== '');
    } else {
        productData.imageUrls = [];
    }

    const productId = productData.id;
    const method = productId ? 'PUT' : 'POST';
    const url = productId ? `/api/products/dto/${productId}` : '/api/products/dto';

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(productData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Помилка збереження продукту: ${response.status} ${response.statusText} - ${errorText}`);
        }

        form.reset();
        cancelAddProductNew();
        fetchProductsNew();
        alert('Продукт успішно збережено!');
    } catch (error) {
        console.error('Помилка збереження продукту:', error);
        alert('Не вдалося зберегти продукт: ' + error.message);
    }
}

// НОВА ФУНКЦІЯ: Обробник видалення продукту
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
        fetchProductsNew();
    } catch (error) {
        console.error('Помилка видалення продукту:', error);
        alert('Не вдалося видалити продукт: ' + error.message);
    }
}

// НОВА ФУНКЦІЯ: Завантаження продукту для редагування
async function handleEditProductNew(productId) {
    try {
        const response = await fetch(`/api/products/dto/${productId}`);
        if (!response.ok) {
            throw new Error(`Помилка завантаження продукту для редагування: ${response.status} ${response.statusText}`);
        }
        const product = await response.json();

        document.getElementById('id').value = product.id;
        document.getElementById('name').value = product.name;
        document.getElementById('price').value = product.price;
        document.getElementById('producer').value = product.producer;
        document.getElementById('CountryOfOrigin').value = product.countryOfOrigin;
        document.getElementById('weight').value = product.weight;
        document.getElementById('description').value = product.description;

        const categorySelect = document.getElementById('category');
        await fetchCategoriesForProductFormDropdownNew();
        if (product.categoryId) {
            categorySelect.value = product.categoryId;
        } else {
            categorySelect.value = '';
        }

        const imageUrlsInput = document.getElementById('imageUrls');
        if (product.imageUrls && product.imageUrls.length > 0) {
            imageUrlsInput.value = product.imageUrls.join(', ');
        } else {
            imageUrlsInput.value = '';
        }

        showAddProductFormNew();
    } catch (error) {
        console.error('Помилка при завантаженні продукту для редагування:', error);
        alert('Не вдалося завантажити продукт для редагування: ' + error.message);
    }
}


window.onload = showProductsWithFilter;

// Функції для клієнтів (без змін)
function showAddCustomerForm() {
    document.getElementById('add-customer-form').style.display = 'block';
}

function showAddCategoryForm() {
    document.getElementById('add-category-form').style.display = 'block';
}

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

async function fetchCategories() {
    try {
        const response = await fetch('/api/categories');
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