package fr.miage.groupe2projetpoo.entity.entretien;

import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.MaintenanceCompany;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;

import java.time.LocalDate;

public class MaintenanceIntervention {
    private String id;
    private Agent agent;
    private Vehicle vehicule;
    private MaintenanceCompany entreprise;
    private LocalDate dateIntervention;
    private double prixPaye;
    private MaintenanceStatus statut;

    public MaintenanceIntervention() {}

    public MaintenanceIntervention(String id, Agent agent, Vehicle vehicule, MaintenanceCompany entreprise, LocalDate dateIntervention, double prixPaye, MaintenanceStatus statut) {
        this.id = id;
        this.agent = agent;
        this.vehicule = vehicule;
        this.entreprise = entreprise;
        this.dateIntervention = dateIntervention;
        this.prixPaye = prixPaye;
        this.statut = statut;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Vehicle getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicle vehicule) {
        this.vehicule = vehicule;
    }

    public MaintenanceCompany getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(MaintenanceCompany entreprise) {
        this.entreprise = entreprise;
    }

    public LocalDate getDateIntervention() {
        return dateIntervention;
    }

    public void setDateIntervention(LocalDate dateIntervention) {
        this.dateIntervention = dateIntervention;
    }

    public double getPrixPaye() {
        return prixPaye;
    }

    public void setPrixPaye(double prixPaye) {
        this.prixPaye = prixPaye;
    }

    public MaintenanceStatus getStatut() {
        return statut;
    }

    public void setStatut(MaintenanceStatus statut) {
        this.statut = statut;
    }
}
