package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
}
