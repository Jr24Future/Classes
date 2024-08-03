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

    function howManyofThis(id) {
        let hmot = cart.filter((cartItem) => cartItem.id === id);
        return hmot.length;
    }

    const listItems = items.map((el) => (
        <div className="row border-top border-bottom" key={el.id}>
            <div className="row main align-items-center">
                <div className="col-2">
                    <img className="img-fluid" src={el.image} alt={el.title} />
                </div>
                <div className="col">
                    <div className="row text-muted">{el.title}</div>
                    <div className="row">{el.category}</div>
                </div>
                <div className="col">
                    <button type="button" className="btn btn-danger" onClick={() => removeFromCart(el)}>-</button>{" "}
                    <button type="button" className="btn btn-success" onClick={() => addToCart(el)}>+</button>
                </div>
                <div className="col">
                    ${el.price} <span className="close">&#10005;</span>{howManyofThis(el.id)}
                </div>
            </div>
        </div>
    ));

    const total = () => {
        let totalVal = cart.reduce((acc, item) => acc + item.price, 0);
        setCartTotal(totalVal);
    };

    useEffect(() => {
        total();
    }, [cart]);

    return (
        <div className="container">
            <div className="row justify-content-center">
                <div className="col-12 col-md-10 col-lg-8">
                    <div className="card">
                        <div className="card-header bg-dark text-white text-center">
                            <h4 className="mb-0"><b>319 Shopping Cart</b></h4>
                        </div>
                        <div className="card-body">
                            <div className="row">
                                <div className="col text-center text-muted">
                                    Products selected: {cart.length}
                                </div>
                            </div>
                            <div className="row justify-content-center">
                                <div className="col-12">
                                    {listItems}
                                </div>
                            </div>
                        </div>
                        <div className="card-footer">
                            <div className="row">
                                <div className="col text-center">
                                    <p className="mb-0">
                                        <span className="small justify-content-center text-muted">Order total:</span>
                                        <span className="lead justify-content-center fw-normal">${cartTotal.toFixed(2)}</span>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Shop;
