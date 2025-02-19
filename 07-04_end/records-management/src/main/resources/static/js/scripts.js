// scripts.js
htmx.config.allowNestedOobSwaps = false;
htmx.config.useTemplateFragments = true;

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

htmx.onLoad(function(content){
    initSortable(content);
})

function initSortable(content){
    var sortables = content.querySelectorAll('#prior-school-list');
    for(var i = 0; i < sortables.length; i++){
        var sortable = sortables[i];

        var sortableInstance = new Sortable(sortable, {
            animation: 150,
            filter: ".htmx-indicator",
            onMove : function(evt){
                return evt.related.className.indexOf('htmx-indicator') === -1;
            },
            onEnd : function(evt){
                this.option("disabled", true);
            }
        });

        sortable.addEventListener("htmx:afterSwap", function(){
            sortableInstance.option("disabled", false);
        })
    }
}

//if (!window.htmxConfigRequestListenerAdded) {
//    document.body.addEventListener('htmx:configRequest', function(event) {
//        const target = event.target;
//        const csrfHeader = target.getAttribute('data-csrf-header');
//        const csrfToken = target.getAttribute('data-csrf-token');
//
//        // If CSRF attributes are found, add them to the request headers
//        if (csrfHeader && csrfToken) {
//            event.detail.headers[csrfHeader] = csrfToken;
//        }
//    });
//    window.htmxConfigRequestListenerAdded = true;
//}
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