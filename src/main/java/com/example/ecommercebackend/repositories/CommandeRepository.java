package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
}
