package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.vehicule.Disponibilite;
import fr.miage.groupe2projetpoo.entity.vehicule.TypeVehicule;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.notation.NoteVehicule;
import fr.miage.groupe2projetpoo.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PrivilegedExceptionAction;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/vehicules")
public class VehicleController {
    // Propriétés
    private final VehicleService vehicleService;

    // constructeur
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /********************************
     * ADD / MODIFIER / DELETE / GET --> VEHICULE
     ************************************/

    // Ajouter un vehicule - POST /api/vehicules/add
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addVehicule(@RequestBody Map<String, String> request) {
        try {
            String type = request.get("type");
            TypeVehicule typeEnum = TypeVehicule.valueOf(type.toUpperCase());
            String id = request.get("idVehicule");
            String marque = request.get("marqueVehicule");
            String couleur = request.get("couleurVehicule");
            String modele = request.get("modeleVehicule");
            String ville = request.get("villeVehicule");
            double prixVehiculeParJour = Double.parseDouble(request.get("prixVehiculeParJour"));
            String proprietaire = request.get("proprietaire");
            boolean estEnPause = Boolean.parseBoolean(request.get("estEnPause"));

            Vehicle vehicule = vehicleService.addVehicule(id, typeEnum, marque, couleur, modele, ville,
                    prixVehiculeParJour, proprietaire, estEnPause);
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

    // Suppression d'un vehicule
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteVehicule(@RequestBody Map<String, String> request) {
        try {
            String id = request.get("idVehicule");
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
    @PutMapping("/update")
    public ResponseEntity<?> updateVehicule(@RequestBody Map<String, String> request) {
        try {
            String idV = request.get("idVehicule");
            String marque = request.get("marqueVehicule");
            String couleur = request.get("couleurVehicule");
            String modele = request.get("modeleVehicule");
            String ville = request.get("villeVehicule");
            double prixVehiculeParJour = Double.parseDouble(request.get("prixVehiculeParJour"));
            String proprietaire = request.get("proprietaire");
            boolean estEnPause = Boolean.parseBoolean(request.get("estEnPause"));
            Vehicle newData = new Vehicle(idV, marque, couleur, modele, ville, prixVehiculeParJour, proprietaire,
                    estEnPause) {
                @Override
                public TypeVehicule getType() {
                    return null;
                }
            };
            Vehicle updatedV = vehicleService.updateVehicule(idV, newData);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Véhicule mis à jour avec succès",
                    "vehicule", mapVehicleInfo(updatedV)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }

    }

    // voir tout les vehicules
    @GetMapping("/all")
    public ResponseEntity<?> getAllVehicules() {
        try {
            Collection<Vehicle> listV = vehicleService.getAllVehicules();
            List<Map<String, Object>> result = listV.stream().map(v -> {
                return mapVehicleInfo(v);
            }).toList();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "vehicules", result));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    /*****************************************
     * Affichage autres info
     ************************************************/

    // afficher l'historique des contrats d'un vehicule
    @GetMapping("/historiqueContrat")
    public ResponseEntity<Map<String, Object>> GetContractListVehicules(@RequestBody Map<String, String> request) {
        try {
            String id = request.get("id");
            Vehicle v = vehicleService.getVehiculeByID(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "historique des contract", v.getHistoriqueContrats()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // afficher la liste des Notations d'un vehicule
    @GetMapping("/notations")
    public ResponseEntity<Map<String, Object>> GetNotationsListVehicules(@RequestBody Map<String, String> request) {
        try {
            String id = request.get("id");
            Vehicle v = vehicleService.getVehiculeByID(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "Notations", v.getNotations()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Afficher les infos d'un vehicule enregistré
    /**
     * Récupérer les information d'un véhicules par id - GET
     * /api/vehicules/{id}/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> GetVehiculesByID(@RequestBody Map<String, String> request) {
        try {
            String id = request.get("id");
            Vehicle v = vehicleService.getVehiculeByID(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "vehicule", mapVehicleInfo(v)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/infoCompleted")
    public ResponseEntity<Map<String, Object>> GetVehiculesByIDCompleted(@RequestBody Map<String, String> request) {
        try {
            String id = request.get("id");
            Vehicle v = vehicleService.getVehiculeByID(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "vehicule", mapVehicleToMap(v)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    /*******************************************
     * RECHERCHE
     **********************************************/
    // Par ville
    // http://localhost:8080/api/vehicules/
    @GetMapping("/parVille")
    public ResponseEntity<?> getVehiculesByVille(@RequestBody Map<String, String> request) {
        try {
            String ville = request.get("ville");
            List<Vehicle> listV = vehicleService.getVehiculeByVille(ville);
            List<Map<String, Object>> result = listV.stream().map(v -> {
                return mapVehicleInfo(v);
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

    // chercher par type
    // http://localhost:8080/api/vehicules/type/{type}
    @GetMapping("/parType")
    public ResponseEntity<?> getVehiculesByType(@RequestBody Map<String, String> request) {
        try {
            String type = (request.get("type"));
            List<Vehicle> listV = vehicleService.getVehiculesByType(type);
            List<Map<String, Object>> result = listV.stream().map(v -> {
                return mapVehicleInfo(v);
            }).toList();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "type", type,
                    "vehicules", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // chercher les vehicules qui ne sont pas en pause sur le marché: disponible
    @GetMapping("/parDisponibilité")
    public ResponseEntity<?> getTousVehiculesDisponibles() {

        try {
            List<Vehicle> listV = vehicleService.getVehiculeByEnPause();
            List<Map<String, Object>> result = listV.stream().map(v -> {
                return mapVehicleInfo(v);
            }).toList();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "vehicules", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // par prix
    @GetMapping("/parPrix")
    public ResponseEntity<?> getVehiculesByPrix(@RequestBody Map<String, String> request) {

        try {
            double min = Double.parseDouble(request.get("min"));
            double max = Double.parseDouble(request.get("max"));
            List<Vehicle> listV = vehicleService.getVehiculeByPrix(min, max);
            List<Map<String, Object>> result = listV.stream().map(v -> {
                return mapVehicleInfo(v);
            }).toList();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "vehicules", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }

    }

    // par modele
    @GetMapping("/parModele")
    public ResponseEntity<?> getVehiculesByModele(@RequestBody Map<String, String> request) {
        try {
            String modele = request.get("modele");
            List<Vehicle> listV = vehicleService.getVehiculesByModele(modele);
            List<Map<String, Object>> result = listV.stream().map(v -> {
                return mapVehicleInfo(v);
            }).toList();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "modele", modele,
                    "vehicules", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // par marque
    @GetMapping("/parMarque")
    public ResponseEntity<?> getVehiculesByMarque(@RequestBody Map<String, String> request) {
        try {
            String marque = request.get("marque");
            List<Vehicle> listV = vehicleService.getVehiculesByMarque(marque);
            List<Map<String, Object>> result = listV.stream().map(v -> {
                return mapVehicleInfo(v);
            }).toList();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "marque", marque,
                    "vehicules", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    /************************************
     * Planning Disponibilités
     ********************************/

    // Récupérer le planning complet d’un véhicule
    @GetMapping("/planning")
    public ResponseEntity<?> getPlanning(@RequestBody Map<String, String> request) {
        try {
            String id = request.get("id");
            List<Disponibilite> planning = vehicleService.getPlanning(id);
            // Construire une liste numérotée
            List<Map<String, Object>> planningFormatte = new ArrayList<>();
            int index = 1;
            for (Disponibilite d : planning) {
                Map<String, Object> creneau = new HashMap<>();
                creneau.put("creneau", index);
                creneau.put("debut", d.getDebut());
                creneau.put("fin", d.getFin());
                planningFormatte.add(creneau);
                index++;
            }
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "planning", planningFormatte));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Ajouter un créneau de disponibilité
    @PostMapping("/planning/add")
    public ResponseEntity<?> addDisponibilite(@RequestBody Map<String, String> request) {
        try {
            String id = request.get("id");
            LocalDate debut = LocalDate.parse(request.get("debut"));
            LocalDate fin = LocalDate.parse(request.get("fin"));
            vehicleService.addDisponibilite(id, debut, fin);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Créneau ajouté avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Supprimer un créneau par index
    // http://localhost:8080/api/vehicules
    @DeleteMapping("/planning/delete")
    public ResponseEntity<?> removeDisponibilite(@RequestBody Map<String, String> request) {
        try {
            String id = request.get("id");
            int index = Integer.parseInt(request.get("index"));
            vehicleService.removeDisponibilite(id, index - 1);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Créneau supprimé avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Vérifier si un véhicule est disponible sur une période
    @PostMapping("/planning/verifDisponible")
    public ResponseEntity<?> estDisponible(@RequestBody Map<String, String> request) {
        try {
            String id = request.get("id");
            LocalDate debut = LocalDate.parse(request.get("debut"));
            LocalDate fin = LocalDate.parse(request.get("fin"));
            if (debut.isAfter(fin)) {
                return ResponseEntity.status(404).body(Map.of(
                        "success", true,
                        "message", "La date de debut doit être inferieur à la date de fin"));
            }
            boolean dispo = vehicleService.estDisponiblePlanning(id, debut, fin);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "disponible", dispo));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // les infos simple de vehicule en map
    private Map<String, Object> mapVehicleInfo(Vehicle v) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", v.getIdVehicule());
        map.put("type", v.getType());
        map.put("marque", v.getMarqueVehicule());
        map.put("modele", v.getModeleVehicule());
        map.put("couleur", v.getCouleurVehicule());
        map.put("ville", v.getVilleVehicule());
        map.put("estEnPause", v.getEstEnpause());
        map.put("prixParJour", v.getPrixVehiculeParJour());
        return map;
    }

    private Map<String, Object> mapVehicleToMap(Vehicle v) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", v.getIdVehicule());
        map.put("type", v.getType());
        map.put("marque", v.getMarqueVehicule());
        map.put("modele", v.getModeleVehicule());
        map.put("couleur", v.getCouleurVehicule());
        map.put("ville", v.getVilleVehicule());
        map.put("estEnPause", v.getEstEnpause());
        map.put("prixParJour", v.getPrixVehiculeParJour());
        map.put("Planning", v.getPlanningDisponible());

        // Ajout des notations
        List<Map<String, Object>> notationsList = v.getNotations().stream().map(n -> {
            Map<String, Object> noteMap = new HashMap<>();
            noteMap.put("id", n.getId());
            noteMap.put("auteur", n.getAuthorEmail());
            noteMap.put("commentaire", n.getCommentaire());
            noteMap.put("noteGlobale", n.calculerNoteGlobale());
            noteMap.put("date", n.getDate());
            noteMap.put("confort", n.getConfort());
            noteMap.put("proprete", n.getProprete());
            return noteMap;
        }).toList();
        map.put("notations", notationsList);

        // Ajout de l'historique des contrats
        List<Map<String, Object>> contratsList = v.getHistoriqueContrats().stream().map(c -> {
            Map<String, Object> contratMap = new HashMap<>();
            contratMap.put("id", c.getIdC());
            contratMap.put("dateDebut", c.getDateDebut());
            contratMap.put("dateFin", c.getDateFin());
            contratMap.put("statut", c.isStatut());
            contratMap.put("prixTotal", c.getPrixTotal());
            if (c.getLoueur() != null) {
                contratMap.put("loueur", c.getLoueur().getEmail());
            }
            return contratMap;
        }).toList();
        map.put("historiqueContrats", contratsList);

        return map;
    }

    // gere la liste des dates de disponibilité
    /*
     * @PostMapping("/{id}/disponibilites")
     * public ResponseEntity<?> setDisponibilitesMap(@PathVariable String
     * id, @RequestBody Map<String, Boolean> request) {
     * try {
     * vehicleService.upDateDisponibilites(id, request);
     * return ResponseEntity.ok(Map.of(
     * "message", "Disponibilités mises à jour",
     * "success", true,
     * "ID Vehicule", id));
     * 
     * } catch (Exception e) {
     * return ResponseEntity.badRequest().body(Map.of(
     * "Erreur", e.getMessage()));
     * }
     * }
     */

    // afficher le calendrier des disponibilites d'un vehicule
    /*
     * @GetMapping("/{id}/dispo")
     * public ResponseEntity<Map<String, Object>>
     * GetDispoListVehicules(@PathVariable String id) {
     * try {
     * Vehicle v = vehicleService.getVehiculeByID(id);
     * 
     * // Transformer les infos du véhicules en Map pour la réponse JSON
     * Map<LocalDate, Boolean> map = new HashMap<>();
     * map = v.getDisponibilites();
     * return ResponseEntity.ok(Map.of(
     * "success", true,
     * "vehicule", map));
     * } catch (IllegalArgumentException e) {
     * return ResponseEntity.status(404).body(Map.of(
     * "success", false,
     * "message", e.getMessage()));
     * }
     * }
     */

    // verifier les disponibilites d'un vehicule
    /*
     * @GetMapping("/{id}/disponible")
     * public ResponseEntity<?> estDisponible(@PathVariable String id, @RequestParam
     * String debut,
     * 
     * @RequestParam String fin) {
     * LocalDate d = LocalDate.parse(debut);
     * LocalDate f = LocalDate.parse(fin);
     * 
     * boolean dispo = vehicleService.verifierDisponibilite(id, d, f);
     * return ResponseEntity.ok(Map.of(
     * "vehicule", id,
     * "disponible", dispo,
     * "debut", d,
     * "fin", f));
     * }
     */
}
