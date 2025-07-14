package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.entities.Paiement;
import com.example.ecommercebackend.services.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/paiements")
@CrossOrigin(origins = "http://localhost:4200")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @PostMapping("/effectuer")
    public ResponseEntity<?> effectuerPaiement(@RequestParam double montant,
                                               @RequestParam String methode,
                                               @RequestParam String tokenCarte) {
        try {
            // Validation du token de carte (simulé)
            if (!validerTokenCarte(tokenCarte)) {
                return ResponseEntity.badRequest().body("Token de paiement invalide");
            }

            Paiement paiement = paiementService.enregistrerPaiement(montant, methode);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "paiement", paiement,
                    "message", "Paiement effectué avec succès"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erreur lors du paiement: " + e.getMessage()
            ));
        }
    }

    private boolean validerTokenCarte(String token) {
        // Implémentez une vraie validation avec un service de paiement
        return token != null && token.length() > 10;
    }
}
