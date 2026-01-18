package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.maintenance.ControleTechnique;
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
    public ResponseEntity<Map<String, Object>> deleteVehicule(@RequestBody Map<String, String> request) {
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
    public ResponseEntity<Map<String, Object>> updateVehicule(@RequestBody Map<String, String> request) {
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

    // Modifier status de vehicule
    @PutMapping("/updateEstEnPause")
    public ResponseEntity<Map<String, Object>> updateVehiculeEnPause(@RequestBody Map<String, String> request) {
        try {
            String id = request.get("idVehicule");
            boolean estEnPause = Boolean.parseBoolean(request.get("estEnPause"));
            vehicleService.setvehiculeEnPause(id, estEnPause);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Statut de pause modifié avec succès",
                    "idVehicule", id,
                    "estEnPause", estEnPause));
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // voir tout les vehicules
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllVehicules() {
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
    public ResponseEntity<Map<String, Object>> getVehiculesByVille(@RequestBody Map<String, String> request) {
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
    public ResponseEntity<Map<String, Object>> getVehiculesByType(@RequestBody Map<String, String> request) {
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
    public ResponseEntity<Map<String, Object>> getTousVehiculesDisponibles() {

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
    public ResponseEntity<Map<String, Object>> getVehiculesByPrix(@RequestBody Map<String, String> request) {

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
    public ResponseEntity<Map<String, Object>> getVehiculesByModele(@RequestBody Map<String, String> request) {
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
    public ResponseEntity<Map<String, Object>> getVehiculesByMarque(@RequestBody Map<String, String> request) {
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

    @PostMapping("/MultiFiltrage")
    public ResponseEntity<Map<String, Object>> filtrerVehicules(@RequestBody Map<String, String> request) {
        try {
            List<Vehicle> vehicules = vehicleService.getAllVehicules().stream()
                    .filter(v -> request.get("ville") == null
                            || v.getVilleVehicule().equalsIgnoreCase(request.get("ville")))
                    .filter(v -> request.get("type") == null
                            || v.getType().toString().equalsIgnoreCase(request.get("type")))
                    .filter(v -> request.get("estEnPause") == null
                            || v.getEstEnpause() == Boolean.parseBoolean(request.get("estEnPause")))
                    .filter(v -> request.get("modele") == null
                            || v.getModeleVehicule().equalsIgnoreCase(request.get("modele")))
                    .filter(v -> request.get("couleur") == null
                            || v.getCouleurVehicule().equalsIgnoreCase(request.get("couleur")))
                    .filter(v -> request.get("marque") == null
                            || v.getMarqueVehicule().equalsIgnoreCase(request.get("marque")))
                    .filter(v -> {
                        if (request.get("debut") == null || request.get("fin") == null)
                            return true;
                        LocalDate debut = LocalDate.parse(request.get("debut"));
                        LocalDate fin = LocalDate.parse(request.get("fin"));
                        return vehicleService.estDisponiblePlanning(v.getIdVehicule(), debut, fin);
                    })
                    .filter(v -> {
                        if (request.get("min") == null && request.get("max") == null)
                            return true;
                        double prix = v.getPrixVehiculeParJour();
                        double min = request.get("min") != null ? Double.parseDouble(request.get("min")) : 0;
                        double max = request.get("max") != null ? Double.parseDouble(request.get("max")) : Double.MAX_VALUE;
                        return prix >= min && prix <= max;
                    }).toList();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "resultat", vehicules.stream().map(this::mapVehicleInfo).toList()));
        }catch (IllegalArgumentException e) { return ResponseEntity.status(404).body(Map.of(
                "success", false,
                "message", e.getMessage()));
        }
    }


    /************************************
     * Planning Disponibilités
     ********************************/

    // Récupérer le planning complet d’un véhicule
    @GetMapping("/planning")
    public ResponseEntity<Map<String, Object>> getPlanning(@RequestBody Map<String, String> request) {
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
    public ResponseEntity<Map<String, Object>> addDisponibilite(@RequestBody Map<String, String> request) {
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
    public ResponseEntity<Map<String, Object>> removeDisponibilite(@RequestBody Map<String, String> request) {
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
    public ResponseEntity<Map<String, Object>> estDisponible(@RequestBody Map<String, String> request) {
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

        // Ajout du label de réduction parking Vienci
        boolean hasParkingReduction = vehicleService.hasParkingReduction(v);
        map.put("tarifReduit", hasParkingReduction);
        if (hasParkingReduction) {
            double tauxReduction = vehicleService.getParkingReductionRate(v);
            int pourcentage = (int) (tauxReduction * 100);
            map.put("labelReduction", "Tarif réduit - Retour en parking partenaire (-" + pourcentage + "%)");
        }
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
        map.put("kilometrageActuel", v.getKilometrageActuel());
        map.put("latitude", v.getLatitude());
        map.put("longitude", v.getLongitude());
        map.put("controleTechnique", v.getControleTechnique());
        map.put("dernierLieuDepose", v.getDernierLieuDepose());

        // Ajouter la listte des entretiens
        List<Map<String, Object>> entretiensList = v.getHistoriqueEntretiens().stream().map(e -> {
            Map<String, Object> entretienMap = new HashMap<>();
            entretienMap.put("typeOperation", e.getTypeOperation());
            entretienMap.put("dateRealisation", e.getDateRealisation());
            entretienMap.put("kilometrageAuMoment", e.getKilometrageAuMoment());
            entretienMap.put("cout", e.getCout());
            entretienMap.put("garagePrestataire", e.getGaragePrestataire());
            return entretienMap;
        }).toList();
        map.put("historiqueEntretiens",entretiensList);

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


    // ===== RECOMMANDATION DE VÉHICULES PAR PROXIMITÉ =====

    /**
     * GET /api/vehicules/proches - Trouver les véhicules disponibles à proximité
     * 
     * Body JSON:
     * {
     * "ville": "Paris",
     * "rayonKm": 50
     * }
     * 
     * Rayon:
     * - 0-49 km : même ville uniquement
     * - 50-99 km : même région
     * - 100+ km : toutes les régions
     */
    @PostMapping("/proches")
    public ResponseEntity<?> getVehiculesProches(@RequestBody Map<String, Object> request) {
        try {
            String ville = (String) request.get("ville");
            int rayonKm = 50; // Défaut: même région

            if (request.containsKey("rayonKm")) {
                Object rayonObj = request.get("rayonKm");
                if (rayonObj instanceof Number) {
                    rayonKm = ((Number) rayonObj).intValue();
                } else if (rayonObj instanceof String) {
                    rayonKm = Integer.parseInt((String) rayonObj);
                }
            }

            List<Vehicle> vehiculesProches = vehicleService.getVehiculesProches(ville, rayonKm);

            List<Map<String, Object>> result = vehiculesProches.stream().map(v -> {
                return mapVehicleInfo(v);
            }).toList();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "villeRecherche", ville,
                    "rayonKm", rayonKm,
                    "nombreResultats", result.size(),
                    "vehicules", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    /**
     * GET /api/vehicules/suggestions/{email} - Suggestions pour un loueur basées
     * sur son profil
     * 
     * Récupère la ville du loueur depuis son profil et suggère les véhicules
     * proches
     */
    @GetMapping("/suggestions/{email}")
    public ResponseEntity<?> getSuggestionsPourLoueur(
            @PathVariable String email,
            @RequestParam(defaultValue = "50") int rayonKm) {
        try {
            // Cette méthode nécessite UserRepository - à implémenter via injection
            // Pour l'instant, retourne une erreur suggérant d'utiliser /proches
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message",
                    "Utilisez POST /api/vehicules/proches/gps avec vos coordonnées GPS pour obtenir des suggestions",
                    "exemple", Map.of("latitude", 48.8566, "longitude", 2.3522, "rayonKm", 50)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    /**
     * POST /api/vehicules/proches/gps - Trouver les véhicules par coordonnées GPS
     * 
     * Body JSON:
     * {
     * "latitude": 48.8566,
     * "longitude": 2.3522,
     * "rayonKm": 50
     * }
     * 
     * Retourne les véhicules triés par distance croissante
     */
    @PostMapping("/proches/gps")
    public ResponseEntity<?> getVehiculesProchesGPS(@RequestBody Map<String, Object> request) {
        try {
            Double latitude = null;
            Double longitude = null;
            int rayonKm = 50;

            // Parser latitude
            if (request.containsKey("latitude")) {
                Object latObj = request.get("latitude");
                if (latObj instanceof Number) {
                    latitude = ((Number) latObj).doubleValue();
                } else if (latObj instanceof String) {
                    latitude = Double.parseDouble((String) latObj);
                }
            }

            // Parser longitude
            if (request.containsKey("longitude")) {
                Object lonObj = request.get("longitude");
                if (lonObj instanceof Number) {
                    longitude = ((Number) lonObj).doubleValue();
                } else if (lonObj instanceof String) {
                    longitude = Double.parseDouble((String) lonObj);
                }
            }

            // Parser rayon
            if (request.containsKey("rayonKm")) {
                Object rayonObj = request.get("rayonKm");
                if (rayonObj instanceof Number) {
                    rayonKm = ((Number) rayonObj).intValue();
                } else if (rayonObj instanceof String) {
                    rayonKm = Integer.parseInt((String) rayonObj);
                }
            }

            // Validation
            if (latitude == null || longitude == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Les coordonnées GPS (latitude et longitude) sont requises"));
            }

            List<Map<String, Object>> resultatsGPS = vehicleService.getVehiculesProchesGPS(latitude, longitude,
                    rayonKm);

            // Transformer les résultats pour l'API
            List<Map<String, Object>> vehiculesFormates = new ArrayList<>();
            for (Map<String, Object> entry : resultatsGPS) {
                Vehicle v = (Vehicle) entry.get("vehicule");
                Map<String, Object> vehiculeInfo = mapVehicleInfo(v);
                vehiculeInfo.put("distanceKm", entry.get("distanceKm"));
                vehiculesFormates.add(vehiculeInfo);
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "coordonneesRecherche", Map.of("latitude", latitude, "longitude", longitude),
                    "rayonKm", rayonKm,
                    "nombreResultats", vehiculesFormates.size(),
                    "vehicules", vehiculesFormates));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }
}
