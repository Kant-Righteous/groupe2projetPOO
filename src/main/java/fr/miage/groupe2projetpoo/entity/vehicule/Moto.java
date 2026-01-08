package fr.miage.groupe2projetpoo.entity.vehicule;

import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import java.time.LocalDate;
import java.util.List;

public class Moto extends Vehicle {
    public Moto(String idVehicule, String typeVehicule, String marqueVehicule, String couleurVehicule,
            String modeleVehicule, String villeVehicule, boolean estDisponible, double prixVehiculeJour,
            String proprietaire, List<LocalDate> listeDisponibilites) {
        super(idVehicule, typeVehicule, marqueVehicule, couleurVehicule, modeleVehicule, villeVehicule, estDisponible,
                prixVehiculeJour, proprietaire, listeDisponibilites);
    }
}
