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
