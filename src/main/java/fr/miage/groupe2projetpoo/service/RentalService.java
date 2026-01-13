package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.repository.RentalRepository;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import fr.miage.groupe2projetpoo.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository, UserRepository userRepository,
            VehicleRepository vehicleRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // ===== MÉTHODES CRUD =====

    /**
     * Créer un nouveau contrat de location
     * 
     * @param loueurEmail  Email du loueur
     * @param vehiculeId   ID du véhicule
     * @param dateDebut    Date de début
     * @param dateFin      Date de fin
     * @param lieuPrise    Lieu de prise
     * @param lieuDepose   Lieu de dépôt
     * @param assuranceNom Nom de l'assurance
     */
    public RentalContract creerContrat(String loueurEmail, String vehiculeId,
            Date dateDebut, Date dateFin,
            String lieuPrise, String lieuDepose,
            String assuranceNom) {

        Utilisateur user = userRepository.findByEmail(loueurEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + loueurEmail));

        if (!(user instanceof Loueur)) {
            throw new RuntimeException("L'utilisateur n'est pas un loueur: " + loueurEmail);
        }
        Loueur loueur = (Loueur) user;

        Vehicle vehicule = vehicleRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec l'ID: " + vehiculeId));

        Assurance assurance = getAssuranceByNom(assuranceNom);
        if (assurance == null) {
            throw new RuntimeException("Assurance non trouvée avec le nom: " + assuranceNom);
        }

        RentalContract contrat = new RentalContract(
                loueur, vehicule, dateDebut, dateFin, lieuPrise, lieuDepose, assurance);

        // Find the owner (Agent) of the vehicle
        String proprietaireEmail = vehicule.getProprietaire();
        Utilisateur proprietaire = userRepository.findByEmail(proprietaireEmail)
                .orElse(null);

        if (proprietaire instanceof Agent) {
            contrat.setAgent((Agent) proprietaire);
            // Also link the contract to the agent
            ((Agent) proprietaire).addContract(contrat);
        }

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
        return userRepository.getAllLoueurs();
    }

    public List<Vehicle> getTousLesVehicules() {
        return (List<Vehicle>) vehicleRepository.findAll();
    }

    public List<Assurance> getToutesLesAssurances() {
        return userRepository.getAllAssurances();
    }

    private Assurance getAssuranceByNom(String nom) {
        return userRepository.getAllAssurances().stream()
                .filter(a -> a.getNom().equals(nom))
                .findFirst()
                .orElse(null);
    }

    // ===== MÉTHODES POUR L'ACCEPTATION MANUELLE =====

    /**
     * L'agent accepte manuellement un contrat
     */
    public RentalContract accepterContratParAgent(int contratId) {
        RentalContract contrat = rentalRepository.findById(contratId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé avec l'ID: " + contratId));

        contrat.signerAgent();
        return rentalRepository.save(contrat);
    }

    /**
     * Récupérer les contrats en attente d'acceptation par l'agent
     */
    public List<RentalContract> getContratsEnAttente() {
        return rentalRepository.findAll().stream()
                .filter(c -> c.estEnAttenteAgent())
                .toList();
    }
}
