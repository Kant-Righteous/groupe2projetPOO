package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.vehicule.Disponibilite;
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
    public List<Vehicle> findByType(String type);
    public List<Vehicle> findByEnPause();
    public Collection<Vehicle> findAll();
    // Verifier l'existance de l'ID
    public boolean existsById(String id);

    // Chercher par disponibilité
    public Optional<Vehicle> findByDisponibility(LocalDate debut, LocalDate fin);

    // Suppression by Id
    public void deleteById(String id);

    // Modifier un vehicule
    public void modifVehicule(String id, Vehicle modif);

    public List<Disponibilite> getPlanning(String id);
    public void addPlanning(String id,LocalDate debut, LocalDate fin);
    public void removeCreneau(String id, int index);
    public boolean estDisponible(String id, LocalDate debut, LocalDate fin);
}
