package fr.miage.groupe2projetpoo.entity.vehicule;

import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import java.time.LocalDate;
import java.util.List;

public abstract class Vehicle {
    // Propriétés
    private String idVehicule;
    private String typeVehicule;
    private String marqueVehicule;
    private String couleurVehicule;
    private String modeleVehicule;
    private String villeVehicule;
    private boolean estDisponible;
    private double prixVehiculeParJour;
    private String Proprietaire;
    private List<LocalDate> listeDisponibilites;


    // Constructeur
    public Vehicle(String idVehicule, String typeVehicule, String marqueVehicule,
            String couleurVehicule, String modeleVehicule, String villeVehicule, boolean estDisponible,
            double prixVehiculeJour, String proprietaire) {
        this.idVehicule = idVehicule;
        this.typeVehicule = typeVehicule;
        this.marqueVehicule = marqueVehicule;
        this.couleurVehicule = couleurVehicule;
        this.modeleVehicule = modeleVehicule;
        this.villeVehicule = villeVehicule;
        this.estDisponible = estDisponible;
        this.prixVehiculeParJour = prixVehiculeJour;
        this.Proprietaire = proprietaire;
    }

    // Getters
    public String getIdVehicule() {
        return idVehicule;
    }

    public String getTypeVehicule() {
        return typeVehicule;
    }

    public String getMarqueVehicule() {
        return marqueVehicule;
    }

    public String getCouleurVehicule() {
        return couleurVehicule;
    }

    public String getModeleVehicule() {
        return modeleVehicule;
    }

    public String getVilleVehicule() {
        return villeVehicule;
    }

    public boolean getEstDisponible() {
        return estDisponible;
    }

    public double getPrixVehiculeParJour() {
        return prixVehiculeParJour;
    }

    public String getProprietaire() {
        return Proprietaire;
    }

    public List<LocalDate> getListeDisponibilites() {
        return listeDisponibilites;
    }

    // Setters
    public void setIdVehicule(String idV) {
        this.idVehicule = idV;
    }

    public void setTypeVehicule(String type) {
        this.typeVehicule = type;
    }

    public void setMarqueVehicule(String marque) {
        this.marqueVehicule = marque;
    }

    public void setCouleurVehicule(String couleur) {
        this.couleurVehicule = couleur;
    }

    public void setModeleVehicule(String modele) {
        this.modeleVehicule = modele;
    }

    public void setVilleVehicule(String ville) {
        this.villeVehicule = ville;
    }

    public void setEstDisponible(boolean estDisponible) {
        this.estDisponible = estDisponible;
    }

    public void setPrixVehiculeParJour(double prixVehiculeJour) {
        this.prixVehiculeParJour = prixVehiculeJour;
    }

    public void setProprietaire(String proprietaire) {
        this.Proprietaire = proprietaire;
    }


    public boolean estDisponible(LocalDate debut, LocalDate fin) {
        LocalDate d = debut;
        boolean test = true;
        while (!d.isAfter(fin)) {
            if (listeDisponibilites.contains(d)) {
                d = d.plusDays(1);
            } else {
                test = false;
            }
        }
        return test == true;
    }

}
