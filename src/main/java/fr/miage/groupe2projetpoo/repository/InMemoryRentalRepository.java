package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryRentalRepository implements RentalRepository {

    private final Map<Integer, RentalContract> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public RentalContract save(RentalContract contract) {
        if (contract.getIdC() == 0) {
            contract.setIdC(idCounter.getAndIncrement());
        }
        storage.put(contract.getIdC(), contract);
        return contract;
    }

    @Override
    public Optional<RentalContract> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<RentalContract> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(int id) {
        storage.remove(id);
    }

    @Override
    public List<RentalContract> findByLoueurEmail(String email) {
        return storage.values().stream()
                .filter(c -> c.getLoueur() != null &&
                        c.getLoueur().getEmail().equals(email))
                .collect(Collectors.toList());
    }

    @Override
    public List<RentalContract> findByVehiculeId(String vehiculeId) {
        return storage.values().stream()
                .filter(c -> c.getVehicule() != null &&
                        c.getVehicule().getIdVehicule().equals(vehiculeId))
                .collect(Collectors.toList());
    }
}
