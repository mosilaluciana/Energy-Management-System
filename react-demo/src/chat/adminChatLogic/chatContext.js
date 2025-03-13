import React, { createContext, useState, useContext, useEffect } from 'react';

// Creăm contextul pentru mesaje
const ChatContext = createContext();

export const useChatContext = () => {
    return useContext(ChatContext);
};

export const ChatProvider = ({ children }) => {
    const [messages, setMessages] = useState(() => {
        // Încarcă mesajele din localStorage
        const savedMessages = localStorage.getItem('messages');
        return savedMessages ? JSON.parse(savedMessages) : {};
    });

    const addMessage = (sender, receiver, message) => {
        setMessages(prevMessages => {
            const newMessages = { ...prevMessages };
            if (!newMessages[sender]) {
                newMessages[sender] = {};
            }
            if (!newMessages[receiver]) {
                newMessages[receiver] = {};
            }
            if (!newMessages[sender][receiver]) {
                newMessages[sender][receiver] = [];
            }
            if (!newMessages[receiver][sender]) {
                newMessages[receiver][sender] = [];
            }

            // Adaugă mesajele pentru ambele direcții
            newMessages[sender][receiver].push(message);
            newMessages[receiver][sender].push(message); // Mesajele primite

            // Salvează mesajele în localStorage
            localStorage.setItem('messages', JSON.stringify(newMessages));

            return newMessages;
        });
    };

    return (
        <ChatContext.Provider value={{ messages, addMessage }}>
            {children}
        </ChatContext.Provider>
    );
};




