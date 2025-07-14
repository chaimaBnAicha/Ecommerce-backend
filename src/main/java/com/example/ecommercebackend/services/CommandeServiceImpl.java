package com.example.ecommercebackend.services;

import com.example.ecommercebackend.DTO.CoordonneesDTO;
import com.example.ecommercebackend.entities.*;
import com.example.ecommercebackend.repositories.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Service
@Transactional
public class CommandeServiceImpl implements CommandeService {

    // Constants
    private static final String UTILISATEUR_INTROUVABLE = "Utilisateur introuvable";
    private static final String PANIER_INTROUVABLE = "Panier introuvable";
    private static final String PANIER_VIDE = "Votre panier est vide";
    private static final String STATUT_PAYE = "Payé";
    private static final String EMAIL_SUBJECT = "Confirmation de votre commande #%d";
    private static final Font PDF_TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);

    // Services
    private final EmailService emailService;
    private final FactureGenerator factureGenerator;

    // Repositories
    private final CommandeRepository commandeRepository;
    private final PaiementRepository paiementRepository;
    private final PanierRepository panierRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommandeServiceImpl(EmailService emailService,
                               CommandeRepository commandeRepository,
                               PaiementRepository paiementRepository,
                               PanierRepository panierRepository,
                               UserRepository userRepository,
                               FactureGenerator factureGenerator) {
        this.emailService = emailService;
        this.commandeRepository = commandeRepository;
        this.paiementRepository = paiementRepository;
        this.panierRepository = panierRepository;
        this.userRepository = userRepository;
        this.factureGenerator = factureGenerator;
    }

    @Override
    public Commande passerCommande(Long userId, CoordonneesDTO coordonnees, String methodePaiement) {
        validateInputs(userId, coordonnees, methodePaiement);

        User utilisateur = updateUserInfo(userId, coordonnees);
        Panier panier = validatePanier(userId);

        double montantTotal = calculerMontantTotal(panier);
        Paiement paiement = creerPaiement(montantTotal, methodePaiement);
        Commande commande = creerCommande(utilisateur, paiement);

        envoyerConfirmation(utilisateur, coordonnees.getEmail(), commande, montantTotal);
        viderPanier(panier);

        return commande;
    }

    // Validation methods
    private void validateInputs(Long userId, CoordonneesDTO coordonnees, String methodePaiement) {
        Objects.requireNonNull(userId, "User ID ne peut pas être null");
        Objects.requireNonNull(coordonnees, "Coordonnées ne peuvent pas être null");
        Objects.requireNonNull(methodePaiement, "Méthode de paiement ne peut pas être null");
    }

    // User operations
    private User updateUserInfo(Long userId, CoordonneesDTO coordonnees) {
        return userRepository.findById(userId)
                .map(user -> updateUserFields(user, coordonnees))
                .orElseThrow(() -> new BusinessException(UTILISATEUR_INTROUVABLE));
    }

    private User updateUserFields(User user, CoordonneesDTO coordonnees) {
        user.setNom(coordonnees.getNom());
        user.setPrenom(coordonnees.getPrenom());
        user.setAdresse(coordonnees.getAdresse());
        user.setTelephone(coordonnees.getTelephone());
        return userRepository.save(user);
    }

    // Panier operations
    private Panier validatePanier(Long userId) {
        Panier panier = panierRepository.findByUtilisateurId(userId)
                .orElseThrow(() -> new BusinessException(PANIER_INTROUVABLE));

        if (panier.getProduits().isEmpty()) {
            throw new BusinessException(PANIER_VIDE);
        }

        return panier;
    }

    private double calculerMontantTotal(Panier panier) {
        return panier.getProduits().stream()
                .filter(ProduitClassique.class::isInstance)
                .mapToDouble(this::calculerPrixProduit)
                .sum();
    }

    private double calculerPrixProduit(Produit produit) {
        return ((ProduitClassique) produit).getPrixFixe() * produit.getQuantity();
    }

    // Payment operations
    private Paiement creerPaiement(double montantTotal, String methodePaiement) {
        Paiement paiement = new Paiement();
        paiement.setDate(Date.valueOf(LocalDate.now()));
        paiement.setMontant(montantTotal);
        paiement.setMethode(methodePaiement);
        paiement.setStatut(STATUT_PAYE);
        return paiementRepository.save(paiement);
    }

    // Order operations
    private Commande creerCommande(User utilisateur, Paiement paiement) {
        Commande commande = new Commande();
        commande.setDate(Date.valueOf(LocalDate.now()));
        commande.setEtat(EnumEtat.EN_ATTENTE);
        commande.setPaiement(paiement);
        commande.setUtilisateur(utilisateur);
        return commandeRepository.save(commande);
    }

    // Email operations
    private void envoyerConfirmation(User user, String email, Commande commande, double total) {
        byte[] facturePDF = factureGenerator.generate(user, commande, total);
        String emailContent = buildEmailContent(user.getPrenom(), commande.getId());

        try {
            emailService.envoyerEmailAvecFacture(
                    email,
                    String.format(EMAIL_SUBJECT, commande.getId()),
                    emailContent,
                    facturePDF
            );
        } catch (MessagingException e) {
            throw new EmailException("Erreur lors de l'envoi de l'email de confirmation", e);
        }
    }

    private String buildEmailContent(String prenom, Long commandeId) {
        return String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #2c3e50;">Merci pour votre achat, %s !</h2>
                <p>Votre commande #%d est confirmée et en cours de préparation.</p>
                <p>Vous trouverez votre facture en pièce jointe.</p>
                <div style="margin-top: 20px; padding: 15px; background-color: #f8f9fa; border-radius: 5px;">
                    <p style="margin: 0;">Besoin d'aide ? Contactez notre service client.</p>
                </div>
            </div>
            """, prenom, commandeId);
    }

    // Panier cleanup
    private void viderPanier(Panier panier) {
        panier.getProduits().clear();
        panierRepository.save(panier);
    }
}

// Facture Generator Interface
interface FactureGenerator {
    byte[] generate(User user, Commande commande, double total);
}

// PDF Facture Implementation
@Service
class PdfFactureGenerator implements FactureGenerator {

    @Override
    public byte[] generate(User user, Commande commande, double total) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();
            addDocumentHeader(document, user);
            addOrderDetails(document, commande, total);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new FactureGenerationException("Erreur lors de la génération de la facture", e);
        }
    }

    private void addDocumentHeader(Document document, User user) throws DocumentException {
        Paragraph header = new Paragraph("Facture - EcommerceApp");
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        document.add(new Paragraph("Date: " + LocalDate.now()));
        document.add(new Paragraph("Client: " + user.getNom()));
        document.add(new Paragraph("Adresse: " + user.getAdresse()));
    }

    private void addOrderDetails(Document document, Commande commande, double total) throws DocumentException {
        document.add(new Paragraph("Montant total: " + total + " TND"));
        document.add(new Paragraph("Mode de paiement: " + commande.getPaiement().getMethode()));
        document.add(new Paragraph("Statut: " + commande.getEtat()));
        document.add(new Paragraph("\nMerci pour votre confiance !"));
    }
}

// Custom Exceptions
class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

class EmailException extends RuntimeException {
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}

class FactureGenerationException extends RuntimeException {
    public FactureGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
