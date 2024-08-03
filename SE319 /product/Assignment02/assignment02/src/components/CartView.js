import React, { useState } from 'react';
import BrowseView from './BrowseView';
import App from '../App';

function CartView({ cart, onReturnToBrowse, onOrder, onDelete, productData, updateUserInformation }) {
  // Helper function to find a product by ID
  const findProductById = (productId) => {
    return productData.find((product) => product.id === parseInt(productId, 10));
  };

  // Calculate the total price of items in the cart
  const calculateTotal = () => {
    return Object.keys(cart).reduce((total, productId) => {
      const product = findProductById(productId);
      return total + (product?.price * cart[productId]);
    }, 0).toFixed(2);
  };

  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    card: '',
    address1: '',
    address2: '',
    city: '',
    state: '',
    zip: '',
  });

  const states = [
    'Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware',
    'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky',
    'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi',
    'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico',
    'New York', 'North Carolina', 'North Dakota', 'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania',
    'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont',
    'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
  ];

  // Tax calculations
  const TAX_RATE = 0.15;
  const subtotal = calculateTotal();
  const tax = (subtotal * TAX_RATE).toFixed(2);
  const totalWithTax = (parseFloat(subtotal) + parseFloat(tax)).toFixed(2);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    updateUserInformation(formData); // This function will update the userInformation state in App.js
    onOrder();
  };

  return (
    <div className="container mx-auto p-4">
      {/* Button to return to browsing */}
      <div className="mb-6">
        <button onClick={onReturnToBrowse} className="text-blue-500">
          ← Return
        </button>
      </div>

      {/* Shopping cart table */}
      <div className="overflow-x-auto mb-6">
        <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
          <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
            <tr>
              <th scope="col" className="p-4">
                Item
              </th>
              <th scope="col" className="p-4">
                Quantity
              </th>
              <th scope="col" className="p-4">
                Price
              </th>
              <th scope="col" className="p-4">
                Action
              </th>
            </tr>
          </thead>
          <tbody>
            {Object.keys(cart).map((productId) => {
              const product = findProductById(productId);
              return (
                <tr key={productId} className="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                  <td className="p-4 dark:border-dark-5">
                    <img src={product.image} alt={product.name} className="h-10 w-10 rounded" />
                    {product.name}
                  </td>
                  <td className="p-4 dark:border-dark-5">{cart[productId]}</td>
                  <td className="p-4 dark:border-dark-5">${(product.price * cart[productId]).toFixed(2)}</td>
                  <td className="p-4 dark:border-dark-5">
                    <button onClick={() => onDelete(productId)} className="text-red-500 hover:text-red-700">
                      Delete
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      {/* Total calculation */}
      <div className="mb-6">
        <div className="flex justify-between">
          <span>Total</span>
          <span>${subtotal} + ${tax} Tax = ${totalWithTax}</span>
        </div>
      </div>

      {/* Payment information form */}
      <form onSubmit={handleSubmit} className="space-y-4">
        <h3 className="text-lg font-bold">Payment Information</h3>

        {/* Full Name and Email */}
        <div className="grid grid-cols-1 gap-4 mb-4">
          <input
            type="text"
            name="fullName"
            placeholder="Full Name"
            className="border p-2 rounded w-full"
            value={formData.fullName}
            onChange={handleInputChange}
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            className="border p-2 rounded w-full"
            value={formData.email}
            onChange={handleInputChange}
          />
        </div>

        {/* Card Details */}
        <input
          type="text"
          name="card"
          placeholder="Card Number"
          className="border p-2 rounded w-full mb-4"
          value={formData.card}
          onChange={handleInputChange}
        />

        {/* Address */}
        <input
          type="text"
          name="address1"
          placeholder="Address"
          className="border p-2 rounded w-full mb-4"
          value={formData.address1}
          onChange={handleInputChange}
        />
        <input
          type="text"
          name="address2"
          placeholder="Apartment, studio, or floor"
          className="border p-2 rounded w-full mb-4"
          value={formData.address2}
          onChange={handleInputChange}
        />

        {/* City, State, Zip */}
        <div className="grid grid-cols-3 gap-4 mb-4">
          <input
            type="text"
            name="city"
            placeholder="City"
            className="border p-2 rounded w-full"
            value={formData.city}
            onChange={handleInputChange}
          />
          {/* Replace this with your actual state options */}
          <select
            name="state"
            className="border p-2 rounded w-full"
            value={formData.state}
            onChange={handleInputChange}
          >
            <option value="">Select State</option>
            {states.map(state => (
              <option key={state} value={state}>{state}</option>
            ))}
            {/* Populate with actual state options */}
            <option value="">State</option>
          </select>
          <input
            type="text"
            name="zip"
            placeholder="Zip"
            className="border p-2 rounded w-full"
            value={formData.zip}
            onChange={handleInputChange}
          />
        </div>

        <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded">
          Place Order
        </button>
      </form>
    </div>
  );
}

export default CartView;