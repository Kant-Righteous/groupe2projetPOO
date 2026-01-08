package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
public class VehicleController {
    //Propriétés
    private final VehicleService vehicleService;

    //constructeur
    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }

    /**
     * Ajouter un vehicule - POST /api/vehicules/add
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addVehicule(@RequestBody Map<String, String> request) {
        String id = request.get("idVehicule");
        String type = request.get("typeVehicule");
        String marque = request.get("marqueVehicule");
        String couleur = request.get("couleurVehicule");
        String modele = request.get("modeleVehicule");
        String ville = request.get("villeVehicule");
        boolean estDisponible = Boolean.parseBoolean(request.get("estDisponible"));
        double prixVehiculeParJour =Double.parseDouble(request.get("prixVehiculeParJour"));
        String proprietaire = request.get("proprietaire");

        Vehicle vehicule = vehicleService.addVehicule(id, type, marque, couleur, modele, ville, estDisponible, prixVehiculeParJour, proprietaire);
        if (vehicule != null) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Ajout réussie",
                    "email", vehicule.getIdVehicule()));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Échec de l'inscription, email déjà existant"));
        }
    }

    @PostMapping("/disponibilites")
    public ResponseEntity<?> setListeDisponibilites(@RequestBody VehicleService serv) {
        List<LocalDate> dates = serv.getListeDisponibilites();
        return ResponseEntity.ok(dates); }

}
