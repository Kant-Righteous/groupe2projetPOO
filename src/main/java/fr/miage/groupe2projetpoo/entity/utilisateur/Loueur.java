package fr.miage.groupe2projetpoo.entity.utilisateur;

import fr.miage.groupe2projetpoo.entity.notation.NoteLoueur;
import java.util.ArrayList;
import java.util.List;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Loueur - propriétaire qui met en location ses véhicules
 */
public class Loueur extends Utilisateur {

    private String iban;
    private String nomSociete;
    private List<NoteLoueur> notations = new ArrayList<>();

    // Liste des véhicules possédés par le loueur (relation un-à-plusieurs)
    private List<Vehicle> vehicles;

    // Liste des contrats de location du loueur (relation un-à-plusieurs)
    @JsonIgnore
    private List<RentalContract> contracts;

    // === Parrainage (US.L.9) ===

    // Référence au parrain (qui m'a parrainé)
    @JsonIgnore
    private Loueur parrain;

    // Liste des filleuls (ceux que j'ai parrainés)
    @JsonIgnore
    private List<Loueur> filleuls = new ArrayList<>();

    // Solde de parrainage (utilisable pour louer des véhicules)
    private double soldeParrainage = 0.0;

    // Indique si le loueur a complété au moins une location (pour déclencher la
    // récompense)
    private boolean aCompletePremiereLocation = false;

    // Constructeur par défaut
    public Loueur() {
        super();
        this.vehicles = new ArrayList<>();
        this.contracts = new ArrayList<>();
    }

    // Constructeur avec paramètres de base
    public Loueur(String nom, String prenom, String password, String email, String tel) {
        super(nom, prenom, password, email, tel);
        this.vehicles = new ArrayList<>();
        this.contracts = new ArrayList<>();
    }

    // Constructeur complet
    public Loueur(String nom, String prenom, String password, String email, String tel,
            String iban, String nomSociete) {
        super(nom, prenom, password, email, tel);
        this.iban = iban;
        this.nomSociete = nomSociete;
    }

    @Override
    public Role getRole() {
        return Role.LOUEUR;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public List<NoteLoueur> getNotations() {
        return notations;
    }

    public void setNotations(List<NoteLoueur> notations) {
        this.notations = notations;
    }

    // Méthodes pour les notations
    public void ajouterNotation(NoteLoueur notation) {
        this.notations.add(notation);
    }

    public double calculerNoteMoyenne() {
        if (notations.isEmpty()) {
            return 0.0;
        }
        double somme = 0.0;
        for (NoteLoueur note : notations) {
            somme += note.calculerNoteGlobale();
        }
        return somme / notations.size();
    }

    // === Gestion des véhicules ===

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        if (this.vehicles == null) {
            this.vehicles = new ArrayList<>();
        }
        this.vehicles.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        if (this.vehicles != null) {
            this.vehicles.remove(vehicle);
        }
    }

    // === Gestion des contrats ===

    public List<RentalContract> getContracts() {
        return contracts;
    }

    public void setContracts(List<RentalContract> contracts) {
        this.contracts = contracts;
    }

    public void addContract(RentalContract contract) {
        if (this.contracts == null) {
            this.contracts = new ArrayList<>();
        }
        this.contracts.add(contract);
    }

    public void removeContract(RentalContract contract) {
        if (this.contracts != null) {
            this.contracts.remove(contract);
        }
    }

    // === Gestion du parrainage (US.L.9) ===

    public Loueur getParrain() {
        return parrain;
    }

    public void setParrain(Loueur parrain) {
        this.parrain = parrain;
    }

    public List<Loueur> getFilleuls() {
        if (this.filleuls == null) {
            this.filleuls = new ArrayList<>();
        }
        return filleuls;
    }

    public void setFilleuls(List<Loueur> filleuls) {
        this.filleuls = filleuls;
    }

    public void addFilleul(Loueur filleul) {
        if (this.filleuls == null) {
            this.filleuls = new ArrayList<>();
        }
        this.filleuls.add(filleul);
    }

    public double getSoldeParrainage() {
        return soldeParrainage;
    }

    public void setSoldeParrainage(double soldeParrainage) {
        this.soldeParrainage = soldeParrainage;
    }

    /**
     * Ajouter une récompense au solde de parrainage
     * 
     * @param montant Montant à ajouter
     */
    public void ajouterRecompenseParrainage(double montant) {
        this.soldeParrainage += montant;
    }

    /**
     * Utiliser le solde de parrainage pour une location
     * 
     * @param montant Montant à utiliser
     * @return true si le montant a pu être utilisé, false sinon
     */
    public boolean utiliserSoldeParrainage(double montant) {
        if (montant <= this.soldeParrainage) {
            this.soldeParrainage -= montant;
            return true;
        }
        return false;
    }

    public boolean isACompletePremiereLocation() {
        return aCompletePremiereLocation;
    }

    public void setACompletePremiereLocation(boolean aCompletePremiereLocation) {
        this.aCompletePremiereLocation = aCompletePremiereLocation;
    }

    /**
     * Obtenir l'email du parrain (pour éviter la sérialisation récursive)
     */
    public String getParrainEmail() {
        return parrain != null ? parrain.getEmail() : null;
    }

    /**
     * Obtenir le nombre de filleuls
     */
    public int getNombreFilleuls() {
        return filleuls != null ? filleuls.size() : 0;
    }
}
