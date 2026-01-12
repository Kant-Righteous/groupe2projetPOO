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

    /**
     * Récupérer tous les agents (pour les visiteurs)
     */
    public List<Agent> getAllAgents() {
        return userRepository.getAllAgents();
    }

    public Utilisateur updateUserInfo(String email, java.util.Map<String, String> updates) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return null;
        }

        Utilisateur user = userOpt.get();

        // Vérifier que l'utilisateur est un Loueur ou un Agent
        if (!(user instanceof Loueur) && !(user instanceof Agent)) {
            return null;
        }

        // Mettre à jour les champs communs (Utilisateur)
        if (updates.containsKey("nom")) {
            user.setNom(updates.get("nom"));
        }
        if (updates.containsKey("prenom")) {
            user.setPrenom(updates.get("prenom"));
        }
        if (updates.containsKey("tel")) {
            user.setTel(updates.get("tel"));
        }
        if (updates.containsKey("password")) {
            user.setPassword(updates.get("password"));
        }

        // Mettre à jour les champs spécifiques au Loueur
        if (user instanceof Loueur) {
            Loueur loueur = (Loueur) user;
            if (updates.containsKey("iban")) {
                loueur.setIban(updates.get("iban"));
            }
            if (updates.containsKey("nomSociete")) {
                loueur.setNomSociete(updates.get("nomSociete"));
            }
        }

        // Mettre à jour les champs spécifiques à l'AgentProfessionnel
        if (user instanceof AgentProfessionnel) {
            AgentProfessionnel agentPro = (AgentProfessionnel) user;
            if (updates.containsKey("nomEntreprise")) {
                agentPro.setNomEntreprise(updates.get("nomEntreprise"));
            }
            if (updates.containsKey("siret")) {
                agentPro.setSiret(updates.get("siret"));
            }
        }

        return userRepository.save(user);
    }
}
