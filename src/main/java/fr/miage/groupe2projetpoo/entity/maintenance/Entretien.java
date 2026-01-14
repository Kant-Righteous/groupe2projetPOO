package fr.miage.groupe2projetpoo.entity.maintenance;

import java.time.LocalDate;

/**
 * Représente une opération d'entretien sur un véhicule.
 * US.A.10 : Spécifier les derniers entretiens techniques.
 */
public class Entretien {

    private String typeOperation; // ex: "Vidange", "Courroie", "Pneus"
    private LocalDate dateRealisation;
    private int kilometrageAuMoment;
    private double cout;
    private String garagePrestataire;

    public Entretien(String typeOperation, LocalDate dateRealisation, int kilometrageAuMoment, double cout,
            String garagePrestataire) {
        this.typeOperation = typeOperation;
        this.dateRealisation = dateRealisation;
        this.kilometrageAuMoment = kilometrageAuMoment;
        this.cout = cout;
        this.garagePrestataire = garagePrestataire;
    }

    // Getters et Setters
    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public LocalDate getDateRealisation() {
        return dateRealisation;
    }

    public void setDateRealisation(LocalDate dateRealisation) {
        this.dateRealisation = dateRealisation;
    }

    public int getKilometrageAuMoment() {
        return kilometrageAuMoment;
    }

    public void setKilometrageAuMoment(int kilometrageAuMoment) {
        this.kilometrageAuMoment = kilometrageAuMoment;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public String getGaragePrestataire() {
        return garagePrestataire;
    }

    public void setGaragePrestataire(String garagePrestataire) {
        this.garagePrestataire = garagePrestataire;
    }
}
