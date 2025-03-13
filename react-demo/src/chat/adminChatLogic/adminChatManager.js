import React, { useState } from 'react';
import PersonSelector from './personSelector';
import ChatApp from './chatApp';
import { ChatProvider } from './chatContext';  // Importăm contextul

const AdminChatManager = () => {
    const [selectedPerson, setSelectedPerson] = useState(null);

    return (
        <ChatProvider>
            <div>
                {!selectedPerson ? (
                    <PersonSelector onSelectPerson={setSelectedPerson} />
                ) : (
                    <div>
                        <button onClick={() => setSelectedPerson(null)}>Back</button>
                        <ChatApp receiverAddress={selectedPerson.address} userAddress="admin@gmail.com" />
                    </div>
                )}
            </div>
        </ChatProvider>
    );
};

export default AdminChatManager;
