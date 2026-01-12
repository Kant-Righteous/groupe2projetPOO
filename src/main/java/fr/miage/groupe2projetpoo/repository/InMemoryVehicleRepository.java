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
@Repository
public class InMemoryVehicleRepository implements VehicleRepository {
    // Map de vehicule
    private final Map<String, Vehicle> vehicules = new ConcurrentHashMap<>();

    public Map<String, Vehicle> getVehicules() {
        return vehicules;
    }
    public Vehicle save(Vehicle v) {
        vehicules.put(v.getIdVehicule(), v);
        return v;
    }
    // Chercher par ID
    public Optional<Vehicle> findById(String id) {
        return Optional.ofNullable(vehicules.get(id));
    }

    // Chercher par ville
    public List<Vehicle> findByVille(String ville) {
        return vehicules.values().stream()
                .filter(v -> v.getVilleVehicule().equalsIgnoreCase(ville))
                .toList();
    }

    // Verifier l'existance de l'ID
    public boolean existsById(String id) {
        return vehicules.containsKey(id);
    }

    // Chercher par disponibilité
    public Optional<Vehicle> findByDisponibility(LocalDate debut, LocalDate fin) {
        return vehicules.values().stream()
                .filter(v -> v.estDisponible(debut, fin))
                .findFirst();
    }

    // Suppression by Id
    public void deleteById(String id){
        if(!vehicules.containsKey(id)){
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        vehicules.remove(id);
    }

    // Modifier un vehicule
    public void modifVehicule(String id, Vehicle modif) {
        if (!vehicules.containsKey(id)) {
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        vehicules.put(id, modif);
    }
}