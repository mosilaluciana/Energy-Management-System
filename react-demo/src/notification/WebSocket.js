// import React, { Component } from 'react';
// import SockJS from 'sockjs-client';
// import Stomp from 'stompjs';
// import {HOST} from "../commons/hosts";
//
// class WebSocket extends Component {
//     constructor(props) {
//         super(props);
//         this.state = {
//             notifications: [],
//             userId: localStorage.getItem("userId"), // Initialize userId from localStorage
//         };
//         this.stompClient = null; // STOMP client instance
//         this.sentNotifications = new Set(); // Track already sent notifications
//     }
//
//     componentDidMount() {
//         this.connectWebSocket();
//
//         // Monitor localStorage changes
//         this.storageInterval = setInterval(() => {
//             const storedUserId = localStorage.getItem("userId");
//             if (storedUserId !== this.state.userId) {
//                 console.log("Detected userId change in localStorage:", storedUserId);
//                 this.setState({ userId: storedUserId }, () => {
//                     this.reconnectWebSocket();
//                 });
//             }
//         }, 100); // Check every 100ms
//     }
//
//     componentWillUnmount() {
//         if (this.stompClient) {
//             this.stompClient.disconnect(); // Disconnect WebSocket when unmounting
//         }
//         clearInterval(this.storageInterval); // Clear interval for localStorage monitoring
//     }
//
//     connectWebSocket() {
//         const socket = new SockJS(`${HOST.backend_device_measurement}/ws`); // Your WebSocket URL
//         this.stompClient = Stomp.over(socket); // Create a STOMP client
//         const { userId } = this.state;
//
//         if (!userId) {
//             console.error("No userId available to subscribe to WebSocket topics.");
//             return;
//         }
//
//         this.stompClient.connect({}, (frame) => {
//             console.log('Connected: ' + frame);
//
//             // Subscribe to the notifications topic
//             this.subscribeToNotifications(userId);
//         }, (error) => {
//             console.error("WebSocket connection error:", error);
//         });
//     }
//
//     reconnectWebSocket() {
//         if (this.stompClient) {
//             console.log("Reconnecting WebSocket for new userId...");
//             this.stompClient.disconnect(() => {
//                 this.connectWebSocket();
//             });
//         } else {
//             this.connectWebSocket();
//         }
//     }
//
//     subscribeToNotifications(userId) {
//         this.stompClient.subscribe(`/topic/notifications/${userId}`, (messageOutput) => {
//             const notification = messageOutput.body;
//             this.addNotification(notification);
//         });
//     }
//
//     addNotification(message) {
//         // Prevent duplicate notifications
//         if (this.sentNotifications.has(message)) {
//             console.log("Duplicate notification detected, ignoring:", message);
//             return;
//         }
//
//         // Add notification to state and track it
//         this.setState((prevState) => ({
//             notifications: [...prevState.notifications, message],
//         }));
//         this.sentNotifications.add(message); // Track this notification
//     }
//
//     renderNotifications() {
//         return this.state.notifications.map((message, index) => (
//             <div key={index} style={{ border: '1px solid #ccc', margin: '5px', padding: '10px' }}>
//                 <p><strong>Notification:</strong> {message}</p>
//             </div>
//         ));
//     }
//
//     render() {
//         return (
//             <div>
//                 <h3>Notifications</h3>
//                 {this.renderNotifications()}
//             </div>
//         );
//     }
// }
//
// export default WebSocket;
import React, { Component } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { HOST } from "../commons/hosts";

class WebSocket extends Component {
    constructor(props) {
        super(props);
        this.state = {
            notifications: [],
            userId: localStorage.getItem("userId"), // Initialize userId from localStorage
        };
        this.stompClient = null; // STOMP client instance
        this.sentNotifications = new Set(); // Track already sent notifications
    }

    componentDidMount() {
        this.connectWebSocket();

        // Monitor localStorage changes
        this.storageInterval = setInterval(() => {
            const storedUserId = localStorage.getItem("userId");
            if (storedUserId !== this.state.userId) {
                console.log("Detected userId change in localStorage:", storedUserId);
                this.setState({ userId: storedUserId }, () => {
                    this.reconnectWebSocket();
                });
            }
        }, 100); // Check every 100ms
    }

    componentWillUnmount() {
        // Ensure we disconnect only if the WebSocket client is connected
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.disconnect(() => {
                console.log("WebSocket disconnected successfully.");
            });
        } else {
            console.log("WebSocket client is not connected yet, skipping disconnect.");
        }

        clearInterval(this.storageInterval); // Clear interval for localStorage monitoring
    }

    connectWebSocket() {
        const socket = new SockJS(`${HOST.backend_device_measurement}/ws`); // Your WebSocket URL
        this.stompClient = Stomp.over(socket); // Create a STOMP client
        const { userId } = this.state;

        if (!userId) {
            console.error("No userId available to subscribe to WebSocket topics.");
            return;
        }

        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);

            // Subscribe to the notifications topic
            this.subscribeToNotifications(userId);
        }, (error) => {
            console.error("WebSocket connection error:", error);
        });
    }

    reconnectWebSocket() {
        if (this.stompClient) {
            console.log("Reconnecting WebSocket for new userId...");
            this.stompClient.disconnect(() => {
                this.connectWebSocket();
            });
        } else {
            this.connectWebSocket();
        }
    }

    subscribeToNotifications(userId) {
        this.stompClient.subscribe(`/topic/notifications/${userId}`, (messageOutput) => {
            const notification = messageOutput.body;
            this.addNotification(notification);
        });
    }

    addNotification(message) {
        // Prevent duplicate notifications
        if (this.sentNotifications.has(message)) {
            console.log("Duplicate notification detected, ignoring:", message);
            return;
        }

        // Add notification to state and track it
        this.setState((prevState) => ({
            notifications: [...prevState.notifications, message],
        }));
        this.sentNotifications.add(message); // Track this notification
    }

    renderNotifications() {
        return this.state.notifications.map((message, index) => (
            <div key={index} style={{ border: '1px solid #ccc', margin: '5px', padding: '10px' }}>
                <p><strong>Notification:</strong> {message}</p>
            </div>
        ));
    }

    render() {
        return (
            <div>
                <h3>Notifications</h3>
                {this.renderNotifications()}
            </div>
        );
    }
}

export default WebSocket;
