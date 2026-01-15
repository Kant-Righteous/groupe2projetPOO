package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.assurance.OptionEntretien;
import fr.miage.groupe2projetpoo.entity.entretien.MaintenanceIntervention;
import fr.miage.groupe2projetpoo.entity.entretien.MaintenancePrice;
import fr.miage.groupe2projetpoo.entity.entretien.MaintenanceStatus;
import fr.miage.groupe2projetpoo.entity.utilisateur.MaintenanceCompany;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.vehicule.TypeVehicule;
import fr.miage.groupe2projetpoo.entity.maintenance.ControleTechnique;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.AgentParticulier;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import fr.miage.groupe2projetpoo.service.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final UserRepository userRepository;

    public MaintenanceController(MaintenanceService maintenanceService, UserRepository userRepository) {
        this.maintenanceService = maintenanceService;
        this.userRepository = userRepository;
    }

    @PostMapping("/companies/register")
    public ResponseEntity<?> registerCompany(@RequestBody Map<String, String> request) {
        try {
            MaintenanceCompany company = new MaintenanceCompany(
                    request.get("nom"),
                    request.get("prenom"),
                    request.get("password"),
                    request.get("email"),
                    request.get("tel"));
            MaintenanceCompany registered = maintenanceService.registerCompany(company);
            return ResponseEntity
                    .ok(Map.of("success", true, "message", "Entreprise enregistrée", "email", registered.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/companies/{email:.+}/refere")
    public ResponseEntity<?> setCompanyRefere(@PathVariable String email, @RequestParam(required = false, defaultValue = "true") boolean refere) {
        try {
            maintenanceService.setCompanyRefere(email, refere);
            return ResponseEntity.ok(Map.of("success", true, "message", "Statut référencé mis à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/companies/{email:.+}/prices")
    public ResponseEntity<?> addPrice(@PathVariable String email, @RequestBody Map<String, Object> request) {
        try {
            MaintenancePrice price = new MaintenancePrice(
                    TypeVehicule.valueOf(((String) request.get("typeVehicule")).toUpperCase()),
                    (String) request.get("modele"),
                    Double.parseDouble(request.get("prix").toString()));
            maintenanceService.addPrice(email, price);
            return ResponseEntity.ok(Map.of("success", true, "message", "Tarif ajouté au catalogue"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/companies/{email:.+}/prices/import")
    public ResponseEntity<?> importPrices(@PathVariable String email,
            @RequestBody List<Map<String, Object>> pricesRequest) {
        try {
            List<MaintenancePrice> prices = pricesRequest.stream().map(req -> new MaintenancePrice(
                    TypeVehicule.valueOf(((String) req.get("typeVehicule")).toUpperCase()),
                    (String) req.get("modele"),
                    Double.parseDouble(req.get("prix").toString()))).toList();
            maintenanceService.importPrices(email, prices);
            return ResponseEntity
                    .ok(Map.of("success", true, "message", prices.size() + " tarifs importés avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/companies/available")
    public ResponseEntity<?> getAvailableCompanies() {
        return ResponseEntity.ok(maintenanceService.getAvailableCompanies());
    }

    @GetMapping("/companies/{email:.+}")
    public ResponseEntity<?> getCompanyByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(maintenanceService.getCompanyByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestMaintenance(@RequestBody Map<String, String> request) {
        try {
            MaintenanceIntervention intervention = maintenanceService.requestMaintenance(
                    request.get("agentEmail"),
                    request.get("vehicleId"),
                    request.get("companyEmail"),
                    LocalDate.parse(request.get("date")));
            return ResponseEntity.ok(Map.of("success", true, "message", "Demande d'entretien créée", "interventionId",
                    intervention.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/agent/{email:.+}/preferred-company")
    public ResponseEntity<?> setPreferredCompany(@PathVariable String email, @RequestBody Map<String, String> request) {
        try {
            String companyEmail = request.get("companyEmail");
            Utilisateur user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Agent introuvable"));
            if (!(user instanceof Agent)) throw new IllegalArgumentException("L'utilisateur n'est pas un agent");
            
            MaintenanceCompany company = maintenanceService.getCompanyByEmail(companyEmail);
            ((Agent) user).setEntrepriseEntretienPreferee(company);
            
            return ResponseEntity.ok(Map.of("success", true, "message", "Entreprise préférée mise à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/agent/{email:.+}/subscribe-option")
    public ResponseEntity<?> subscribeOption(@PathVariable String email, @RequestBody Map<String, Boolean> request) {
        try {
            boolean automatique = request.getOrDefault("automatique", false);
            Utilisateur user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Agent introuvable"));
            if (!(user instanceof Agent)) throw new IllegalArgumentException("L'utilisateur n'est pas un agent");
            
            Agent agent = (Agent) user;
            if (!agent.aOptionActive(OptionEntretien.class)) {
                agent.ajouterOption(new OptionEntretien(automatique));
            } else {
                OptionEntretien opt = agent.getOption(OptionEntretien.class);
                if (automatique) opt.activerModeAutomatique();
                else opt.activerModePonctuel();
            }
            
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "Abonnement à l'option " + (automatique ? "Automatique" : "Ponctuelle") + " réussi",
                "facture_mensuelle", agent.calculerFactureMensuelle()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/interventions/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Map<String, String> request) {
        try {
            MaintenanceStatus status = MaintenanceStatus.valueOf(request.get("statut").toUpperCase());
            maintenanceService.updateInterventionStatus(id, status);
            return ResponseEntity.ok(Map.of("success", true, "message", "Statut de l'intervention mis à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/agent/{email:.+}/history")
    public ResponseEntity<?> getAgentHistory(@PathVariable String email) {
        try {
            List<MaintenanceIntervention> history = maintenanceService.getInterventionsByAgent(email);
            return ResponseEntity.ok(transformInterventions(history));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/vehicle/{id}/history")
    public ResponseEntity<?> getVehicleHistory(@PathVariable String id) {
        try {
            List<MaintenanceIntervention> history = maintenanceService.getHistoryByVehicle(id);
            return ResponseEntity.ok(transformInterventions(history));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/rappels-test")
    public Map<String, Object> testerRappels() {
        // 1. Création d'un Agent test
        Agent agent = new AgentParticulier("Diallo", "Mamadou", "pass", "mamadou@test.com", "0600000000");

        // 2. Création de véhicules
        // Véhicule A : CT OK (fait il y a 6 mois)
        Vehicle v1 = new Voiture("V1", "Renault", "Rouge", "Clio", "Paris", 30.0, "mamadou@test.com", false);
        v1.setControleTechnique(new ControleTechnique(LocalDate.now().minusMonths(6), true, "Centre Auto", "RAS"));
        v1.setKilometrageActuel(15050); // Devrait déclencher alerte Vidange (15000)

        // Véhicule B : CT Périmé bientôt (fait il y a 1 an et 11 mois et 20 jours) ->
        // Reste 10 jours !
        Vehicle v2 = new Voiture("V2", "Peugeot", "Noire", "208", "Lyon", 35.0, "mamadou@test.com", false);
        v2.setControleTechnique(
                new ControleTechnique(LocalDate.now().minusYears(2).plusDays(10), true, "Centre Auto", "Pneus usés"));
        v2.setKilometrageActuel(102000); // Devrait déclencher alerte Courroie (100000)

        // Véhicule C : Jamais de CT
        Vehicle v3 = new Voiture("V3", "BMW", "Blanche", "Serie 1", "Marseille", 50.0, "mamadou@test.com", false);
        // Pas de setControleTechnique

        agent.addVehicle(v1);
        agent.addVehicle(v2);
        agent.addVehicle(v3);

        // 3. Appel du service
        List<String> rappels = maintenanceService.genererRappelsControleTechnique(agent);
        List<String> conseils = maintenanceService.genererRecommandationsEntretien(agent);

        // 4. Retour du résultat
        return Map.of(
                "agent", agent.getNom(),
                "nombre_vehicules", agent.getVehicleList().size(),
                "alertes_recues", rappels);
    }

    /**
     * Test US.A.10 : Historique des entretiens
     */
    @GetMapping("/test-us-a10")
    public Map<String, Object> testUSA10() {
        // 1. Création véhicule
        Vehicle v = new Voiture("TEST-10", "Audi", "Noire", "A3", "Lille", 45.0, "agent@test.com", false);

        // 2. Ajout d'entretiens (Ce que l'agent renseigne)
        // Besoin d'importer Entretien
        fr.miage.groupe2projetpoo.entity.maintenance.Entretien e1 = new fr.miage.groupe2projetpoo.entity.maintenance.Entretien(
                "Changement Pneus Avant", LocalDate.now().minusMonths(6), 48000, 350.0, "Norauto");

        fr.miage.groupe2projetpoo.entity.maintenance.Entretien e2 = new fr.miage.groupe2projetpoo.entity.maintenance.Entretien(
                "Vidange Complète", LocalDate.now().minusMonths(1), 55000, 120.0, "Midas");

        v.getHistoriqueEntretiens().add(e1);
        v.getHistoriqueEntretiens().add(e2);

        // 3. Affichage pour vérification
        return Map.of(
                "test", "US.A.10 - Historique Entretiens",
                "vehicule", v.getModeleVehicule(),
                "historique", v.getHistoriqueEntretiens());
    }

    /**
     * Endpoint pour obtenir l'historique des maintenances
     */
    @GetMapping("/historique")
    public Map<String, Object> getHistoriqueMaintenance() {
        // Création d'un véhicule de démonstration avec historique
        Vehicle v = new Voiture("DEMO-01", "Toyota", "Grise", "Yaris", "Paris", 40.0, "demo@test.com", false);

        // Ajout d'entretiens de démonstration
        fr.miage.groupe2projetpoo.entity.maintenance.Entretien e1 = new fr.miage.groupe2projetpoo.entity.maintenance.Entretien(
                "Vidange huile moteur", LocalDate.now().minusMonths(3), 45000, 89.0, "Speedy");

        fr.miage.groupe2projetpoo.entity.maintenance.Entretien e2 = new fr.miage.groupe2projetpoo.entity.maintenance.Entretien(
                "Remplacement plaquettes de frein", LocalDate.now().minusMonths(6), 42000, 180.0, "Midas");

        fr.miage.groupe2projetpoo.entity.maintenance.Entretien e3 = new fr.miage.groupe2projetpoo.entity.maintenance.Entretien(
                "Révision complète", LocalDate.now().minusYears(1), 30000, 450.0, "Toyota Garage");

        v.getHistoriqueEntretiens().add(e1);
        v.getHistoriqueEntretiens().add(e2);
        v.getHistoriqueEntretiens().add(e3);

        // Contrôle technique
        v.setControleTechnique(new ControleTechnique(LocalDate.now().minusMonths(8), true, "Dekra", "RAS"));

        // Utiliser des HashMap pour éviter les limitations de Map.of avec les Maps imbriqués
        java.util.HashMap<String, Object> result = new java.util.HashMap<>();
        
        java.util.HashMap<String, Object> vehiculeInfo = new java.util.HashMap<>();
        vehiculeInfo.put("id", v.getIdVehicule());
        vehiculeInfo.put("marque", v.getMarqueVehicule());
        vehiculeInfo.put("modele", v.getModeleVehicule());
        vehiculeInfo.put("kilometrage", v.getKilometrageActuel());
        
        java.util.HashMap<String, Object> ctInfo = new java.util.HashMap<>();
        ctInfo.put("date", v.getControleTechnique().getDatePassage().toString());
        ctInfo.put("valide", v.getControleTechnique().isEstValide());
        ctInfo.put("centre", v.getControleTechnique().getCentreControle());
        
        result.put("vehicule", vehiculeInfo);
        result.put("historique_entretiens", v.getHistoriqueEntretiens());
        result.put("controle_technique", ctInfo);
        
        return result;
    }

    private List<Map<String, Object>> transformInterventions(List<MaintenanceIntervention> interventions) {
        return interventions.stream().map(i -> {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", i.getId());
            map.put("agentEmail", i.getAgent().getEmail());
            map.put("vehicleId", i.getVehicule().getIdVehicule());
            map.put("companyName", i.getEntreprise().getNom());
            map.put("companyEmail", i.getEntreprise().getEmail());
            map.put("date", i.getDateIntervention().toString());
            map.put("prix", i.getPrixPaye());
            map.put("statut", i.getStatut());
            return map;
        }).toList();
    }
}
