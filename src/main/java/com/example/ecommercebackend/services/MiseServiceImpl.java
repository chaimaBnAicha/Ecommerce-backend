package com.example.ecommercebackend.services;

import com.example.ecommercebackend.DTO.MiseDTO;
import com.example.ecommercebackend.entities.Mise;
import com.example.ecommercebackend.entities.ProduitEnchere;
import com.example.ecommercebackend.entities.User;
import com.example.ecommercebackend.repositories.MiseRepository;
import com.example.ecommercebackend.repositories.ProduitEnchereRepository;
import com.example.ecommercebackend.repositories.ProduitRepository;
import com.example.ecommercebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MiseServiceImpl implements MiseService {

    @Autowired
    private MiseRepository miseRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProduitEnchereRepository produitEnchereRepository;
    @Autowired
    private EmailService emailService;


    @Override
    public Mise creerMise(MiseDTO dto) {
        if (dto.getClientId() == null && (dto.getEmailVisiteur() == null || dto.getEmailVisiteur().isEmpty())) {
            throw new RuntimeException("Les visiteurs doivent fournir un email.");
        }
        System.out.println(">>> clientId = " + dto.getClientId());
        System.out.println(">>> emailVisiteur = " + dto.getEmailVisiteur());
        System.out.println(">>> anonyme = " + dto.isAnonyme());



        ProduitEnchere produit = (ProduitEnchere) produitRepository.findById(dto.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (LocalDateTime.now().isAfter(produit.getDateFin())) {
            throw new RuntimeException("L'enchère est terminée.");
        }

        Mise mise = new Mise();
        mise.setMontant(dto.getMontant());
        mise.setDate(new Date(System.currentTimeMillis()));
        mise.setProduit(produit);

        // Traitement pour client ou visiteur
        if (dto.isAnonyme()) {
            mise.setClient(null);
            mise.setEmailVisiteur(dto.getEmailVisiteur());
        } else {
            User client = userRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));
            mise.setClient(client);
            System.out.println(">>> Recherche client avec ID: " + dto.getClientId());

        }

        // Mettre à jour le prix actuel du produit si la mise est supérieure
        if (dto.getMontant() > produit.getPrixActuel()) {
            produit.setPrixActuel(dto.getMontant());
        }

        // Enregistrer la mise
        Mise savedMise = miseRepository.save(mise);

        // 📧 ENVOI DE MAIL
        if (dto.isAnonyme() && dto.getEmailVisiteur() != null) {
            emailService.envoyerEmailConfirmation(
                    dto.getEmailVisiteur(),
                    "Votre mise a été reçue",
                    "Merci pour votre participation à l'enchère sur le produit : " + produit.getNom() +
                            ". Veuillez patienter jusqu'à la fin pour savoir si vous avez gagné. Bonne chance ! 🍀"
            );
        } else if (!dto.isAnonyme() && mise.getClient() != null && mise.getClient().getEmail() != null) {
            emailService.envoyerEmailConfirmation(
                    mise.getClient().getEmail(),
                    "Confirmation de votre mise",
                    "Votre mise de " + dto.getMontant() + " DT sur le produit : " + produit.getNom() +
                            " a bien été enregistrée. Restez à l'écoute pour connaître le résultat ! 🤞"
            );
        }

        return savedMise;
    }


    @Override
    public List<Mise> getDernieresMises(Long produitId) {
        ProduitEnchere produit = (ProduitEnchere) produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        return miseRepository.findTop3ByProduitOrderByDateDesc(produit);
    }
    @Override
    public void verifierFinEnchereEtNotifier(Long produitId) {
        ProduitEnchere produit = produitEnchereRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (LocalDateTime.now().isBefore(produit.getDateFin())) {
            throw new RuntimeException("L'enchère est encore en cours.");
        }

        List<Mise> mises = miseRepository.findByProduitOrderByMontantDesc(produit);

        if (mises.isEmpty()) return;

        Mise gagnante = mises.get(0); // plus grande mise
        produit.setClientGagnant(gagnante.getClient());
        produitRepository.save(produit);

        // Génère le lien vers Angular
        String lienPaiement = "http://localhost:4200/paiement/" + produitId;

        String message = "🎉 Félicitations ! Vous avez remporté le produit : " + produit.getNom() +
                " avec une mise de " + gagnante.getMontant() + " DT.\n" +
                "👉 Cliquez ici pour finaliser votre achat : " + lienPaiement;

        if (gagnante.getClient() != null) {
            emailService.envoyerEmailConfirmation(
                    gagnante.getClient().getEmail(),
                    "Félicitations ! Vous avez gagné l'enchère",
                    message
            );
        } else if (gagnante.getEmailVisiteur() != null) {
            emailService.envoyerEmailConfirmation(
                    gagnante.getEmailVisiteur(),
                    "Félicitations ! Vous avez gagné l'enchère",
                    message
            );
        }
    }

    @Override
    public List<MiseDTO> getDernieresMisess(Long produitId) {
        ProduitEnchere produit = produitEnchereRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        List<Mise> mises = miseRepository.findTop3ByProduitOrderByDateDesc(produit);

        return mises.stream().map(mise -> {
            MiseDTO dto = new MiseDTO();
            dto.setMontant(mise.getMontant());
            dto.setDateMise(mise.getDate());
            dto.setAnonyme(mise.getClient() == null);
            dto.setProduitId(produitId);
            if (mise.getClient() != null) {
                dto.setUtilisateurId(mise.getClient().getId());
                dto.setUtilisateurNom(mise.getClient().getNom()); // 👈 Important pour l'affichage
            } else {
                dto.setEmailVisiteur(mise.getEmailVisiteur());
            }
            return dto;
        }).collect(Collectors.toList());
    }


}
