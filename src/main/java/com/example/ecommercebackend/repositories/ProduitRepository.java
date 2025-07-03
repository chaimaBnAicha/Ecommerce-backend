package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // ✅ Produits d’un vendeur spécifique
    List<Produit> findByVendeurId(Long vendeurId);

    // ✅ Recherche par mot-clé dans le nom ou la description
    @Query("SELECT p FROM Produit p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Produit> searchProduits(@Param("keyword") String keyword);
}
