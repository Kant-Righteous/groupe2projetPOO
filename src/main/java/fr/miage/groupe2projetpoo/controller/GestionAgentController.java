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
import fr.miage.groupe2projetpoo.service.RentalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/gestion-agent")
public class GestionAgentController {

    private final MaintenanceService maintenanceService;
    // On garde RentalService si besoin pour d'autres tests, sinon on peut l'enlever
    // private final RentalService rentalService;

    public GestionAgentController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // =================================================================================
    // 1. MAINTENANCE ET CONTRÔLE TECHNIQUE (US.A.8, US.A.9, US.A.11)
    // =================================================================================

    /**
     * US.A.9 : Rappels Contrôle Technique
     * US.A.11 : Recommandations basées sur le kilométrage
     */
    @GetMapping("/maintenance/alertes")
    public Map<String, Object> getAlertesMaintenance() {
        // 1. Setup Agent & Véhicules
        Agent agent = new AgentParticulier("Diallo", "Mamadou", "pass", "mamadou@test.com", "0600000000");

        Vehicle v1 = new Voiture("V100", "Renault", "Rouge", "Clio", "Paris", 30.0, "mamadou@test.com", false);
        // CT périmé dans 10 jours (Rappel attendu)
        v1.setControleTechnique(
                new ControleTechnique(LocalDate.now().minusYears(2).plusDays(10), true, "Centre A", "RAS"));
        v1.setKilometrageActuel(15500); // Vidange attendue (>15000)

        agent.addVehicle(v1);

        // 2. Appel Service
        List<String> rappelsCT = maintenanceService.genererRappelsControleTechnique(agent);
        List<String> conseilsKm = maintenanceService.genererRecommandationsEntretien(agent);

        // 3. Réponse
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("titre", "Tableau de Bord Maintenance - Agent");
        response.put("vehicule", v1.getModeleVehicule() + " (" + v1.getKilometrageActuel() + " km)");
        response.put("alertes_controle_technique", rappelsCT);
        response.put("conseils_entretien_km", conseilsKm);

        return response;
    }

    /**
     * US.A.8 : Renseigner CT (Visualisation)
     */
    @GetMapping("/maintenance/info-ct")
    public Map<String, Object> getInfoCT() {
        Vehicle v = new Voiture("V200", "Peugeot", "Noire", "308", "Lyon", 40.0, "agent@test.com", false);
        LocalDate dateCT = LocalDate.of(2025, 1, 14);
        v.setControleTechnique(new ControleTechnique(dateCT, true, "AutoSur Lyon", "Usure pneus avant"));

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("vehicule", v.getModeleVehicule());
        info.put("ct_fait_le", dateCT.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        info.put("centre", v.getControleTechnique().getCentreControle());
        info.put("remarques", v.getControleTechnique().getRemarques());
        info.put("est_valide", v.getControleTechnique().isEstValide());

        return info;
    }

    // =================================================================================
    // 2. FINANCES ET OPTIONS (Option Parking, Option Entretien)
    // =================================================================================

    @GetMapping("/finance/facture")
    public Map<String, Object> simulerFacture() {
        Agent agent = new AgentParticulier("Martin", "Alice", "pass", "alice@test.com", "0611223344");

        // Ajout Option Parking (ex: 20€)
        Parking parking = new Parking(1, "Parking Gare", "10 rue de la Gare", "Lyon", 50, 20.0);
        OptionParking optP = new OptionParking("Option Parking Gare", 20.0, 72, 30, 2.5, parking);
        agent.ajouterOption(optP);

        // Ajout Option Entretien Auto (ex: 50€)
        OptionEntretien optE = new OptionEntretien(true);
        agent.ajouterOption(optE);

        double total = agent.calculerFactureMensuelle();

        return Map.of(
                "agent", agent.getNom() + " " + agent.getPrenom(),
                "options_souscrites", List.of("Parking Gare (20.0€)", "Entretien Automatique (50.0€)"),
                "total_mensuel_a_payer", total + "€");
    }

    // =================================================================================
    // 3. LOCATION (Option Retour Parking)
    // =================================================================================

    @GetMapping("/location/retour-parking")
    public Map<String, Object> testRetourParking() {
        // Simulation d'un contrat avec retour dans un parking partenaire
        Parking p = new Parking(99, "Parking Centre", "1 Place Bellecour", "Lyon", 100, 15.0);

        String lieuPrise = "Agence Lyon Part-Dieu";
        String lieuRetour = "Mise au parking : " + p.getNom() + " (" + p.getAdresse() + ")";

        return Map.of(
                "message", "Simulation Contrat avec Option Parking",
                "depart", lieuPrise,
                "retour_prevu", lieuRetour,
                "note", "Le véhicule sera déposé directement au parking partenaire.");
    }
}
