package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.entity.vehicule.*;
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

    /********************************
     * ADD / MODIFIER / DELETE / GET --> VEHICULE
     ************************************/

    // Ajout d'un vehicule
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

    // Update vehicule
    public Vehicle updateVehicule(String id, Vehicle newData) {
        Vehicle vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable!"));
        // Mise à jour des champs
        // vehicule.setIdVehicule(newData.getIdVehicule());
        // vehicule.setTypeVehicule(newData.getTypeVehicule());
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

    // suppression d'un vehicule
    public void deleteVehicule(String id) {
        if (vehiculeRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Véhicule introuvable!");
        }
        ;
        vehiculeRepository.deleteById(id);
    }

    // recuperer tous les vehicules
    public Collection<Vehicle> getAllVehicules() {
        return vehiculeRepository.findAll();
    }

    /***********************************
     * GETTER D'INFO
     ************************************/

    // get vehicle infos
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

    // recuperer les vehicules par prix
    public List<Vehicle> getVehiculeByPrix(double min, double max) {

        List<Vehicle> listV = vehiculeRepository.findByPrix(min, max);
        if (listV.isEmpty()) {
            throw new IllegalArgumentException("Aucun véhicule disponible pour le moment");
        }
        return listV;

    }

    // recuperer les vehicule dispo sur le marché
    public List<Vehicle> getVehiculeByEnPause() {
        List<Vehicle> disponibles = vehiculeRepository.findByEnPause();
        if (disponibles.isEmpty()) {
            throw new IllegalArgumentException("Aucun véhicule disponible pour le moment");
        }
        return disponibles;
    }

    public List<Vehicle> getVehiculesByType(String type) {
        List<Vehicle> vehicules = vehiculeRepository.findByType(type);
        if (vehicules.isEmpty()) {
            throw new IllegalArgumentException("Aucun véhicule trouvé pour le type : " + type);
        }
        return vehicules;
    }

    public List<Vehicle> getVehiculesByModele(String modele) {
        List<Vehicle> vehicules = vehiculeRepository.findByModele(modele);
        if (vehicules.isEmpty()) {
            throw new IllegalArgumentException("Aucun véhicule trouvé pour le modèle : " + modele);
        }
        return vehicules;
    }

    public List<Vehicle> getVehiculesByMarque(String marque) {
        List<Vehicle> vehicules = vehiculeRepository.findByMarque(marque);
        if (vehicules.isEmpty()) {
            throw new IllegalArgumentException("Aucun véhicule trouvé pour la marque : " + marque);
        }
        return vehicules;
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
        return v.estDisponibleMap(deb, fin);
    }

    // -----------------------------------------------
    // Planning
    // -----------------------------------------------

    // Récupération du planning
    public List<Disponibilite> getPlanning(String id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        return vehiculeRepository.getPlanning(id);
    }

    // Ajout d’un créneau de disponibilité
    public void addDisponibilite(String idVehicule, LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être nulles");
        }
        if (debut.isAfter(fin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        if (!vehiculeRepository.existsById(idVehicule)) {
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        vehiculeRepository.addPlanning(idVehicule, debut, fin);
    }

    // Suppression d’un créneau
    public void removeDisponibilite(String idVehicule, int index) {
        if (!vehiculeRepository.existsById(idVehicule)) {
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        vehiculeRepository.removeCreneau(idVehicule, index);
    }

    // Vérification de disponibilité
    public boolean estDisponiblePlanning(String idVehicule, LocalDate debut, LocalDate fin) {
        if (!vehiculeRepository.existsById(idVehicule)) {
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        return vehiculeRepository.estDisponiblePlanning(idVehicule, debut, fin);
    }

    // ===== VÉRIFICATION RÉDUCTION PARKING VIENCI =====

    /**
     * Vérifie si un véhicule bénéficie d'une réduction parking partenaire (Vienci).
     * Retourne true si le propriétaire (Agent) a l'option OptionParking activée.
     * 
     * @param vehicule Le véhicule à vérifier
     * @return true si réduction disponible, false sinon
     */
    public boolean hasParkingReduction(Vehicle vehicule) {
        if (vehicule == null || vehicule.getProprietaire() == null) {
            return false;
        }

        Optional<Utilisateur> userOpt = userRepository.findByEmail(vehicule.getProprietaire());
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Agent)) {
            return false;
        }

        Agent agent = (Agent) userOpt.get();
        fr.miage.groupe2projetpoo.entity.assurance.OptionParking optParking = agent
                .getOption(fr.miage.groupe2projetpoo.entity.assurance.OptionParking.class);

        return optParking != null && optParking.isEstActive() && optParking.getParkingPartenaire() != null;
    }

    /**
     * Retourne le taux de réduction parking si disponible.
     * 
     * @param vehicule Le véhicule à vérifier
     * @return Le taux de réduction (ex: 0.20 pour 20%), ou 0 si pas de réduction
     */
    public double getParkingReductionRate(Vehicle vehicule) {
        if (vehicule == null || vehicule.getProprietaire() == null) {
            return 0;
        }

        Optional<Utilisateur> userOpt = userRepository.findByEmail(vehicule.getProprietaire());
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Agent)) {
            return 0;
        }

        Agent agent = (Agent) userOpt.get();
        fr.miage.groupe2projetpoo.entity.assurance.OptionParking optParking = agent
                .getOption(fr.miage.groupe2projetpoo.entity.assurance.OptionParking.class);

        if (optParking != null && optParking.isEstActive()) {
            return optParking.getTauxReduction();
        }
        return 0;
    }

    // ===== RECOMMANDATION DE VÉHICULES PAR PROXIMITÉ (GPS) =====

    // Rayon de la Terre en km
    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Calcule la distance entre deux points GPS en utilisant la formule Haversine.
     * 
     * @return Distance en kilomètres
     */
    public double calculerDistanceHaversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Trouve les véhicules disponibles dans un rayon donné autour d'un point GPS.
     * 
     * @param latitude  Latitude de l'utilisateur
     * @param longitude Longitude de l'utilisateur
     * @param rayonKm   Rayon de recherche en km
     * @return Liste des véhicules proches avec leur distance
     */
    public List<java.util.Map<String, Object>> getVehiculesProchesGPS(double latitude, double longitude, int rayonKm) {
        List<Vehicle> tousVehicules = vehiculeRepository.findByEnPause();
        List<java.util.Map<String, Object>> resultats = new java.util.ArrayList<>();

        for (Vehicle v : tousVehicules) {
            // Vérifier si le véhicule a des coordonnées GPS
            if (v.getLatitude() != null && v.getLongitude() != null) {
                double distance = calculerDistanceHaversine(
                        latitude, longitude,
                        v.getLatitude(), v.getLongitude());

                if (distance <= rayonKm) {
                    java.util.Map<String, Object> vehiculeAvecDistance = new java.util.HashMap<>();
                    vehiculeAvecDistance.put("vehicule", v);
                    vehiculeAvecDistance.put("distanceKm", Math.round(distance * 10.0) / 10.0);
                    resultats.add(vehiculeAvecDistance);
                }
            }
        }

        // Trier par distance croissante
        resultats.sort((a, b) -> Double.compare(
                (Double) a.get("distanceKm"),
                (Double) b.get("distanceKm")));

        return resultats;
    }

    /**
     * Trouve les véhicules disponibles à proximité (compatible avec l'ancienne
     * API).
     * Utilise GPS si disponible, sinon fallback sur le nom de ville.
     */
    public List<Vehicle> getVehiculesProches(String villeUtilisateur, int rayonKm) {
        if (villeUtilisateur == null || villeUtilisateur.isBlank()) {
            throw new IllegalArgumentException("La ville de l'utilisateur doit être renseignée");
        }

        List<Vehicle> tousVehicules = vehiculeRepository.findByEnPause();
        if (tousVehicules.isEmpty()) {
            return new java.util.ArrayList<>();
        }

        List<Vehicle> vehiculesProches = new java.util.ArrayList<>();
        String villeNormalisee = normaliserVille(villeUtilisateur);

        for (Vehicle v : tousVehicules) {
            String villeVehicule = normaliserVille(v.getVilleVehicule());

            // Correspondance exacte de ville
            if (villeNormalisee.equalsIgnoreCase(villeVehicule)) {
                vehiculesProches.add(v);
            }
        }

        return vehiculesProches;
    }

    private String normaliserVille(String ville) {
        if (ville == null || ville.isBlank())
            return "";
        String trimmed = ville.trim();
        return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }

}
