import React, { useEffect, useState } from 'react';
import { getPersons } from '../../person/api/person-api';

const PersonSelector = ({ onSelectPerson }) => {
    const [persons, setPersons] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Preia datele persoanelor
        getPersons((data) => {
            setPersons(data || []); // Setează un array gol dacă nu există date
            setLoading(false); // Oprește mesajul de încărcare
        });
    }, []);

    return (
        <div>
            <h2>Select a Person</h2>
            {loading ? (
                <p>Loading persons...</p>
            ) : (
                <ul>
                    {persons.length > 0 ? (
                        persons.map((person) => (
                            <li key={person.address}>
                                <button onClick={() => onSelectPerson(person)}>
                                    {person.name} ({person.address})
                                </button>
                            </li>
                        ))
                    ) : (
                        <li>No persons available.</li>
                    )}
                </ul>
            )}
        </div>
    );
};

export default PersonSelector;

