package com.example.ecommercebackend.services;

import com.example.ecommercebackend.DTO.ProduitDTO;
import com.example.ecommercebackend.entities.*;
import com.example.ecommercebackend.repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProduitServiceImpl implements ProduitService {

    @Autowired
    private ProduitRepository produitRepository;


    @Override
    public Produit ajouterProduit(ProduitDTO dto, User vendeur) {
        // Validation
        if (dto.getNom() == null || dto.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du produit est requis");
        }
        if (dto.getTypeProduit() == null) {
            throw new IllegalArgumentException("Le type de produit est requis");
        }

        Produit produit;

        if (dto.getTypeProduit() == TypeProduit.CLASSIQUE) {
            if (dto.getPrixFixe() == null || dto.getPrixFixe() <= 0) {
                throw new IllegalArgumentException("Prix fixe invalide");
            }
            ProduitClassique pc = new ProduitClassique();
            pc.setPrixFixe(dto.getPrixFixe());
            produit = pc;
        } else {
            if (dto.getPrixDepart() == null || dto.getPrixDepart() <= 0) {
                throw new IllegalArgumentException("Prix de départ invalide");
            }
            if (dto.getDateDebut() == null || dto.getDateFin() == null ||
                    dto.getDateFin().isBefore(dto.getDateDebut())) {
                throw new IllegalArgumentException("Dates d'enchère invalides");
            }

            ProduitEnchere pe = new ProduitEnchere();
            pe.setPrixDepart(dto.getPrixDepart());
            pe.setPrixActuel(dto.getPrixDepart());
            pe.setDateDebut(dto.getDateDebut());
            pe.setDateFin(dto.getDateFin());
            produit = pe;
        }

        produit.setNom(dto.getNom());
        produit.setDescription(dto.getDescription());
        produit.setImageUrls(dto.getImageUrls());
        produit.setVendeur(vendeur);

        return produitRepository.save(produit);
    }

    @Override
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @Override
    public Produit getProduitById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
    }

    @Override
    public void supprimerProduit(Long id) {
        produitRepository.deleteById(id);
    }
}