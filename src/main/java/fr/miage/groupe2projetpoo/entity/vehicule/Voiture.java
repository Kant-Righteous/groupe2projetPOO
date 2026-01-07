package fr.miage.groupe2projetpoo.entity.vehicule;

public class Voiture extends Vehicle {
    public Voiture(int idVehicule, String typeVehicule, String marqueVehicule, String couleurVehicule,
            String modeleVehicule, String villeVehicule, boolean estDisponible, double prixVehiculeJour) {
        super(idVehicule, typeVehicule, marqueVehicule, couleurVehicule, modeleVehicule, villeVehicule, estDisponible,
                prixVehiculeJour);
    }
}
