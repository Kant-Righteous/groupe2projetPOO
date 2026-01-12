package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;
import fr.miage.groupe2projetpoo.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    
    // Stockage temporaire en mémoire avec EMAIL comme clé pour les loueurs
    private final Map<String, Loueur> loueurs = new HashMap<>();
    private final Map<String, Vehicle> vehicules = new HashMap<>();
    private final Map<String, Assurance> assurances = new HashMap<>();

    @Autowired
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
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

        // Créer des disponibilités (janvier 2026)
        List<LocalDate> dispos = new ArrayList<>();
        for (int jour = 1; jour <= 31; jour++) {
            dispos.add(LocalDate.of(2026, 1, jour));
        }

        // Créer des véhicules de test (clé = idVehicule)
        Voiture voiture1 = new Voiture("V001", "Citadine", "Renault", "Bleu", "Clio", 
                "Paris", true, 30.0, "Agent001", dispos);
        voiture1.setListeDisponibilites(dispos);
        vehicules.put("V001", voiture1);

        Voiture voiture2 = new Voiture("V002", "SUV", "Peugeot", "Noir", "3008", 
                "Lyon", true, 50.0, "Agent002", dispos);
        voiture2.setListeDisponibilites(dispos);
        vehicules.put("V002", voiture2);

        // Créer des assurances de test (clé = nom)
        Assurance assurance1 = new Assurance(1, "AZA Classique", 15.0);
        assurances.put("AZA Classique", assurance1);

        Assurance assurance2 = new Assurance(2, "AZA Premium", 25.0);
        assurances.put("AZA Premium", assurance2);
    }

    // ===== MÉTHODES CRUD =====

    /**
     * Créer un nouveau contrat de location
     * @param loueurEmail Email du loueur
     * @param vehiculeId ID du véhicule
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @param lieuPrise Lieu de prise
     * @param lieuDepose Lieu de dépôt
     * @param assuranceNom Nom de l'assurance
     */
    public RentalContract creerContrat(String loueurEmail, String vehiculeId, 
                                        Date dateDebut, Date dateFin,
                                        String lieuPrise, String lieuDepose,
                                        String assuranceNom) {
        
        Loueur loueur = loueurs.get(loueurEmail);
        if (loueur == null) {
            throw new RuntimeException("Loueur non trouvé avec l'email: " + loueurEmail);
        }

        Vehicle vehicule = vehicules.get(vehiculeId);
        if (vehicule == null) {
            throw new RuntimeException("Véhicule non trouvé avec l'ID: " + vehiculeId);
        }

        Assurance assurance = assurances.get(assuranceNom);
        if (assurance == null) {
            throw new RuntimeException("Assurance non trouvée avec le nom: " + assuranceNom);
        }

        RentalContract contrat = new RentalContract(
                loueur, vehicule, dateDebut, dateFin, lieuPrise, lieuDepose, assurance
        );

        return rentalRepository.save(contrat);
    }

    /**
     * Récupérer tous les contrats
     */
    public List<RentalContract> getTousLesContrats() {
        return rentalRepository.findAll();
    }

    /**
     * Récupérer un contrat par son ID
     */
    public Optional<RentalContract> getContratById(int id) {
        return rentalRepository.findById(id);
    }

    /**
     * Signer un contrat (par le loueur)
     */
    public RentalContract signerContrat(int contratId) {
        RentalContract contrat = rentalRepository.findById(contratId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé avec l'ID: " + contratId));
        
        contrat.signerLoueur();
        return rentalRepository.save(contrat);
    }

    /**
     * Supprimer un contrat
     */
    public void supprimerContrat(int id) {
        rentalRepository.deleteById(id);
    }

    /**
     * Récupérer les contrats d'un loueur par son email
     */
    public List<RentalContract> getContratsParLoueur(String email) {
        return rentalRepository.findByLoueurEmail(email);
    }

    /**
     * Récupérer les contrats d'un véhicule
     */
    public List<RentalContract> getContratsParVehicule(String vehiculeId) {
        return rentalRepository.findByVehiculeId(vehiculeId);
    }

    // ===== MÉTHODES UTILITAIRES =====

    public List<Loueur> getTousLesLoueurs() {
        return new ArrayList<>(loueurs.values());
    }

    public List<Vehicle> getTousLesVehicules() {
        return new ArrayList<>(vehicules.values());
    }

    public List<Assurance> getToutesLesAssurances() {
        return new ArrayList<>(assurances.values());
    }
}
