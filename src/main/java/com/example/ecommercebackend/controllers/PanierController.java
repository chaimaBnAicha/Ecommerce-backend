package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.DTO.PanierDTO;
import com.example.ecommercebackend.DTO.ProduitDTO;
import com.example.ecommercebackend.entities.Panier;
import com.example.ecommercebackend.entities.ProduitClassique;
import com.example.ecommercebackend.repositories.PanierRepository;
import com.example.ecommercebackend.services.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paniers")
@CrossOrigin(origins = "http://localhost:4200")
public class PanierController {

    @Autowired
    private PanierService panierService;
    @Autowired
    private  PanierRepository panierRepository;
    private PanierDTO convertToDTO(Panier panier) {

        List<ProduitDTO> produits = panier.getProduits().stream().map(produit -> {
            ProduitDTO pDto = new ProduitDTO();
            pDto.setId(produit.getId());
            pDto.setNom(produit.getNom());
            pDto.setDescription(produit.getDescription());
            pDto.setImageUrls(produit.getImageUrls()); // Assure-toi que c'est bien rempli dans ton entité

            if (produit instanceof ProduitClassique produitClassique) {
                pDto.setPrixFixe(produitClassique.getPrixFixe());
            }

            return pDto;
        }).toList();

        PanierDTO dto = new PanierDTO();
        dto.setId(panier.getId());
        dto.setUtilisateurId(panier.getUtilisateur().getId());
        dto.setProduits(produits);

        return dto;
    }


    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createPanier(@PathVariable Long userId) {
        try {
            Panier panier = panierService.createPanier(userId);
            PanierDTO dto = convertToDTO(panier);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur création panier: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPanier(@PathVariable Long id) {
        try {
            Panier panier = panierService.getPanierById(id);
            return ResponseEntity.ok(panier);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/utilisateur/{userId}")
    public ResponseEntity<?> getPanierByUser(@PathVariable Long userId) {
        try {
            Panier panier = panierService.getPanierByUtilisateur(userId);
            PanierDTO dto = convertToDTO(panier);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{panierId}/ajouter/{produitId}")
    public ResponseEntity<?> ajouterProduit(
            @PathVariable Long panierId,
            @PathVariable Long produitId,
            @RequestParam(defaultValue = "1") int quantity) {

        try {
            Panier panier = panierService.ajouterProduit(panierId, produitId, quantity);
            PanierDTO dto = convertToDTO(panier);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur ajout produit: " + e.getMessage());
        }
    }

    @DeleteMapping("/{panierId}/retirer/{produitId}")
    public ResponseEntity<?> retirerProduit(@PathVariable Long panierId, @PathVariable Long produitId) {
        Panier panier = panierRepository.findById(panierId).orElseThrow();

        // Ne supprime que la relation
        panier.getProduits().removeIf(p -> p.getId().equals(produitId));
        panierRepository.save(panier);

        PanierDTO dto = convertToDTO(panier); // Conversion en DTO propre
        return ResponseEntity.ok(dto);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerPanier(@PathVariable Long id) {
        try {
            panierService.supprimerPanier(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur suppression panier: " + e.getMessage());
        }
    }
}
