package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller pour simuler les prix de location
 * Permet de tester la fonctionnalité US.A.7 (réduction longue durée)
 */
@RestController
@RequestMapping("/api/prix")
public class PrixController {

    private final RentalService rentalService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public PrixController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * POST /api/prix/simuler - Simuler le prix d'une location sans créer de contrat
     * 
     * Permet de tester les réductions longue durée (US.A.7) :
     * - 7-13 jours : -5% sur commission variable
     * - 14-29 jours : -10% sur commission variable
     * - 30+ jours : -15% sur commission variable
     * 
     * Body JSON attendu:
     * {
     *   "vehiculeId": "V001",
     *   "dateDebut": "2026-01-15",
     *   "dateFin": "2026-01-20",
     *   "assuranceNom": "AZA Classique"
     * }
     */
    @PostMapping("/simuler")
    public ResponseEntity<?> simulerPrix(@RequestBody Map<String, Object> request) {
        try {
            String vehiculeId = (String) request.get("vehiculeId");
            Date dateDebut = dateFormat.parse((String) request.get("dateDebut"));
            Date dateFin = dateFormat.parse((String) request.get("dateFin"));
            String assuranceNom = (String) request.get("assuranceNom");

            // Récupérer le véhicule
            Vehicle vehicule = rentalService.getTousLesVehicules().stream()
                    .filter(v -> v.getIdVehicule().equals(vehiculeId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Véhicule non trouvé: " + vehiculeId));

            // Récupérer l'assurance
            Assurance assurance = rentalService.getToutesLesAssurances().stream()
                    .filter(a -> a.getNom().equals(assuranceNom))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Assurance non trouvée: " + assuranceNom));

            // Calculer la durée
            long diffInMillies = Math.abs(dateFin.getTime() - dateDebut.getTime());
            long nbJours = (diffInMillies / (1000 * 60 * 60 * 24)) + 1;

            // Calcul du prix de base
            double prixJournalier = vehicule.getPrixVehiculeParJour();
            double montantAgent = prixJournalier * nbJours;
            double prixAssurance = assurance.calculerPrime(vehicule);

            // Commission de base
            double commissionPourcentage = 0.10; // 10%
            double commissionFixeParJour = 2.0;  // 2€

            // Application de la réduction longue durée (US.A.7)
            String reductionAppliquee = "Aucune";
            double tauxReduction = 0.0;
            
            if (nbJours >= 30) {
                commissionPourcentage = 0.10 * 0.85; // -15%
                reductionAppliquee = "-15% (≥30 jours)";
                tauxReduction = 0.15;
            } else if (nbJours >= 14) {
                commissionPourcentage = 0.10 * 0.90; // -10%
                reductionAppliquee = "-10% (≥14 jours)";
                tauxReduction = 0.10;
            } else if (nbJours >= 7) {
                commissionPourcentage = 0.10 * 0.95; // -5%
                reductionAppliquee = "-5% (≥7 jours)";
                tauxReduction = 0.05;
            }

            // Calcul des commissions
            double commissionVariable = montantAgent * commissionPourcentage;
            double commissionFixe = nbJours * commissionFixeParJour;
            double montantPlatforme = commissionVariable + commissionFixe;

            // Prix total
            double prixTotal = montantAgent + montantPlatforme + prixAssurance;

            // Calcul de l'économie
            double commissionSansReduction = montantAgent * 0.10;
            double economieAgent = commissionSansReduction - commissionVariable;

            // Réponse détaillée
            Map<String, Object> response = new LinkedHashMap<>();
            
            // Informations générales
            response.put("vehicule", Map.of(
                "id", vehicule.getIdVehicule(),
                "marque", vehicule.getMarqueVehicule(),
                "modele", vehicule.getModeleVehicule(),
                "prixJournalier", prixJournalier + "€"
            ));
            
            response.put("periode", Map.of(
                "dateDebut", request.get("dateDebut"),
                "dateFin", request.get("dateFin"),
                "nbJours", nbJours
            ));

            // Réduction longue durée (US.A.7)
            response.put("reductionLongueDuree", Map.of(
                "applicable", nbJours >= 7,
                "reduction", reductionAppliquee,
                "tauxReduction", String.format("%.0f%%", tauxReduction * 100),
                "commissionAvantReduction", "10%",
                "commissionApresReduction", String.format("%.1f%%", commissionPourcentage * 100)
            ));

            // Détail des montants
            response.put("detailPrix", Map.of(
                "montantAgent", String.format("%.2f€", montantAgent),
                "commissionVariable", String.format("%.2f€", commissionVariable),
                "commissionFixe", String.format("%.2f€", commissionFixe),
                "montantPlatforme", String.format("%.2f€", montantPlatforme),
                "prixAssurance", String.format("%.2f€", prixAssurance),
                "prixTotal", String.format("%.2f€", prixTotal)
            ));

            // Économie pour l'agent
            if (nbJours >= 7) {
                response.put("economieAgent", Map.of(
                    "montant", String.format("%.2f€", economieAgent),
                    "pourcentage", String.format("%.1f%%", (economieAgent / commissionSansReduction) * 100),
                    "message", "L'agent économise " + String.format("%.2f€", economieAgent) + " grâce à la réduction longue durée"
                ));
            }

            return ResponseEntity.ok(response);

        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Format de date invalide. Utilisez: yyyy-MM-dd"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/prix/baremes - Afficher les barèmes de réduction
     */
    @GetMapping("/baremes")
    public ResponseEntity<?> getBaremes() {
        return ResponseEntity.ok(Map.of(
            "titre", "Barème des réductions longue durée (US.A.7)",
            "description", "Réductions appliquées sur la commission variable de la plateforme",
            "baremes", new Object[] {
                Map.of(
                    "duree", "Moins de 7 jours",
                    "reduction", "0%",
                    "commissionVariable", "10%"
                ),
                Map.of(
                    "duree", "7 à 13 jours",
                    "reduction", "-5%",
                    "commissionVariable", "9.5%"
                ),
                Map.of(
                    "duree", "14 à 29 jours",
                    "reduction", "-10%",
                    "commissionVariable", "9%"
                ),
                Map.of(
                    "duree", "30 jours et plus",
                    "reduction", "-15%",
                    "commissionVariable", "8.5%"
                )
            },
            "note", "La commission fixe de 2€/jour reste inchangée"
        ));
    }
}
