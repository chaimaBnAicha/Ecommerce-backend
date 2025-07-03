package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.entities.Panier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PanierRepository extends JpaRepository<Panier, Long> {
    Optional<Panier> findByUtilisateurId(Long utilisateurId);}
