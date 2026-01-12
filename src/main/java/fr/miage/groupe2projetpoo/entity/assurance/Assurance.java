package fr.miage.groupe2projetpoo.entity.assurance;

import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import java.util.Objects;

import static fr.miage.groupe2projetpoo.entity.vehicule.TypeVehicule.*;

/**
 * Représente une assurance proposée pour un véhicule lors d'une location.
 * L'assurance par défaut est AZA, mais un agent professionnel peut proposer
 * sa propre assurance via une option payante.
 */
public class Assurance {

    private int idA;
    private String nom;
    private double tarifBase;

    public Assurance() {
    }

    public Assurance(int idA, String nom, double tarifBase) {
        this.idA = idA;
        this.nom = nom;
        this.tarifBase = tarifBase;
    }

    // Getters et Setters
    public int getIdA() {
        return idA;
    }

    public void setIdA(int idA) {
        this.idA = idA;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getTarifBase() {
        return tarifBase;
    }

    public void setTarifBase(double tarifBase) {
        this.tarifBase = tarifBase;
    }

    /**
     * Calcule la prime d'assurance en fonction du véhicule.
     * Les options payantes de la plateforme ne sont PAS prises en compte ici.
     */
    public double calculerPrime(Vehicle vehicule) {
        double prime = tarifBase;

        if (vehicule != null) {
            switch (vehicule.getType()) {
                case SUV:
                    prime += 20;
                    break;
                case UTILITAIRE:
                    prime += 30;
                    break;
                case CITADINE:
                    prime += 10;
                    break;
                default:
                    break;
            }
        }

        return prime;
    }

    @Override
    public String toString() {
        return "Assurance{" +
                "idA=" + idA +
                ", nom='" + nom + '\'' +
                ", tarifBase=" + tarifBase +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Assurance))
            return false;
        Assurance assurance = (Assurance) o;
        return idA == assurance.idA &&
                Double.compare(assurance.tarifBase, tarifBase) == 0 &&
                Objects.equals(nom, assurance.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idA, nom, tarifBase);
    }
}
