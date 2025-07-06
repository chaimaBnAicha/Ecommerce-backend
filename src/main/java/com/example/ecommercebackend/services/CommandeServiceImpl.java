package com.example.ecommercebackend.services;

import com.example.ecommercebackend.DTO.CoordonneesDTO;
import com.example.ecommercebackend.entities.*;
import com.example.ecommercebackend.repositories.CommandeRepository;
import com.example.ecommercebackend.repositories.PaiementRepository;
import com.example.ecommercebackend.repositories.PanierRepository;
import com.example.ecommercebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class CommandeServiceImpl implements CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Commande passerCommande(Long userId, CoordonneesDTO coordonnees, String methodePaiement) {

        User utilisateur = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Met à jour les coordonnées utilisateur
        utilisateur.setNom(coordonnees.getNom());
        utilisateur.setPrenom(coordonnees.getPrenom());
        utilisateur.setAdresse(coordonnees.getAdresse());
        utilisateur.setTelephone(coordonnees.getTelephone());
        userRepository.save(utilisateur);

        Panier panier = panierRepository.findByUtilisateurId(userId)
                .orElseThrow(() -> new RuntimeException("Panier introuvable"));

        if (panier.getProduits().isEmpty()) {
            throw new RuntimeException("Votre panier est vide");
        }

        // Calcul du montant total en fonction du type de produit et quantité
        double montantTotal = panier.getProduits().stream()
                .mapToDouble(produit -> {
                    if (produit instanceof ProduitClassique) {
                        return ((ProduitClassique) produit).getPrixFixe() * produit.getQuantity();
                    }
                    // TODO: gérer d'autres types de produits ici si besoin
                    return 0;
                })
                .sum();

        Paiement paiement = new Paiement();
        paiement.setDate(Date.valueOf(LocalDate.now()));
        paiement.setMontant(montantTotal);
        paiement.setMethode(methodePaiement);
        paiement.setStatut("Payé");

        paiementRepository.save(paiement);

        Commande commande = new Commande();
        commande.setDate(Date.valueOf(LocalDate.now()));
        commande.setEtat(EnumEtat.EN_ATTENTE);
        commande.setPaiement(paiement);
        commande.setUtilisateur(utilisateur);

        commandeRepository.save(commande);

        // Vide le panier après commande
        panier.getProduits().clear();
        panierRepository.save(panier);

        return commande;
    }
}
