document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll('.offcanvas-body .nav-link').forEach(function(link) {
      link.addEventListener('click', function(event) {
        event.preventDefault();
  
        var slideTo = link.getAttribute('data-bs-slide-to'); // Get the carousel slide index
        var targetSectionId = link.getAttribute('href'); // Get the target section ID from the href attribute
        var bsCarousel = bootstrap.Carousel.getInstance(document.querySelector('#engineeringCarousel'));
        
        bsCarousel.to(slideTo); // Slide the carousel to the specific index
  
        // Smooth scroll to the section after the carousel slides into view
        setTimeout(function() {
          var targetSection = document.querySelector(targetSectionId);
          if (targetSection) {
            targetSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
          }
        }, 300);
  
        var offcanvasElement = document.querySelector('.offcanvas');
        var bsOffcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
        bsOffcanvas.hide();
      });
    });
  });
