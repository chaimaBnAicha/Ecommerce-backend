package com.example.ecommercebackend.services;

import com.example.ecommercebackend.DTO.CoordonneesDTO;
import com.example.ecommercebackend.entities.Commande;

public interface CommandeService {
    Commande passerCommande(Long userId, CoordonneesDTO coordonnees, String methodePaiement);
}
