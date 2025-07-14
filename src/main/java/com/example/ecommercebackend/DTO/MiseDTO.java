package com.example.ecommercebackend.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class MiseDTO {
    private double montant;
    private Long produitId;
    private Long clientId;
    private boolean anonyme;
    private String emailVisiteur;
    // ✅ Ajouts nécessaires pour affichage Front
    private Date dateMise;          // Pour afficher la date de la mise
    private Long utilisateurId;     // Pour savoir qui a misé
    private String utilisateurNom;  //

    public Date getDateMise() {
        return dateMise;
    }

    public void setDateMise(Date dateMise) {
        this.dateMise = dateMise;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getUtilisateurNom() {
        return utilisateurNom;
    }

    public void setUtilisateurNom(String utilisateurNom) {
        this.utilisateurNom = utilisateurNom;
    }

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

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public boolean isAnonyme() {
        return anonyme;
    }

    public void setAnonyme(boolean anonyme) {
        this.anonyme = anonyme;
    }
}