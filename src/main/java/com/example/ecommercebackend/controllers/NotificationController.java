package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.DTO.NotificationDTO;
import com.example.ecommercebackend.services.OneSignalNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")

public class NotificationController {

    @Autowired
    private OneSignalNotificationService oneSignalService;

    @PostMapping
    public ResponseEntity<String> envoyerNotification(@RequestBody NotificationDTO dto) {
        try {
            oneSignalService.sendNotification(dto.getTitle(), dto.getMessage(), dto.getUrl());
            return ResponseEntity.ok("Notification envoyée avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de l'envoi de la notification.");
        }
    }

}

