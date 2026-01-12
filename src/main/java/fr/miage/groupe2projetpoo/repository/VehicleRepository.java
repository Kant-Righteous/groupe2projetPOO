package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    Optional<Vehicle> findById(String id);

    List<Vehicle> findAll();

    boolean existsById(String id);

    Vehicle save(Vehicle vehicle);

    java.util.Map<String, Vehicle> getVehicules();
}
