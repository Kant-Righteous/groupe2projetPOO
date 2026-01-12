package fr.miage.groupe2projetpoo.entity.vehicule;

import java.time.LocalDate;
import java.util.List;

public class Voiture extends Vehicle {
    public Voiture(String idVehicule, String marqueVehicule, String couleurVehicule,
            String modeleVehicule, String villeVehicule, double prixVehiculeParJour,
            String proprietaire, Boolean estEnpause) {
        super(idVehicule, marqueVehicule, couleurVehicule, modeleVehicule, villeVehicule,
                prixVehiculeParJour, proprietaire, estEnpause);
    }

    @Override
    public TypeVehicule getType() {
        return TypeVehicule.VOITURE;
    }
}
