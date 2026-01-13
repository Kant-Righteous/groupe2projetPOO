package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.maintenance.ControleTechnique;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service gérant la maintenance et les rappels pour les véhicules.
 * Couvre les US.A.9 (Rappels CT) et US.A.11 (Recommandations entretien).
 */
@Service
public class MaintenanceService {

    /**
     * US.A.9 : Vérifie les contrôles techniques et génère des rappels.
     * 
     * @param agent L'agent concerné
     * @return Liste des alertes/rappels
     */
    public List<String> genererRappelsControleTechnique(Agent agent) {
        List<String> rappels = new ArrayList<>();

        if (agent == null || agent.getVehicleList() == null) {
            return rappels;
        }

        for (Vehicle vehicule : agent.getVehicleList()) {
            ControleTechnique ct = vehicule.getControleTechnique();

            // Cas 1 : Jamais de CT passé
            if (ct == null) {
                rappels.add("⚠️ Le véhicule " + vehicule.getModeleVehicule() + " (" + vehicule.getIdVehicule()
                        + ") n'a jamais passé de contrôle technique renseigné !");
                continue;
            }

            // Cas 2 : CT invalide (contre-visite)
            if (!ct.isEstValide()) {
                rappels.add("🚨 URGENT : Le véhicule " + vehicule.getModeleVehicule()
                        + " a un contrôle technique NON VALIDE (Contre-visite requise).");
                continue;
            }

            // Cas 3 : Date d'expiration proche ou dépassée
            // On considère qu'un CT est valide 2 ans
            LocalDate dateExpiration = ct.getDatePassage().plusYears(2);
            LocalDate aujourdhui = LocalDate.now();
            LocalDate dansUnMois = aujourdhui.plusMonths(1);

            // Format français : JJ/MM/AAAA
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateStr = dateExpiration.format(formatter);

            if (dateExpiration.isBefore(aujourdhui)) {
                rappels.add("🚨 EXPIRE : Le contrôle technique de la " + vehicule.getModeleVehicule()
                        + " est périmé depuis le " + dateStr + ".");
            } else if (dateExpiration.isBefore(dansUnMois)) {
                rappels.add("⚠️ RAPPEL : Le contrôle technique de la " + vehicule.getModeleVehicule()
                        + " expire bientôt (le " + dateStr + "). Pensez à prendre rendez-vous !");
            }
        }

        return rappels;
    }

    /**
     * US.A.11 : Recommandations d'entretiens par rapport aux km.
     * 
     * @param agent L'agent concerné
     * @return Liste des conseils
     */
    public List<String> genererRecommandationsEntretien(Agent agent) {
        List<String> conseils = new ArrayList<>();

        if (agent == null || agent.getVehicleList() == null) {
            return conseils;
        }

        for (Vehicle vehicule : agent.getVehicleList()) {
            int km = vehicule.getKilometrageActuel();
            String modele = vehicule.getModeleVehicule();

            // Recommandation Vidange (tous les 15 000 ou 20 000)
            if (km > 1000 && km % 15000 < 1000) {
                conseils.add(
                        "🔧 CONSEIL " + modele + " (" + km + " km) : Une vidange est recommandée tous les 15 000 km.");
            }

            // Recommandation Courroie (vers 100 000 ou 120 000)
            if (km >= 100000 && km <= 105000) {
                conseils.add("⛓️ IMPORTANT " + modele + " (" + km
                        + " km) : Avez-vous changé la courroie de distribution ? (Recommandé à 100 000 km).");
            }

            // Recommandation Pneus (tous les 40 000 - estimation)
            if (km > 0 && km % 40000 < 2000) {
                conseils.add("🔘 PNEUS " + modele + " (" + km + " km) : Vérifiez l'usure de vos pneus.");
            }
        }
        return conseils;
    }
}
