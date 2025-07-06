package com.example.ecommercebackend.entities;

import java.util.List;

public class ProduitAvecQuantiteDTO {
    private Long id;
    private String nom;
    private String description;
    private List<String> imageUrls;
    private Double prixFixe;
    private Integer quantity;

    public ProduitAvecQuantiteDTO(Long id, String nom, String description, List<String> imageUrls, Double prixFixe, Integer quantity) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.imageUrls = imageUrls;
        this.prixFixe = prixFixe;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Double getPrixFixe() {
        return prixFixe;
    }

    public void setPrixFixe(Double prixFixe) {
        this.prixFixe = prixFixe;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
