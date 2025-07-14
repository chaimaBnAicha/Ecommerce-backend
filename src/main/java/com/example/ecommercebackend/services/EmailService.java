package com.example.ecommercebackend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void envoyerEmailAvecFacture(String to, String sujet, String contenu, byte[] facturePdf) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(sujet);
        helper.setText(contenu, true); // HTML
        helper.addAttachment("facture.pdf", new ByteArrayResource(facturePdf));

        mailSender.send(message);
    }
    public void envoyerEmailConfirmation(String destinataire, String sujet, String contenu) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setSubject(sujet);
        message.setText(contenu);
        message.setFrom("no-reply@votresite.com");

        try {
            mailSender.send(message);
            System.out.println("Email envoyé avec succès à " + destinataire);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
            throw new RuntimeException("Échec de l'envoi de l'email", e);
        }
    }
}
