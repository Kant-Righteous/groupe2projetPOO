package fr.miage.groupe2projetpoo.entity.vehicule;

public class Moto extends Vehicle {
    public Moto(int idVehicule, String typeVehicule, String marqueVehicule, String couleurVehicule,
            String modeleVehicule, String villeVehicule, boolean estDisponible, double prixVehiculeJour) {
        super(idVehicule, typeVehicule, marqueVehicule, couleurVehicule, modeleVehicule, villeVehicule, estDisponible,
                prixVehiculeJour);
    }
}
