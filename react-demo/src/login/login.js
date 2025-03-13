// import axios from "axios";
// import React, { useState } from "react";
// import { useNavigate } from "react-router-dom";
// import {HOST} from "../commons/hosts";
//
//
// export default function Login() {
//     const navigate = useNavigate();
//     const [customer, setCustomer] = useState({
//         address: "",
//         password: "",
//     });
//
//     const [errors, setErrors] = useState({
//         address: false,
//         password: false,
//         notRegistered: false,
//         general: false, // New state for general error
//     });
//
//     const { address, password } = customer;
//
//     const onInputChange = (e) => {
//         setCustomer({ ...customer, [e.target.name]: e.target.value });
//         if (errors[e.target.name]) {
//             setErrors({ ...errors, [e.target.name]: false, general: false });
//         }
//     };
//
//     const validateForm = () => {
//         const newErrors = {
//             address: !validateaddress(customer.address),
//             password: !validatePassword(customer.password),
//         };
//
//         setErrors(newErrors);
//         return !Object.values(newErrors).some(isError => isError);
//     };
//
//     const validateaddress = (address) => {
//         const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
//         return regex.test(address);
//     };
//
//     const validatePassword = (password) => {
//         return password.length >= 4;
//     };
//
//     const onSubmit = async (e) => {
//         e.preventDefault();
//
//         if (validateForm()) {
//             try {
//                 const response = await axios.post(
//                     `${HOST.backend_user}/person/login?address=${address}&password=${password}`
//                 );
//
//                 const role = response.data;
//
//                 if (role !== null) {
//                     console.log("Login successful:", role);
//                     localStorage.setItem('loggedInUsername', address);
//                     localStorage.setItem('userRole', role);
//
//                     if (role === true) {
//                         navigate("/person");
//                     }
//                     if(role === false){
//                         navigate("/user-devices");
//                     }
//                 }
//             } catch (error) {
//                 if (error.response && error.response.status === 404) {
//                     console.error("Error during login: address not found");
//                     setErrors({ address: true, password: true, notRegistered: true, general: true }); // Set both to true
//                 } else {
//                     console.error("Error during login:", error);
//                 }
//             }
//         } else {
//             // If validation fails, set general error to true
//             setErrors({ ...errors, general: true, address: true, password: true });
//         }
//     };
//
//     return (
//         <div className="container">
//             <div className="row">
//                 <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
//                     <h2 className="text-center m-4">Log In</h2>
//
//                     <form onSubmit={onSubmit}>
//                         <div className="mb-3">
//                             <label htmlFor="address" className={`form-label ${errors.address || errors.notRegistered || errors.general ? 'text-danger' : ''}`}>
//                                 E-mail {errors.address && <span className="text-danger">- Invalid address format</span>}
//                                 {errors.notRegistered && <span className="text-danger">- address not registered</span>}
//                                 {errors.general && <span className="text-danger">- address or password invalid</span>} {/* General error message */}
//                             </label>
//                             <input
//                                 type="text"
//                                 className={`form-control ${errors.address || errors.notRegistered || errors.general ? 'is-invalid' : ''}`}
//                                 placeholder="Enter your address"
//                                 name="address"
//                                 value={address}
//                                 onChange={onInputChange}
//                             />
//                         </div>
//                         <div className="mb-3">
//                             <label htmlFor="Password" className={`form-label ${errors.password || errors.general ? 'text-danger' : ''}`}>
//                                 Password {errors.password && <span className="text-danger">- Password must be at least 4 characters long</span>}
//                             </label>
//                             <input
//                                 type="password"
//                                 className={`form-control ${errors.password || errors.general ? 'is-invalid' : ''}`}
//                                 placeholder="Enter your password"
//                                 name="password"
//                                 value={password}
//                                 onChange={onInputChange}
//                             />
//                         </div>
//
//                         <button type="submit" className="btn btn-outline-danger mx-2">
//                             Log In
//                         </button>
//                     </form>
//                 </div>
//             </div>
//         </div>
//     );
// }
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { HOST } from "../commons/hosts";

export default function Login() {
    const navigate = useNavigate();
    const [customer, setCustomer] = useState({
        address: "",
        password: "",
    });

    const [errors, setErrors] = useState({
        address: false,
        password: false,
        notRegistered: false,
        general: false,
    });

    const { address, password } = customer;

    const onInputChange = (e) => {
        setCustomer({ ...customer, [e.target.name]: e.target.value });
        if (errors[e.target.name]) {
            setErrors({ ...errors, [e.target.name]: false, general: false });
        }
    };

    const validateForm = () => {
        const newErrors = {
            address: !validateAddress(customer.address),
            password: !validatePassword(customer.password),
        };

        setErrors(newErrors);
        return !Object.values(newErrors).some((isError) => isError);
    };

    const validateAddress = (address) => {
        const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return regex.test(address);
    };

    const validatePassword = (password) => {
        return password.length >= 4;
    };

    const onSubmit = async (e) => {
        e.preventDefault();

        if (validateForm()) {
            try {
                const response = await axios.post(
                    `${HOST.backend_user}/api/auth/login?address=${address}&password=${password}`
                );

                const { accessToken, tokenType } = response.data;

                if (accessToken) {
                    console.log("Login successful, JWT token received:", accessToken);

                    // Store the JWT token in localStorage with the token type
                    const fullToken = `${tokenType.trim()} ${accessToken}`;
                    localStorage.setItem("jwtToken", fullToken);
                    localStorage.setItem("loggedInUsername", address);

                    // Decode the token to check for roles
                    const tokenPayload = JSON.parse(atob(accessToken.split(".")[1]));
                    const roles = tokenPayload.roles;

                    console.log("Decoded JWT roles:", roles);

                    localStorage.setItem("userRole", roles);

                    if (roles && roles.includes("[ADMIN]")) {
                        console.log("User is an admin");
                        navigate("/person");
                    } else {
                        console.log("User is not an admin");
                        navigate("/user-devices");
                    }
                }
            } catch (error) {
                if (error.response && error.response.status === 401) {
                    console.error("Error during login: Invalid credentials");
                    setErrors({ address: true, password: true, general: true });
                } else {
                    console.error("Error during login:", error);
                    setErrors({ general: true });
                }
            }
        } else {
            setErrors({ ...errors, general: true });
        }
    };

    useEffect(() => {
        // This hook will trigger after the component renders
        // You could also add any cleanup logic here if needed
    }, [errors]);

    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
                    <h2 className="text-center m-4">Log In</h2>

                    <form onSubmit={onSubmit}>
                        <div className="mb-3">
                            <label
                                htmlFor="address"
                                className={`form-label ${
                                    errors.address || errors.general ? "text-danger" : ""
                                }`}
                            >
                                E-mail{" "}
                                {errors.address && (
                                    <span className="text-danger">- Invalid address format</span>
                                )}
                                {errors.general && (
                                    <span className="text-danger">- Invalid credentials</span>
                                )}
                            </label>
                            <input
                                type="text"
                                className={`form-control ${
                                    errors.address || errors.general ? "is-invalid" : ""
                                }`}
                                placeholder="Enter your address"
                                name="address"
                                value={address}
                                onChange={onInputChange}
                            />
                        </div>
                        <div className="mb-3">
                            <label
                                htmlFor="Password"
                                className={`form-label ${
                                    errors.password || errors.general ? "text-danger" : ""
                                }`}
                            >
                                Password{" "}
                                {errors.password && (
                                    <span className="text-danger">
                                        - Password must be at least 4 characters long
                                    </span>
                                )}
                            </label>
                            <input
                                type="password"
                                className={`form-control ${
                                    errors.password || errors.general ? "is-invalid" : ""
                                }`}
                                placeholder="Enter your password"
                                name="password"
                                value={password}
                                onChange={onInputChange}
                            />
                        </div>

                        <button type="submit" className="btn btn-outline-danger mx-2">
                            Log In
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}
