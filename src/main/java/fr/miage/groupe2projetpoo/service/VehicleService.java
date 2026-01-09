package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import fr.miage.groupe2projetpoo.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    // Propriétés
    private final VehicleRepository vehiculeRepository;
    private final UserRepository userRepository;
    private List<LocalDate> listeDisponibilites;

    //Constructeur
    public VehicleService(VehicleRepository vehiculeRepository, UserRepository userRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.userRepository = userRepository;
    }

    //Methodes
    /**
     * ajout d'un vehicule
     */
    public Vehicle addVehicule(String idVehicule, String typeVehicule, String marqueVehicule,
                            String couleurVehicule, String modeleVehicule, String villeVehicules, boolean estDisponible, double prixVehiculeParJour, String proprietaire) {

        // Existance de vehicule
        if (vehiculeRepository.existsById(idVehicule)) {
            return null;
        }
        Optional<Utilisateur> userOpt = userRepository.findByEmail(proprietaire);

        if (userOpt.isEmpty()) {
            return null;
        }
        // Creation d'une instance de vehicule
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
