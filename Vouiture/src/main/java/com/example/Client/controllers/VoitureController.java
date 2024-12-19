package com.example.Client.controllers;

import com.example.Client.entities.Voiture;
import com.example.Client.repositories.VoitureRepository;
import com.example.Client.tools.Client;
import com.example.Client.tools.ClientService;
import com.example.Client.tools.VoitureResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/voitures")
public class VoitureController {

    @Autowired
    private VoitureRepository voitureRepository;

    /*@Autowired
    private VoitureService voitureService;*/

    @Autowired
    private ClientService clientService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/voitures", produces = "application/json")
    public ResponseEntity<Object> findAll() {
        try {
            List<Voiture> voitures = voitureRepository.findAll();
            List<VoitureResponse> responseList = new ArrayList<>();

            for (Voiture voiture : voitures) {
                // Fetch the client details using RestTemplate
                if (voiture.getIdClient() != null) {
                    String clientUrl = "http://localhost:8088/clients/" + voiture.getIdClient();
                    ResponseEntity<Client> response = restTemplate.getForEntity(clientUrl, Client.class);

                    if (response.getStatusCode() == HttpStatus.OK) {
                        Client client = response.getBody();
                        if (client != null) {
                            // Create a custom VoitureResponse DTO
                            VoitureResponse voitureResponse = new VoitureResponse(
                                    voiture.getId(),
                                    voiture.getMarque(),
                                    voiture.getMatricule(),
                                    voiture.getModel(),
                                    client.getNom(),    // Set the client's name
                                    client.getAge(),     // Set the client's age
                                    client.getId()
                            );
                            responseList.add(voitureResponse);
                        }
                    } else {
                        // Log error if client is not found
                        System.out.println("Client with ID " + voiture.getIdClient() + " not found.");
                    }
                } else {
                    System.out.println("Voiture with ID " + voiture.getId() + " has no associated client.");
                }
            }

            // Return the custom response list
            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching voitures: " + e.getMessage());
        }
    }





    @GetMapping("/voitures/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            Voiture voiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture not found with ID: " + id));

            VoitureResponse voitureResponse = new VoitureResponse();

            // Populate VoitureResponse with the Voiture's data
            voitureResponse.setId(voiture.getId());
            voitureResponse.setMarque(voiture.getMarque());
            voitureResponse.setMatricule(voiture.getMatricule());
            voitureResponse.setModel(voiture.getModel());

            // Fetch the client details using RestTemplate
            if (voiture.getIdClient() != null) {
                String clientUrl = "http://localhost:8088/clients/" + voiture.getIdClient();
                ResponseEntity<Client> response = restTemplate.getForEntity(clientUrl, Client.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    Client client = response.getBody();
                    if (client != null) {
                        // Set client details in the VoitureResponse DTO
                        voitureResponse.setClientNom(client.getNom());
                        voitureResponse.setClientAge(client.getAge());
                        voitureResponse.setIdClient(client.getId());
                    } else {
                        // Log if client is not found
                        System.out.println("Client with ID " + voiture.getIdClient() + " not found.");
                    }
                } else {
                    // Handle error if the client is not found
                    System.out.println("Error fetching client with ID " + voiture.getIdClient());
                }
            } else {
                System.out.println("Voiture with ID " + id + " has no associated client.");
            }

            // Return the custom VoitureResponse object
            return ResponseEntity.ok(voitureResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voiture not found with ID: " + id);
        }
    }







    @GetMapping("/voitures/client/{id}")
    public ResponseEntity<Object> findByClient(@PathVariable Long id) {
        try {
            Client client = clientService.clientById(id);
            if (client != null) {
                // Fetch the list of voitures associated with the client
                List<Voiture> voitures = voitureRepository.findByIdClient(id);
                List<VoitureResponse> responseList = new ArrayList<>();

                for (Voiture voiture : voitures) {
                    VoitureResponse voitureResponse = new VoitureResponse();

                    // Populate VoitureResponse with the Voiture's data
                    voitureResponse.setId(voiture.getId());
                    voitureResponse.setMarque(voiture.getMarque());
                    voitureResponse.setMatricule(voiture.getMatricule());
                    voitureResponse.setModel(voiture.getModel());

                    // Set the client details from the provided client
                    voitureResponse.setClientNom(client.getNom());
                    voitureResponse.setClientAge(client.getAge());
                    voitureResponse.setIdClient(client.getId());

                    // Add the VoitureResponse to the response list
                    responseList.add(voitureResponse);
                }

                // Return the list of VoitureResponse objects
                return ResponseEntity.ok(responseList);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching voitures for client: " + e.getMessage());
        }
    }






    @PostMapping("/voitures/{clientId}")
    public ResponseEntity<Object> save(@PathVariable Long clientId, @RequestBody Voiture voiture) {
        try {
            // Fetch the client details using the clientService
            Client client = clientService.clientById(clientId);
            if (client != null) {
                // Set the fetched client in the voiture object
                voiture.setClient(client);
                voiture.setIdClient(clientId);
                Voiture savedVoiture = voitureRepository.save(voiture);
                return ResponseEntity.ok(savedVoiture);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found with ID: " + clientId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving voiture: " + e.getMessage());
        }
    }

    @PutMapping("/voitures/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Voiture updatedVoiture) {
        try {
            Voiture existingVoiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture not found with ID: " + id));

            // Update only the non-null fields from the request body
            if (updatedVoiture.getMatricule() != null && !updatedVoiture.getMatricule().isEmpty()) {
                existingVoiture.setMatricule(updatedVoiture.getMatricule());
            }
            if (updatedVoiture.getMarque() != null && !updatedVoiture.getMarque().isEmpty()) {
                existingVoiture.setMarque(updatedVoiture.getMarque());
            }
            if (updatedVoiture.getModel() != null && !updatedVoiture.getModel().isEmpty()) {
                existingVoiture.setModel(updatedVoiture.getModel());
            }

            // Save the updated Voiture
            Voiture savedVoiture = voitureRepository.save(existingVoiture);
            return ResponseEntity.ok(savedVoiture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating voiture: " + e.getMessage());
        }
    }
}
