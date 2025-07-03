package com.example.ecommercebackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("CLASSIQUE")
public class ProduitClassique extends Produit {
    private double prixFixe;

    public double getPrixFixe() {
        return prixFixe;
    }

    public void setPrixFixe(double prixFixe) {
        this.prixFixe = prixFixe;
    }
}
