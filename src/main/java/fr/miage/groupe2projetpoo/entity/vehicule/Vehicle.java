package fr.miage.groupe2projetpoo.entity.vehicule;

public abstract class Vehicle {
    //Propriétés
    private int idVehicule;
    private String typeVehicule;
    private String marqueVehicule;
    private String couleurVehicule;
    private String modeleVehicule;
    private String villeVehicule;

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
}
