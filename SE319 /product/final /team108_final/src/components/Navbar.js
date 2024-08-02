import React from 'react';
import './Navbar.css';

function Navbar({ products, onSelectCategory }) {
  const categories = Array.from(new Set(products.map(product => product.category)));

  return (
    <nav className="navbar">
        <div className="navbar-brand">Gem Shop</div>
        <ul className="navbar-nav">
            {categories.map(category => (
                <li key={category}>
                    <button onClick={() => onSelectCategory(category)}>
                        {category}
                    </button>
                </li>
            ))}
        </ul>
    </nav>

  );
}

export default Navbar;
