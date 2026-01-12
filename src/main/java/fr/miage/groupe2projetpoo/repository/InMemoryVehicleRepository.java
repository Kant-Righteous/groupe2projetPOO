package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

/**
 * Implémentation en mémoire du repository de véhicule.
 * Note: Dans ce projet, le InMemoryUserRepository gère également la liste des
 * véhicules
 * pour faciliter l'initialisation des données de test.
 * Cette classe délègue au UserRepository pour la cohérence des données.
 */
@Repository
public class InMemoryVehicleRepository implements VehicleRepository {

    private final InMemoryUserRepository userRepository;
    private final Map<String, Vehicle> vehicules = new HashMap<>();

    public InMemoryVehicleRepository(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        Vehicle vehicle = vehicules.get(id);
        if (vehicle != null) {
            return Optional.of(vehicle);
        }
        return userRepository.getAllVehicles().stream()
                .filter(v -> v.getIdVehicule().equals(id))
                .findFirst();
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> allVehicles = new ArrayList<>(userRepository.getAllVehicles());
        allVehicles.addAll(vehicules.values());
        return allVehicles;
    }

    @Override
    public boolean existsById(String id) {
        return vehicules.containsKey(id) || findById(id).isPresent();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        vehicules.put(vehicle.getIdVehicule(), vehicle);
        return vehicle;
    }

    @Override
    public Map<String, Vehicle> getVehicules() {
        return vehicules;
    }
}
