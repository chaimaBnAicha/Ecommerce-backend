package com.example.ecommercebackend.services;

import com.example.ecommercebackend.DTO.PanierDTO;
import com.example.ecommercebackend.DTO.ProduitDTO;
import com.example.ecommercebackend.entities.Panier;
import com.example.ecommercebackend.entities.Produit;
import com.example.ecommercebackend.entities.User;
import com.example.ecommercebackend.repositories.PanierRepository;
import com.example.ecommercebackend.repositories.ProduitRepository;
import com.example.ecommercebackend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PanierServiceImpl implements PanierService {
    private static final Logger logger = LoggerFactory.getLogger(PanierService.class);
    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private UserRepository userRepository;



    public Panier createPanier(Long userId) {
        logger.info("Tentative de création de panier pour l'utilisateur ID: {}", userId);

        Optional<Panier> existingPanier = panierRepository.findByUtilisateurId(userId);
        if (existingPanier.isPresent()) {
            logger.info("Panier existant trouvé pour l'utilisateur ID: {}", userId);
            return existingPanier.get();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Utilisateur non trouvé avec l'ID: {}", userId);
                    return new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId);
                });

        Panier panier = new Panier();
        panier.setUtilisateur(user);

        Panier savedPanier = panierRepository.save(panier);
        logger.info("Nouveau panier créé avec l'ID: {} pour l'utilisateur ID: {}", savedPanier.getId(), userId);

        return savedPanier;
    }



    public Panier getPanierById(Long id) {
        return panierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Panier introuvable"));
    }

    public Panier getPanierByUtilisateur(Long userId) {
        return panierRepository.findByUtilisateurId(userId)
                .orElseThrow(() -> new RuntimeException("Panier introuvable"));
    }


    public Panier ajouterProduit(Long panierId, Long produitId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier introuvable"));

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        produit.setPanier(panier);  // Lien côté Produit
        panier.getProduits().add(produit);

        produitRepository.save(produit);  // Important : save le produit aussi
        return panierRepository.save(panier);
    }


    public Panier retirerProduit(Long panierId, Long produitId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier introuvable"));

        panier.getProduits().removeIf(p -> p.getId().equals(produitId));
        return panierRepository.save(panier);
    }

    public void supprimerPanier(Long id) {
        panierRepository.deleteById(id);
    }
}