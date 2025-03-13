import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import NavigationBar from './navigation-bar';
import Home from './home/home';
import PersonContainer from './person/person-container';
import DeviceContainer from './device/device-container';
import ErrorPage from './commons/errorhandling/error-page';
import Login from './login/login';
import UserDevicesContainer from './device/user-devices-container';
import WebSocket from './notification/WebSocket';

import AdminChatManager from './chat/adminChatLogic/adminChatManager'; // Componenta pentru admin
import UserChatApp from './chat/userChatLogic/userChatApp'; // Componenta pentru user

class App extends React.Component {
    render() {

        const userRole = localStorage.getItem('userRole');

        return (
            <div>
                <Router>
                    <Routes>
                        {/* Public route */}
                        <Route path='/' element={<Login />} />

                        {/* Admin route */}
                        <Route path='/person' element={
                            <>
                                <NavigationBar />
                                <PersonContainer />
                            </>
                        } />

                        <Route path='/device' element={
                                 <>
                                   <NavigationBar />
                                   <DeviceContainer/>
                                 </>
                        } />

                        {/* User route */}
                        <Route path='/user-devices' element={
                            <>
                                <WebSocket />
                                <NavigationBar />
                                <UserDevicesContainer />
                            </>
                        } />

                        {/* Common routes */}
                        <Route path='/home' element={
                            <>
                                <NavigationBar />
                                <Home />
                            </>
                        } />

                        {/* Chat */}
                        {userRole === '[ADMIN]' && (
                            <Route
                                path='/chat'
                                element={
                                    <>
                                        <NavigationBar />
                                        <AdminChatManager />
                                    </>
                                }
                            />
                        )}
                        {userRole === '[USER]' && (
                            <Route
                                path='/chat'
                                element={
                                    <>
                                        <NavigationBar />
                                        <UserChatApp />
                                    </>
                                }
                            />
                        )}
                        {/* Error route */}
                        <Route path='/error' element={<ErrorPage />} />
                        <Route path='*' element={<ErrorPage />} />
                    </Routes>
                </Router>
            </div>
        );
    }
}

export default App;
