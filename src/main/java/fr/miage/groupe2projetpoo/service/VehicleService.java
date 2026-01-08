package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import fr.miage.groupe2projetpoo.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VehicleService {
    /*calculer le prix total d’une location

    vérifier la disponibilité

    appliquer des promotions ou commissions*/

    // Propriétés
    private final VehicleRepository vehiculeRepository;

    //Constructeur
    public VehicleService(VehicleRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
    }

    //Methodes
    /**
     * ajout d'un vehicule
     */
    public Vehicle addVehicule(String idVehicule, String typeVehicule, String marqueVehicule,
                            String couleurVehicule, String modeleVehicule, String villeVehicules, boolean estDisponible, double prixVehiculeParJour) {
        if (vehiculeRepository.existsById(idVehicule)) {
            return null;
        }

        Vehicle vehicule;
        vehicule = new Voiture(idVehicule, typeVehicule, marqueVehicule, couleurVehicule,modeleVehicule,villeVehicules, estDisponible,60,null,null);


        return vehiculeRepository.save(vehicule);
    }
}
