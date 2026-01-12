package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import java.util.Optional;

/**
 * Interface pour l'accès aux données utilisateur
 */
public interface UserRepository {

    public abstract Utilisateur save(Utilisateur user);

    public abstract Optional<Utilisateur> findByEmail(String email);

    public abstract boolean existsByEmail(String email);

    // Méthodes pour les assurances
    public abstract java.util.List<fr.miage.groupe2projetpoo.entity.assurance.Assurance> getAllAssurances();

    public abstract java.util.Optional<fr.miage.groupe2projetpoo.entity.assurance.Assurance> findAssuranceById(int id);

    public abstract java.util.Optional<fr.miage.groupe2projetpoo.entity.vehicule.Vehicle> findVehicleById(String id);
}
