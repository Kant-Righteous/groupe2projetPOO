package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public Utilisateur register(String nom, String prenom, String password, String email, String tel, Role role) {
        if (userRepository.existsByEmail(email)) {
            return null;
        }

        Utilisateur user;
        switch (role) {
            case AGENT_PARTICULIER:
                user = new AgentParticulier(nom, prenom, password, email, tel);
                break;
            case AGENT_PROFESSIONNEL:
                user = new AgentProfessionnel(nom, prenom, password, email, tel, null, null);
                break;
            case LOUEUR:
                user = new Loueur(nom, prenom, password, email, tel, null, null);
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

    /**
     * Récupérer les véhicules d'un utilisateur (Loueur ou Agent)
     */
    public List<Vehicle> getVehiclesByUserEmail(String email) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Utilisateur user = userOpt.get();

        if (user instanceof Loueur) {
            return ((Loueur) user).getVehicles();
        } else if (user instanceof Agent) {
            return ((Agent) user).getVehicleList();
        }

        return new ArrayList<>();
    }

    /**
     * Récupérer les contrats d'un utilisateur (Loueur ou Agent)
     */
    public List<RentalContract> getContractsByUserEmail(String email) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Utilisateur user = userOpt.get();

        if (user instanceof Loueur) {
            return ((Loueur) user).getContracts();
        } else if (user instanceof Agent) {
            return ((Agent) user).getContracts();
        }

        return new ArrayList<>();
    }
}
