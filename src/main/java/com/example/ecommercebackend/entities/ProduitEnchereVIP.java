package com.example.ecommercebackend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ENCHERE_VIP")
public class ProduitEnchereVIP extends ProduitEnchere {

    private double fraisInscription;

    @ManyToMany
    @JoinTable(
            name = "vip_inscriptions",
            joinColumns = @JoinColumn(name = "produit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> clientsInscrits = new ArrayList<>();

    public double getFraisInscription() {
        return fraisInscription;
    }

    public void setFraisInscription(double fraisInscription) {
        this.fraisInscription = fraisInscription;
    }

    public List<User> getClientsInscrits() {
        return clientsInscrits;
    }

    public void setClientsInscrits(List<User> clientsInscrits) {
        this.clientsInscrits = clientsInscrits;
    }

    public void ajouterClientInscrit(User user) {
        this.clientsInscrits.add(user);
    }

    public boolean estInscrit(User user) {
        return this.clientsInscrits.contains(user);
    }
}
