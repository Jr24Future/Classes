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

// Modified /listRobots endpoint to always return 404 Not Found
app.get("/listRobots", (req, res) => {
  res.status(404).send("404 Not Found: Robots data is unavailable.");
});

// Endpoint to serve a JavaScript object as JSON
app.get("/person", (req, res) => {
  const person = {
    name: 'alex',
    email: 'alex@mail.com',
    job: 'software developer'
  };
  res.status(200).json(person);
});

app.listen(port, () => {
  console.log(`App listening at http://${host}:${port}`);
});
