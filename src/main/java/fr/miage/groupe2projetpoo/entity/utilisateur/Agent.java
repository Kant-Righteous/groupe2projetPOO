package fr.miage.groupe2projetpoo.entity.utilisateur;

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

    // Constructeur par défaut
    public Agent() {
        super();
        this.vehicleList = new ArrayList<>();
        this.contracts = new ArrayList<>();
    }

    // Constructeur avec paramètres
    public Agent(String nom, String prenom, String password, String email, String tel) {
        super(nom, prenom, password, email, tel);
        this.vehicleList = new ArrayList<>();
        this.contracts = new ArrayList<>();
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

    public List<NoteAgent> getNotations() {
        return notations;
    }

    public void setNotations(List<NoteAgent> notations) {
        this.notations = notations;
    }

    // Méthodes pour les notations
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
