import React, { useState } from 'react';
import BrowseView from './components/BrowseView';
import CartView from './components/CartView';
import ConfirmationView from './components/ConfirmationView';
import { Products } from "./Products"; // Import your Products array

function App() {
  const [cart, setCart] = useState({});
  const [currentView, setCurrentView] = useState('browse');

  const addToCart = (productId) => {
    setCart((prevCart) => ({
      ...prevCart,
      [productId]: (prevCart[productId] || 0) + 1,
    }));
  };

  const removeFromCart = (productId) => {
    setCart((prevCart) => ({
      ...prevCart,
      [productId]: Math.max((prevCart[productId] || 0) - 1, 0),
    }));
  };

  const onReturnToBrowse = () => {
    setCurrentView('browse');
  };

  const [userInformation, setUserInformation] = useState({
    fullName: '',
    email: '',
    address1: '',
    address2: '',
    city: '',
    state: '',
    zip: '',
    card: '',
  });

  const deleteFromCart = (productId) => {
    setCart((prevCart) => {
      const newCart = { ...prevCart };
      delete newCart[productId];
      return newCart;
    });
  };

  const updateUserInformation = (newUserInfo) => {
    setUserInformation(newUserInfo);
  };

  const checkout = () => {
    setCurrentView('cart');
  };

  const confirmOrder = () => {
    setCurrentView('confirmation');
  };

  const startNewOrder = () => {
    setCart({});
    setCurrentView('browse');
  };

  let viewComponent;
  switch (currentView) {
    case 'browse':
      viewComponent = (
        <BrowseView
          productData={Products} // Correctly using Products
          cart={cart}
          addToCart={addToCart}
          removeFromCart={removeFromCart}
          checkout={checkout}
        />
      );
      break;
    case 'confirmation':
      viewComponent = (
        <ConfirmationView
          cart={cart}
          productData={Products}
          startNewOrder={startNewOrder}
          userInformation={userInformation} // Pass this prop to ConfirmationView
        />
      );
      break;
      case 'cart':
        viewComponent = (
          <CartView
            cart={cart}
            productData={Products}
            onReturnToBrowse={onReturnToBrowse}
            onOrder={confirmOrder}
            onDelete={deleteFromCart}
            updateUserInformation={updateUserInformation} // Pass this function as a prop
          />
        );
        break;
    case 'confirmation':
      viewComponent = (
        <ConfirmationView
          cart={cart}
          productData={Products} // And pass Products here too
          startNewOrder={startNewOrder}
        />
      );
      break;
    default:
      viewComponent = <div>404 - Not Found</div>;
  }

  return <div className="App">{viewComponent}</div>;
}

export default App;
