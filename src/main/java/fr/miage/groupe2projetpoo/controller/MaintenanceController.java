package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.entretien.MaintenanceIntervention;
import fr.miage.groupe2projetpoo.entity.entretien.MaintenancePrice;
import fr.miage.groupe2projetpoo.entity.entretien.MaintenanceStatus;
import fr.miage.groupe2projetpoo.entity.utilisateur.MaintenanceCompany;
import fr.miage.groupe2projetpoo.entity.vehicule.TypeVehicule;
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

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @PostMapping("/companies/register")
    public ResponseEntity<?> registerCompany(@RequestBody Map<String, String> request) {
        try {
            MaintenanceCompany company = new MaintenanceCompany(
                    request.get("nom"),
                    request.get("prenom"),
                    request.get("password"),
                    request.get("email"),
                    request.get("tel")
            );
            MaintenanceCompany registered = maintenanceService.registerCompany(company);
            return ResponseEntity.ok(Map.of("success", true, "message", "Entreprise enregistrée", "email", registered.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/companies/{email:.+}/refere")
    public ResponseEntity<?> setCompanyRefere(@PathVariable String email, @RequestBody Map<String, Boolean> request) {
        try {
            boolean refere = request.getOrDefault("refere", true);
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
                    Double.parseDouble(request.get("prix").toString())
            );
            maintenanceService.addPrice(email, price);
            return ResponseEntity.ok(Map.of("success", true, "message", "Tarif ajouté au catalogue"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/companies/{email:.+}/prices/import")
    public ResponseEntity<?> importPrices(@PathVariable String email, @RequestBody List<Map<String, Object>> pricesRequest) {
        try {
            List<MaintenancePrice> prices = pricesRequest.stream().map(req -> new MaintenancePrice(
                    TypeVehicule.valueOf(((String) req.get("typeVehicule")).toUpperCase()),
                    (String) req.get("modele"),
                    Double.parseDouble(req.get("prix").toString())
            )).toList();
            maintenanceService.importPrices(email, prices);
            return ResponseEntity.ok(Map.of("success", true, "message", prices.size() + " tarifs importés avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/companies/available")
    public ResponseEntity<?> getAvailableCompanies() {
        return ResponseEntity.ok(maintenanceService.getAvailableCompanies());
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestMaintenance(@RequestBody Map<String, String> request) {
        try {
            MaintenanceIntervention intervention = maintenanceService.requestMaintenance(
                    request.get("agentEmail"),
                    request.get("vehicleId"),
                    request.get("companyEmail"),
                    LocalDate.parse(request.get("date"))
            );
            return ResponseEntity.ok(Map.of("success", true, "message", "Demande d'entretien créée", "interventionId", intervention.getId()));
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
