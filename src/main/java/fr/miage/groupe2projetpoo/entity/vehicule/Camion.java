package fr.miage.groupe2projetpoo.entity.vehicule;


import java.time.LocalDate;
import java.util.List;

public class Camion extends Vehicle {
    public Camion(String idVehicule, String typeVehicule, String marqueVehicule, String couleurVehicule,
            String modeleVehicule, String villeVehicule, boolean estDisponible, double prixVehiculeJour,
            String proprietaire, List<LocalDate> listeDisponibilites) {
        super(idVehicule, typeVehicule, marqueVehicule, couleurVehicule, modeleVehicule, villeVehicule, estDisponible,
                prixVehiculeJour, proprietaire);
    }
}
