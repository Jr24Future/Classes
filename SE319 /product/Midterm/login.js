document.getElementById('login-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    fetch('login.json')
        .then(response => response.json())
        .then(users => {
            const user = users.find(user => user.username === username && user.password === password);
            if (user) {
                localStorage.setItem('isLoggedIn', 'true');

                // After successful login, check if there's a product to add
                const productToAddId = sessionStorage.getItem('productToAdd');
                if (productToAddId) {
                    fetch('shopping.json')
                        .then(response => response.json())
                        .then(allProducts => {
                            const productToAdd = allProducts.find(p => p.id === productToAddId);
                            if (productToAdd) {
                                const cart = JSON.parse(localStorage.getItem('cart')) || [];
                                cart.push(productToAdd);
                                localStorage.setItem('cart', JSON.stringify(cart));
                            }
                            // Clear the stored product ID and redirect
                            sessionStorage.removeItem('productToAdd');
                            window.location.href = sessionStorage.getItem('returnPath') || 'shopping_cart.html';
                        });
                } else {
                    // Redirect if there's no product to add
                    window.location.href = sessionStorage.getItem('returnPath') || 'shopping_cart.html';
                }
            } else {
                document.getElementById('error-message').textContent = 'Invalid username or password.';
                localStorage.removeItem('isLoggedIn');
            }
        })
        .catch(error => {
            console.error('Error fetching user credentials:', error);
        });
});