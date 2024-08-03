function getInputValue() {
    let movieName = document.forms["my_form"]["inputMovieName"];
    let inputMovieName = movieName.value;
    fetch("./errollb_Activity08_MoviesFromJSON.json")
    .then(response => response.json())
    .then(myMovies => {
        loadMovies(myMovies, inputMovieName);
    });
}

function loadMovies(myMovies, inputMovieName) {
    var mainContainer = document.getElementById("goodmovies");
    mainContainer.innerHTML = ''; // Clear previous results
    for (var i = 0; i < myMovies.movies.length; i++) {
        if (myMovies.movies[i].title === inputMovieName) {
            var div = document.createElement("div");
            div.innerHTML = '<h2>Title: ' + myMovies.movies[i].title + '</h2>' 
            + '<p>Year: ' + myMovies.movies[i].year + '</p>'
            + '<img src="' + myMovies.movies[i].url + '" alt="' + myMovies.movies[i].title + '" style="width:200px; height:300px;">';
            mainContainer.appendChild(div);
            break; // Stop the loop once the movie is found
        }
    }
}
