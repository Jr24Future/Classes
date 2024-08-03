// errollb_activity15_backend.js
// Author: Erroll Barker
// ISU Netid: errollb@iastate.edu
// Date: 4/18/2024

const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const { MongoClient, ObjectId } = require('mongodb');
const app = express();

app.use(cors());
app.use(bodyParser.json());

const url = 'mongodb://localhost:27017';
const dbName = 'secoms319';
const client = new MongoClient(url);
let db;

// Establish connection to MongoDB once when the server starts
async function connectMongo() {
  try {
    await client.connect();
    console.log("Connected successfully to MongoDB");
    db = client.db(dbName); // Assign the database handle globally
  } catch (error) {
    console.error('Failed to connect to MongoDB', error);
    process.exit(1); // Stop the process if we can't connect to the database
  }
}

connectMongo();

app.get('/listRobots', async (req, res) => {
  try {
    const robots = await db.collection('robot').find({}).toArray();
    res.status(200).json(robots);
  } catch (error) {
    console.error('Error fetching robots', error);
    res.status(500).send('Failed to fetch robots');
  }
});

app.get("/robot/:id", async (req, res) => {
  const robotId = req.params.id;
  if (!robotId || robotId.length !== 24) {
    return res.status(400).send("Invalid robot ID format");
  }

  try {
    const robot = await db.collection("robot").findOne({ _id: new ObjectId(robotId) });
    if (robot) {
      res.status(200).json(robot);
    } else {
      res.status(404).send("Robot not found");
    }
  } catch (error) {
    console.error("Error retrieving robot", error);
    res.status(500).json({ error: error.message });
  }
});

app.post('/addRobot', async (req, res) => {
  try {
    const result = await db.collection('robot').insertOne(req.body);
    if (result.acknowledged) {
      res.status(201).json({ _id: result.insertedId, ...req.body });
    } else {
      res.status(500).send('Failed to insert the robot');
    }
  } catch (error) {
    console.error('Error adding robot', error);
    res.status(500).send('Failed to add robot');
  }
});

app.put('/modifyRobot/:id', async (req, res) => {
  try {
    const result = await db.collection('robot').updateOne(
      { _id: new ObjectId(req.params.id) },
      { $set: req.body }
    );
    if (result.modifiedCount > 0) {
      res.status(200).json(result);
    } else {
      res.status(404).send('Robot not found or no changes made');
    }
  } catch (error) {
    console.error('Error modifying robot', error);
    res.status(500).send('Failed to modify robot');
  }
});

app.delete('/deleteRobot/:id', async (req, res) => {
  try {
    const result = await db.collection('robot').deleteOne({ _id: new ObjectId(req.params.id) });
    if (result.deletedCount > 0) {
      res.status(200).send('Robot deleted');
    } else {
      res.status(404).send('Robot not found');
    }
  } catch (error) {
    console.error('Error deleting robot', error);
    res.status(500).send('Failed to delete robot');
  }
});

const port = 8081;
app.listen(port, () => console.log(`Server listening at http://localhost:${port}`));
