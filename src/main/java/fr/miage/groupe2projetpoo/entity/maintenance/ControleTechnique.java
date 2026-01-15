package fr.miage.groupe2projetpoo.entity.maintenance;

import java.time.LocalDate;

/**
 * Représente un contrôle technique passé par un véhicule.
 * US.A.8 : Renseigner les informations sur le contrôle technique.
 */
public class ControleTechnique {

    private LocalDate datePassage;
    private LocalDate dateExpiration; // Date d'expiration du CT (généralement 2 ans après passage)
    private boolean estValide; // true = OK, false = Contre-visite requise
    private String centreControle;
    private String remarques;
    private String resultat; // "Favorable", "Défavorable", "Contre-visite"
    private String observations; // Observations du contrôleur

    // Constructeur complet
    public ControleTechnique(LocalDate datePassage, boolean estValide, String centreControle, String remarques) {
        this.datePassage = datePassage;
        this.estValide = estValide;
        this.centreControle = centreControle;
        this.remarques = remarques;
        // Par défaut, expiration = 2 ans après passage
        this.dateExpiration = datePassage.plusYears(2);
        this.resultat = estValide ? "Favorable" : "Défavorable";
    }

    // Constructeur simplifié
    public ControleTechnique() {
        this.estValide = true;
    }

    /**
     * Vérifie si le contrôle technique est expiré
     * @return true si la date d'expiration est dépassée
     */
    public boolean estExpire() {
        if (dateExpiration == null) {
            return true; // Pas de date d'expiration = expiré
        }
        return LocalDate.now().isAfter(dateExpiration);
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

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
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

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
