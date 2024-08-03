// errollb_activity15_frontend.js
// Author: Erroll Barker
// ISU Netid: errollb@iastate.edu
// Date: 4/18/2024

function fetchAllRobots() {
  fetch('http://localhost:8081/listRobots')
    .then(response => response.json())
    .then(robots => {
      const robotsContainer = document.getElementById('robots');
      robotsContainer.innerHTML = ''; // Clear the container
      robots.forEach(robot => {
        const robotElement = document.createElement('div');
        robotElement.className = 'robot';
        robotElement.innerHTML = `
          <h3>${robot.name}</h3>
          <p>Description: ${robot.description}</p>
          <p>Price: $${robot.price.toFixed(2)}</p> <!-- Displaying the price -->
          <img src="${robot.imageUrl}" alt="${robot.name}" style="height: 100px;">
          <br>
          <button onclick="deleteRobot('${robot._id}')">Delete</button>
          <button onclick="setupUpdateForm('${robot._id}')">Update</button>
        `;
        robotsContainer.appendChild(robotElement);
      });
    })
    .catch(error => console.error('Error:', error));
}

let currentRobotId = null;

function setupUpdateForm(id) {
  currentRobotId = id; // Store the current robot ID
  fetch(`http://localhost:8081/robot/${id}`)
    .then(response => response.json())
    .then(robot => {
      document.getElementById('updateRobotName').value = robot.name;
      document.getElementById('updateRobotDescription').value = robot.description;
      document.getElementById('updateRobotImageUrl').value = robot.imageUrl;
      document.getElementById('updateRobotPrice').value = robot.price;
      
      document.getElementById('updateRobotForm').scrollIntoView();
    })
    .catch(error => console.error('Error:', error));
}

function updateRobot(id) {
  const robotData = {
    name: document.getElementById('updateRobotName').value,
    description: document.getElementById('updateRobotDescription').value,
    imageUrl: document.getElementById('updateRobotImageUrl').value,
    price: parseFloat(document.getElementById('updateRobotPrice').value),
  };

  fetch(`http://localhost:8081/modifyRobot/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(robotData)
  })
  .then(response => response.json())
  .then(updatedRobot => {
    console.log('Update successful:', updatedRobot);
    fetchAllRobots(); // Refresh the list of robots
  })
  .catch(error => {
    console.error('Error:', error);
  });
}

function addRobot() {
  const robotData = {
    name: document.getElementById('robotName').value,
    description: document.getElementById('robotDescription').value,
    imageUrl: document.getElementById('robotImageUrl').value,
    price: parseFloat(document.getElementById('robotPrice').value),
  };

  fetch('http://localhost:8081/addRobot', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(robotData),
  })
  .then(response => response.json())
  .then(data => {
    console.log('Success:', data);
    fetchAllRobots(); // Refresh the list of robots
  })
  .catch((error) => {
    console.error('Error:', error);
  });
}

function deleteRobot(id) {
  fetch(`http://localhost:8081/deleteRobot/${id}`, {
    method: 'DELETE'
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Delete operation failed');
    }
    return response.text(); // or .json() if your server sends a JSON response
  })
  .then(() => {
    console.log('Robot deleted');
    fetchAllRobots(); // Refresh the list of robots
  })
  .catch(error => {
    console.error('Error:', error);
  });
}

function getRobot() {
  const robotId = document.getElementById('robotId').value;
  if (!robotId || robotId.length !== 24) {
    alert("Please enter a valid 24-character robot ID");
    return;
  }

  fetch(`http://localhost:8081/robot/${robotId}`)
    .then(response => {
      if (!response.ok) {
        throw new Error('No robot found');
      }
      return response.json();
    })
    .then(robot => {
      const robotDetailsContainer = document.getElementById('robotDetails');
      robotDetailsContainer.innerHTML = `
        <div>
          <h3>${robot.name}</h3>
          <p>${robot.description}</p>
          <img src="${robot.imageUrl}" alt="${robot.name}" height="100">
        </div>`;
    })
    .catch(error => {
      console.error('Error fetching robot:', error);
      document.getElementById('robotDetails').innerHTML = 'Robot not found';
    });
}

window.onload = fetchAllRobots;
