package com.example.ecommercebackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Data
public class Mise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double montant;
    private Date date;
    private Boolean anonyme;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private ProduitEnchere produit;

    public Boolean getAnonyme() {
        return anonyme;
    }

    public void setAnonyme(Boolean anonyme) {
        this.anonyme = anonyme;
    }
    @Column(name = "email_visiteur")
    private String emailVisiteur;

    public String getEmailVisiteur() {
        return emailVisiteur;
    }

    public void setEmailVisiteur(String emailVisiteur) {
        this.emailVisiteur = emailVisiteur;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public ProduitEnchere getProduit() {
        return produit;
    }

    public void setProduit(ProduitEnchere produit) {
        this.produit = produit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
