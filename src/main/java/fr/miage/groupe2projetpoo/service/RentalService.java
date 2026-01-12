package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.repository.InMemoryRentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RentalService {

    private final InMemoryRentalRepository rentalRepository;

    @Autowired
    public RentalService(InMemoryRentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
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

        Loueur loueur = rentalRepository.getLoueurByEmail(loueurEmail);
        if (loueur == null) {
            throw new RuntimeException("Loueur non trouvé avec l'email: " + loueurEmail);
        }

        Vehicle vehicule = rentalRepository.getVehiculeById(vehiculeId);
        if (vehicule == null) {
            throw new RuntimeException("Véhicule non trouvé avec l'ID: " + vehiculeId);
        }

        Assurance assurance = rentalRepository.getAssuranceByNom(assuranceNom);
        if (assurance == null) {
            throw new RuntimeException("Assurance non trouvée avec le nom: " + assuranceNom);
        }

        RentalContract contrat = new RentalContract(
                loueur, vehicule, dateDebut, dateFin, lieuPrise, lieuDepose, assurance);

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
        return rentalRepository.getAllLoueurs();
    }

    public List<Vehicle> getTousLesVehicules() {
        return rentalRepository.getAllVehicules();
    }

    public List<Assurance> getToutesLesAssurances() {
        return rentalRepository.getAllAssurances();
    }
}
