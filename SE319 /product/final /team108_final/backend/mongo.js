const mongoose = require("mongoose");

mongoose.connect("mongodb://0.0.0.0:27017/react-login-tut")
    .then(() => {
        console.log("mongodb connected");
    })
    .catch(() => {
        console.log('failed');
    });

const newSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    lastname: {
        type: String,
        required: true
    },
    phone: {
        type: String,
        required: true
    },
    address: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true,
        unique: true
    },
    password: {
        type: String,
        required: true
    }
});

const collection = mongoose.model("collection", newSchema);

module.exports = collection;
