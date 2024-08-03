import React, { useState, useEffect } from "react";
import items from "./products.json";

const Shop = () => {
    const [cart, setCart] = useState([]);
    const [cartTotal, setCartTotal] = useState(0);

    const addToCart = (el) => {
        setCart([...cart, el]);
    };

    const removeFromCart = (el) => {
        let hardCopy = [...cart];
        hardCopy = hardCopy.filter((cartItem) => cartItem.id !== el.id);
        setCart(hardCopy);
    };

    const listItems = items.map((el) => (
        <div key={el.id}>
            <img className="img-fluid" src={el.image} width={100} />
            {el.title}<br/>
            {el.category}<br/>
            ${el.price}<br/>
            <button type="button" onClick={() => removeFromCart(el)}>-</button>{" "}
            <button type="button" variant="light" onClick={() => addToCart(el)}> + </button>
        </div>
    ));

    const cartItems = cart.map((el) => (
        <div key={el.id}>
            <img className="img-fluid" src={el.image} width={150} />
            {el.title} 
            ${el.price}
        </div>
    ));

    const total = () => {
        let totalVal = 0;
        for (let i = 0; i < cart.length; i++) {
            totalVal += cart[i].price;
        }
        setCartTotal(totalVal);
    };

    useEffect(() => {
        total();
    }, [cart]);

    return (
        <div>
            <div>{listItems}</div>
            <div>Items in Cart:</div>
            <div>{cartItems}</div>
            <div>Order total to pay: ${cartTotal}</div>
        </div>
    );
}

export default Shop;
