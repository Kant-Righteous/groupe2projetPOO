package fr.miage.groupe2projetpoo.entity.entretien;

import fr.miage.groupe2projetpoo.entity.vehicule.TypeVehicule;

public class MaintenancePrice {
    private TypeVehicule typeVehicule;
    private String modele; // Optionnel
    private double prix;

    public MaintenancePrice() {}

    public MaintenancePrice(TypeVehicule typeVehicule, String modele, double prix) {
        this.typeVehicule = typeVehicule;
        this.modele = modele;
        this.prix = prix;
    }

    public TypeVehicule getTypeVehicule() {
        return typeVehicule;
    }

    public void setTypeVehicule(TypeVehicule typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
