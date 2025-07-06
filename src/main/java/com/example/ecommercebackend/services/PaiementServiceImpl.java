package com.example.ecommercebackend.services;

import com.example.ecommercebackend.entities.Paiement;
import com.example.ecommercebackend.repositories.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class PaiementServiceImpl implements PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    @Override
    public Paiement enregistrerPaiement(double montant, String methode) {
        Paiement paiement = new Paiement();
        paiement.setDate(Date.valueOf(LocalDate.now()));
        paiement.setMontant(montant);
        paiement.setMethode(methode);
        paiement.setStatut("Payé");

        return paiementRepository.save(paiement);
    }
}
