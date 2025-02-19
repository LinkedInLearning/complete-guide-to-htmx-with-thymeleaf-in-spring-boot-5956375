//used for http request (put, patch, and delete)
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
           const targetElement = document.getElementById('alert-message');
           targetElement.innerHTML = "";

           if (targetElement) {
               const alertDiv = document.createElement('div');
               alertDiv.className = `alert alert-${level}`;
               alertDiv.role = 'alert';
               alertDiv.innerText = message;

               targetElement.appendChild(alertDiv);

               // Remove the alert after 3 seconds
               setTimeout(function() {
                   alertDiv.classList.add('fade-out');
                   alertDiv.addEventListener('transitionend', function() {
                       alertDiv.remove();
                   }, { once: true });
               }, 3000);

           } else {
               console.error('Alert target element not found');
           }
       }, 600); // Adjust delay based on your transition duration
   });
}

