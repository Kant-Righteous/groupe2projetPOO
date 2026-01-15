package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.miage.groupe2projetpoo.entity.location.StatutLocation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // ===== ENDPOINTS POUR LES CONTRATS =====

    /**
     * GET /api/rentals - Récupérer tous les contrats
     */
    @GetMapping
    public ResponseEntity<List<RentalContract>> getAllContrats() {
        return ResponseEntity.ok(rentalService.getTousLesContrats());
    }

    /**
     * GET /api/rentals/{id} - Récupérer un contrat par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getContratById(@PathVariable int id) {
        return rentalService.getContratById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/rentals - Créer un nouveau contrat
     * 
     * Body JSON attendu:
     * {
     * "loueurEmail": "jean@email.com",
     * "vehiculeId": "V001",
     * "dateDebut": "2026-01-15",
     * "dateFin": "2026-01-20",
     * "lieuPrise": "Paris",
     * "lieuDepose": "Paris",
     * "assuranceNom": "AZA Classique"
     * }
     */
    @PostMapping
    public ResponseEntity<?> creerContrat(@RequestBody Map<String, Object> request) {
        try {
            String loueurEmail = (String) request.get("loueurEmail");
            String vehiculeId = (String) request.get("vehiculeId");
            Date dateDebut = dateFormat.parse((String) request.get("dateDebut"));
            Date dateFin = dateFormat.parse((String) request.get("dateFin"));
            String lieuPrise = (String) request.get("lieuPrise");
            String lieuDepose = (String) request.get("lieuDepose");
            String assuranceNom = (String) request.get("assuranceNom");

            // Nouveau paramètre optionnel
            boolean avecOptionParking = false;
            if (request.containsKey("avecOptionParking")) {
                Object optVal = request.get("avecOptionParking");
                if (optVal instanceof Boolean) {
                    avecOptionParking = (Boolean) optVal;
                } else if (optVal instanceof String) {
                    avecOptionParking = Boolean.parseBoolean((String) optVal);
                }
            }

            RentalContract contrat = rentalService.creerContrat(
                    loueurEmail, vehiculeId, dateDebut, dateFin, lieuPrise, lieuDepose, assuranceNom,
                    avecOptionParking);

            return ResponseEntity.status(HttpStatus.CREATED).body(contrat);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Format de date invalide. Utilisez: yyyy-MM-dd");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * POST /api/rentals/{id}/sign - Signer un contrat
     */
    @PostMapping("/{id}/sign")
    public ResponseEntity<?> signerContrat(@PathVariable int id) {
        try {
            RentalContract contrat = rentalService.signerContrat(id);
            return ResponseEntity.ok(contrat);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * PUT /api/rentals/{id}/termine - Terminer une location (déclenche l'entretien
     * auto si option active)
     */
    @PutMapping("/{id}/termine")
    public ResponseEntity<?> terminerContrat(@PathVariable int id) {
        try {
            RentalContract contrat = rentalService.terminerContrat(id);
            return ResponseEntity.ok(contrat);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * DELETE /api/rentals/{id} - Supprimer un contrat
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerContrat(@PathVariable int id) {
        rentalService.supprimerContrat(id);
        return ResponseEntity.ok("Contrat supprimé");
    }

    /**
     * GET /api/rentals/loueur/{email} - Récupérer les contrats d'un loueur par
     * email
     */
    @GetMapping("/loueur/{email}")
    public ResponseEntity<List<RentalContract>> getContratsParLoueur(@PathVariable String email) {
        return ResponseEntity.ok(rentalService.getContratsParLoueur(email));
    }

    // ===== ENDPOINTS UTILITAIRES (pour voir les données de test) =====

    /**
     * GET /api/rentals/loueurs - Liste des loueurs disponibles
     */
    @GetMapping("/loueurs")
    public ResponseEntity<List<Loueur>> getLoueurs() {
        return ResponseEntity.ok(rentalService.getTousLesLoueurs());
    }

    /**
     * GET /api/rentals/vehicules - Liste des véhicules disponibles
     */
    @GetMapping("/vehicules")
    public ResponseEntity<List<Vehicle>> getVehicules() {
        return ResponseEntity.ok(rentalService.getTousLesVehicules());
    }

    /**
     * GET /api/rentals/assurances - Liste des assurances disponibles
     */
    @GetMapping("/assurances")
    public ResponseEntity<List<Assurance>> getAssurances() {
        return ResponseEntity.ok(rentalService.getToutesLesAssurances());
    }

    // ===== ENDPOINTS POUR L'ACCEPTATION MANUELLE =====

    /**
     * POST /api/rentals/{id}/accept - L'agent accepte manuellement un contrat
     */
    @PostMapping("/{id}/accept")
    public ResponseEntity<?> accepterContrat(@PathVariable int id) {
        try {
            RentalContract contrat = rentalService.accepterContratParAgent(id);
            return ResponseEntity.ok(contrat);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/rentals/pending - Contrats en attente d'acceptation par l'agent
     */
    @GetMapping("/pending")
    public ResponseEntity<List<RentalContract>> getContratsEnAttente() {
        return ResponseEntity.ok(rentalService.getContratsEnAttente());
    }

    // ===== ENDPOINTS POUR LE KILOMÉTRAGE (US.L.10) =====

    /**
     * POST /api/rentals/{id}/kilometrage-debut - Renseigner le kilométrage à la
     * prise
     * 
     * Body JSON:
     * {
     * "kilometrage": 45230,
     * "photoNom": "photo_km_debut_contrat1_20260114.jpg"
     * }
     */
    @PostMapping("/{id}/kilometrage-debut")
    public ResponseEntity<?> renseignerKilometrageDebut(
            @PathVariable int id,
            @RequestBody Map<String, Object> request) {
        try {
            RentalContract contrat = rentalService.getContratById(id)
                    .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));

            int km = ((Number) request.get("kilometrage")).intValue();
            String photoNom = (String) request.get("photoNom");

            contrat.renseignerKilometrageDebut(km, photoNom);
            rentalService.getTousLesContrats(); // Force save (workaround for InMemory)

            return ResponseEntity.ok(Map.of(
                    "message", "Kilométrage départ enregistré avec succès",
                    "kilometrage", km,
                    "photo", photoNom,
                    "date", contrat.getDateRenseignementDebut()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/rentals/{id}/kilometrage-fin - Renseigner le kilométrage au retour
     * 
     * Body JSON:
     * {
     * "kilometrage": 45580,
     * "photoNom": "photo_km_fin_contrat1_20260120.jpg"
     * }
     */
    @PostMapping("/{id}/kilometrage-fin")
    public ResponseEntity<?> renseignerKilometrageFin(
            @PathVariable int id,
            @RequestBody Map<String, Object> request) {
        try {
            RentalContract contrat = rentalService.getContratById(id)
                    .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));

            int km = ((Number) request.get("kilometrage")).intValue();
            String photoNom = (String) request.get("photoNom");

            contrat.renseignerKilometrageFin(km, photoNom);
            rentalService.getTousLesContrats(); // Force save

            Integer distance = contrat.calculerDistanceParcourue();

            return ResponseEntity.ok(Map.of(
                    "message", "Kilométrage retour enregistré avec succès",
                    "kilometrageDebut", contrat.getKilometrageDebut(),
                    "kilometrageFin", km,
                    "distanceParcourue", distance != null ? distance : 0,
                    "photo", photoNom,
                    "date", contrat.getDateRenseignementFin()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/rentals/{id}/kilometrage - Consulter les informations kilométriques
     * d'un contrat
     */
    @GetMapping("/{id}/kilometrage")
    public ResponseEntity<?> getKilometrage(@PathVariable int id) {
        return rentalService.getContratById(id)
                .map(contrat -> ResponseEntity.ok(Map.of(
                        "contratId", id,
                        "kilometrageDebut",
                        contrat.getKilometrageDebut() != null ? contrat.getKilometrageDebut() : "Non renseigné",
                        "photoDebut",
                        contrat.getPhotoKilometrageDebut() != null ? contrat.getPhotoKilometrageDebut()
                                : "Non renseignée",
                        "kilometrageFin",
                        contrat.getKilometrageFin() != null ? contrat.getKilometrageFin() : "Non renseigné",
                        "photoFin",
                        contrat.getPhotoKilometrageFin() != null ? contrat.getPhotoKilometrageFin() : "Non renseignée",
                        "distanceParcourue",
                        contrat.calculerDistanceParcourue() != null ? contrat.calculerDistanceParcourue() + " km"
                                : "Non disponible")))
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== ENDPOINT POUR CHANGER LE STATUT DU CONTRAT =====

    /**
     * PUT /api/rentals/{id}/status - Mettre à jour le statut d'un contrat
     * 
     * Body JSON:
     * {
     * "statutLocation": "TERMINEE"
     * }
     * 
     * Valeurs possibles: EN_ATTENTE_SIGNATURE, SIGNE, EN_COURS, TERMINEE, ANNULEE
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateContratStatus(
            @PathVariable int id,
            @RequestBody Map<String, String> request) {
        try {
            String statutStr = request.get("statutLocation");
            if (statutStr == null || statutStr.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Le champ 'statutLocation' est requis",
                        "valeursAcceptees", "EN_ATTENTE_SIGNATURE, SIGNE, EN_COURS, TERMINEE, ANNULEE"));
            }

            StatutLocation nouveauStatut;
            try {
                nouveauStatut = StatutLocation.valueOf(statutStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Statut invalide: " + statutStr,
                        "valeursAcceptees", "EN_ATTENTE_SIGNATURE, SIGNE, EN_COURS, TERMINEE, ANNULEE"));
            }

            RentalContract contrat = rentalService.updateContratStatus(id, nouveauStatut);

            return ResponseEntity.ok(Map.of(
                    "message", "Statut du contrat mis à jour avec succès",
                    "contratId", id,
                    "nouveauStatut", contrat.getStatutLocation().toString(),
                    "contrat", contrat));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ===== ENDPOINT POUR LES INFORMATIONS PARKING VIENCI =====

    /**
     * GET /api/rentals/{id}/parking-info - Obtenir les informations d'accès au
     * parking Vienci
     * 
     * Ce endpoint permet au loueur de recevoir les informations d'accès au parking
     * partenaire (adresse, code, procédure) lors de la validation du contrat.
     */
    @GetMapping("/{id}/parking-info")
    public ResponseEntity<?> getParkingInfo(@PathVariable int id) {
        return rentalService.getContratById(id)
                .map(contrat -> {
                    if (!contrat.isOptionParkingSelectionnee()) {
                        return ResponseEntity.ok(Map.of(
                                "contratId", id,
                                "parkingDisponible", false,
                                "message", "Aucun parking partenaire associé à ce contrat"));
                    }

                    if (!contrat.hasParkingInfo()) {
                        return ResponseEntity.ok(Map.of(
                                "contratId", id,
                                "parkingDisponible", false,
                                "message", "Les informations du parking ne sont pas encore disponibles"));
                    }

                    return ResponseEntity.ok(Map.of(
                            "contratId", id,
                            "parkingDisponible", true,
                            "parking", Map.of(
                                    "nom", contrat.getParkingNom() != null ? contrat.getParkingNom() : "",
                                    "adresse", contrat.getParkingAdresse() != null ? contrat.getParkingAdresse() : "",
                                    "ville", contrat.getParkingVille() != null ? contrat.getParkingVille() : "",
                                    "codeAcces",
                                    contrat.getParkingCodeAcces() != null ? contrat.getParkingCodeAcces() : "",
                                    "procedureAcces",
                                    contrat.getParkingProcedureAcces() != null ? contrat.getParkingProcedureAcces()
                                            : "",
                                    "instructionsSpeciales",
                                    contrat.getParkingInstructionsSpeciales() != null
                                            ? contrat.getParkingInstructionsSpeciales()
                                            : ""),
                            "message", "Veuillez déposer le véhicule à ce parking pour bénéficier de la réduction"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
