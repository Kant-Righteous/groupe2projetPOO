package fr.miage.groupe2projetpoo.entity.vehicule;

import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import java.time.LocalDate;
import java.util.List;

public abstract class Vehicle {
    // Propriétés
    private int idVehicule;
    private String typeVehicule;
    private String marqueVehicule;
    private String couleurVehicule;
    private String modeleVehicule;
    private String villeVehicule;
    private boolean estDisponible;
    private double prixVehiculeJour;
    private Agent Proprietaire;
    private List<LocalDate> listeDisponibilites;

    // Constructeur
    public Vehicle(int idVehicule, String typeVehicule, String marqueVehicule,
            String couleurVehicule, String modeleVehicule, String villeVehicule, boolean estDisponible,
            double prixVehiculeJour, Agent proprietaire, List<LocalDate> listeDisponibilites) {
        this.idVehicule = idVehicule;
        this.typeVehicule = typeVehicule;
        this.marqueVehicule = marqueVehicule;
        this.couleurVehicule = couleurVehicule;
        this.modeleVehicule = modeleVehicule;
        this.villeVehicule = villeVehicule;
        this.estDisponible = estDisponible;
        this.prixVehiculeJour = prixVehiculeJour;
        this.Proprietaire = proprietaire;
        this.listeDisponibilites = listeDisponibilites;
    }

    // Getters
    public int getIdVehicule() {
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

    public double getPrixVehiculeJour() {
        return prixVehiculeJour;
    }

    public Agent getProprietaire() {
        return Proprietaire;
    }

    public List<LocalDate> getListeDisponibilites() {
        return listeDisponibilites;
    }

    // Setters
    public void setIdVehicule(int idV) {
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

    public void setPrixVehiculeJour(double prixVehiculeJour) {
        this.prixVehiculeJour = prixVehiculeJour;
    }

    public void setProprietaire(Agent proprietaire) {
        this.Proprietaire = proprietaire;
    }

    public void setListeDisponibilites(List<LocalDate> listeDisponibilites) {
        this.listeDisponibilites = listeDisponibilites;
    }
}
