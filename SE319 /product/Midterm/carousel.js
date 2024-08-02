document.addEventListener('DOMContentLoaded', function() {
    const navbarLinks = document.querySelectorAll('.navbar-nav .nav-link');
    const engineeringCarousel = document.querySelector('#engineeringCarousel');
    const carousel = new bootstrap.Carousel(engineeringCarousel);

    navbarLinks.forEach((link, index) => {
        link.addEventListener('click', function(event) {
            event.preventDefault(); // Prevent the default link behavior

            // Programmatically move to the carousel slide corresponding to the clicked navigation link
            carousel.to(index);

            // Scroll the engineeringCarousel into view
            engineeringCarousel.scrollIntoView({ behavior: 'smooth', block: 'nearest', inline: 'nearest' });
        });
    });
});
