package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.DTO.CoordonneesDTO;
import com.example.ecommercebackend.entities.Commande;
import com.example.ecommercebackend.services.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/commandes")
@CrossOrigin(origins = "http://localhost:4200")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @PostMapping("/passer")
    public ResponseEntity<?> passerCommande(
            @RequestParam Long userId,
            @RequestParam String methodePaiement,
            @RequestBody CoordonneesDTO coordonnees) {
        try {
            System.out.println("Coordonnées reçues : " + coordonnees); // ✅ Ajoute ce log

            if (!validateCoordonnees(coordonnees)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Coordonnées invalides"
                ));
            }

            Commande commande = commandeService.passerCommande(userId, coordonnees, methodePaiement);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "commande", commande,
                    "message", "Commande passée avec succès"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erreur lors de la commande : " + e.getMessage()
            ));
        }
    }


    private boolean validateCoordonnees(CoordonneesDTO coordonnees) {
        return coordonnees != null &&
                coordonnees.getCodePostal() != null &&
                coordonnees.getCodePostal().matches("\\d{4}");
    }

}