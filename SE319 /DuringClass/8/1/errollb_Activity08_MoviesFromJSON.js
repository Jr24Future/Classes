//Author: Erroll Barker 
//ISU Netid : errollb@iastate.edu
//Date : 02, 25, 2024
function loadMovies(myMovies) {
    var mainContainer = document.getElementById("goodmovies");
    console.log("Loading movies...");
    for (var i = 0; i < myMovies.movies.length; i++) {
        console.log("Movie " + (i + 1) + ": " + myMovies.movies[i].title);
        var div = document.createElement("div");
        div.innerHTML = '<h2>Title: ' + myMovies.movies[i].title + '</h2>' 
        + '<p>Year: ' + myMovies.movies[i].year + '</p>'
        + '<img src="' + myMovies.movies[i].url + '" alt="' + myMovies.movies[i].title + '" style="width:200px; height:300px;">';
        mainContainer.appendChild(div);
    }
    console.log("Movies loaded successfully.");
}

fetch("./errollb_Activity08_MoviesFromJSON.json")
    .then(response => response.json())
    .then(myMovies => {
        console.log("Movies fetched successfully.");
        loadMovies(myMovies);
    })
    .catch(error => console.error("Error loading movies:", error));





