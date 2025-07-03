package com.example.ecommercebackend.services;

import com.example.ecommercebackend.entities.Commande;
import com.example.ecommercebackend.entities.Mise;
import com.example.ecommercebackend.entities.Panier;
import com.example.ecommercebackend.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User getUserFromToken(String token);

    Optional<User> findById(Long id);

    User save(User user);

    boolean existsByEmail(String email);

    List<User> findByRole(User.Role role);

    // Méthodes spécifiques pour les relations
    List<Mise> getUserMises(Long userId);

    Panier getUserPanier(Long userId);

    List<Commande> getUserCommandes(Long userId);
}