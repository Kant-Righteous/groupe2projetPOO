package fr.miage.groupe2projetpoo.entity.utilisateur;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant un agent
 */
public abstract class Agent extends Utilisateur {

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
}
