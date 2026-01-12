package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import java.util.List;
import java.util.Optional;

public interface RentalRepository {
    
    RentalContract save(RentalContract contract);
    
    Optional<RentalContract> findById(int id);
    
    List<RentalContract> findAll();
    
    void deleteById(int id);
    
    List<RentalContract> findByLoueurEmail(String email);
    
    List<RentalContract> findByVehiculeId(String vehiculeId);
}
