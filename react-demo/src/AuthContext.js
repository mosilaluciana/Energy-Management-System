import React, { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();
export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [role, setRole] = useState(() => localStorage.getItem('role') || null);

    const login = (isAdmin) => {
        const newRole = isAdmin ? true : false;
        setRole(newRole);
        localStorage.setItem('role', newRole);
    };


    useEffect(() => {
        const storedRole = localStorage.getItem('role');
        if (storedRole) {
            setRole(storedRole);
        }
    }, []);

    return (
        <AuthContext.Provider value={{ role, login}}>
            {children}
        </AuthContext.Provider>
    );
};
