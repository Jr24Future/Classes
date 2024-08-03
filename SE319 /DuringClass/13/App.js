const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");
const fs = require("fs");

const app = express();
app.use(cors());
app.use(bodyParser.json());

const port = 8081;
const host = 'localhost';

// Serve HTML at the root route
app.get("/", (req, res) => {
  res.send(`
    <html>
      <head>
        <title>Hello Node</title>
        <style>
          body { background-color: black; color: green; font-size: 24px; }
          h1 { border: 0px solid; padding: 20px; }
        </style>
      </head>
      <body>
        <h1>Hello World From Node</h1>
      </body>
    </html>
  `);
});

// Endpoint to serve robots data from a JSON file
app.get("/listRobots", (req, res) => {
  fs.readFile(__dirname + "/robots.json", "utf8", (err, data) => {
    if (err) {
      console.error("Error reading file:", err);
      return res.status(500).send("Error reading file.");
    }
    res.status(200).send(data);
  });
});

// Endpoint to serve a JavaScript object as JSON
app.get("/person", (req, res) => {
  const person = {
    name: 'alex',
    email: 'alex@mail.com',
    job: 'software developer'
  };
  res.status(200).json(person); // .json() method sets appropriate Content-Type
});

app.listen(port, () => {
  console.log(`App listening at http://${host}:${port}`);
});
