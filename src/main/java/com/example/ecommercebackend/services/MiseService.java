package com.example.ecommercebackend.services;

import com.example.ecommercebackend.DTO.MiseDTO;
import com.example.ecommercebackend.entities.Mise;

import java.util.List;

public interface MiseService {
    Mise creerMise(MiseDTO miseDTO);
    List<Mise> getDernieresMises(Long produitId);
    List<MiseDTO> getDernieresMisess(Long produitId);


    void verifierFinEnchereEtNotifier(Long produitId);
}
