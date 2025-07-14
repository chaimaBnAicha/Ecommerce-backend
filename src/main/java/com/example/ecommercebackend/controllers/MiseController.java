package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.DTO.MiseDTO;
import com.example.ecommercebackend.entities.Mise;
import com.example.ecommercebackend.services.EmailService;
import com.example.ecommercebackend.services.MiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mises")
@CrossOrigin(origins = "http://localhost:4200")
public class MiseController {

    @Autowired
    private MiseService miseService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/placer")
    public ResponseEntity<?> placerMise(@RequestBody MiseDTO miseDTO) {
        try {
            Mise mise = miseService.creerMise(miseDTO);

            // Si c'est un visiteur, stocker la participation
            if (miseDTO.getEmailVisiteur() != null) {
                // Envoyer un email de confirmation
                emailService.envoyerEmailConfirmation(
                        miseDTO.getEmailVisiteur(),
                        "Confirmation de votre enchère",
                        "Votre enchère a été enregistrée. Nous vous contacterons si vous gagnez."
                );
            }

            return ResponseEntity.ok(mise);
        } catch (Exception e) {
            e.printStackTrace(); // Ajoute ceci pour voir la stacktrace complète dans ta console
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/dernieres/{produitId}")
    public List<MiseDTO> getDernieresMises(@PathVariable Long produitId) {
        return miseService.getDernieresMisess(produitId);
    }

    @GetMapping("/verifier-fin/{produitId}")
    public ResponseEntity<?> verifierFin(@PathVariable Long produitId) {
        try {
            miseService.verifierFinEnchereEtNotifier(produitId);
            return ResponseEntity.ok("Enchère traitée.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
