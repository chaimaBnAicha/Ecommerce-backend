package com.example.ecommercebackend.security;

import com.example.ecommercebackend.entities.User;
import com.example.ecommercebackend.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Autowired
    UserRepository userRepository;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        System.out.println("JWT généré pour userId=" + user.getId());

        return Jwts.builder()
                .setSubject(user.getEmail()) // ou getUsername()
                .claim("userId", user.getId())  // ✅ AJOUTER CETTE LIGNE
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }



    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}