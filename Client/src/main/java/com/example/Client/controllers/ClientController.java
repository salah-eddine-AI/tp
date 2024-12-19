package com.example.Client.controllers;

import com.example.Client.entities.Client;
import com.example.Client.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable Long id) throws Exception {
        return clientRepository.findById(id)
                .orElseThrow(() -> new Exception("Client not found"));
    }
}
