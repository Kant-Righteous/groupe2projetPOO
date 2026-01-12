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

    // Stockage temporaire en mémoire avec EMAIL comme clé pour les loueurs
    private final Map<String, Loueur> loueurs = new HashMap<>();
    private final Map<String, Vehicle> vehicules = new HashMap<>();
    private final Map<String, Assurance> assurances = new HashMap<>();

    public InMemoryRentalRepository() {
        initDonneesTest();
    }

    /**
     * Initialise des données de test pour pouvoir tester l'API
     */
    private void initDonneesTest() {
        // Créer des loueurs de test (clé = email)
        Loueur loueur1 = new Loueur("Dupont", "Jean", "pass123", "jean@email.com", "0601020304");
        loueurs.put("jean@email.com", loueur1);

        Loueur loueur2 = new Loueur("Martin", "Marie", "pass456", "marie@email.com", "0605060708");
        loueurs.put("marie@email.com", loueur2);

        // Créer des véhicules de test (clé = idVehicule)
        // Paramètres: idVehicule, marqueVehicule, couleurVehicule, modeleVehicule,
        // villeVehicule, prixVehiculeParJour, proprietaire, estEnpause
        Voiture voiture1 = new Voiture("V001", "Renault", "Bleu", "Clio",
                "Paris", 30.0, "Agent001", false);
        vehicules.put("V001", voiture1);

        Voiture voiture2 = new Voiture("V002", "Peugeot", "Noir", "3008",
                "Lyon", 50.0, "Agent002", false);
        vehicules.put("V002", voiture2);

        // Créer des assurances de test (clé = nom)
        Assurance assurance1 = new Assurance(1, "AZA Classique", 15.0);
        assurances.put("AZA Classique", assurance1);

        Assurance assurance2 = new Assurance(2, "AZA Premium", 25.0);
        assurances.put("AZA Premium", assurance2);
    }

    // ===== MÉTHODES D'ACCÈS AUX DONNÉES DE TEST =====

    public Loueur getLoueurByEmail(String email) {
        return loueurs.get(email);
    }

    public Vehicle getVehiculeById(String id) {
        return vehicules.get(id);
    }

    public Assurance getAssuranceByNom(String nom) {
        return assurances.get(nom);
    }

    public List<Loueur> getAllLoueurs() {
        return new ArrayList<>(loueurs.values());
    }

    public List<Vehicle> getAllVehicules() {
        return new ArrayList<>(vehicules.values());
    }

    public List<Assurance> getAllAssurances() {
        return new ArrayList<>(assurances.values());
    }

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
