package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.entities.Mise;
import com.example.ecommercebackend.entities.ProduitEnchere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MiseRepository extends JpaRepository<Mise, Long> {
    List<Mise> findTop3ByProduitOrderByDateDesc(ProduitEnchere produit);
    List<Mise> findByProduitOrderByMontantDesc(ProduitEnchere produit);
}
