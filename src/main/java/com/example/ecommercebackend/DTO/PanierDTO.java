package com.example.ecommercebackend.DTO;

import com.example.ecommercebackend.entities.Panier;

import java.util.List;

public class PanierDTO {
    private Long id;
    private List<ProduitDTO> produits;
    private Long utilisateurId;

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public PanierDTO() {
    }

    public PanierDTO(Panier panier) {
        this.id = panier.getId();
        this.produits = panier.getProduits().stream()
                .map(ProduitDTO::new)
                .toList();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProduitDTO> getProduits() {
        return produits;
    }

    public void setProduits(List<ProduitDTO> produits) {
        this.produits = produits;
    }
}
