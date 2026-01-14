package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.assurance.OptionEntretien;
import fr.miage.groupe2projetpoo.entity.assurance.OptionParking;
import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.maintenance.ControleTechnique;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.AgentParticulier;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;
import fr.miage.groupe2projetpoo.service.MaintenanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Contrôle centralisé pour la gestion des fonctionnalités avancées de l'Agent.
 * Regroupe : Maintenance, Facturation Options, Gestion Parking.
 */
@RestController
@RequestMapping("/api/gestion-agent")
public class GestionAgentController {

    private final MaintenanceService maintenanceService;

    public GestionAgentController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // ============================================================
    // US.A.8, US.A.9, US.A.11 : GESTION MAINTENANCE
    // ============================================================

    /**
     * Voir les alertes et recommandations de maintenance pour la flotte.
     */
    @GetMapping("/maintenance/alertes")
    public Map<String, Object> consulterAlertesMaintenance() {
        Agent agent = new AgentParticulier("Diallo", "Mamadou", "pass", "mamadou@test.com", "0600000000");

        // Véhicule A : CT OK, mais Vidange à faire
        Vehicle v1 = new Voiture("V1", "Renault", "Rouge", "Clio", "Paris", 30.0, "mamadou@test.com", false);
        v1.setControleTechnique(new ControleTechnique(LocalDate.now().minusMonths(6), true, "Centre Auto", "RAS"));
        v1.setKilometrageActuel(15050);

        // Véhicule B : CT Périmé bientôt + Courroie
        Vehicle v2 = new Voiture("V2", "Peugeot", "Noire", "208", "Lyon", 35.0, "mamadou@test.com", false);
        v2.setControleTechnique(
                new ControleTechnique(LocalDate.now().minusYears(2).plusDays(10), true, "Centre Auto", "Pneus usés"));
        v2.setKilometrageActuel(102000);

        // Véhicule C : Jamais de CT
        Vehicle v3 = new Voiture("V3", "BMW", "Blanche", "Serie 1", "Marseille", 50.0, "mamadou@test.com", false);

        agent.addVehicle(v1);
        agent.addVehicle(v2);
        agent.addVehicle(v3);

        List<String> rappels = maintenanceService.genererRappelsControleTechnique(agent);
        List<String> conseils = maintenanceService.genererRecommandationsEntretien(agent);

        return Map.of(
                "agent", agent.getNom(),
                "alertes_ct", rappels,
                "conseils_entretien_km", conseils);
    }

    /**
     * Simuler le renseignement d'un Contrôle Technique.
     */
    @GetMapping("/maintenance/info-ct")
    public Map<String, Object> simulerRenseignementCT() {
        Vehicle v = new Voiture("TEST-8", "Citroen", "Blanche", "C3", "Paris", 25.0, "agent@test.com", false);

        // Renseignement du CT
        ControleTechnique ct = new ControleTechnique(LocalDate.now(), true, "AutoSur Paris 15", "Pare-brise fissuré");
        v.setControleTechnique(ct);

        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return Map.of(
                "action", "Renseignement CT effectué",
                "vehicule", v.getModeleVehicule(),
                "ct_enregistre", Map.of(
                        "date", v.getControleTechnique().getDatePassage().format(fmt),
                        "valide", v.getControleTechnique().isEstValide(),
                        "remarques", v.getControleTechnique().getRemarques()));
    }

    // ============================================================
    // US OPTIONS : FACTURATION
    // ============================================================

    /**
     * Voir la facture mensuelle simulée avec options (Parking, Entretien).
     */
    @GetMapping("/finance/facture")
    public Map<String, Object> simulerFactureMensuelle() {
        Agent agent = new AgentParticulier("Diallo", "Mamadou", "pass", "mamadou@test.com", "0600000000");

        // Ajout Option Parking (50€)
        Parking parkingLille = new Parking(1, "Parking Lille Centre", "Rue Nationale", "Lille", 200, 15.0);
        OptionParking optParking = new OptionParking("Forfait Stationnement", 50.0, 24, 30, 10.0, parkingLille);
        agent.ajouterOption(optParking);

        // Ajout Option Entretien Automatique (20€)
        OptionEntretien optEntretien = new OptionEntretien(true);
        agent.ajouterOption(optEntretien);

        double total = agent.calculerFactureMensuelle();

        return Map.of(
                "agent", agent.getNom(),
                "options_souscrites", agent.getOptionsPayantes(),
                "total_facture_mensuelle", total + " €");
    }

    // ============================================================
    // US PARKING : LOCATION POINT ARRIVEE
    // ============================================================

    /**
     * Simuler une location avec un point de retour différent (Parking partenaire).
     */
    @GetMapping("/location/retour-parking")
    public Map<String, Object> simulerLocationRetourDifferent() {
        Agent agent = new AgentParticulier("Diallo", "Mamadou", "pass", "mamadou@test.com", "0600000000");
        Parking parkingLille = new Parking(1, "Parking Gare Lille", "1 Place de la Gare", "Lille", 500, 10.0);

        // Option Parking activée
        OptionParking optParking = new OptionParking("Option Parking", 50.0, 48, 30, 8.0, parkingLille);
        agent.ajouterOption(optParking);

        // Loueur et Véhicule
        Loueur loueur = new Loueur("Dupont", "Jean", "pass", "jean@test.com", "0612345678", "FR76 0001", "Google");
        Vehicle v1 = new Voiture("V1", "Renault", "Rouge", "Clio", "Paris", 30.0, "mamadou@test.com", false);
        agent.addVehicle(v1);

        // Contrat avec retour différent
        Date debut = new Date();
        Date fin = Date.from(LocalDate.now().plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Assurance assurance = new Assurance(1, "Tous Risques", 5.0);

        String lieuDepart = "Agence Paris";
        String lieuRetour = parkingLille.getNom() + " (" + parkingLille.getVille() + ")";

        RentalContract contrat = new RentalContract(loueur, v1, debut, fin, lieuDepart, lieuRetour, assurance);

        return Map.of(
                "message", "Simulation location avec retour Parking",
                "lieu_depart", contrat.getLieuPrise(),
                "lieu_retour_prevu", contrat.getLieuDepose(),
                "prix_total", contrat.getPrixTotal() + " €");
    }
}
