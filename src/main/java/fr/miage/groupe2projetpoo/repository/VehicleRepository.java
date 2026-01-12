package fr.miage.groupe2projetpoo.repository;

import org.springframework.stereotype.Repository;

import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public interface VehicleRepository {
    // Map de vehicule
    public Vehicle save(Vehicle v);
    // Chercher par ID
    public Optional<Vehicle> findById(String id);
    // Chercher par ville
    public List<Vehicle> findByVille(String ville);
    // Verifier l'existance de l'ID
    public boolean existsById(String id);

    // Chercher par disponibilité
    public Optional<Vehicle> findByDisponibility(LocalDate debut, LocalDate fin);

    // Suppression by Id
    public void deleteById(String id);

    // Modifier un vehicule
    public void modifVehicule(String id, Vehicle modif);
}
