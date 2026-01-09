package fr.miage.groupe2projetpoo.repository;

import org.springframework.stereotype.Repository;

import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class VehicleRepository {
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


}
