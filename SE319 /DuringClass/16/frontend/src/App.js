    //Author: Erroll Barker
    //ISU Netid : errollb@iastate.edu
    //Date :  4/26, 2024

import React, { useState, useEffect } from 'react';

function App() {
    const [products, setProducts] = useState([]);
    const [oneProduct, setOneProduct] = useState(null);
    const [category, setCategory] = useState('');
    const [editProduct, setEditProduct] = useState({
        id: null,
        title: "",
        price: 0,
        description: "",
        category: "",
        image: "",
        rating: 0,
    });
    const [newProduct, setNewProduct] = useState({
        id: "",
        title: "",
        price: "",
        description: "",
        category: "",
        image: "",
        rating: "",
    });
    const [viewer1, setViewer1] = useState(false);
    const [viewer2, setViewer2] = useState(false);


    useEffect(() => {
        getAllProducts();
    }, []);

    function getAllProducts() {
        fetch("http://127.0.0.1:4000/catalog")
            .then((response) => response.json())
            .then((data) => {
                console.log("Show Catalog of Products:");
                console.log(data);
                setProducts(data);
            });
        setViewer1(!viewer1);
    }
    function getOneProduct(id) {
        const productId = parseInt(id);
        if (productId >= 1 && productId <= 20) {
            fetch(`http://127.0.0.1:4000/catalog/${productId}`)
                .then(response => response.json())
                .then(data => {
                    setOneProduct(data[0]);
                    setEditProduct(data[0]);
                    setViewer2(true);
                })
                .catch(error => {
                    console.error('Failed to fetch product:', error);
                    setViewer2(false);
                });
        } else {
            console.error("Invalid Product ID.");
            setViewer2(false);
            setOneProduct(null);
        }
    }

    function handleEditChange(e) {
        const { name, value } = e.target;
        setEditProduct(prev => ({ ...prev, [name]: value }));
    }

    function handleNewProductChange(e) {
        const { name, value } = e.target;
        setNewProduct(prev => ({ ...prev, [name]: value }));
    }

    function fetchProductsByCategory() {
        fetch(`http://127.0.0.1:4000/catalog/category/${category}`)
            .then(response => response.json())
            .then(data => {
                console.log("Products by Category:", data);
                setProducts(data);  // Update products state with the fetched data
                setViewer1(true);   // Ensure the viewer is updated to show the fetched products
            })
            .catch(error => {
                console.error('Failed to fetch products by category:', error);
                setViewer1(false);
            });
    }

    function deleteProduct(productId) {
        fetch(`http://127.0.0.1:4000/catalog/${productId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json(); // or response.text() if you're not returning JSON
            })
            .then(() => {
                setProducts(currentProducts =>
                    currentProducts.filter(product => product.id !== productId)
                );
                alert("Product deleted successfully!");
            })
            .catch(error => {
                console.error('Failed to delete product:', error);
                alert("Failed to delete product: " + error.message);
            });
    }

    function updateProduct() {
        if (!editProduct.id) {
            console.error('No product ID provided for update.');
            return;
        }

        console.log("Attempting to update product with data:", editProduct);

        fetch(`http://127.0.0.1:4000/catalog/${editProduct.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(editProduct)
        })
            .then(response => {
                if (response.ok) {
                    return response.json(); // This assumes your server always returns JSON-formatted responses
                } else {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
            })
            .then(data => {
                console.log("Product Updated:", data);
                alert("Product updated successfully!"); // Provide feedback to the user
            })
            .catch(error => {
                console.error('Failed to update product:', error);
                alert("Failed to update product: " + error.message); // Provide error feedback to the user
            });
    }

    function createNewProduct(e) {
        e.preventDefault();
        
        // Convert ID from string to number before submission
        const productWithNumericId = {
            ...newProduct,
            id: Number(newProduct.id),
            price: Number(newProduct.price),
            rating: Number(newProduct.rating),
        };

        fetch("http://127.0.0.1:4000/catalog", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(productWithNumericId),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            setProducts([...products, data]);
            alert("Product created successfully!");
            setNewProduct({
                id: "",
                title: "",
                price: "",
                description: "",
                category: "",
                image: "",
                rating: "",
            });
        })
        .catch(error => {
            console.error('Failed to create new product:', error);
            alert("Failed to create new product: " + error.message);
        });
    }




    const showAllItems = products.map(product => (
        <div key={product.id}>
            <img src={product.image} width="30" alt={product.title} /> <br />
            Title: {product.title}<br />
            Category: {product.category}<br />
            Price: {product.price}<br />
            Rating: {product.rating}<br />
            <button onClick={() => deleteProduct(product.id)}>Delete</button>
        </div>
    ));

    const createProductForm = (
        <div>
            <h3>Create New Product</h3>
            <form onSubmit={createNewProduct}>
                <input type="number" name="id" placeholder="ID" value={newProduct.id} onChange={handleNewProductChange} required />
                <input type="text" name="title" placeholder="Title" value={newProduct.title} onChange={handleNewProductChange} required />
                <input type="number" step="0.01" name="price" placeholder="Price" value={newProduct.price} onChange={handleNewProductChange} required />
                <input type="text" name="description" placeholder="Description" value={newProduct.description} onChange={handleNewProductChange} required />
                <input type="text" name="category" placeholder="Category" value={newProduct.category} onChange={handleNewProductChange} required />
                <input type="url" name="image" placeholder="Image URL" value={newProduct.image} onChange={handleNewProductChange} required />
                <input type="number" step="0.1" name="rating" placeholder="Rating" value={newProduct.rating} onChange={handleNewProductChange} required />
                <button type="submit">Create Product</button>
            </form>
        </div>
    );

    const showOneItem = oneProduct && (
        <div key={oneProduct.id}>
            <img src={oneProduct.image} width="100" alt={oneProduct.title} /><br></br>
            <input type="text" value={editProduct.title} name="title" onChange={handleEditChange} /><br />
            <input type="text" value={editProduct.description} name="description" onChange={handleEditChange} /><br />
            <input type="text" value={editProduct.category} name="category" onChange={handleEditChange} /><br />
            <input type="number" value={editProduct.price} name="price" onChange={handleEditChange} /><br />
            <input type="text" value={editProduct.image} name="image" onChange={handleEditChange} /><br />
            <input type="number" value={editProduct.rating} name="rating" onChange={handleEditChange} /><br />
            <button onClick={updateProduct}>Update Product</button>
        </div>
    );


    return (
        <div>
            <h1>Catalog of Products</h1>
            <div>
                <h3>Show all available Products.</h3>
                <input type="text" placeholder="Enter category" onChange={(e) => setCategory(e.target.value)} />
                <button onClick={fetchProductsByCategory}>Fetch by Category</button><br></br>
                <button onClick={getAllProducts}>Show All...</button>
                {viewer1 && showAllItems}
            </div>
            <div>
                <h3>Show one Product by Id:</h3>
                <input type="text" placeholder="Enter Product ID" onChange={(e) => getOneProduct(e.target.value)} />
                {viewer2 && showOneItem}
            </div>

            <div> 
            {createProductForm}
            </div>

        </div>
    );
}

export default App;