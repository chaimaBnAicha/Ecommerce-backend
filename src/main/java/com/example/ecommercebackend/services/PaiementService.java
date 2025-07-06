package com.example.ecommercebackend.services;

import com.example.ecommercebackend.entities.Paiement;

public interface PaiementService {
    Paiement enregistrerPaiement(double montant, String methode);
}
