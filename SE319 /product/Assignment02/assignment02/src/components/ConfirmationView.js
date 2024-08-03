import React from 'react';

function ConfirmationView({ cart, productData, startNewOrder, userInformation }) {
  const findProductById = (productId) => {
    return productData.find((product) => product.id === parseInt(productId, 10));
  };

  const maskCreditCard = (cardNumber) => {
    return cardNumber.slice(0, -4).replace(/./g, '*') + cardNumber.slice(-4);
  };

  // Calculate the total price of items in the cart
  const calculateTotal = () => {
    return Object.keys(cart).reduce((total, productId) => {
      const product = findProductById(productId);
      return total + (product?.price * cart[productId]);
    }, 0).toFixed(2);
  };

  const subtotal = calculateTotal();
  const TAX_RATE = 0.15;
  const tax = (subtotal * TAX_RATE).toFixed(2);
  const totalWithTax = (parseFloat(subtotal) + parseFloat(tax)).toFixed(2);

  return (
    <div className="container mx-auto p-4">
      <h2 className="text-lg font-bold mb-4">Order Confirmation</h2>
      <div className="mb-6">
        <h3 className="text-md font-bold">Purchased Items:</h3>
        <ul>
          {Object.keys(cart).map((productId) => {
            const product = findProductById(productId);
            return (
              <li key={productId} className="flex justify-between items-center my-2">
                <img src={product.image} alt={product.name} className="h-10 w-10 rounded mr-4" />
                <span>{product.name}</span>
                <span>Quantity: {cart[productId]}</span>
                <span>Total: ${(product.price * cart[productId]).toFixed(2)}</span>
              </li>
            );
          })}
        </ul>
      </div>
      <div className="mb-6">
        <h3 className="text-md font-bold">Total Amount:</h3>
        <p>Subtotal: ${subtotal}</p>
        <p>Tax: ${tax}</p>
        <p>Total: ${totalWithTax}</p>
      </div>
      <div className="mb-6">
        <h3 className="text-md font-bold">User Information:</h3>
        <p>Name: {userInformation.fullName}</p>
        <p>Email: {userInformation.email}</p>
        <p>Address: {userInformation.address1} {userInformation.address2}</p>
        <p>City: {userInformation.city}</p>
        <p>State: {userInformation.state}</p>
        <p>Zip: {userInformation.zip}</p>
        <p>Card: {maskCreditCard(userInformation.card)}</p>
      </div>

      <button onClick={startNewOrder} className="bg-blue-500 text-white py-2 px-4 rounded">
        Start New Order
      </button>
    </div>
  );
}

export default ConfirmationView;
