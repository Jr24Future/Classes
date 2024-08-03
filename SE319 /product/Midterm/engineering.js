let vantaEffect; // Variable to store the VANTA effect

function loadTabs(engineeringInfo) {
    const navTab = document.getElementById('nav-tab');
    const tabContent = document.getElementById('nav-tabContent');
    const dynamicTitle = document.getElementById('dynamic-title');

    engineeringInfo.forEach((info, index) => {
        // Create tab button
        const tab = document.createElement('button');
        tab.className = `nav-link ${index === 0 ? 'active' : ''}`;
        tab.id = `nav-${info.title}-tab`;
        tab.dataset.bsToggle = 'tab';
        tab.dataset.bsTarget = `#nav-${info.title}`;
        tab.type = 'button';
        tab.role = 'tab';
        tab.ariaControls = `nav-${info.title}`;
        tab.ariaSelected = index === 0 ? 'true' : 'false';
        tab.textContent = info.title;
        tab.addEventListener('click', () => updateTabContent(info));
        navTab.appendChild(tab);

        // Create tab content container
        const content = document.createElement('div');
        content.className = `tab-pane fade ${index === 0 ? 'show active' : ''}`;
        content.id = `nav-${info.title}`;
        content.role = 'tabpanel';
        content.ariaLabelledby = `nav-${info.title}-tab`;
        content.innerHTML = `
            <h1 id="main-header">${info.header}</h1>
            <p class="lead">${info.description}</p>
            <img src="${info.src}" width="${info['image-width']}" height="${info['image-height']}">
            <a href="${info.link}" class="btn btn-custom margin-right">Learn more</a>
            <a href="./shopping.html" class="btn btn-custom">View products</a>
        `;
        tabContent.appendChild(content);

        // Set the initial title and image
        if (index === 0) {
            updateTabContent(info); // Set the initial content
        }
    });
}

function updateTabContent(info) {
    // Update the dynamic title and insert the image next to the title text
    const dynamicTitle = document.getElementById('dynamic-title');
    dynamicTitle.innerHTML = `<img src="${info['title-image']}" alt="${info.title}" style="height: 40px; margin-right: 0.5rem;">${info.title}`;

    // Update the VANTA.NET effect if it's already been initialized
    if (vantaEffect) {
        vantaEffect.setOptions({
            color: parseInt(info.color, 16),
            backgroundColor: 0x0
        });
    }
}

document.addEventListener("DOMContentLoaded", function() {
    // Fetch the JSON only once when the DOM content has fully loaded
    fetch('engineering-info.json')
        .then(response => response.json())
        .then(data => {
            loadTabs(data);
            if (data.length > 0) {
                updateTabContent(data[0]);
            }

            // Initialize VANTA.NET if VANTA is defined
            if (typeof VANTA !== 'undefined' && VANTA.NET) {
                vantaEffect = VANTA.NET({
                    el: "body",
                    mouseControls: true,
                    touchControls: true,
                    minHeight: 200.00,
                    minWidth: 200.00,
                    scale: 1.00,
                    scaleMobile: 1.00,
                    color: 0xffffff, // This is a default color, will be updated in updateTabContent
                    backgroundColor: 0x0
                });
            }
        })
        .catch(error => {
            console.error('Error loading the JSON data:', error);
        });
});
