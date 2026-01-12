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
    private List<LocalDate> listeDisponibilites;

    //Constructeur
    public VehicleService(VehicleRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
    }

    //Methodes
    /**
     * ajout d'un vehicule
     */
    public Vehicle addVehicule(String idVehicule, String typeVehicule, String marqueVehicule,
                            String couleurVehicule, String modeleVehicule, String villeVehicules, boolean estDisponible, double prixVehiculeParJour, String proprietaire) {
        if (vehiculeRepository.existsById(idVehicule)) {
            return null;
        }
        Vehicle vehicule;
        vehicule = new Voiture(idVehicule, typeVehicule, marqueVehicule, couleurVehicule,modeleVehicule,villeVehicules, estDisponible,prixVehiculeParJour,proprietaire,null);
        return vehiculeRepository.save(vehicule);
    }

    /**
     * ajout de la liste des disponibilités
     */
    public List<LocalDate> getListeDisponibilites() {
        return listeDisponibilites;
    }
    public void setListeDisponibilites(List<LocalDate> listeDisponibilites) {
        this.listeDisponibilites = listeDisponibilites;
    }

    /**
     * suppression d'un vehicule
     */
    public boolean suppVehicule(String id) {
        vehiculeRepository.getVehicules().remove(id);
        return true;
    }
}
