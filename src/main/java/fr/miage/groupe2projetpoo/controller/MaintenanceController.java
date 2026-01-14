package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.maintenance.ControleTechnique;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.AgentParticulier;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;
import fr.miage.groupe2projetpoo.service.MaintenanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                "alertes_ct", rappels,
                "conseils_entretien_km", conseils);
    }

    /**
     * Test US.A.8 : Renseigner informations Contrôle Technique
     */
    @GetMapping("/test-us-a8")
    public Map<String, Object> testUSA8() {
        // 1. Création véhicule
        Vehicle v = new Voiture("TEST-8", "Citroen", "Blanche", "C3", "Paris", 25.0, "agent@test.com", false);

        // 2. Renseignement du CT (L'action de l'US)
        ControleTechnique ct = new ControleTechnique(LocalDate.now(), true, "AutoSur Paris 15", "Pare-brise fissuré");
        v.setControleTechnique(ct);

        // 3. Vérification que c'est bien enregistré
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return Map.of(
                "test", "US.A.8 - Renseigner CT",
                "vehicule", v.getModeleVehicule() + " (" + v.getIdVehicule() + ")",
                "ct_enregistre", Map.of(
                        "date", v.getControleTechnique().getDatePassage().format(fmt),
                        "valide", v.getControleTechnique().isEstValide(),
                        "centre", v.getControleTechnique().getCentreControle(),
                        "remarques", v.getControleTechnique().getRemarques()));
    }

    /**
     * Test US.A.9 : Rappels Contrôle Technique
     */
    @GetMapping("/test-us-a9")
    public Map<String, Object> testUSA9() {
        Agent agent = new AgentParticulier("Test9", "Agent", "pass", "t9@test.com", "0102030405");

        // Véhicule avec CT expirant bientôt (validité 2 ans, fait il y a 2 ans moins 10
        // jours)
        Vehicle v = new Voiture("TEST-9", "Peugeot", "Grise", "308", "Lyon", 40.0, "agent@test.com", false);
        LocalDate dateCT = LocalDate.now().minusYears(2).plusDays(10); // Reste 10 jours
        v.setControleTechnique(new ControleTechnique(dateCT, true, "Dekra", "RAS"));

        agent.addVehicle(v);

        // Appel du service
        List<String> rappels = maintenanceService.genererRappelsControleTechnique(agent);

        return Map.of(
                "test", "US.A.9 - Rappels CT",
                "vehicule_situation", "CT fait le " + dateCT + " (Expire dans 10 jours)",
                "alertes_recues", rappels);
    }
}
