package com.example.Client.tools;


import lombok.Data;

@Data
public class Client {
    private Long id;  // Client ID
    private String nom;  // Client name
    private Float age;  // Client age


    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAge(Float age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Float getAge() {
        return age;
    }
}
