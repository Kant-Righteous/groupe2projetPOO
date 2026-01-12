package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.entity.vehicule.*;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implémentation en mémoire du repository utilisateur avec données de test
 * complètes
 */
@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, Utilisateur> users = new ConcurrentHashMap<>();
    private final List<Vehicle> allVehicles = new ArrayList<>();
    private final List<RentalContract> allContracts = new ArrayList<>();
    private final List<Assurance> allAssurances = new ArrayList<>();

    /**
     * Initialisation des données de test
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

        // === 2. Créer les Agents d'abord (car les véhicules ont besoin d'un
        // propriétaire) ===
        AgentParticulier agentPart1 = new AgentParticulier("Martin", "Alice", "123456", "alice@test.com", "0612345678");
        AgentParticulier agentPart2 = new AgentParticulier("Dupont", "Bob", "123456", "bob@test.com", "0698765432");
        AgentProfessionnel agentPro1 = new AgentProfessionnel("Durand", "Pierre", "123456", "enterprise1@test.com",
                "0611111111",
                "Durand SA", "12345678901234");
        AgentProfessionnel agentPro2 = new AgentProfessionnel("Moreau", "Marie", "123456", "enterprise2@test.com",
                "0622222222",
                "Moreau SARL", "98765432109876");

        // === 3. Créer les listes de disponibilités ===
        LocalDate today = LocalDate.now();

        // Disponibilités pour les prochains 30 jours
        List<LocalDate> disponibilites30Jours = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            disponibilites30Jours.add(today.plusDays(i));
        }

        // Disponibilités pour les prochains 14 jours
        List<LocalDate> disponibilites14Jours = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            disponibilites14Jours.add(today.plusDays(i));
        }

        // Disponibilités limitées (seulement week-ends)
        List<LocalDate> disponibilitesWeekend = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            LocalDate date = today.plusDays(i);
            if (date.getDayOfWeek().getValue() >= 6) { // Samedi et Dimanche
                disponibilitesWeekend.add(date);
            }
        }

        // === 4. Créer les véhicules avec propriétaire et disponibilités ===
        // Véhicules d'Alice (AgentParticulier)
        Voiture voiture1 = new Voiture("1", "Renault", "Bleu", "Clio", "Paris", 45.0,
                "alice@test.com", false);
        Voiture voiture2 = new Voiture("2", "Peugeot", "Noir", "308", "Lyon", 55.0,
                "alice@test.com", false);

        // Véhicules de Bob (AgentParticulier)
        Voiture voiture3 = new Voiture("3", "BMW", "Blanc", "Serie 3", "Marseille", 85.0,
                "bob@test.com", false);
        Moto moto1 = new Moto("4", "Yamaha", "Rouge", "MT-07", "Nice", 60.0,
                "bob@test.com", false);

        // Véhicules de Durand SA (AgentProfessionnel)
        Voiture voiture4 = new Voiture("5", "Mercedes", "Gris", "Classe A", "Paris", 90.0,
                "enterprise1@test.com",false);
        Camion camion1 = new Camion("6", "Renault", "Blanc", "Master", "Paris", 120.0,
                "enterprise1@test.com", false);
        Camion camion2 = new Camion("7", "Mercedes", "Jaune", "Sprinter", "Lyon", 135.0,
                "enterprise1@test.com",false);

        // Véhicules de Moreau SARL (AgentProfessionnel)
        Moto moto2 = new Moto("8", "Honda", "Noir", "CB650R", "Bordeaux", 65.0,
                "bob@test.com",false);

        allVehicles.addAll(Arrays.asList(voiture1, voiture2, voiture3, voiture4, moto1, moto2, camion1, camion2));

        // === 5. Assigner les véhicules aux agents ===
        agentPart1.addVehicle(voiture1);
        agentPart1.addVehicle(voiture2);

        agentPart2.addVehicle(voiture3);
        agentPart2.addVehicle(moto1);

        agentPro1.addVehicle(voiture4);
        agentPro1.addVehicle(camion1);
        agentPro1.addVehicle(camion2);

        agentPro2.addVehicle(moto2);

        // === 6. Créer les Loueurs ===
        Loueur loueur1 = new Loueur("Bernard", "Luc", "123456", "loueur1@test.com", "0633333333",
                "FR7630001007941234567890185",
                "Bernard Auto");
        Loueur loueur2 = new Loueur("Petit", "Sophie", "123456", "loueur2@test.com", "0644444444",
                "FR7630004000031234567890143",
                "Sophie Location");

        // === 7. Créer les Contrats de location ===
        Calendar cal = Calendar.getInstance();
        Date today_date = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date nextWeek = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date twoWeeksLater = cal.getTime();

        cal.setTime(today_date);
        cal.add(Calendar.DAY_OF_MONTH, -5);
        Date fiveDaysAgo = cal.getTime();

        // Contrat 1: Loueur1 loue la Renault Clio d'Alice
        RentalContract contract1 = new RentalContract(loueur1, voiture1, today_date, nextWeek, "Paris - Gare du Nord",
                "Paris - Aéroport CDG", assuranceBasic);
        contract1.setIdC(1);
        loueur1.addContract(contract1);
        loueur1.addVehicle(voiture1);
        agentPart1.addContract(contract1);

        // Contrat 2: Loueur2 loue la BMW Serie 3 de Bob (contrat terminé)
        RentalContract contract2 = new RentalContract(loueur2, voiture3, fiveDaysAgo, today_date, "Marseille Centre",
                "Marseille Aéroport", assuranceComplete);
        contract2.setIdC(2);
        contract2.setStatut(true);
        contract2.setSignatureLoueur(true);
        contract2.setSignatureAgent(true);
        loueur2.addContract(contract2);
        loueur2.addVehicle(voiture3);
        agentPart2.addContract(contract2);

        // Contrat 3: Loueur1 loue le camion Renault Master de Durand SA
        RentalContract contract3 = new RentalContract(loueur1, camion1, today_date, twoWeeksLater, "Paris - Entrepôt",
                "Lyon - Centre", assurancePremium);
        contract3.setIdC(3);
        loueur1.addContract(contract3);
        loueur1.addVehicle(camion1);
        agentPro1.addContract(contract3);

        // Contrat 4: Loueur2 loue la moto Honda de Moreau SARL
        RentalContract contract4 = new RentalContract(loueur2, moto2, nextWeek, twoWeeksLater,
                "Bordeaux - Centre Ville", "Bordeaux - Gare", assuranceBasic);
        contract4.setIdC(4);
        loueur2.addContract(contract4);
        loueur2.addVehicle(moto2);
        agentPro2.addContract(contract4);

        allContracts.addAll(Arrays.asList(contract1, contract2, contract3, contract4));

        // === 8. Sauvegarder tous les utilisateurs ===
        save(agentPart1);
        save(agentPart2);
        save(agentPro1);
        save(agentPro2);
        save(loueur1);
        save(loueur2);
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

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(allVehicles);
    }

    public List<RentalContract> getAllContracts() {
        return new ArrayList<>(allContracts);
    }

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
        return allVehicles.stream()
                .filter(v -> v.getIdVehicule().equals(id))
                .findFirst();
    }

    public List<Utilisateur> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public List<Loueur> getAllLoueurs() {
        List<Loueur> loueurs = new ArrayList<>();
        for (Utilisateur u : users.values()) {
            if (u instanceof Loueur) {
                loueurs.add((Loueur) u);
            }
        }
        return loueurs;
    }

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
