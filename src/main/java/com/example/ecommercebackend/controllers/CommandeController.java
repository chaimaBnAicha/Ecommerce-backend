package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.DTO.CoordonneesDTO;
import com.example.ecommercebackend.entities.Commande;
import com.example.ecommercebackend.services.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            Commande commande = commandeService.passerCommande(userId, coordonnees, methodePaiement);
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
