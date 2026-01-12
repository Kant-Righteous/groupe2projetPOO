package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.vehicule.TypeVehicule;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PrivilegedExceptionAction;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

    @RestController
    @RequestMapping("/api/vehicules")
    public class VehicleController {
    // Propriétés
    private final VehicleService vehicleService;

    // constructeur
    public VehicleController(VehicleService vehicleService) {
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
        double prixVehiculeParJour = Double.parseDouble(request.get("prixVehiculeParJour"));
        String proprietaire = request.get("proprietaire");
        // boolean estDisponible = Boolean.parseBoolean(request.get("estDisponible"));

        try {
            Vehicle vehicule = vehicleService.addVehicule(id, type, marque, couleur, modele, ville,
                    prixVehiculeParJour, proprietaire);
            // if (vehicule != null) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Ajout réussie",
                    "ID Vehicule", vehicule.getIdVehicule()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // gere la liste des dates de disponibilité
    @PostMapping("/{id}/disponibilites")
    public ResponseEntity<?> setDisponibilites(@PathVariable String id, @RequestBody Map<String, Boolean> request) {
        try {
            vehicleService.upDateDisponibilites(id, request);
            return ResponseEntity.ok(Map.of(
                    "message", "Disponibilités mises à jour",
                    "success", true,
                    "ID Vehicule", id));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "Erreur", e.getMessage()));
        }
    }

    // Afficher les infos d'un vehicule enregistré

    /**
     * Récupérer les information d'un véhicules par id - GET
     * /api/vehicules/{id}/info
     */
    @GetMapping("/{id}/info")
    public ResponseEntity<Map<String, Object>> GetVehiculesByID(@PathVariable String id) {
        try {
            Vehicle v = vehicleService.getVehiculeByID(id);

            // Transformer les infos du véhicules en Map pour la réponse JSON
            Map<String, Object> map = new HashMap<>();
            map.put("id", v.getIdVehicule());
            map.put("type", v.getTypeVehicule());
            map.put("marque", v.getMarqueVehicule());
            map.put("modele", v.getModeleVehicule());
            map.put("couleur", v.getCouleurVehicule());
            map.put("ville", v.getVilleVehicule());
            // map.put("disponible", v.getEstDisponible());
            map.put("prixParJour", v.getPrixVehiculeParJour());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "vehicule", map));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // afficher le calendrier des disponibilites d'un vehicule
    @GetMapping("/{id}/dispo")
    public ResponseEntity<Map<String, Object>> GetDispoListVehicules(@PathVariable String id) {
        try {
            Vehicle v = vehicleService.getVehiculeByID(id);

            // Transformer les infos du véhicules en Map pour la réponse JSON
            Map<LocalDate, Boolean> map = new HashMap<>();
            map = v.getDisponibilites();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "vehicule", map));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // voir les disponibilites d'un vehicule
    @GetMapping("/{id}/disponible")
    public ResponseEntity<?> estDisponible(@PathVariable String id, @RequestParam String debut,
            @RequestParam String fin) {
        LocalDate d = LocalDate.parse(debut);
        LocalDate f = LocalDate.parse(fin);

        boolean dispo = vehicleService.verifierDisponibilite(id, d, f);
        return ResponseEntity.ok(Map.of(
                "vehicule", id,
                "disponible", dispo,
                "debut", d,
                "fin", f));
    }

    // chercher par ville
    // http://localhost:8080/api/vehicules/
    @GetMapping("/{ville}/parVille")
    public ResponseEntity<?> getVehiculesByVille(@PathVariable String ville) {
        try {
            List<Vehicle> listV = vehicleService.getVehiculeByVille(ville);
            List<Map<String, Object>> result = listV.stream().map(v -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", v.getIdVehicule());
                map.put("type", v.getTypeVehicule());
                map.put("marque", v.getMarqueVehicule());
                map.put("modele", v.getModeleVehicule());
                map.put("couleur", v.getCouleurVehicule());
                map.put("ville", v.getVilleVehicule());
                // map.put("disponible", v.getEstDisponible());
                map.put("prixJour", v.getPrixVehiculeParJour());
                return map;
            }).toList();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "ville", ville,
                    "vehicules", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Suppression d'un vehicule
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicule(@PathVariable String id) {
        try {
            vehicleService.deleteVehicule(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Véhicule supprimé avec succès",
                    "id", id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Modifier vehicule
    @PutMapping("/{idV}")
    public ResponseEntity<?> updateVehicule(@PathVariable String idV, @RequestBody Map<String, String> request) {
        try {
            //String id = request.get("idVehicule");
            String type = request.get("typeVehicule");
            String marque = request.get("marqueVehicule");
            String couleur = request.get("couleurVehicule");
            String modele = request.get("modeleVehicule");
            String ville = request.get("villeVehicule");
            double prixVehiculeParJour = Double.parseDouble(request.get("prixVehiculeParJour"));
            String proprietaire = request.get("proprietaire");
            Vehicle newData = new Vehicle(idV, type, marque, couleur, modele, ville, prixVehiculeParJour,proprietaire ) {
                @Override
                public TypeVehicule getType() {
                    return null;
                }
            };
            Vehicle updatedV = vehicleService.updateVehicule(idV, newData);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Véhicule mis à jour avec succès",
                    "vehicule", updatedV));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }

    }
}
