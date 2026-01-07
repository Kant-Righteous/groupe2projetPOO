package fr.miage.groupe2projetpoo.entity.assurance;

import java.util.Objects;

public class OptionPayante {

    private String nom;
    private int tarifMensuel;
    private boolean estActive;

    public OptionPayante() {
    }

    public OptionPayante(String nom, int tarifMensuel, boolean estActive) {
        this.nom = nom;
        this.tarifMensuel = tarifMensuel;
        this.estActive = estActive;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTarifMensuel() {
        return tarifMensuel;
    }

    public void setTarifMensuel(int tarifMensuel) {
        this.tarifMensuel = tarifMensuel;
    }

    public boolean isEstActive() {
        return estActive;
    }

    public void setEstActive(boolean estActive) {
        this.estActive = estActive;
    }

    @Override
    public String toString() {
        return "OptionPayante{" +
                "nom='" + nom + '\'' +
                ", tarifMensuel=" + tarifMensuel +
                ", estActive=" + estActive +
                '}';
    }

}
