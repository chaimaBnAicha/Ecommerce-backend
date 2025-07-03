package com.example.ecommercebackend.services;

import com.example.ecommercebackend.entities.Panier;

public interface PanierService {

    Panier createPanier(Long userId);

    Panier getPanierById(Long id);

    Panier getPanierByUtilisateur(Long utilisateurId);

    Panier ajouterProduit(Long panierId, Long produitId);

    Panier retirerProduit(Long panierId, Long produitId);

    void supprimerPanier(Long id);
}
