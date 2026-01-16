package fr.miage.groupe2projetpoo.entity.assurance;

import java.util.Objects;

/**
 * Classe abstraite représentant une option payante de la plateforme.
 * Ces options sont souscrites par les agents et facturées mensuellement,
 * indépendamment de leur utilisation.
 * 
 * Options disponibles :
 * - Acceptation manuelle des contrats
 * - Assurance personnalisée (agents pro)
 * - Service d'entretien
 */
public class OptionPayante {

    protected String nom;
    protected double tarifMensuel;
    protected boolean estActive;

    public OptionPayante() {
    }

    protected OptionPayante(String nom, double tarifMensuel) {
        this.nom = nom;
        this.tarifMensuel = tarifMensuel;
        this.estActive = true; // Active par défaut à la création
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public double getTarifMensuel() {
        return tarifMensuel;
    }

    public boolean isEstActive() {
        return estActive;
    }

    public void setEstActive(boolean estActive) {
        this.estActive = estActive;
    }

    /**
     * Active l'option
     */
    public void activer() {
        this.estActive = true;
    }

    /**
     * Désactive l'option
     */
    public void desactiver() {
        this.estActive = false;
    }

    /**
     * Retourne le coût mensuel de l'option.
     * L'option est facturée même si elle n'est pas utilisée.
     */
    public double calculerCoutMensuel() {
        return estActive ? tarifMensuel : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "nom='" + nom + '\'' +
                ", tarifMensuel=" + tarifMensuel + "€" +
                ", estActive=" + estActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OptionPayante))
            return false;
        OptionPayante that = (OptionPayante) o;
        return Double.compare(that.tarifMensuel, tarifMensuel) == 0 &&
                estActive == that.estActive &&
                Objects.equals(nom, that.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, tarifMensuel, estActive);
    }
}
