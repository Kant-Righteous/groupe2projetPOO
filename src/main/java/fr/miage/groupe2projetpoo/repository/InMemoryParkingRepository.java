package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implémentation en mémoire du ParkingRepository
 */
@Repository
public class InMemoryParkingRepository implements ParkingRepository {

    private final ConcurrentHashMap<Integer, Parking> parkings = new ConcurrentHashMap<>();

    @Override
    public List<Parking> findAll() {
        return new ArrayList<>(parkings.values());
    }

    @Override
    public Optional<Parking> findById(int id) {
        return Optional.ofNullable(parkings.get(id));
    }

    @Override
    public Optional<Parking> findByNom(String nom) {
        return parkings.values().stream()
                .filter(p -> p.getNom().equalsIgnoreCase(nom))
                .findFirst();
    }

    @Override
    public Parking save(Parking parking) {
        parkings.put(parking.getIdParking(), parking);
        return parking;
    }

    @Override
    public void deleteById(int id) {
        parkings.remove(id);
    }
}
