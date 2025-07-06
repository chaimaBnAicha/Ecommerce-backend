package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.entities.Paiement;
import com.example.ecommercebackend.services.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paiements")
@CrossOrigin(origins = "http://localhost:4200")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @PostMapping("/effectuer")
    public ResponseEntity<?> effectuerPaiement(@RequestParam double montant,
                                               @RequestParam String methode) {
        try {
            Paiement paiement = paiementService.enregistrerPaiement(montant, methode);
            return ResponseEntity.ok(paiement);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
