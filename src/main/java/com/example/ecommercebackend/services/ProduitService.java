package com.example.ecommercebackend.services;

import com.example.ecommercebackend.DTO.ProduitDTO;
import com.example.ecommercebackend.entities.Produit;
import com.example.ecommercebackend.entities.User;

import java.util.List;

public interface ProduitService {
        Produit ajouterProduit(ProduitDTO dto, User vendeur);
        List<Produit> getAllProduits();
        Produit getProduitById(Long id);
        void supprimerProduit(Long id);
    }

