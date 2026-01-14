package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.assurance.*;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.utilisateur.Role;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.service.UserService;
import fr.miage.groupe2projetpoo.service.RentalService;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;
import fr.miage.groupe2projetpoo.repository.VehicleRepository;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
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
    private final RentalService rentalService;
    private final VehicleRepository vehicleRepository;

    public OptionPayanteController(UserService userService, RentalService rentalService, VehicleRepository vehicleRepository) {
        this.userService = userService;
        this.rentalService = rentalService;
        this.vehicleRepository = vehicleRepository;
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
     * "ENTRETIEN_PONCTUEL" | "ENTRETIEN_AUTO" }
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> ajouterOption(@RequestBody Map<String, String> request) {
        String agentEmail = request.get("agentEmail");
        String typeOption = request.get("typeOption");

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
            default:
                return ResponseEntity.badRequest().body(Map.of("message",
                        "Type d'option invalide (MANUEL, ASSURANCE, ENTRETIEN_PONCTUEL, ENTRETIEN_AUTO)"));
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
     * POST /api/options/test-contrat-assurance - Créer un contrat de test pour
     * vérifier l'exception d'assurance personnalisée
     */
    @PostMapping("/test-contrat-assurance")
    public ResponseEntity<Map<String, Object>> testContratAssurance(@RequestBody Map<String, String> request) {
        String agentEmail = request.get("agentEmail");

        // 1. Récupérer l'agent
        Agent agent = getAgent(agentEmail);
        if (agent == null)
            return ResponseEntity.badRequest().body(Map.of("message", "Agent introuvable"));

        // 2. S'assurer que l'agent a un véhicule
        Vehicle vehicule = null;
        if (agent.getVehicleList().isEmpty()) {
            // Créer un véhicule factice
            vehicule = new Voiture("V_TEST_" + System.currentTimeMillis(), "Peugeot", "Noir", "208", "Paris", 30.0,
                    agent.getEmail(), false);
            vehicleRepository.save(vehicule);
            agent.addVehicle(vehicule);
            // Note: userService.updateUser(agent) n'existe pas, mais en mémoire c'est bon
        } else {
            vehicule = agent.getVehicleList().get(0);
        }

        // 3. Récupérer ou créer un loueur de test
        String loueurEmail = "loueur.test@demo.com";
        Optional<Utilisateur> loueurOpt = userService.findByEmail(loueurEmail);
        if (loueurOpt.isEmpty()) {
            userService.register("Loueur", "Test", "pass123", loueurEmail, "0600000000", Role.LOUEUR);
        }

        try {
            // 4. Créer un contrat avec "Assurance Basic" demandée
            // Si l'agent a l'option perso, c'est celle-ci qui devrait être appliquée par le
            // RentalService/RentalContract
            RentalContract contrat = rentalService.creerContrat(
                    loueurEmail,
                    vehicule.getIdVehicule(),
                    new Date(),
                    new Date(System.currentTimeMillis() + 86400000 * 5), // +5 jours
                    "Paris", "Lyon",
                    "Assurance Basic");

            // 5. Renvoyer les résultats
            boolean aOption = agent.aAssurancePersonnalisee();
            String assuranceNom = contrat.getAssurance().getNom();
            boolean succesOp = aOption ? !assuranceNom.equals("Assurance Basic")
                    : assuranceNom.equals("Assurance Basic");

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Test création contrat terminé");
            response.put("agentEmail", agent.getEmail());
            response.put("agentAOption", aOption);
            response.put("assuranceDemandee", "Assurance Basic");
            response.put("assuranceObtenue", assuranceNom);
            response.put("testReussi", succesOp);
            response.put("coutTotal", contrat.getPrixTotal());

            if (aOption) {
                response.put("infoOption", "L'agent a l'option perso, donc 'Assurance Basic' a dû être remplacée par '"
                        + agent.getAssurancePersonnalisee().getNom() + "'");
            } else {
                response.put("infoOption",
                        "L'agent n'a pas l'option, donc le loueur a bien eu l'assurance demandée.");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
