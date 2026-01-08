package fr.miage.groupe2projetpoo.entity.vehicule;

import fr.miage.groupe2projetpoo.entity.notation.NoteVehicule;
import java.util.ArrayList;
import java.util.List;

public abstract class Vehicle {
    //Propriétés
    private int idVehicule;
    private String typeVehicule;
    private String marqueVehicule;
    private String couleurVehicule;
    private String modeleVehicule;
    private String villeVehicule;
    private List<NoteVehicule> notations = new ArrayList<>();
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
    public List<NoteVehicule> getNotations() {
        return notations;
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
}
