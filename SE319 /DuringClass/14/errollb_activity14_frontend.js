// errollb_activity14_frontend.js
// Author: Erroll Barker
// ISU Netid: errollb@iastate.edu
// Date: 4/16/2024

function getAllRobots() {
  fetch('http://localhost:8081/listRobots')
    .then(response => response.json())
    .then(robots => {
      const robotsContainer = document.getElementById('robots');
      robotsContainer.innerHTML = '';
      robots.forEach(robot => {
        robotsContainer.innerHTML += `
          <div>
            <h3>${robot.name}</h3>
            <p>${robot.description}</p>
            <img src="${robot.imageUrl}" alt="${robot.name}" height="100">
          </div>`;
      });
    })
    .catch(error => console.error('Error fetching all robots:', error));
}

function getRobot() {
  const robotId = document.getElementById('robotId').value;
  fetch(`http://localhost:8081/${robotId}`)
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

// Load all robots on page load
window.onload = getAllRobots;
