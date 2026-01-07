package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.entity.vehicule.*;
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
    private final List<Vehicle> allVehicles = new ArrayList<>();
    private final List<RentalContract> allContracts = new ArrayList<>();
    private final List<Assurance> allAssurances = new ArrayList<>();

    /**
     * Initialisation des données de test
     */
    @PostConstruct
    public void init() {
        // === 1. Créer les assurances ===
        Assurance assuranceBasic = new Assurance(1, "Assurance Basic", "Responsabilité civile", 15.0f);
        Assurance assuranceComplete = new Assurance(2, "Assurance Tous Risques", "Tous risques", 35.0f);
        Assurance assurancePremium = new Assurance(3, "Assurance Premium", "Tous risques + vol + bris de glace", 50.0f);
        allAssurances.add(assuranceBasic);
        allAssurances.add(assuranceComplete);
        allAssurances.add(assurancePremium);

        // === 2. Créer les véhicules ===
        // Voitures
        Voiture voiture1 = new Voiture(1, "Voiture", "Renault", "Bleu", "Clio", "Paris", true, 45.0);
        Voiture voiture2 = new Voiture(2, "Voiture", "Peugeot", "Noir", "308", "Lyon", true, 55.0);
        Voiture voiture3 = new Voiture(3, "Voiture", "BMW", "Blanc", "Serie 3", "Marseille", true, 85.0);
        Voiture voiture4 = new Voiture(4, "Voiture", "Mercedes", "Gris", "Classe A", "Paris", false, 90.0);

        // Motos
        Moto moto1 = new Moto(5, "Moto", "Yamaha", "Rouge", "MT-07", "Nice", true, 60.0);
        Moto moto2 = new Moto(6, "Moto", "Honda", "Noir", "CB650R", "Bordeaux", true, 65.0);

        // Camions
        Camion camion1 = new Camion(7, "Camion", "Renault", "Blanc", "Master", "Paris", true, 120.0);
        Camion camion2 = new Camion(8, "Camion", "Mercedes", "Jaune", "Sprinter", "Lyon", true, 135.0);

        allVehicles.addAll(Arrays.asList(voiture1, voiture2, voiture3, voiture4, moto1, moto2, camion1, camion2));

        // === 3. Créer les Loueurs et assigner les véhicules ===
        Loueur loueur1 = new Loueur("loueur1@test.com", "123456", "Bernard", "Luc", "FR7630001007941234567890185",
                "Bernard Auto");
        loueur1.addVehicle(voiture1);
        loueur1.addVehicle(voiture2);
        loueur1.addVehicle(moto1);

        Loueur loueur2 = new Loueur("loueur2@test.com", "123456", "Petit", "Sophie", "FR7630004000031234567890143",
                "Sophie Location");
        loueur2.addVehicle(voiture3);
        loueur2.addVehicle(voiture4);
        loueur2.addVehicle(moto2);
        loueur2.addVehicle(camion1);
        loueur2.addVehicle(camion2);

        // === 4. Créer les Agents ===
        AgentParticulier agentPart1 = new AgentParticulier("alice@test.com", "123456", "Martin", "Alice");
        AgentParticulier agentPart2 = new AgentParticulier("bob@test.com", "123456", "Dupont", "Bob");

        AgentProfessionnel agentPro1 = new AgentProfessionnel("enterprise1@test.com", "123456", "Durand", "Pierre",
                "Durand SA", "12345678901234");
        AgentProfessionnel agentPro2 = new AgentProfessionnel("enterprise2@test.com", "123456", "Moreau", "Marie",
                "Moreau SARL", "98765432109876");

        // === 5. Créer les Contrats de location ===
        // Calculer les dates
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date nextWeek = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date twoWeeksLater = cal.getTime();

        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -5);
        Date fiveDaysAgo = cal.getTime();

        // Contrat 1: Alice loue la Renault Clio de Luc
        RentalContract contract1 = new RentalContract(loueur1, voiture1, today, nextWeek, "Paris - Gare du Nord",
                "Paris - Aéroport CDG", assuranceBasic);
        contract1.setIdC(1);
        loueur1.addContract(contract1);
        agentPart1.addContract(contract1);
        agentPart1.addVehicle(voiture1);

        // Contrat 2: Bob loue la BMW Serie 3 de Sophie
        RentalContract contract2 = new RentalContract(loueur2, voiture3, fiveDaysAgo, today, "Marseille Centre",
                "Marseille Aéroport", assuranceComplete);
        contract2.setIdC(2);
        contract2.setStatut(true); // Contrat terminé
        contract2.setSignatureLoueur(true);
        contract2.setSignatureAgent(true);
        loueur2.addContract(contract2);
        agentPart2.addContract(contract2);
        agentPart2.addVehicle(voiture3);

        // Contrat 3: Entreprise Durand SA loue le camion Renault Master de Sophie
        RentalContract contract3 = new RentalContract(loueur2, camion1, today, twoWeeksLater, "Paris - Entrepôt",
                "Lyon - Centre", assurancePremium);
        contract3.setIdC(3);
        loueur2.addContract(contract3);
        agentPro1.addContract(contract3);
        agentPro1.addVehicle(camion1);

        // Contrat 4: Entreprise Moreau SARL loue la moto Yamaha de Luc
        RentalContract contract4 = new RentalContract(loueur1, moto1, nextWeek, twoWeeksLater, "Nice - Centre Ville",
                "Nice - Promenade", assuranceBasic);
        contract4.setIdC(4);
        loueur1.addContract(contract4);
        agentPro2.addContract(contract4);
        agentPro2.addVehicle(moto1);

        allContracts.addAll(Arrays.asList(contract1, contract2, contract3, contract4));

        // === 6. Sauvegarder tous les utilisateurs ===
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

    public List<Assurance> getAllAssurances() {
        return new ArrayList<>(allAssurances);
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
