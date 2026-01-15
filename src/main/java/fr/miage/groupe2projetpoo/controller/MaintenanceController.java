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
}
