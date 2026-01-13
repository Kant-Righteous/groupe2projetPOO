package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.entity.vehicule.TypeVehicule;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;
import fr.miage.groupe2projetpoo.entity.vehicule.Moto;
import fr.miage.groupe2projetpoo.entity.vehicule.Camion;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import fr.miage.groupe2projetpoo.repository.VehicleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.security.Key;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VehicleService {

    // Propriétés
    private final VehicleRepository vehiculeRepository;
    private final UserRepository userRepository;
    private List<LocalDate> listeDisponibilites;

    // Constructeur
    public VehicleService(VehicleRepository vehiculeRepository, UserRepository userRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.userRepository = userRepository;
    }

    // Methodes
    /**
     * ajout d'un vehicule
     */
    public Vehicle addVehicule(String idVehicule, TypeVehicule typeVehicule, String marqueVehicule,
                               String couleurVehicule, String modeleVehicule, String villeVehicules, double prixVehiculeParJour,
                               String proprietaire, boolean estEnPause) {

        // Existance de vehicule
        if (vehiculeRepository.existsById(idVehicule)) {
            throw new IllegalArgumentException("Ce véhicule existe déjà");
        }
        Optional<Utilisateur> userOpt = userRepository.findByEmail(proprietaire);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Ce propriétaire n'existe pas");
        }

        // Creation d'une instance de vehicule
        Vehicle vehicule;
        switch (typeVehicule) {
            case VOITURE -> vehicule = new Voiture(idVehicule, marqueVehicule, couleurVehicule, modeleVehicule,
                        villeVehicules, prixVehiculeParJour, proprietaire, estEnPause);
            case MOTO -> vehicule = new Moto(idVehicule, marqueVehicule, couleurVehicule, modeleVehicule,
                    villeVehicules, prixVehiculeParJour, proprietaire, estEnPause);
            case CAMION -> vehicule = new Camion(idVehicule, marqueVehicule, couleurVehicule, modeleVehicule,
                    villeVehicules, prixVehiculeParJour, proprietaire, estEnPause);
            default -> throw new IllegalArgumentException("Type de véhicule non supporté");
        }
        return vehiculeRepository.save(vehicule);
    }

    /**
     * Mise ajour de la liste des disponibilités
     */
    public void upDateDisponibilites(String id, Map<String, Boolean> dispoRquest) {
        Vehicle v = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable"));
        // Entry permet de recupere key + value
        for (Map.Entry<String, Boolean> keyVal : dispoRquest.entrySet()) {
            LocalDate date = LocalDate.parse(keyVal.getKey());
            Boolean dispo = keyVal.getValue();
            v.getDisponibilites().put(date, dispo);
        }
    }

    /**
     * suppression d'un vehicule
     */
    public void deleteVehicule(String id) {
        if (vehiculeRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Véhicule introuvable!");
        }
        ;
        vehiculeRepository.deleteById(id);
    }

    /**
     * get vehicle infos
     */
    public Vehicle getVehiculeByID(String id) {
        Vehicle v = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable"));
        return v;
    }

    // recuperer les vehicules par ville
    public List<Vehicle> getVehiculeByVille(String ville) {
        List<Vehicle> listV = vehiculeRepository.findByVille(ville);
        if (listV.isEmpty()) {
            throw new IllegalArgumentException("Pas de véhicules dans cette ville");
        }
        return listV;
    }

    // recuperer les vehicule dispo sur le marché
    public List<Vehicle> getVehiculeByEnPause(){
        List<Vehicle> disponibles = vehiculeRepository.findByEnPause();
        if(disponibles.isEmpty()){
            throw new IllegalArgumentException("Aucun véhicule disponible pour le moment");
        }
        return disponibles;
    }

    // recuperer tous les vehicules
    public Collection<Vehicle> getAllVehicules(){
        return vehiculeRepository.findAll();
    }


    /**
     * @param id
     * @param deb
     * @param fin
     * @return true si le vehicule est disponible entre date debut et date fin,
     *         false sinon
     */
    public boolean verifierDisponibilite(String id, LocalDate deb, LocalDate fin) {
        Vehicle v = getVehiculeByID(id);
        return v.estDisponible(deb, fin);
    }

    public Vehicle updateVehicule(String id, Vehicle newData) {
        Vehicle vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable!"));
        // Mise à jour des champs
        //vehicule.setIdVehicule(newData.getIdVehicule());
        //vehicule.setTypeVehicule(newData.getTypeVehicule());
        vehicule.setMarqueVehicule(newData.getMarqueVehicule());
        vehicule.setModeleVehicule(newData.getModeleVehicule());
        vehicule.setCouleurVehicule(newData.getCouleurVehicule());
        vehicule.setVilleVehicule(newData.getVilleVehicule());
        vehicule.setPrixVehiculeParJour(newData.getPrixVehiculeParJour());
        vehicule.setProprietaire(newData.getProprietaire());
        vehicule.setEstEnpause(newData.getEstEnpause());
        vehiculeRepository.modifVehicule(id, vehicule);
        return vehicule;
    }
}
