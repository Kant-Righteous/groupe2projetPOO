package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.entity.vehicule.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implémentation en mémoire du repository utilisateur avec données de test
 * complètes
 */
@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, Utilisateur> users = new ConcurrentHashMap<>();
    private final List<Assurance> allAssurances = new ArrayList<>();
    // Ajout de la liste des options
    private final List<fr.miage.groupe2projetpoo.entity.assurance.OptionPayante> allOptions = new ArrayList<>();

    private final VehicleRepository vehicleRepository;

    @Autowired
    public InMemoryUserRepository(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Initialisation des données de test (Assurances uniquement)
     */
    @PostConstruct
    public void init() {
        // === 1. Créer les assurances ===
        Assurance assuranceBasic = new Assurance(1, "Assurance Basic", 15.0);
        Assurance assuranceComplete = new Assurance(2, "Assurance Tous Risques", 35.0);
        Assurance assurancePremium = new Assurance(3, "Assurance Premium", 50.0);
        allAssurances.add(assuranceBasic);
        allAssurances.add(assuranceComplete);
        allAssurances.add(assurancePremium);

        // === 1bis. Créer les Options ===
        fr.miage.groupe2projetpoo.entity.assurance.OptionParking optionParking = new fr.miage.groupe2projetpoo.entity.assurance.OptionParking(
                "Abonnement Parking VIP", 15.0, true, 24, 30, 8.0,
                new fr.miage.groupe2projetpoo.entity.infrastructure.Parking(1, "Gare de Lyon", "18 Rue de Lyon",
                        "Paris", 500, 10.0));
        allOptions.add(optionParking);
    }

    @Override
    public Utilisateur save(Utilisateur user) {
        users.put(user.getEmail(), user);
        return user;
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.containsKey(email);
    }

    // === Méthodes utilitaires pour accéder aux données ===

    @Override
    public List<Assurance> getAllAssurances() {
        return new ArrayList<>(allAssurances);
    }

    @Override
    public Optional<Assurance> findAssuranceById(int id) {
        return allAssurances.stream()
                .filter(a -> a.getIdA() == id)
                .findFirst();
    }

    @Override
    public Optional<Vehicle> findVehicleById(String id) {
        return vehicleRepository.findById(id);
    }

    public List<Utilisateur> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<Loueur> getAllLoueurs() {
        List<Loueur> loueurs = new ArrayList<>();
        for (Utilisateur u : users.values()) {
            if (u instanceof Loueur) {
                loueurs.add((Loueur) u);
            }
        }
        return loueurs;
    }

    @Override
    public List<Agent> getAllAgents() {
        List<Agent> agents = new ArrayList<>();
        for (Utilisateur u : users.values()) {
            if (u instanceof Agent) {
                agents.add((Agent) u);
            }
        }
        return agents;
    }
}
