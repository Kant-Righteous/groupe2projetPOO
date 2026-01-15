package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.maintenance.ControleTechnique;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

/**
 * Controller pour gérer les contrôles techniques des véhicules (US.A.8)
 */
@RestController
@RequestMapping("/api/controle-technique")
public class ControleTechniqueController {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public ControleTechniqueController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * POST /api/controle-technique/{vehiculeId} - Enregistrer un contrôle technique
     * 
     * Body JSON:
     * {
     *   "datePassage": "2025-12-15",
     *   "dateExpiration": "2027-12-15",
     *   "resultat": "Favorable",
     *   "observations": "Aucune anomalie détectée"
     * }
     */
    @PostMapping("/{vehiculeId}")
    public ResponseEntity<?> enregistrerControleTechnique(
            @PathVariable String vehiculeId,
            @RequestBody Map<String, String> request) {
        try {
            // 1. Récupérer le véhicule
            Optional<Vehicle> vehiculeOpt = vehicleRepository.findById(vehiculeId);
            if (!vehiculeOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Véhicule non trouvé avec l'ID: " + vehiculeId));
            }
            Vehicle vehicule = vehiculeOpt.get();

            // 2. Créer le contrôle technique
            ControleTechnique ct = new ControleTechnique();
            ct.setDatePassage(LocalDate.parse(request.get("datePassage")));
            ct.setDateExpiration(LocalDate.parse(request.get("dateExpiration")));
            ct.setResultat(request.get("resultat"));
            
            if (request.containsKey("observations")) {
                ct.setObservations(request.get("observations"));
            }

            // 3. Associer au véhicule
            vehicule.setControleTechnique(ct);

            // 4. Sauvegarder dans le repository
            vehicleRepository.save(vehicule);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Contrôle technique enregistré avec succès",
                    "vehiculeId", vehiculeId,
                    "dateExpiration", ct.getDateExpiration().toString(),
                    "estValide", !ct.estExpire()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Erreur lors de l'enregistrement: " + e.getMessage()));
        }
    }

    /**
     * GET /api/controle-technique/{vehiculeId} - Consulter le contrôle technique d'un véhicule
     */
    @GetMapping("/{vehiculeId}")
    public ResponseEntity<?> getControleTechnique(@PathVariable String vehiculeId) {
        try {
            Optional<Vehicle> vehiculeOpt = vehicleRepository.findById(vehiculeId);
            if (!vehiculeOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Véhicule non trouvé avec l'ID: " + vehiculeId));
            }

            Vehicle vehicule = vehiculeOpt.get();
            ControleTechnique ct = vehicule.getControleTechnique();

            if (ct == null) {
                return ResponseEntity.ok(Map.of(
                        "vehiculeId", vehiculeId,
                        "message", "Aucun contrôle technique enregistré",
                        "estValide", false
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "vehiculeId", vehiculeId,
                    "datePassage", ct.getDatePassage().toString(),
                    "dateExpiration", ct.getDateExpiration().toString(),
                    "resultat", ct.getResultat(),
                    "observations", ct.getObservations() != null ? ct.getObservations() : "",
                    "estExpire", ct.estExpire(),
                    "estValide", !ct.estExpire()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Erreur: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/controle-technique/{vehiculeId} - Supprimer le CT (pour tests)
     */
    @DeleteMapping("/{vehiculeId}")
    public ResponseEntity<?> supprimerControleTechnique(@PathVariable String vehiculeId) {
        try {
            Optional<Vehicle> vehiculeOpt = vehicleRepository.findById(vehiculeId);
            if (!vehiculeOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Véhicule non trouvé avec l'ID: " + vehiculeId));
            }

            Vehicle vehicule = vehiculeOpt.get();
            vehicule.setControleTechnique(null);
            vehicleRepository.save(vehicule); // Persister la modification

            return ResponseEntity.ok(Map.of(
                    "message", "Contrôle technique supprimé",
                    "vehiculeId", vehiculeId
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Erreur: " + e.getMessage()));
        }
    }
}
