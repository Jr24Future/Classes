import React, { useState } from 'react';
import CartView from './CartView';
import ConfirmationView from './ConfirmationView';

function BrowseView({ productData, cart, addToCart, removeFromCart, checkout }) {
  const [searchQuery, setSearchQuery] = useState('');

  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value.toLowerCase());
  };

  const filteredProducts = searchQuery
    ? productData.filter((product) => product.title.toLowerCase().includes(searchQuery))
    : productData;

  return (
    <div className="p-4">
      <div className="flex justify-between">
        <div className="mb-4 w-1/4">
          <input
            type="text"
            placeholder="Search"
            className="border p-2 w-full"
            value={searchQuery}
            onChange={handleSearchChange}
          />
        </div>
        <button className="border px-4 py-2 bg-blue-500 text-white" onClick={checkout}>
          Checkout
        </button>
      </div>

      <div className="grid grid-cols-3 gap-4">
        {filteredProducts.map((product) => (
          <div key={product.id} className="border p-4 flex flex-col items-center justify-between h-full">
            <img src={product.image} alt={product.title} className="mb-2 w-full h-32 object-cover" />
            <div className="text-lg bg-green-500 rounded-full px-4 py-1 text-white">${product.price.toFixed(2)}</div>
            <div className="font-bold">{product.title}</div>
            <p className="text-sm mb-2 text-center">{product.description}</p>
            <div className="flex justify-between items-center w-full">
              <div>
              <button
  onClick={() => addToCart(product.id)}
  className="bg-blue-500 text-white rounded-lg px-3 py-1 mr-2"
>
  Add
</button>
<button
  onClick={() => checkout(product.id)}
  className="bg-green-500 text-white rounded-lg px-3 py-1"
>
  Buy
</button>
              </div>
              <div className="px-4 bg-gray-100 rounded-full ">
                <button onClick={() => removeFromCart(product.id)} className="text-lg px-2 py-1">-</button>
                <span className="text-lg px-4 py-1">{cart[product.id] || 0}</span>
                <button onClick={() => addToCart(product.id)} className="text-lg px-2 py-1">+</button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default BrowseView;
