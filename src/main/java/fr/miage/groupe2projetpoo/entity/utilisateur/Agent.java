package fr.miage.groupe2projetpoo.entity.utilisateur;

import fr.miage.groupe2projetpoo.entity.assurance.OptionAcceptationManuelle;
import fr.miage.groupe2projetpoo.entity.assurance.OptionPayante;
import fr.miage.groupe2projetpoo.entity.notation.NoteAgent;
import java.util.ArrayList;
import java.util.List;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;

/**
 * Classe abstraite représentant un agent
 */
public abstract class Agent extends Utilisateur {

    private List<NoteAgent> notations = new ArrayList<>();

    // Liste des véhicules gérés par l'agent (relation un-à-plusieurs)
    private List<Vehicle> vehicleList;

    // Liste des contrats de location gérés par l'agent (relation un-à-plusieurs)
    private List<RentalContract> contracts;

    // Liste des options payantes souscrites par l'agent
    private List<OptionPayante> optionsPayantes = new ArrayList<>();

    // Constructeur par défaut
    public Agent() {
        super();
        this.vehicleList = new ArrayList<>();
        this.contracts = new ArrayList<>();
        this.optionsPayantes = new ArrayList<>();
    }

    // Constructeur avec paramètres
    public Agent(String nom, String prenom, String password, String email, String tel) {
        super(nom, prenom, password, email, tel);
        this.vehicleList = new ArrayList<>();
        this.contracts = new ArrayList<>();
        this.optionsPayantes = new ArrayList<>();
    }

    // === Gestion des véhicules ===

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public void addVehicle(Vehicle vehicle) {
        if (this.vehicleList == null) {
            this.vehicleList = new ArrayList<>();
        }
        this.vehicleList.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        if (this.vehicleList != null) {
            this.vehicleList.remove(vehicle);
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

    // === Gestion des options payantes ===

    public List<OptionPayante> getOptionsPayantes() {
        return optionsPayantes;
    }

    public void setOptionsPayantes(List<OptionPayante> optionsPayantes) {
        this.optionsPayantes = optionsPayantes;
    }

    /**
     * Ajoute une option payante à l'agent
     */
    public void ajouterOption(OptionPayante option) {
        this.optionsPayantes.add(option);
    }

    /**
     * Retire une option payante de l'agent
     */
    public void retirerOption(OptionPayante option) {
        this.optionsPayantes.remove(option);
    }

    /**
     * Vérifie si l'agent a une option spécifique active
     * @param typeOption La classe de l'option à vérifier
     */
    public boolean aOptionActive(Class<? extends OptionPayante> typeOption) {
        for (OptionPayante opt : optionsPayantes) {
            if (typeOption.isInstance(opt) && opt.isEstActive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupère une option spécifique si elle existe
     */
    @SuppressWarnings("unchecked")
    public <T extends OptionPayante> T getOption(Class<T> typeOption) {
        for (OptionPayante opt : optionsPayantes) {
            if (typeOption.isInstance(opt)) {
                return (T) opt;
            }
        }
        return null;
    }

    /**
     * Vérifie si l'agent a l'option d'acceptation manuelle active
     */
    public boolean aAcceptationManuelle() {
        return aOptionActive(OptionAcceptationManuelle.class);
    }

    /**
     * Calcule le total de la facture mensuelle pour toutes les options actives
     */
    public double calculerFactureMensuelle() {
        double total = 0;
        for (OptionPayante option : optionsPayantes) {
            total += option.calculerCoutMensuel();
        }
        return total;
    }

    // === Gestion des notations ===

    public List<NoteAgent> getNotations() {
        return notations;
    }

    public void setNotations(List<NoteAgent> notations) {
        this.notations = notations;
    }

    public void ajouterNotation(NoteAgent notation) {
        this.notations.add(notation);
    }

    public double calculerNoteMoyenne() {
        if (notations.isEmpty()) {
            return 0.0;
        }
        double somme = 0.0;
        for (NoteAgent note : notations) {
            somme += note.calculerNoteGlobale();
        }
        return somme / notations.size();
    }
}

