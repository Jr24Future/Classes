let allProducts = [];
let cart = JSON.parse(localStorage.getItem('cart')) || [];

// Fetching product data from the JSON file
fetch('shopping.json')
    .then(response => response.json())
    .then(products => {
        allProducts = products; // Store all products
        displayProducts(products); // Display all products initially


    });

// Function to display products
function displayProducts(products) {
    const container = document.querySelector('#product-container');
    // Clear the container
    container.innerHTML = '';
    // Generate HTML for each product
    products.forEach(product => {
        const colDiv = document.createElement('div');
        colDiv.className = 'col';

        const cardDiv = document.createElement('div');
        cardDiv.className = 'card shadow-sm';
        // Note: We're no longer setting the onclick attribute here
        cardDiv.innerHTML = `
            <img class="bd-placeholder-img card-img-top"  height="225" src="${product.image}" alt="${product.title}">
            <div class="card-body">
                <p class="card-text">${product.description}</p>
                <div class="d-flex justify-content-between align-items-center">
                    <div class="btn-group">
                        <button type="button" class="btn btn-sm btn-outline-secondary buy-btn">Buy</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary add-btn">Add</button>
                    </div>
                    <small class="available-body-secondary">${product.available}</small>
                </div>
            </div>
        `;
        colDiv.appendChild(cardDiv);
        container.appendChild(colDiv);

        // Add event listeners to the buttons
        cardDiv.querySelector('.buy-btn').addEventListener('click', function () {
            buyProduct(product.id); // This should call buyProduct
        });
        cardDiv.querySelector('.add-btn').addEventListener('click', function () {
            addToCart(product.id); // This should call addToCart
        });
    });
}

// Function to filter products by category
function filterProducts(category) {
    if (category === 'all') {
        displayProducts(allProducts);
    } else {
        const filteredProducts = allProducts.filter(product => product.category === category);
        displayProducts(filteredProducts);
    }
}

// Function to redirect based on login status
function redirectToPage() {
    const isLoggedIn = localStorage.getItem('isLoggedIn');
    if (isLoggedIn) {
        window.location.href = 'shopping_cart.html';
    } else {
        sessionStorage.setItem('returnPath', 'shopping.html');
        window.location.href = 'login.html';
    }
}

// Add this function to handle login redirection and return path setup
function loginRedirect() {
    // This will take users to the login page and set a return path for after login
    sessionStorage.setItem('returnPath', 'shopping.html');
    location.href = 'login.html';
}

// Add the listener for the login button if it exists on the page
const loginButton = document.querySelector('.header-login-btn');
if (loginButton) {
    loginButton.addEventListener('click', loginRedirect);
}
function logout() {
    localStorage.removeItem('isLoggedIn'); // Clear the logged-in flag
    location.href = 'shopping.html'; // Redirect to the login page upon logout
}


// Event listener for DOMContentLoaded to handle dynamic elements
document.addEventListener('DOMContentLoaded', (event) => {
    // Add the listener for the login button if it exists on the page
    updateCartCount();
    const loginButton = document.querySelector('.header-login-btn');
    if (loginButton) {
        loginButton.addEventListener('click', loginRedirect);
    }

    // Listener for logout button
    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', logout);
    }

    const clearCartButton = document.getElementById('clear-cart-button');
    if (clearCartButton) {
        clearCartButton.addEventListener('click', clearCart);
    }

});


function buyProduct(productId) {
    if (isLoggedIn()) {
        addToCart(productId);
        window.location.href = 'shopping_cart.html';
    } else {
        sessionStorage.setItem('productToAdd', productId);
        sessionStorage.setItem('returnPath', 'shopping_cart.html');
        window.location.href = 'login.html';
    }
}

function addToCart(productId) {
    if (isLoggedIn()) {
        const product = allProducts.find(p => p.id === productId);
        if (product) {
            cart.push(product);
            localStorage.setItem('cart', JSON.stringify(cart));
            updateCartCount();
        }
    } else {
        sessionStorage.setItem('productToAdd', productId);
        sessionStorage.setItem('returnPath', 'shopping.html');
        window.location.href = 'login.html';
    }
}

function updateCartCount() {
    const cartCount = document.getElementById('cart-count');
    if (cartCount) {
        const cartItems = JSON.parse(localStorage.getItem('cart')) || [];
        cartCount.textContent = `(${cartItems.length})`;
    }
}

function isLoggedIn() {
    return localStorage.getItem('isLoggedIn') === 'true';
}



function clearCart() {
    // Clear the cart array
    cart = [];
    // Update localStorage by setting the cart to an empty array
    localStorage.setItem('cart', JSON.stringify(cart));
    // Update the cart count display
    updateCartCount();
    // Optionally, refresh the cart display if you're on the cart page
    if (window.location.pathname.includes('shopping_cart.html')) {
        displayCartItems();
    }
}
function displayCartItems() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const container = document.getElementById('cart-items');
    let totalPrice = 0;

    container.innerHTML = '';
    if (cart.length === 0) {
        container.innerHTML = '<p>Your cart is empty.</p>';
    } else {
        cart.forEach((product, index) => {
            const productDiv = document.createElement('div');
            productDiv.className = 'cart-item';
            productDiv.innerHTML = `
                <img src="${product.image}" alt="${product.title}" class="cart-item-image">
                <div class="cart-item-details">
                    <div>${product.title}</div>
                    <div>$${product.price}</div>
                </div>
                <button class="delete-btn" onclick="removeFromCart(${index})">Delete</button>
            `;
            container.appendChild(productDiv);
            totalPrice += parseFloat(product.price);
        });
    }

    document.getElementById('total-price').textContent = totalPrice.toFixed(2);
}

function removeFromCart(index) {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart.splice(index, 1); // Remove the item at the specified index
    localStorage.setItem('cart', JSON.stringify(cart)); // Update the cart in localStorage
    displayCartItems(); // Refresh the cart display
    updateCartCount(); // Update the cart count
}
