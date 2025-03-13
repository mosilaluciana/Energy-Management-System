import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useChatContext } from './chatContext';
import { HOST } from "../../commons/hosts";

const ChatApp = ({ receiverAddress, userAddress }) => {
    const { messages, addMessage } = useChatContext();
    const [inputMessage, setInputMessage] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const subscriptionRef = useRef(null);
    const messagesEndRef = useRef(null);
    const isTypingRef = useRef(false);

    useEffect(() => {
        const socket = new SockJS(`${HOST.backend_chat}/chat`);
        const stomp = Stomp.over(socket);

        stomp.connect({}, () => {
            console.log('Connected to WebSocket');

            if (subscriptionRef.current) {
                subscriptionRef.current.unsubscribe();
            }

            subscriptionRef.current = stomp.subscribe(`/topic/${userAddress}`, (message) => {
                const receivedMessage = JSON.parse(message.body);

                if (receivedMessage.sender === receiverAddress || receivedMessage.receiver === receiverAddress) {
                    addMessage(userAddress, receiverAddress, receivedMessage);
                } else {
                    console.log('Message received for a different chat tab:', receivedMessage);
                }
            });

            setStompClient(stomp);
        });

        return () => {
            if (subscriptionRef.current) {
                subscriptionRef.current.unsubscribe();
            }
            if (stomp) {
                stomp.disconnect(() => {
                    console.log('WebSocket disconnected.');
                });
            }
        };
    }, [userAddress, receiverAddress, addMessage]);

    const sendMessage = () => {
        if (stompClient && inputMessage.trim()) {
            const message = { sender: userAddress, receiver: receiverAddress, content: inputMessage };
            stompClient.send('/app/send', {}, JSON.stringify(message));
            addMessage(userAddress, receiverAddress, message);
            setInputMessage('');
            isTypingRef.current = false;  // Resetăm referința la `false` după trimiterea mesajului
        }
    };

    const sendReadReceipt = (message) => {
        if (stompClient) {
            const readMessage = { sender: userAddress, receiver: receiverAddress, content: 'Seen' };
            stompClient.send('/app/send', {}, JSON.stringify(readMessage));
        } else {
            console.error("Stomp client is not initialized.");
        }
    };

    // Funcția de a trimite mesajul "typing"
    const sendTypingStatus = () => {
        if (stompClient && inputMessage.trim() && !isTypingRef.current) {
            const typingMessage = {
                sender: userAddress,
                receiver: receiverAddress,
                content: 'typing'
            };
            stompClient.send('/app/send', {}, JSON.stringify(typingMessage));
            isTypingRef.current = true;  // Marcă faptul că "typing" a fost trimis
        }
    };

    const handleTyping = (event) => {
        setInputMessage(event.target.value);

        // Trimite "typing" doar dacă nu a fost trimis deja
        if (event.target.value.trim() && !isTypingRef.current) {
            sendTypingStatus();
        }
    };

    const handleMessageRead = (message) => {
        sendReadReceipt(message);
    };

    const savedMessages = (messages[userAddress] && messages[userAddress][receiverAddress]) || [];

    return (
        <div>
            <h2>Chat with {receiverAddress}</h2>
            <div>
                {savedMessages.map((msg, idx) => (
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
            />
            <button onClick={sendMessage}>Send</button>
        </div>
    );
};

export default ChatApp;
