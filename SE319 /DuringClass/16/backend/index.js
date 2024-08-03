    //Author: Erroll Barker
    //ISU Netid : errollb@iastate.edu
    //Date :  4/26, 2024

const express = require("express");
const db = require("./db.js");
const cors = require("cors");

const app = express();
const PORT = 4000;

app.use(cors());
app.use(express.json());

app.get("/catalog", async (req, res) => {
    try {
        const query = "SELECT * FROM fakestore_catalog";
        const [result] = await db.query(query); 
        console.log("Success in Reading MySQL");
        res.status(200).send(result); 
    } catch (err) {
        console.error("Error in Reading MySQL :", err);
        res.status(500).send({ error: 'An error occurred while fetching items.' });
    }
});

app.get("/catalog/:id", async (req, res) => {
    try {
        const id = req.params.id;
        const query = "SELECT * FROM fakestore_catalog WHERE id = ?";
        const [result] = await db.query(query, [id]); 
        console.log("Success in Reading MySQL");
        res.status(200).send(result);
    } catch (err) {
        console.error("Error in Reading MySQL :", err);
        res.status(500).send({ error: 'An error occurred while fetching items.' });
    }
});

app.post("/catalog", async (req, res) => {
    const { id, title, price, description, category, image, rating } = req.body;
    console.log("Received body for new product:", req.body);

    if (!id||!title || !price || !description || !category || !image || !rating) {
        return res.status(400).json({ error: 'All fields must be provided and valid.' });
    }

    try {
        const query = "INSERT INTO fakestore_catalog (id, title, price, description, category, image, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
        const [result] = await db.query(query, [id, title, price, description, category, image, rating]);
        res.status(201).json({ id, ...req.body });
    } catch (err) {
        console.error("Error in creating new product:", err);
        res.status(500).json({ error: 'An error occurred while creating the new product.', details: err.message });
    }
});


app.get("/catalog/category/:category", async (req, res) => {
    const { category } = req.params;
    console.log("Requested category:", category); 
    try {
        const query = "SELECT * FROM fakestore_catalog WHERE category = ?";
        const [results] = await db.query(query, [category]);
        console.log("Query results:", results); 
        if (results.length === 0) {
            res.status(404).json({ message: "No products found for this category." });
        } else {
            res.status(200).json(results);
        }
    } catch (err) {
        console.error("Error in Reading MySQL by Category:", err);
        res.status(500).send({ error: 'An error occurred while fetching items by category.' });
    }
});

app.put("/catalog/:id", async (req, res) => {
    const { id } = req.params;
    const { title, price, description, category, image, rating } = req.body;
    console.log(`Updating product ${id} with data:`, req.body);

    try {
        const query = "UPDATE fakestore_catalog SET title = ?, price = ?, description = ?, category = ?, image = ?, rating = ? WHERE id = ?";
        const [results] = await db.query(query, [title, price, description, category, image, rating, id]);
        
        if (results.affectedRows === 0) {
            res.status(404).json({ message: 'Product not found or no changes made' });
        } else {
            res.status(200).json({ message: 'Product updated successfully' });
        }
    } catch (err) {
        console.error("Error in Updating MySQL:", err);
        res.status(500).json({ error: 'An error occurred while updating the product.' });
    }
});

app.delete("/catalog/:id", async (req, res) => {
    const { id } = req.params;
    try {
        const query = "DELETE FROM fakestore_catalog WHERE id = ?";
        const [results] = await db.query(query, [id]);
        if (results.affectedRows === 0) {
            return res.status(404).json({ message: 'Product not found' });
        }
        res.status(200).json({ message: 'Product deleted successfully' });
    } catch (err) {
        console.error("Error in Deleting MySQL product:", err);
        res.status(500).json({ error: 'An error occurred while deleting the product.' });
    }
});


app.listen(PORT, () => {
    console.log(`Server is running on ${PORT}`);
});