import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './styles.css'; // Import the CSS file

function Voitures() {
  const [voitures, setVoitures] = useState([]);
  const [clients, setClients] = useState([]);
  const [selectedClientId, setSelectedClientId] = useState('');
  const [searchId, setSearchId] = useState('');
  const [filteredVoitures, setFilteredVoitures] = useState([]);

  // Fetch all clients and voitures from the API
  useEffect(() => {
    // Fetch all clients
    axios.get('http://localhost:8088/clients')
      .then(response => {
        console.log('Clients fetched:', response.data); // Debugging log
        setClients(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching clients:', error);
      });

    // Fetch all voitures (initially showing all)
    axios.get('http://localhost:8089/voitures/voitures')
      .then(response => {
        console.log('Voitures fetched:', response.data); // Debugging log
        setVoitures(response.data);
        setFilteredVoitures(response.data); // Initially show all voitures
      })
      .catch(error => {
        console.error('There was an error fetching voitures:', error);
      });
  }, []);

  // Filter voitures by selected client
  const handleClientChange = (e) => {
    const clientId = e.target.value;
    setSelectedClientId(clientId);
    console.log('Selected client ID:', clientId); // Debugging log

    if (clientId) {
      // Fetch voitures for the selected client from the API
      axios.get(`http://localhost:8089/voitures/voitures/client/${clientId}`)
        .then(response => {
          console.log('Voitures for client:', response.data); // Debugging log
          setFilteredVoitures(response.data); // Display filtered voitures
        })
        .catch(error => {
          console.error('Error fetching voitures for client:', error);
        });
    } else {
      // If no client is selected, reset to show all voitures
      setFilteredVoitures(voitures);
    }
  };

  // Search for voiture by ID
  const handleSearchById = (e) => {
    e.preventDefault();
    if (searchId) {
      axios.get(`http://localhost:8089/voitures/voitures/${searchId}`)
        .then(response => {
          console.log('Voiture fetched by ID:', response.data); // Debugging log
          setFilteredVoitures([response.data]); // Display only the searched voiture
        })
        .catch(error => {
          console.error('Error fetching voiture by ID:', error);
        });
    } else {
      setFilteredVoitures(voitures); // If search is empty, reset to all voitures
    }
  };

  return (
    <div className="container">
      <h1>Voitures</h1>

      {/* Dropdown to select a client */}
      <select className="dropdown" onChange={handleClientChange} value={selectedClientId}>
        <option value="">Select Client</option>
        {clients.map(client => (
          <option key={client.id} value={client.id}>
            {client.nom}
          </option>
        ))}
      </select>

      {/* Search by Voiture ID */}
      <form onSubmit={handleSearchById}>
        <input
          type="number"
          className="search-input"
          placeholder="Search by Voiture ID"
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
        />
        <button type="submit" className="search-button">Search</button>
      </form>

      {/* Display Voitures */}
      <ul>
        {filteredVoitures.length > 0 ? (
          filteredVoitures.map(voiture => (
            <li key={voiture.id}>
              <strong>Marque:</strong> {voiture.marque} - 
              <strong>Matricule:</strong> {voiture.matricule} - 
              <strong>Model:</strong> {voiture.model} - 
              <strong>Client Name:</strong> {voiture.clientNom} - 
              <strong>Client Age:</strong> {voiture.clientAge}
            </li>
          ))
        ) : (
          <p>No voitures </p> // Display this if no voitures match the filter or search
        )}
      </ul>
    </div>
  );
}

export default Voitures;
