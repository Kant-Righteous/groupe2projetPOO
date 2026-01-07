package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service pour la gestion des utilisateurs (inscription et connexion)
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Inscription d'un utilisateur
     */
    public Utilisateur register(String email, String password, String nom, String prenom, Role role) {
        if (userRepository.existsByEmail(email)) {
            return null;
        }

        Utilisateur user;
        switch (role) {
            case AGENT_PARTICULIER:
                user = new AgentParticulier(email, password, nom, prenom);
                break;
            case AGENT_PROFESSIONNEL:
                user = new AgentProfessionnel(email, password, nom, prenom, null, null);
                break;
            case LOUEUR:
                user = new Loueur(email, password, nom, prenom, null, null);
                break;
            default:
                return null;
        }

        return userRepository.save(user);
    }

    /**
     * Connexion d'un utilisateur
     */
    public Utilisateur login(String email, String password) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return null;
        }

        Utilisateur user = userOpt.get();

        if (!user.getPassword().equals(password)) {
            return null;
        }

        return user;
    }

    /**
     * Trouver un utilisateur par email (pour vérification)
     */
    public Optional<Utilisateur> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
