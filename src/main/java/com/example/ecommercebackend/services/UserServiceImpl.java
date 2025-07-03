package com.example.ecommercebackend.services;

import com.example.ecommercebackend.entities.*;
import com.example.ecommercebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));
    }

    @Override
    public User getUserFromToken(String token) {
        // Implémentation de la récupération du user depuis le token JWT
        // (à adapter selon votre système d'authentification)
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public List<Mise> getUserMises(Long userId) {
        return userRepository.findById(userId)
                .map(User::getMises)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Override
    public Panier getUserPanier(Long userId) {
        return userRepository.findById(userId)
                .map(User::getPanier)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Override
    public List<Commande> getUserCommandes(Long userId) {
        return userRepository.findById(userId)
                .map(User::getCommandes)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}