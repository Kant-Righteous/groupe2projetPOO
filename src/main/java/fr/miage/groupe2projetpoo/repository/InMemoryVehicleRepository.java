package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation en mémoire du repository de véhicule.
 * Note: Dans ce projet, le InMemoryUserRepository gère également la liste des véhicules
 * pour faciliter l'initialisation des données de test.
 * Cette classe délègue au UserRepository pour la cohérence des données.
 */
@Repository
public class InMemoryVehicleRepository implements VehicleRepository {

    private final InMemoryUserRepository userRepository;

    public InMemoryVehicleRepository(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Vehicle> findById(int id) {
        return userRepository.getAllVehicles().stream()
                .filter(v -> v.getIdVehicule() == id)
                .findFirst();
    }

    @Override
    public List<Vehicle> findAll() {
        return userRepository.getAllVehicles();
    }
}
