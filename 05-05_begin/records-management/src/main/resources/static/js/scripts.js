// scripts.js
htmx.config.allowNestedOobSwaps = false;

function swapFade(elementId, shown){
    const element = document.querySelector('#'+elementId);
    if( shown ){
        element.classList.remove('fade-out');
        element.classList.add('fade-in');
    } else {
        element.classList.add('fade-out');
        element.classList.remove('fade-in');
    }
}

function removeStyleClasses(classNames) {
    // Split the comma-delimited string into an array of class names
    const classes = classNames.split(',').map(className => className.trim());

    // Loop through each class name
    classes.forEach(className => {
        // Select all elements with the current class name
        const elements = document.querySelectorAll(`.${className}`);

        // Remove the class from each element
        elements.forEach(element => {
            element.classList.remove(className);
        });
    });
}


document.body.addEventListener('htmx:configRequest', function(event) {
    const target = event.target;
    const csrfHeader = target.getAttribute('data-csrf-header');
    const csrfToken = target.getAttribute('data-csrf-token');

    // If CSRF attributes are found, add them to the request headers
    if (csrfHeader && csrfToken) {
        event.detail.headers[csrfHeader] = csrfToken;
    }
});

// Listen directly for the 'emailUpdated' event
if(!window.emailUpdatedListenerAdded){
    window.emailUpdatedListenerAdded = true;

    document.body.addEventListener('emailUpdated', function(event) {

        const message = event.detail.message || 'Email updated successfully';
        const level = event.detail.level || 'info';

        // Delay the alert display to allow transitions to complete
        setTimeout(function() {
            const targetElement = document.getElementById('email-alert-message');
            targetElement.innerHTML = "";

            if (targetElement) {
                const alertDiv = document.createElement('div');
                alertDiv.className = `alert alert-${level}`;
                alertDiv.role = 'alert';
                alertDiv.innerText = message;

                targetElement.appendChild(alertDiv);

                // Remove the alert after 3 seconds
                setTimeout(function() {
                    //modify from alertDiv to targetElement
                    targetElement.classList.add('fade-out');
                    targetElement.addEventListener('transitionend', function() {
                        alertDiv.remove();
                        targetElement.classList.remove('fade-out');
                    }, { once: true });
                }, 3000);

            } else {
                console.error('Alert target element not found');
            }
        }, 600); // Adjust delay based on your transition duration
    });
}
