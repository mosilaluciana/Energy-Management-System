import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ element, requiredRole }) => {
    const userRole = localStorage.getItem('userRole');


    if (!requiredRole.includes(userRole)) {
        return <Navigate to="/" replace />;
    }

    return element;
};

export default ProtectedRoute;
