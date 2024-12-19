package com.example.Client.tools;

public class VoitureResponse {
    private Long id;
    private String marque;
    private String matricule;
    private String model;
    private Long idClient;
    private String clientNom;
    private Float clientAge; // You can add any other client details you want to return

    public VoitureResponse(){}
    public VoitureResponse(Long id, String marque, String matricule, String model, String clientNom, Float clientAge, Long idClient) {
        this.id = id;
        this.marque = marque;
        this.matricule = matricule;
        this.model = model;
        this.idClient = idClient;
        this.clientNom = clientNom;
        this.clientAge = clientAge;
    }


    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public Float getClientAge() {
        return clientAge;
    }

    public void setClientAge(Float clientAge) {
        this.clientAge = clientAge;
    }
}

