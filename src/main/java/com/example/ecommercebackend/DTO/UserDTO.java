package com.example.ecommercebackend.DTO;

import com.example.ecommercebackend.entities.User.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String nom;
    private String email;
    private Role role;

    // Pour éviter les références circulaires dans les réponses JSON
    @Data
    public static class MiseDTO {
        private Long id;
        private Double montant;
    }

    @Data
    public static class CommandeDTO {
        private Long id;
        private String statut;
    }

    private List<MiseDTO> mises;
    private List<CommandeDTO> commandes;
}