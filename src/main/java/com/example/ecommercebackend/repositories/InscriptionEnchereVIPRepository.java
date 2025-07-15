package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.entities.InscriptionEnchereVIP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscriptionEnchereVIPRepository extends JpaRepository<InscriptionEnchereVIP, Long> {
    boolean existsByClientIdAndProduitIdAndPayeTrue(Long clientId, Long produitId);
}
