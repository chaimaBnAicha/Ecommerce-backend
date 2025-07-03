package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.DTO.ProduitDTO;
import com.example.ecommercebackend.entities.*;
import com.example.ecommercebackend.repositories.ProduitRepository;
import com.example.ecommercebackend.repositories.UserRepository;
import com.example.ecommercebackend.services.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    @Autowired
    private ProduitService produitService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProduitRepository produitRepository;


    @PostMapping("/ajoutProduit")
    public ResponseEntity<?> ajouterProduit(@RequestBody ProduitDTO dto) {
        try {
            User vendeur = userRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Vendeur introuvable"));

            Produit saved = produitService.ajouterProduit(dto, vendeur);

            ProduitDTO responseDto = new ProduitDTO();
            responseDto.setId(saved.getId());
            responseDto.setNom(saved.getNom());
            responseDto.setDescription(saved.getDescription());
            responseDto.setImageUrls(saved.getImageUrls());

            if (saved instanceof ProduitClassique pc) {
                responseDto.setTypeProduit(TypeProduit.CLASSIQUE);
                responseDto.setPrixFixe(pc.getPrixFixe());
            } else if (saved instanceof ProduitEnchere pe) {
                responseDto.setTypeProduit(TypeProduit.ENCHERE);
                responseDto.setPrixDepart(pe.getPrixDepart());
                responseDto.setDateDebut(pe.getDateDebut());
                responseDto.setDateFin(pe.getDateFin());
            }

            return ResponseEntity.ok(responseDto);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body("Erreur serveur");
        }
    }


    @GetMapping
    public List<ProduitDTO> getAllProduits() {
        return produitRepository.findAll().stream().map(p -> {
            ProduitDTO dto = new ProduitDTO();
            dto.setNom(p.getNom());
            dto.setDescription(p.getDescription());
            dto.setImageUrls(p.getImageUrls()); // Modifier ici pour utiliser getImageUrls()

            if (p instanceof ProduitClassique pc) {
                dto.setId(p.getId());  // Manquant chez toi
                dto.setTypeProduit(TypeProduit.CLASSIQUE);
                dto.setPrixFixe(pc.getPrixFixe());
            } else if (p instanceof ProduitEnchere pe) {
                dto.setId(p.getId());  // Manquant chez toi
                dto.setTypeProduit(TypeProduit.ENCHERE);
                dto.setPrixDepart(pe.getPrixDepart());
                dto.setDateDebut(pe.getDateDebut());
                dto.setDateFin(pe.getDateFin());
            }

            return dto;
        }).toList();
    }

// Faire la même modification dans getProduitsParType

    // 2. Récupérer les produits par type (CLASSIQUE ou ENCHERE)
    @GetMapping("/type/{type}")
    public List<ProduitDTO> getProduitsParType(@PathVariable String type) {
        return produitRepository.findAll().stream()
                .filter(p -> {
                    if (type.equalsIgnoreCase("CLASSIQUE") && p instanceof ProduitClassique) {
                        return true;
                    } else if (type.equalsIgnoreCase("ENCHERE") && p instanceof ProduitEnchere) {
                        return true;
                    }
                    return false;
                })
                .map(p -> {
                    ProduitDTO dto = new ProduitDTO();
                    dto.setId(p.getId());  // <-- Important
                    dto.setNom(p.getNom());
                    dto.setDescription(p.getDescription());
                    dto.setImageUrls(p.getImageUrls());

                    if (p instanceof ProduitClassique pc) {
                        dto.setTypeProduit(TypeProduit.CLASSIQUE);
                        dto.setPrixFixe(pc.getPrixFixe());
                    } else if (p instanceof ProduitEnchere pe) {
                        dto.setTypeProduit(TypeProduit.ENCHERE);
                        dto.setPrixDepart(pe.getPrixDepart());
                        dto.setDateDebut(pe.getDateDebut());
                        dto.setDateFin(pe.getDateFin());
                    }

                    return dto;
                })
                .toList();
    }

}




