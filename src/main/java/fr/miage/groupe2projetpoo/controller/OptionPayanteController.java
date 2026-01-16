package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.assurance.*;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller REST pour tester les options payantes via Hoppscotch
 */
@RestController
@RequestMapping("/api/options")
public class OptionPayanteController {

    private final UserService userService;

    public OptionPayanteController(UserService userService) {
        this.userService = userService;
    }

    // Méthode utilitaire pour récupérer un agent
    private Agent getAgent(String email) {
        if (email == null)
            return null;
        Optional<Utilisateur> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get() instanceof Agent) {
            return (Agent) userOpt.get();
        }
        return null;
    }

    /**
     * GET /api/options - Voir toutes les options d'un agent
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> voirOptions(@RequestParam String agentEmail) {
        Agent agent = getAgent(agentEmail);
        if (agent == null)
            return ResponseEntity.badRequest().body(Map.of("message", "Agent introuvable"));

        Map<String, Object> result = new HashMap<>();
        result.put("agentEmail", agent.getEmail());
        result.put("agentNom", agent.getNom() + " " + agent.getPrenom());

        List<Map<String, Object>> options = new ArrayList<>();
        for (OptionPayante opt : agent.getOptionsPayantes()) {
            Map<String, Object> optInfo = new HashMap<>();
            optInfo.put("type", opt.getClass().getSimpleName());
            optInfo.put("nom", opt.getNom());
            optInfo.put("tarifMensuel", opt.getTarifMensuel());
            optInfo.put("active", opt.isEstActive());
            options.add(optInfo);
        }

        result.put("options", options);
        result.put("factureMensuelle", agent.calculerFactureMensuelle());
        result.put("AcceptationManuelle", agent.aAcceptationManuelle());

        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/options/add - Ajouter une option payante
     * Body: { "agentEmail": "...", "typeOption": "MANUEL" | "ASSURANCE" |
     * "ENTRETIEN_PONCTUEL" | "ENTRETIEN_AUTO" | "PARKING" }
     * 
     * Pour PARKING, on peut optionnellement passer:
     * { "agentEmail": "...", "typeOption": "PARKING",
     * "parkingNom": "...", "parkingVille": "...", "parkingCodeAcces": "...",
     * "parkingProcedure": "...", "parkingInstructions": "...", "tarifMensuel": 15.0
     * }
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> ajouterOption(@RequestBody Map<String, Object> request) {
        String agentEmail = (String) request.get("agentEmail");
        String typeOption = (String) request.get("typeOption");

        Agent agent = getAgent(agentEmail);
        if (agent == null)
            return ResponseEntity.badRequest().body(Map.of("message", "Agent introuvable"));

        OptionPayante option = null;
        String message = "";

        switch (typeOption) {
            case "MANUEL":
                option = new OptionAcceptationManuelle();
                message = "Option Acceptation Manuelle ajoutée";
                break;
            case "ASSURANCE":
                // Création simplifiée pour le test
                Assurance assurancePerso = new Assurance(99, "Assurance Agent " + agent.getNom(), 12.0);
                option = new OptionAssurancePersonnalisee(assurancePerso);
                message = "Option Assurance Personnalisée ajoutée";
                break;
            case "ENTRETIEN_PONCTUEL":
                option = new OptionEntretien(false);
                message = "Option Entretien Ponctuel ajoutée";
                break;
            case "ENTRETIEN_AUTO":
                option = new OptionEntretien(true);
                message = "Option Entretien Automatique ajoutée";
                break;
            case "PARKING":
                double tarifMensuel = request.containsKey("tarifMensuel")
                        ? ((Number) request.get("tarifMensuel")).doubleValue()
                        : 15.0;

                option = new OptionParking("Abonnement Parking", tarifMensuel, 24, 30, 8.0, null);
                message = "Option Parking activée - Le loueur pourra choisir un parking lors de la location";
                break;
            default:
                return ResponseEntity.badRequest().body(Map.of("message",
                        "Type d'option invalide (MANUEL, ASSURANCE, ENTRETIEN_PONCTUEL, ENTRETIEN_AUTO, PARKING)"));
        }

        if (option != null) {
            agent.ajouterOption(option);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", message,
                    "option", Map.of(
                            "nom", option.getNom(),
                            "tarifMensuel", option.getTarifMensuel()),
                    "nouvelleFacture", agent.calculerFactureMensuelle()));
        }

        return ResponseEntity.badRequest().body(Map.of("message", "Erreur lors de la création de l'option"));
    }

    /**
     * DELETE /api/options/reset - Supprimer toutes les options (pour recommencer
     * les tests)
     */
    @DeleteMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetOptions(@RequestParam String agentEmail) {
        Agent agent = getAgent(agentEmail);
        if (agent == null)
            return ResponseEntity.badRequest().body(Map.of("message", "Agent introuvable"));

        agent.getOptionsPayantes().clear();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Toutes les options ont été supprimées pour " + agentEmail,
                "factureMensuelle", agent.calculerFactureMensuelle()));
    }

    /**
     * GET /api/options/facture - Voir la facture mensuelle détaillée
     */
    @GetMapping("/facture")
    public ResponseEntity<Map<String, Object>> voirFacture(@RequestParam String agentEmail) {
        Agent agent = getAgent(agentEmail);
        if (agent == null)
            return ResponseEntity.badRequest().body(Map.of("message", "Agent introuvable"));

        List<Map<String, Object>> details = new ArrayList<>();
        double total = 0;

        for (OptionPayante opt : agent.getOptionsPayantes()) {
            double cout = opt.calculerCoutMensuel();
            details.add(Map.of(
                    "option", opt.getNom(),
                    "tarif", opt.getTarifMensuel(),
                    "active", opt.isEstActive(),
                    "cout", cout));
            total += cout;
        }

        return ResponseEntity.ok(Map.of(
                "agentEmail", agent.getEmail(),
                "nombreOptions", agent.getOptionsPayantes().size(),
                "details", details,
                "totalFacture", total));
    }

    /**
     * GET /api/options/catalogue - Voir toutes les options payantes disponibles
     */
    @GetMapping("/catalogue")
    public ResponseEntity<Map<String, Object>> voirCatalogue() {
        List<Map<String, Object>> catalogue = new ArrayList<>();
        
        catalogue.add(Map.of(
            "type", "MANUEL",
            "nom", "Acceptation Manuelle",
            "description", "Valider manuellement chaque contrat (délai 6h)",
            "tarifMensuel", 10.0
        ));
        
        catalogue.add(Map.of(
            "type", "ASSURANCE",
            "nom", "Assurance Personnalisée",
            "description", "Proposer votre propre assurance aux loueurs",
            "tarifMensuel", 20.0
        ));
        
        catalogue.add(Map.of(
            "type", "ENTRETIEN_PONCTUEL",
            "nom", "Entretien Ponctuel",
            "description", "Service d'entretien sur demande",
            "tarifMensuel", 5.0
        ));
        
        catalogue.add(Map.of(
            "type", "ENTRETIEN_AUTO",
            "nom", "Entretien Automatique",
            "description", "Entretien automatique après chaque location",
            "tarifMensuel", 15.0
        ));
        
        catalogue.add(Map.of(
            "type", "PARKING",
            "nom", "Option Parking Vienci",
            "description", "Accès aux parkings partenaires pour dépose différente",
            "tarifMensuel", 15.0
        ));
        
        return ResponseEntity.ok(Map.of(
            "message", "Catalogue des options payantes disponibles",
            "nombreOptions", catalogue.size(),
            "options", catalogue
        ));
    }
}
