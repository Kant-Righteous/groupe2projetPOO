package fr.miage.groupe2projetpoo.entity.utilisateur;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Loueur - propriétaire qui met en location ses véhicules
 */
public class Loueur extends Utilisateur {

    private String iban;
    private String nomSociete;

    // Liste des véhicules possédés par le loueur (relation un-à-plusieurs)
    private List<Vehicle> vehicles;

    // Liste des contrats de location du loueur (relation un-à-plusieurs)
    private List<RentalContract> contracts;

    // Constructeur par défaut
    public Loueur() {
        super();
        this.vehicles = new ArrayList<>();
        this.contracts = new ArrayList<>();
    }

    // Constructeur avec paramètres de base
    public Loueur(String email, String password, String nom, String prenom) {
        super(email, password, nom, prenom);
        this.vehicles = new ArrayList<>();
        this.contracts = new ArrayList<>();
    }

    // Constructeur complet
    public Loueur(String email, String password, String nom, String prenom,
            String iban, String nomSociete) {
        super(email, password, nom, prenom);
        this.iban = iban;
        this.nomSociete = nomSociete;
        this.vehicles = new ArrayList<>();
        this.contracts = new ArrayList<>();
    }

    @Override
    public Role getRole() {
        return Role.LOUEUR;
    }

    // === Getters et Setters pour iban et nomSociete ===

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
}
