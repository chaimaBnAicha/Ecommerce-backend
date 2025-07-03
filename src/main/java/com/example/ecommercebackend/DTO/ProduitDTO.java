package com.example.ecommercebackend.DTO;

import com.example.ecommercebackend.entities.Produit;
import com.example.ecommercebackend.entities.ProduitClassique;
import com.example.ecommercebackend.entities.TypeProduit;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProduitDTO {
    private Long id;
    private String nom;
    private String description;
    private TypeProduit typeProduit;

    private Double prixFixe;
    private Double prixDepart;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateDebut;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateFin;
    @ElementCollection
    @CollectionTable(name = "produit_images", joinColumns = @JoinColumn(name = "produit_id"))
    @Column(name = "image_url")
    private List<String> imageUrl = new ArrayList<>();

    // getters & setters
    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();
    public ProduitDTO() {
    }

    public ProduitDTO(Produit produit) {
        this.id = produit.getId();
        this.nom = produit.getNom();
        this.description = produit.getDescription();
        if (produit instanceof ProduitClassique) {
            this.prixFixe = ((ProduitClassique) produit).getPrixFixe();
        }
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

    public TypeProduit getTypeProduit() {
        return typeProduit;
    }

    public void setTypeProduit(TypeProduit typeProduit) {
        this.typeProduit = typeProduit;
    }

    public Double getPrixFixe() {
        return prixFixe;
    }

    public void setPrixFixe(Double prixFixe) {
        this.prixFixe = prixFixe;
    }

    public Double getPrixDepart() {
        return prixDepart;
    }

    public void setPrixDepart(Double prixDepart) {
        this.prixDepart = prixDepart;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}