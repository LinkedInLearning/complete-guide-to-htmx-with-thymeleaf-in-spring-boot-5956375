(function() {
  htmx.defineExtension('csrf-ext', {
    onEvent: function(name, evt) {
      if (name === 'htmx:configRequest') {
        // Use global CSRF configuration if available
        if (window.csrfConfig) {
          const { headerName, token } = window.csrfConfig;
          evt.detail.headers = evt.detail.headers || {};
          evt.detail.headers[headerName] = token;
        }
      }
    }
  });
})();
