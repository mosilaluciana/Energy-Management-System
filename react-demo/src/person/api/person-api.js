import {HOST} from '../../commons/hosts';
import RestApiClient from "../../commons/api/rest-client";

import axios from 'axios';

const endpoint = {
    person: '/person'
};
function getToken(){
    return localStorage.getItem('jwtToken');
}
//Obține toate persoanele
function getPersons(callback) {
    const token = getToken();
    console.log(token)
    let request = new Request(HOST.backend_user + endpoint.person, {
        method: 'GET',
        headers: {
            'Authorization': token  // Add token to the request
        }
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

// Obține o persoană după ID
function getPersonById(params, callback){
    let request = new Request(HOST.backend_user + endpoint.person + params.id, {
       method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

//Creeaza o persoana
function postPerson(user, callback){
    const token = getToken();
    let request = new Request(HOST.backend_user + endpoint.person , {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization' : token
        },
        body: JSON.stringify(user)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

// Actualizează o persoană pe baza email-ului
function updatePersonByAddress(user, callback) {
    let request = new Request(HOST.backend_user + endpoint.person + '/person/address', {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': getToken()
        },
        body: JSON.stringify(user)
    });

    console.log("URL: " + request.url);
    RestApiClient.performRequest(request, callback);
}


// Obține o persoană după address
function getPersonByAddress(address, callback) {
    let request = new Request(HOST.backend_user + endpoint.person + '/address/' + address, {
        method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}


// Șterge o persoană pe baza adresei
function deletePersonByAddress(address) {
    const url = `${HOST.backend_user}${endpoint.person}/person/address/${address}`;
    console.log("Deleting person at URL:", url);
    console.log(address);
    return axios.delete(url,{
        headers: {
            Authorization: getToken(), // Add the token to the headers
        },
    })
        .then(response => {
            alert("Person deleted successfully.");
            return response;
        })
        .catch(error => {
            if (error.response && error.response.status === 404) {
                alert("Person not found.");
            } else {
                console.error("There was an error deleting the person:", error);
            }
            throw error;
        });
}



export {
    getPersons,
    getPersonById,
    getPersonByAddress,
    postPerson,
    updatePersonByAddress,
    deletePersonByAddress
};
