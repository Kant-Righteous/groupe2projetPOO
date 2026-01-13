package fr.miage.groupe2projetpoo.entity.maintenance;

import java.time.LocalDate;

/**
 * Représente un contrôle technique passé par un véhicule.
 * US.A.8 : Renseigner les informations sur le contrôle technique.
 */
public class ControleTechnique {

    private LocalDate datePassage;
    private boolean estValide; // true = OK, false = Contre-visite requise
    private String centreControle;
    private String remarques;

    public ControleTechnique(LocalDate datePassage, boolean estValide, String centreControle, String remarques) {
        this.datePassage = datePassage;
        this.estValide = estValide;
        this.centreControle = centreControle;
        this.remarques = remarques;
    }

    public boolean estAjour() {
        // Un CT est généralement valide 2 ans pour les voitures > 4 ans
        return estValide && datePassage.plusYears(2).isAfter(LocalDate.now());
    }

    // Getters et Setters
    public LocalDate getDatePassage() {
        return datePassage;
    }

    public void setDatePassage(LocalDate datePassage) {
        this.datePassage = datePassage;
    }

    public boolean isEstValide() {
        return estValide;
    }

    public void setEstValide(boolean estValide) {
        this.estValide = estValide;
    }

    public String getCentreControle() {
        return centreControle;
    }

    public void setCentreControle(String centreControle) {
        this.centreControle = centreControle;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }
}
