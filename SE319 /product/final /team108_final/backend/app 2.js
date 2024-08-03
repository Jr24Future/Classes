const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cors());

// MongoDB connection
mongoose.connect("mongodb://0.0.0.0:27017/react-login-tut")
    .then(() => {
        console.log("mongodb connected");
    })
    .catch((err) => {
        console.error('MongoDB connection failed:', err);
    });

// Define schema and model
const newSchema = new mongoose.Schema({
    firstName: {
        type: String,
        required: true
    },
    lastName: {
        type: String,
        required: true
    },
    phoneNumber: {
        type: String,
        required: true
    },
    address: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    }
});

const collection = mongoose.model("collection", newSchema);

app.get("/", cors(), (req, res) => {});

app.post("/", async (req, res) => {
    const { email, password } = req.body;

    try {
        const check = await collection.findOne({ email: email });

        if (check) {
            if (check.password === password) {
                res.json({ status: "exist", userId: check._id });
            } else {
                res.json({ status: "incorrect" });
            }
        } else {
            res.json({ status: "notexist" });
        }
    } catch (e) {
        res.json("fail");
    }
});

app.post("/signup", async (req, res) => {
    const { email, password, firstName, lastName, phoneNumber, address } = req.body;

    const data = {
        email: email,
        password: password,
        firstName: firstName,
        lastName: lastName,
        phoneNumber: phoneNumber,
        address: address
    };

    try {
        const check = await collection.findOne({ email: email });

        if (check) {
            res.json("exist");
        } else {
            await collection.insertMany([data]);
            res.json("notexist");
        }
    } catch (e) {
        console.error('Error during signup:', e);
        res.json("fail");
    }
});


app.get("/user/:id", async (req, res) => {
    const { id } = req.params;

    try {
        const user = await collection.findById(id);

        if (user) {
            res.json(user);
        } else {
            res.status(404).json("notexist");
        }
    } catch (e) {
        console.error('Error in GET /user/:id:', e);
        res.status(500).json("fail");
    }
});

app.put("/settings", async (req, res) => {
    const { userId, firstName, lastName, phoneNumber, address, password } = req.body;

    try {
        const user = await collection.findById(userId);

        if (user) {
            user.firstName = firstName;
            user.lastName = lastName;
            user.phoneNumber = phoneNumber;
            user.address = address;
            user.password = password;
            await user.save();
            res.json("success");
        } else {
            res.status(404).json("notexist");
        }
    } catch (e) {
        console.error('Error in PUT /settings:', e);
        res.status(500).json("fail");
    }
});

// Delete user
app.delete("/user/:id", async (req, res) => {
    const { id } = req.params;

    try {
        const user = await collection.findByIdAndDelete(id);

        if (user) {
            res.json("success");
        } else {
            res.status(404).json("notexist");
        }
    } catch (e) {
        console.error('Error in DELETE /user/:id:', e);
        res.status(500).json("fail");
    }
});

app.listen(8000, () => {
    console.log("Server running on port 8000");
});
