package com.example.ecommercebackend.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Enumerated;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_produit", discriminatorType = DiscriminatorType.STRING)
@Data


public abstract class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;

    @Lob  // indique que c'est un type large (TEXT/LONGTEXT selon base)
    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }


    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    @ManyToOne
    @JoinColumn(name = "panier_id")  // ou le nom correct de ta colonne FK
    @JsonBackReference
    private Panier panier;


    @ManyToOne
    @JoinColumn(name = "vendeur_id")
    private User vendeur;

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

    public User getVendeur() {
        return vendeur;
    }

    public void setVendeur(User vendeur) {
        this.vendeur = vendeur;
    }


}
