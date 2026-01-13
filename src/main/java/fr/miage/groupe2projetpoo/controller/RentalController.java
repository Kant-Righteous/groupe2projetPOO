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
     *   "loueurEmail": "jean@email.com",
     *   "vehiculeId": "V001",
     *   "dateDebut": "2026-01-15",
     *   "dateFin": "2026-01-20",
     *   "lieuPrise": "Paris",
     *   "lieuDepose": "Paris",
     *   "assuranceNom": "AZA Classique"
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

            RentalContract contrat = rentalService.creerContrat(
                    loueurEmail, vehiculeId, dateDebut, dateFin, lieuPrise, lieuDepose, assuranceNom
            );

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
     * DELETE /api/rentals/{id} - Supprimer un contrat
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerContrat(@PathVariable int id) {
        rentalService.supprimerContrat(id);
        return ResponseEntity.ok("Contrat supprimé");
    }

    /**
     * GET /api/rentals/loueur/{email} - Récupérer les contrats d'un loueur par email
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
}

