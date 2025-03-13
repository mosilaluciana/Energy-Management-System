import React, { useEffect, useState, useRef } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { HOST } from "../../commons/hosts";

const UserChatApp = () => {
    const [messages, setMessages] = useState([]);
    const [inputMessage, setInputMessage] = useState('');
    const [canReply, setCanReply] = useState(false);
    const [isConnected, setIsConnected] = useState(false);
    const [userAddress] = useState(localStorage.getItem('loggedInUsername'));  // Adresa utilizatorului logat
    const stompClientRef = useRef(null);
    const subscriptionRef = useRef(null);  // Referință pentru subscription
    const messagesEndRef = useRef(null);  // Referință pentru sfârșitul conversației
    const isTypingRef = useRef(false);  // Referință pentru a verifica dacă utilizatorul scrie

    useEffect(() => {
        const socket = new SockJS(`${HOST.backend_chat}/chat`);
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            setIsConnected(true);  // Setează că conexiunea este stabilită

            if (subscriptionRef.current) {
                subscriptionRef.current.unsubscribe();
            }

            const userTopic = `/topic/${userAddress}`;
            subscriptionRef.current = stompClient.subscribe(userTopic, (message) => {
                const receivedMessage = JSON.parse(message.body);
                setMessages((prev) => [...prev, receivedMessage]);

                if (receivedMessage.sender === 'admin@gmail.com') {
                    setCanReply(true);
                }
            });

            stompClientRef.current = stompClient;  // Salvează stompClient
        }, (error) => {
            console.error('WebSocket connection error:', error);
        });

        return () => {
            if (subscriptionRef.current) {
                subscriptionRef.current.unsubscribe();
            }
            if (stompClientRef.current) {
                stompClientRef.current.disconnect(() => {
                    console.log('WebSocket disconnected.');
                });
            }
        };
    }, [userAddress]);

    const sendMessage = () => {
        if (stompClientRef.current && inputMessage.trim() && canReply && isConnected) {
            const message = { sender: userAddress, receiver: 'admin@gmail.com', content: inputMessage };

            stompClientRef.current.send('/app/send', {}, JSON.stringify(message));
            setMessages((prev) => [...prev, message]);
            setInputMessage('');
            isTypingRef.current = false;  // Resetăm referința la `false` după trimiterea mesajului
        }
    };

    const sendTypingStatus = () => {
        if (stompClientRef.current && inputMessage.trim() && !isTypingRef.current) {
            isTypingRef.current = true;
            const typingMessage = {
                sender: userAddress,
                receiver: 'admin@gmail.com',
                content: 'typing'
            };
            stompClientRef.current.send('/app/send', {}, JSON.stringify(typingMessage));
        }
    };

    const handleTyping = (event) => {
        setInputMessage(event.target.value);

        // Trimite "typing" doar dacă utilizatorul începe să scrie și "typing" nu a fost deja trimis
        if (event.target.value.trim() && !isTypingRef.current) {
            sendTypingStatus();  // Trimite mesajul "typing"
        }
    };

    const sendReadReceipt = (message) => {
        const readMessage = { sender: userAddress, receiver: 'admin@gmail.com', content: 'Seen' };
        stompClientRef.current.send('/app/send', {}, JSON.stringify(readMessage));
    };

    useEffect(() => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    }, [messages]);

    const handleMessageRead = (message) => {
        sendReadReceipt(message);
    };

    return (
        <div>
            <h2>Chat with Admin</h2>
            <div>
                {messages.map((msg, idx) => (
                    <div key={idx} onClick={() => handleMessageRead(msg)}>
                        <strong>{msg.sender}:</strong>
                        {msg.content === 'Seen' ? (
                            <span style={{ color: 'blue' }}>Seen</span>
                        ) : msg.content === 'typing' ? (
                            <span style={{ fontStyle: 'italic', color: 'gray' }}>Typing...</span>
                        ) : (
                            ` ${msg.content}`
                        )}
                    </div>
                ))}
                <div ref={messagesEndRef} />
            </div>
            <input
                type="text"
                value={inputMessage}
                onChange={handleTyping}
                disabled={!canReply}
            />
            <button onClick={sendMessage} disabled={!canReply || !isConnected}>
                {canReply ? 'Send' : 'Reply Disabled'}
            </button>
            {!isConnected && <p>Connecting...</p>}
        </div>
    );
};

export default UserChatApp;


