package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour l'accès aux données utilisateur
 */
public interface UserRepository {

    public abstract Utilisateur save(Utilisateur user);

    public abstract Optional<Utilisateur> findByEmail(String email);

    public abstract boolean existsByEmail(String email);

    // Méthodes pour les assurances
    public abstract List<Assurance> getAllAssurances();

    public abstract Optional<Assurance> findAssuranceById(int id);

    public abstract Optional<Vehicle> findVehicleById(String id);

    // Méthode pour récupérer tous les agents
    public abstract List<Agent> getAllAgents();
}
