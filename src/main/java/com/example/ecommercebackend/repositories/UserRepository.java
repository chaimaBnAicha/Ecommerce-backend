package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Méthodes spécifiques selon les rôles
    List<User> findByRole(User.Role role);

    // Pour la gestion du panier
    Optional<User> findByPanierId(Long panierId);
}