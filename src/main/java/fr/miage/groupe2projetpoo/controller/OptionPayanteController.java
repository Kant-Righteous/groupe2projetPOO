package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.assurance.*;
import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.AgentParticulier;
import fr.miage.groupe2projetpoo.entity.utilisateur.AgentProfessionnel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller REST pour tester les options payantes via Hoppscotch
 */
@RestController
@RequestMapping("/api/options-test")
public class OptionPayanteController {

    // Agent de test en mémoire
    private Agent agentTest;

    public OptionPayanteController() {
        // Créer un agent professionnel de test avec nomEntreprise et siret
        agentTest = new AgentProfessionnel("Dupont", "Jean", "pass123", "agent@test.com", "0601020304", "Entreprise Test", "12345678901234");
    }

    /**
     * GET /api/options-test - Voir toutes les options de l'agent de test
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> voirOptions() {
        Map<String, Object> result = new HashMap<>();
        result.put("agentEmail", agentTest.getEmail());
        result.put("agentNom", agentTest.getNom() + " " + agentTest.getPrenom());
        
        List<Map<String, Object>> options = new ArrayList<>();
        for (OptionPayante opt : agentTest.getOptionsPayantes()) {
            Map<String, Object> optInfo = new HashMap<>();
            optInfo.put("type", opt.getClass().getSimpleName());
            optInfo.put("nom", opt.getNom());
            optInfo.put("tarifMensuel", opt.getTarifMensuel());
            optInfo.put("active", opt.isEstActive());
            options.add(optInfo);
        }
        
        result.put("options", options);
        result.put("factureMensuelle", agentTest.calculerFactureMensuelle());
        result.put("aAcceptationManuelle", agentTest.aAcceptationManuelle());
        
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/options-test/acceptation-manuelle - Ajouter l'option Acceptation Manuelle (10€/mois)
     */
    @PostMapping("/acceptation-manuelle")
    public ResponseEntity<Map<String, Object>> ajouterAcceptationManuelle() {
        OptionAcceptationManuelle option = new OptionAcceptationManuelle();
        agentTest.ajouterOption(option);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Option Acceptation Manuelle ajoutée",
            "option", Map.of(
                "nom", option.getNom(),
                "tarifMensuel", option.getTarifMensuel(),
                "delaiHeures", OptionAcceptationManuelle.getDelaiEnHeures()
            ),
            "nouvelleFacture", agentTest.calculerFactureMensuelle()
        ));
    }

    /**
     * POST /api/options-test/assurance-personnalisee - Ajouter l'option Assurance Personnalisée (15€/mois)
     */
    @PostMapping("/assurance-personnalisee")
    public ResponseEntity<Map<String, Object>> ajouterAssurancePersonnalisee() {
        // Créer une assurance personnalisée
        Assurance assurancePerso = new Assurance(99, "Assurance Agent Dupont", 12.0);
        OptionAssurancePersonnalisee option = new OptionAssurancePersonnalisee(assurancePerso);
        agentTest.ajouterOption(option);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Option Assurance Personnalisée ajoutée",
            "option", Map.of(
                "nom", option.getNom(),
                "tarifMensuel", option.getTarifMensuel(),
                "assuranceAgent", assurancePerso.getNom()
            ),
            "nouvelleFacture", agentTest.calculerFactureMensuelle()
        ));
    }

    /**
     * POST /api/options-test/entretien-ponctuel - Ajouter l'option Entretien Ponctuel (5€/mois)
     */
    @PostMapping("/entretien-ponctuel")
    public ResponseEntity<Map<String, Object>> ajouterEntretienPonctuel() {
        OptionEntretien option = new OptionEntretien(false);  // ponctuel
        agentTest.ajouterOption(option);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Option Entretien Ponctuel ajoutée",
            "option", Map.of(
                "nom", option.getNom(),
                "tarifMensuel", option.getTarifMensuel(),
                "automatique", option.isAutomatique()
            ),
            "nouvelleFacture", agentTest.calculerFactureMensuelle()
        ));
    }

    /**
     * POST /api/options-test/entretien-automatique - Ajouter l'option Entretien Automatique (20€/mois)
     */
    @PostMapping("/entretien-automatique")
    public ResponseEntity<Map<String, Object>> ajouterEntretienAutomatique() {
        OptionEntretien option = new OptionEntretien(true);  // automatique
        agentTest.ajouterOption(option);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Option Entretien Automatique ajoutée",
            "option", Map.of(
                "nom", option.getNom(),
                "tarifMensuel", option.getTarifMensuel(),
                "automatique", option.isAutomatique()
            ),
            "nouvelleFacture", agentTest.calculerFactureMensuelle()
        ));
    }

    /**
     * DELETE /api/options-test/reset - Supprimer toutes les options (pour recommencer les tests)
     */
    @DeleteMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetOptions() {
        agentTest.getOptionsPayantes().clear();
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Toutes les options ont été supprimées",
            "factureMensuelle", agentTest.calculerFactureMensuelle()
        ));
    }

    /**
     * GET /api/options-test/facture - Voir la facture mensuelle détaillée
     */
    @GetMapping("/facture")
    public ResponseEntity<Map<String, Object>> voirFacture() {
        List<Map<String, Object>> details = new ArrayList<>();
        double total = 0;
        
        for (OptionPayante opt : agentTest.getOptionsPayantes()) {
            double cout = opt.calculerCoutMensuel();
            details.add(Map.of(
                "option", opt.getNom(),
                "tarif", opt.getTarifMensuel(),
                "active", opt.isEstActive(),
                "cout", cout
            ));
            total += cout;
        }
        
        return ResponseEntity.ok(Map.of(
            "agentEmail", agentTest.getEmail(),
            "nombreOptions", agentTest.getOptionsPayantes().size(),
            "details", details,
            "totalFacture", total
        ));
    }
}
