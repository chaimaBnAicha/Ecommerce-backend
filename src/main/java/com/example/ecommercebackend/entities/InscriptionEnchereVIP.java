package com.example.ecommercebackend.entities;

import jakarta.persistence.*;

@Entity
public class InscriptionEnchereVIP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User client;

    @ManyToOne
    private ProduitEnchere produit;

    private boolean paye = false;
}
