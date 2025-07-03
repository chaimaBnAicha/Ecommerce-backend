package com.example.ecommercebackend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@DiscriminatorValue("ENCHERE")
public class ProduitEnchere extends Produit {
    private double prixDepart;
    private double prixActuel;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;


    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<Mise> mises;


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

    public List<Mise> getMises() {
        return mises;
    }

    public void setMises(List<Mise> mises) {
        this.mises = mises;
    }

    public double getPrixDepart() {
        return prixDepart;
    }

    public void setPrixDepart(double prixDepart) {
        this.prixDepart = prixDepart;
    }

    public double getPrixActuel() {
        return prixActuel;
    }

    public void setPrixActuel(double prixActuel) {
        this.prixActuel = prixActuel;
    }


}
