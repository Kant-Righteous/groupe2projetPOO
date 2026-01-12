package fr.miage.groupe2projetpoo.entity.vehicule;

import java.time.LocalDate;
import java.util.List;

public class Voiture extends Vehicle {
    public Voiture(String idVehicule, String typeVehicule, String marqueVehicule, String couleurVehicule,
            String modeleVehicule, String villeVehicule, double prixVehiculeParJour,
            String proprietaire) {
        super(idVehicule, typeVehicule, marqueVehicule, couleurVehicule, modeleVehicule, villeVehicule,
                prixVehiculeParJour, proprietaire);
    }

    @Override
    public TypeVehicule getType() {
        return TypeVehicule.VOITURE;
    }
}
