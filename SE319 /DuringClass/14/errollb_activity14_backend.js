// errollb_activity14_backend.js
// Author: erroll barker
// ISU Netid: errollb@iastate.edu
// Date: 4/16/2024

const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const { MongoClient } = require('mongodb');

const app = express();
app.use(cors());
app.use(bodyParser.json());

const url = "mongodb://127.0.0.1:27017";
const dbName = "secoms319";
let db;

const client = new MongoClient(url);

app.get("/listRobots", async (req, res) => {
  try {
    await client.connect();
    db = client.db(dbName);
    const results = await db.collection("robot").find({}).toArray();
    res.status(200).json(results);
  } catch (error) {
    res.status(500).json({ error: error.message });
  } finally {
    await client.close();
  }
});

app.get("/:id", async (req, res) => {
  const robotId = parseInt(req.params.id);
  try {
    await client.connect();
    db = client.db(dbName);
    const robot = await db.collection("robot").findOne({ id: robotId });
    if (robot) {
      res.status(200).json(robot);
    } else {
      res.status(404).send("Robot not found");
    }
  } catch (error) {
    res.status(500).json({ error: error.message });
  } finally {
    await client.close();
  }
});

const port = 8081;
app.listen(port, () => console.log(`Server listening at http://localhost:${port}`));
