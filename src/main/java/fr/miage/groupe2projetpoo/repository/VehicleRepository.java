package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    Optional<Vehicle> findById(int id);
    List<Vehicle> findAll();
}
