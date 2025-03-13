import {HOST} from '../../commons/hosts';
import RestApiClient from "../../commons/api/rest-client";
import axios from "axios";
import * as API_USERS from "../../person/api/person-api";


const endpoint = {
    device: '/device'
};
function getToken(){
    return localStorage.getItem('jwtToken');
}

//Obtine toate device-urile
function getDevices(callback) {
    let request = new Request(HOST.backend_device + endpoint.device, {
        method: 'GET',
        headers:{
            'Authorization': getToken(),
         }
    });
    console.log(getToken());
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

//Obtine un device dupa ID
function getDeviceById(params, callback){
    let request = new Request(HOST.backend_device + endpoint.device + params.id, {
       method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

// Obtine device-urile asociate unui user
function getDevicesByUserId(userId, callback) {
    let request = new Request(`${HOST.backend_device}/user/${userId}/devices`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        }
    });

    console.log("Fetching devices from URL:", request.url);
    RestApiClient.performRequest(request, callback);
}

// Obtine device-urile asociate unui user prin userId de referinta
function getDevicesByReferenceUserId(userId, callback) {
    // Construct the request URL with the userId
    const url = `${HOST.backend_device}/device/user-reference/${userId}`;
    let request = new Request(url, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Authorization': getToken(),
        }
    });

    console.log("Fetching associated devices from URL:", url);

    // Perform the request using RestApiClient and pass callback to handle the response
    RestApiClient.performRequest(request, (result, status, error) => {
        if (result !== null && status === 200) {
            // Success: Pass result and status to the callback
            callback(result, status);
        } else {
            // Log an error message and pass error details to the callback
            console.error("Error fetching devices by user ID:", error || `Status code: ${status}`);
            callback(null, status, error || "Failed to fetch devices for the specified user.");
        }
    });
}




// Fetch user ID by email
function getUserIdByEmail(email, callback) {
    let request = new Request(`${HOST.backend_user}/person/users/email/${email}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Authorization': getToken(),
        }
    });

    console.log("URL:", request.url);
    RestApiClient.performRequest(request, callback);
}




function getUserReferenceByUserId(userId, callback) {
    let request = new Request(`${HOST.backend_device}/user-reference/${userId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Authorization': getToken(),
        }
    });

    console.log("Fetching UserReference from URL:", request.url);
    RestApiClient.performRequest(request, callback);
}


function postDevice(device, callback) {
    // Step 1: Fetch the user ID based on the email
    getUserIdByEmail(device.email, (result, status, error) => {
        if (result !== null && status === 200) {
            const userId = result; // Assuming the response includes userId
            if (!userId) {
                console.error("User ID is undefined");
                callback(null, 400, "User ID is undefined");
                return; // Early return to avoid further processing
            }

            device.user_reference_id = userId;
            console.log("Fetched User ID:", userId);

            // Step 2: Fetch the UserReference based on the user ID
            getUserReferenceByUserId(userId, (userReferenceResult, userReferenceStatus, userReferenceError) => {
                if (userReferenceResult !== null && userReferenceStatus === 200) {
                    device.userReference = userReferenceResult; // Assuming the API returns UserReference

                    // Step 3: Send the updated device object to the backend
                    let request = new Request(HOST.backend_device + endpoint.device, {
                        method: 'POST',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json',
                            'Authorization': getToken()
                        },
                        body: JSON.stringify(device)
                    });

                    console.log("Device POST URL:", request.url);
                    RestApiClient.performRequest(request, callback);
                } else {
                    // Handle error when fetching the UserReference
                    console.error("Failed to fetch UserReference by user ID:", userReferenceError);
                    callback(null, userReferenceStatus, userReferenceError);
                }
            });
        } else {
            // Handle error when fetching the user ID
            console.error("Failed to fetch user ID by email:", error);
            callback(null, status, error);
        }
    });
}


// Actualizează un device pe baza id-ului
function updateDeviceByID(selectedID, device, callback) {
    console.log("Device object being sent:", JSON.stringify(device));

    fetch(`${HOST.backend_device}${endpoint.device}/device/id/${selectedID}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': getToken(),
        },
        body: JSON.stringify(device),
    })
        .then(response => {
            if (!response.ok) {
                // Throw an error with the status code if response is not OK
                throw new Error(`Server error: ${response.status}`);
            }
            // Parse response as JSON and return an object with data and status
            return response.json().then(data => ({ data, status: response.status }));
        })
        .then(({ data, status }) => callback(data, status)) // Send data and status to callback
        .catch(error => {
            console.error("Request failed:", error); // Log error to console for debugging
            callback(null, 500, error.message); // Pass error to callback with status 500
        });
}


// // Șterge un device pe baza ID-ului
// function deleteDeviceByID(selectedID) {
//     const url = `${HOST.backend_device}${endpoint.device}/device/id/${selectedID}`;
//     console.log("Deleting device at URL:", url);
//     console.log(selectedID);
//     return axios.delete(url)
//         .then(response => {
//             alert("Device deleted successfully.");
//             return response;
//         })
//         .catch(error => {
//             if (error.response && error.response.status === 404) {
//                 alert("Device not found.");
//             } else {
//                 console.error("There was an error deleting the device:", error);
//             }
//             throw error;
//         });
// }
// Șterge un device pe baza ID-ului
function deleteDeviceByID(selectedID) {
    const url = `${HOST.backend_device}${endpoint.device}/device/id/${selectedID}`;
    console.log("Deleting device at URL:", url);
    console.log(selectedID);

    // Adăugarea antetului Authorization
    const config = {
        headers: {
            Authorization: getToken() // getToken() trebuie să returneze token-ul JWT
        },
    };

    return axios.delete(url, config)
        .then(response => {
            alert("Device deleted successfully.");
            return response;
        })
        .catch(error => {
            if (error.response && error.response.status === 404) {
                alert("Device not found.");
            } else {
                console.error("There was an error deleting the device:", error);
            }
            throw error;
        });
}


// Fetch all devices associated with a user based on their email stored in localStorage
function getDevicesForCurrentUser(callback) {
    // Retrieve the email from localStorage
    const email = localStorage.getItem("loggedInUsername");

    if (!email) {
        console.error("No email found in localStorage.");
        callback(null, 400, "No email found in localStorage");
        return;
    }

    // Step 1: Fetch the user ID based on the email
    getUserIdByEmail(email, (userIdResult, userIdStatus, userIdError) => {
        if (userIdResult !== null && userIdStatus === 200) {
            const userId = userIdResult; // Assuming `userIdResult` contains the user ID directly
            localStorage.setItem("userId", userId);

            // Step 2: Use the fetched user ID to get the UserReference
            getUserReferenceByUserId(userId, (userReferenceResult, userReferenceStatus, userReferenceError) => {
                if (userReferenceResult !== null && userReferenceStatus === 200) {
                    const userReferenceId = userReferenceResult.id; // Assuming `userReferenceResult` has the `id` property

                    // Step 3: Fetch devices associated with the obtained user reference ID
                    getDevicesByReferenceUserId(userReferenceId, (devicesResult, devicesStatus, devicesError) => {
                        if (devicesResult !== null && devicesStatus === 200) {
                            // Success: Return devices to the callback
                            callback(devicesResult, devicesStatus);
                        } else {
                            // Error fetching devices
                            console.error("Error fetching devices:", devicesError);
                            callback(null, devicesStatus, devicesError);
                        }
                    });
                } else {
                    // Error fetching user reference
                    console.error("Error fetching UserReference by user ID:", userReferenceError);
                    callback(null, userReferenceStatus, userReferenceError);
                }
            });
        } else {
            // Error fetching user ID
            console.error("Error fetching user ID by email:", userIdError);
            callback(null, userIdStatus, userIdError);
        }
    });
}






// Funcția principală pentru ștergerea utilizatorului și a dispozitivelor asociate acestuia
function deleteUserWithDevices(userAddress, callback) {
    // 1. Obține userId pe baza adresei de email a utilizatorului
    getUserIdByEmail(userAddress, (userIdResult, userIdStatus, userIdError) => {
        if (userIdResult !== null && userIdStatus === 200) {
            const userId = userIdResult;

            // 2. Obține userReferenceId pe baza userId-ului
            getUserReferenceByUserId(userId, (userReferenceResult, userReferenceStatus, userReferenceError) => {
                if (userReferenceResult !== null && userReferenceStatus === 200) {
                    const userReferenceId = userReferenceResult.id;

                    // 3. Obține toate dispozitivele asociate acestui userReferenceId
                    getDevicesByReferenceUserId(userReferenceId, (devicesResult, devicesStatus, devicesError) => {
                        if (devicesResult !== null && devicesStatus === 200) {
                            // 4. Șterge fiecare dispozitiv în parte pe baza ID-ului dispozitivului
                            const deletePromises = devicesResult.map(device => deleteDeviceByID(device.id));

                            // Așteaptă până când toate dispozitivele sunt șterse
                            Promise.all(deletePromises)
                                .then(() => {
                                    console.log("Toate dispozitivele au fost șterse.");

                                    // 5. După ștergerea dispozitivelor, șterge utilizatorul
                                    API_USERS.deletePersonByAddress(userAddress) // Apelăm ștergerea utilizatorului
                                        .then(() => {
                                            console.log("Utilizatorul a fost șters cu succes.");
                                            callback(true, 200); // Succes
                                        })
                                        .catch(userDeleteError => {
                                            console.error("Eroare la ștergerea utilizatorului:", userDeleteError);
                                            callback(false, 500, userDeleteError.message);
                                        });
                                })
                                .catch(deviceDeleteError => {
                                    console.error("Eroare la ștergerea dispozitivelor:", deviceDeleteError);
                                    callback(false, 500, deviceDeleteError.message);
                                });
                        } else {
                            console.error("Eroare la obținerea dispozitivelor asociate:", devicesError);
                            callback(false, devicesStatus, devicesError);
                        }
                    });
                } else {
                    console.error("Eroare la obținerea userReferenceId:", userReferenceError);
                    callback(false, userReferenceStatus, userReferenceError);
                }
            });
        } else {
            console.error("Eroare la obținerea userId:", userIdError);
            callback(false, userIdStatus, userIdError);
        }
    });
}


function sendDateAndString  (data, callback){
    const requestBody = {
        date: data.date,
        string: data.string,  // Aici este userId
    };

    fetch(`${HOST.backend_device_measurement}/device/send-date-and-string`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': getToken(),
        },
        body: JSON.stringify(requestBody),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Server error: ${response.status}`);
            }
            return response.json();
        })
        .then(data => callback(data, 200)) // Succes
        .catch(error => {
            console.error("Request failed:", error);
            callback(null, 500, error.message); // Erorile sunt trimise în callback
        });
};



export {
    getDevices,
    getDeviceById,
    getDevicesByUserId,
    postDevice,
    updateDeviceByID,
    deleteDeviceByID,
    getDevicesForCurrentUser,
    deleteUserWithDevices,
    getUserIdByEmail,
    sendDateAndString,
};
