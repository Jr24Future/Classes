// Read the file with movies:
fetch("./errollb_Activity08_MoviesFromJSON.json")
    .then(response => response.json())
    .then(myMovies => loadMovies(myMovies));

// Function loadMovies:
function loadMovies(myMovies) {
    // Find the element “col” in HTML
    var CardMovie = document.getElementById("col");
    var checkboxes = [];
    var cards = [];

    for (var i = 0; i < myMovies.movies.length; i++) {
        let title = myMovies.movies[i].title;
        let year = myMovies.movies[i].year;
        let url = myMovies.movies[i].url;

        let checkbox = "checkbox" + i.toString();
        let card = "card" + i.toString();

        // create a new division for each movie
        var AddCardMovie = document.createElement("div");

        // create Bootstrap card
        AddCardMovie.innerHTML = `
            <input type="checkbox" id=${checkbox} class="form-check-input" checked>
            <label for=${checkbox} class="form-check-label">Show Image ${i}</label>
            <div id=${card} class="card shadow-sm">
                <img src=${url} class="card-img-top" alt="...">
                <div class="card-body">
                    <p class="card-text"> <strong>${title}</strong>, ${year}</p>
                    <div class="d-flex justify-content-between align-items-center">
                    </div>
                </div>
            </div>
        `;

        // append new division
        CardMovie.appendChild(AddCardMovie);

        // Get the elements by ID and push to arrays
        let cbox = document.getElementById(checkbox);
        checkboxes.push(cbox);
        let ccard = document.getElementById(card);
        cards.push(ccard);
    } // end of for

    // Add event listeners to checkboxes to toggle card visibility
    checkboxes.forEach((checkboxParam, index) => {
        checkboxParam.addEventListener('change', () => {
            if (checkboxParam.checked) {
                cards[index].style.display = 'block'; // Show the card
            } else {
                cards[index].style.display = 'none'; // Hide the card
            }
        });
    });

    // Log the arrays to see the formed words
    console.log("checkboxes:", checkboxes);
    console.log("cards:", cards);
} // end of function