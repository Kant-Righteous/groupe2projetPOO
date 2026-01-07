package fr.miage.groupe2projetpoo.entity.assurance;

import java.util.Objects;

public class Assurance {

    private int idA;
    private String nom;
    private String option;
    private float tarif;

    public Assurance() {
    }

    // Constructeur
    public Assurance(int idA, String nom, String option, float tarif) {
        this.idA = idA;
        this.nom = nom;
        this.option = option;
        this.tarif = tarif;
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

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public float getTarif() {
        return tarif;
    }

    public void setTarif(float tarif) {
        this.tarif = tarif;
    }

    @Override
    public String toString() {
        return "Assurance{" +
                "idA=" + idA +
                ", nom='" + nom + '\'' +
                ", option='" + option + '\'' +
                ", tarif=" + tarif +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Assurance assurance = (Assurance) o;
        return idA == assurance.idA && Float.compare(assurance.tarif, tarif) == 0 && Objects.equals(nom, assurance.nom)
                && Objects.equals(option, assurance.option);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idA, nom, option, tarif);
    }
}
