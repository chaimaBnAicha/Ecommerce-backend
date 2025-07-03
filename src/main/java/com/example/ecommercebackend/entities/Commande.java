package com.example.ecommercebackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    @Enumerated(EnumType.STRING)
    private EnumEtat etat;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paiement_id", referencedColumnName = "id")
    private Paiement paiement;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EnumEtat getEtat() {
        return etat;
    }

    public void setEtat(EnumEtat etat) {
        this.etat = etat;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public User getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }
}
