function performRequest(request, callback) {
    fetch(request)
        .then(response => {
            if (!response.ok) {
                // Log status and response for better debugging
                console.error(`Error: ${response.status} ${response.statusText}`);
            }
            // Store response and read text
            return response.text().then(text => ({ text, status: response.status }));
        })
        .then(({ text, status }) => {
            try {
                const data = JSON.parse(text); // Try parsing JSON
                callback(data, status); // If successful, return parsed data
            } catch (error) {
                console.warn("Response is not valid JSON:", text);
                callback(null, status, "Invalid JSON response from server");
            }
        })
        .catch(error => {
            console.error("Network or server error:", error);
            callback(null, 500, error.message); // Return error in case of failure
        });
}
module.exports = {
    performRequest
};
