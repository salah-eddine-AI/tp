package com.example.Client.tools;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-CLIENT")  // Name of the Client microservice registered in Eureka
public interface ClientService {

    @GetMapping("/clients/{id}")  // REST endpoint to fetch a client by ID
    public Client clientById(@PathVariable("id") Long id);
}
