package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des Parkings
 */
public interface ParkingRepository {

    /**
     * Récupérer tous les parkings
     */
    List<Parking> findAll();

    /**
     * Trouver un parking par son ID
     */
    Optional<Parking> findById(int id);

    /**
     * Trouver un parking par son nom
     */
    Optional<Parking> findByNom(String nom);

    /**
     * Sauvegarder un parking
     */
    Parking save(Parking parking);

    /**
     * Supprimer un parking par son ID
     */
    void deleteById(int id);
}
