package fr.miage.groupe2projetpoo.entity.vehicule;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.notation.NoteVehicule;

import java.util.*;

import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import org.springframework.cglib.core.Local;

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
    private List<NoteVehicule> notations = new ArrayList<>();
    //private boolean estDisponible;
    private double prixVehiculeParJour;
    private String Proprietaire;
    private Map<LocalDate,Boolean> disponibilites = new HashMap<>();
    private List<RentalContract> historiqueContrats = new ArrayList<>();

    // Constructeur
    public Vehicle(String idVehicule, String typeVehicule, String marqueVehicule,
            String couleurVehicule, String modeleVehicule, String villeVehicule,
            double prixVehiculeJour, String proprietaire) {
        this.idVehicule = idVehicule;
        this.typeVehicule = typeVehicule;
        this.marqueVehicule = marqueVehicule;
        this.couleurVehicule = couleurVehicule;
        this.modeleVehicule = modeleVehicule;
        this.villeVehicule = villeVehicule;
        //this.estDisponible = estDisponible;
        this.prixVehiculeParJour = prixVehiculeJour;
        this.Proprietaire = proprietaire;
        initiliserDisponibilites();
    }

    private void initiliserDisponibilites(){
        LocalDate dateDeb = LocalDate.now().withDayOfYear(1);
        LocalDate dateFin = dateDeb.plusYears(1);

        while(!dateDeb.isAfter(dateFin)){
            disponibilites.put(dateDeb,true);
            dateDeb = dateDeb.plusDays(1);
        }
    }

    // Getters

    public abstract TypeVehicule getType();
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

    public List<NoteVehicule> getNotations() {
        return notations;
    }

    /*public boolean getEstDisponible() {
        return estDisponible;
    }*/

    public double getPrixVehiculeParJour() {
        return prixVehiculeParJour;
    }

    public String getProprietaire() {
        return Proprietaire;
    }

    public Map<LocalDate, Boolean> getDisponibilites() {
        return disponibilites;
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

   /* public void setEstDisponible(boolean estDisponible) {
        this.estDisponible = estDisponible;
    }*/

    public void setPrixVehiculeParJour(double prixVehiculeJour) {
        this.prixVehiculeParJour = prixVehiculeJour;
    }

    public void setProprietaire(String proprietaire) {
        this.Proprietaire = proprietaire;
    }


    public boolean estDisponible(LocalDate debut, LocalDate fin) {
        LocalDate d = debut;
        while (!d.isAfter(fin)) {
            Boolean dispo = disponibilites.get(d);
            if (dispo == null || !dispo) {
                return false;
            }
            d = d.plusDays(1);
        }
        return true;
    }
    public void setNotations(List<NoteVehicule> notations) {
        this.notations = notations;
    }

    // Méthodes pour les notations
    public void ajouterNotation(NoteVehicule notation) {
        this.notations.add(notation);
    }

    public double calculerNoteMoyenne() {
        if (notations.isEmpty()) {
            return 0.0;
        }
        double somme = 0.0;
        for (NoteVehicule note : notations) {
            somme += note.calculerNoteGlobale();
        }
        return somme / notations.size();
    }

    // === Gestion de l'historique des contrats ===
    public List<RentalContract> getHistoriqueContrats() {
        return historiqueContrats;
    }

    public void ajouterContrat(RentalContract contrat) {
        this.historiqueContrats.add(contrat);
    }
}
