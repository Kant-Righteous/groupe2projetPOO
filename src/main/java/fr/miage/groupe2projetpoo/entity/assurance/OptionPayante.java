package fr.miage.groupe2projetpoo.entity.assurance;

import java.util.Objects;

/**
 * Représente une option payante de la plateforme.
 * Ces options sont souscrites par les agents et facturées mensuellement,
 * indépendamment de leur utilisation.
 */
public class OptionPayante {

    private String nom;
    private double tarifMensuel;
    private boolean estActive;

    public OptionPayante() {
    }

    public OptionPayante(String nom, double tarifMensuel, boolean estActive) {
        this.nom = nom;
        this.tarifMensuel = tarifMensuel;
        this.estActive = estActive;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getTarifMensuel() {
        return tarifMensuel;
    }

    public void setTarifMensuel(double tarifMensuel) {
        this.tarifMensuel = tarifMensuel;
    }

    public boolean isEstActive() {
        return estActive;
    }

    public void setEstActive(boolean estActive) {
        this.estActive = estActive;
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
        return "OptionPayante{" +
                "nom='" + nom + '\'' +
                ", tarifMensuel=" + tarifMensuel +
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
