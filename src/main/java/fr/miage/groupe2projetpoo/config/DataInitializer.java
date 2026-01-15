package fr.miage.groupe2projetpoo.config;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.entity.vehicule.Camion;
import fr.miage.groupe2projetpoo.entity.vehicule.Moto;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;
import fr.miage.groupe2projetpoo.entity.assurance.OptionAcceptationManuelle;
import fr.miage.groupe2projetpoo.entity.assurance.OptionEntretien;
import fr.miage.groupe2projetpoo.repository.RentalRepository;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import fr.miage.groupe2projetpoo.repository.VehicleRepository;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import fr.miage.groupe2projetpoo.entity.maintenance.ControleTechnique;

@Configuration
// Trigger rebuild
public class DataInitializer {

        private final UserRepository userRepository;
        private final VehicleRepository vehicleRepository;
        private final RentalRepository rentalRepository;

        public DataInitializer(UserRepository userRepository, VehicleRepository vehicleRepository,
                        RentalRepository rentalRepository) {
                this.userRepository = userRepository;
                this.vehicleRepository = vehicleRepository;
                this.rentalRepository = rentalRepository;
        }

        @PostConstruct
        public void init() {
                // === 1. Assurances (Initialisées dans UserRepository pour le moment, ou à
                // déplacer ici si Repository Assurance existe) ===
                // Note: Si UserRepository gère les assurances en dur, on peut les laisser ou
                // créer un AssuranceRepository.
                // Pour l'instant on se concentre sur Utilisateurs et Contrats.

                // === 2. Créer les Agents ===
                AgentParticulier agentPart1 = new AgentParticulier("Martin", "Alice", "123456", "alice@test.com",
                                "0612345678");
                AgentParticulier agentPart2 = new AgentParticulier("Dupont", "Bob", "123456", "bob@test.com",
                                "0698765432");
                AgentProfessionnel agentPro1 = new AgentProfessionnel("Durand", "Pierre", "123456",
                                "enterprise1@test.com",
                                "0611111111", "Durand SA", "12345678901234");
                AgentProfessionnel agentPro2 = new AgentProfessionnel("Moreau", "Marie", "123456",
                                "enterprise2@test.com",
                                "0622222222", "Moreau SARL", "98765432109876");

                // === 3. Créer les Loueurs ===
                Loueur loueur1 = new Loueur("Bernard", "Luc", "123456", "loueur1@test.com", "0633333333",
                                "FR7630001007941234567890185", "Bernard Auto");
                loueur1.setVille("Paris"); // Ville de résidence pour suggestions de véhicules

                Loueur loueur2 = new Loueur("Petit", "Sophie", "123456", "loueur2@test.com", "0644444444",
                                "FR7630004000031234567890143", "Sophie Location");
                loueur2.setVille("Marseille"); // Ville de résidence pour suggestions de véhicules

                // Ajouter aussi les loueurs "Legacy" du RentalRepository pour éviter de casser
                // des tests existants s'ils dépendent de ces noms
                Loueur loueurJean = new Loueur("Dupont", "Jean", "pass123", "jean@email.com", "0601020304");
                loueurJean.setVille("Lyon");

                Loueur loueurMarie = new Loueur("Martin", "Marie", "pass456", "marie@email.com", "0605060708");
                loueurMarie.setVille("Nice");

                // Sauvegarder les users
                userRepository.save(agentPart1);
                userRepository.save(agentPart2);
                userRepository.save(agentPro1);
                userRepository.save(agentPro2);
                userRepository.save(loueur1);
                userRepository.save(loueur2);
                userRepository.save(loueurJean);
                userRepository.save(loueurMarie);

                // === 4. Créer les Véhicules ===
                // Véhicules d'Alice
                Voiture voiture1 = new Voiture("1", "Renault", "Bleu", "Clio", "Paris", 45.0, "alice@test.com", false);
                voiture1.setCoordonnees(48.8566, 2.3522); // Paris centre
                // US.A.9/11: CT récent + km proche vidange
                voiture1.setKilometrageActuel(15200); // Proche du seuil 15000 km → vidange recommandée
                voiture1.setControleTechnique(
                                new ControleTechnique(LocalDate.now().minusMonths(6), true, "Dekra Paris", "RAS"));

                Voiture voiture2 = new Voiture("2", "Peugeot", "Noir", "308", "Lyon", 55.0, "alice@test.com", false);
                voiture2.setCoordonnees(45.7640, 4.8357); // Lyon centre
                // US.A.9/11: CT expire bientôt + km proche changement pneus
                voiture2.setKilometrageActuel(40500); // Proche du seuil 40000 km → vérifier pneus
                voiture2.setControleTechnique(new ControleTechnique(LocalDate.now().minusYears(2).plusDays(20), true,
                                "AutoSur Lyon", "Pneus légèrement usés"));

                // Véhicules de Bob
                Voiture voiture3 = new Voiture("3", "BMW", "Blanc", "Serie 3", "Marseille", 85.0, "bob@test.com",
                                false);
                voiture3.setCoordonnees(43.2965, 5.3698); // Marseille centre
                // US.A.9/11: Haut kilométrage → courroie de distribution
                voiture3.setKilometrageActuel(102000); // Proche du seuil 100000 km → courroie recommandée
                voiture3.setControleTechnique(
                                new ControleTechnique(LocalDate.now().minusMonths(10), true, "Dekra Marseille", "RAS"));

                Moto moto1 = new Moto("4", "Yamaha", "Rouge", "MT-07", "Nice", 60.0, "bob@test.com", false);
                moto1.setCoordonnees(43.7102, 7.2620); // Nice centre
                // US.A.9: Jamais de CT enregistré (alerte attendue)
                moto1.setKilometrageActuel(8500);

                // Véhicules de Durand SA
                Voiture voiture4 = new Voiture("5", "Mercedes", "Gris", "Classe A", "Paris", 90.0,
                                "enterprise1@test.com", false);
                voiture4.setCoordonnees(48.8738, 2.2950); // Paris 16e (La Défense)
                // US.A.9/11: CT valide, kilométrage normal
                voiture4.setKilometrageActuel(25000);
                voiture4.setControleTechnique(new ControleTechnique(LocalDate.now().minusMonths(12), true,
                                "AutoVision Paris", "RAS"));

                Camion camion1 = new Camion("6", "Renault", "Blanc", "Master", "Paris", 120.0, "enterprise1@test.com",
                                false);
                camion1.setCoordonnees(48.9362, 2.3574); // Saint-Denis (près Paris)
                // US.A.9: CT périmé (alerte urgente attendue)
                camion1.setKilometrageActuel(75000);
                camion1.setControleTechnique(new ControleTechnique(LocalDate.now().minusYears(2).minusMonths(1), true,
                                "Norisko", "Freins limites"));

                Camion camion2 = new Camion("7", "Mercedes", "Jaune", "Sprinter", "Lyon", 135.0, "enterprise1@test.com",
                                false);
                camion2.setCoordonnees(45.7485, 4.8467); // Lyon Villeurbanne
                // US.A.9/11: Kilométrage vidange + CT OK
                camion2.setKilometrageActuel(30200); // Proche du seuil 30000 km → vidange
                camion2.setControleTechnique(
                                new ControleTechnique(LocalDate.now().minusMonths(4), true, "Dekra Lyon", "RAS"));

                // Véhicules de Moreau SARL
                Moto moto2 = new Moto("8", "Honda", "Noir", "CB650R", "Bordeaux", 65.0, "enterprise2@test.com", false);
                moto2.setCoordonnees(44.8378, -0.5792); // Bordeaux centre
                // US.A.9/11: Moto avec CT invalide (contre-visite)
                moto2.setKilometrageActuel(12000);
                moto2.setControleTechnique(new ControleTechnique(LocalDate.now().minusMonths(1), false,
                                "Contrôle Auto Bordeaux", "Fuite d'huile - Contre-visite requise"));

                // Véhicules "Legacy" de RentalRepository (V001, V002)
                Voiture voitureV1 = new Voiture("V001", "Bleu", "Renault", "Clio", "Paris", 30.0, "jean@email.com",
                                false);
                voitureV1.setCoordonnees(48.8606, 2.3376); // Paris Louvre

                Voiture voitureV2 = new Voiture("V002", "Peugeot", "Noir", "3008", "Lyon", 50.0, "marie@email.com",
                                false);
                voitureV2.setCoordonnees(45.7578, 4.8320); // Lyon Part-Dieu

                vehicleRepository.save(voiture1);
                vehicleRepository.save(voiture2);
                vehicleRepository.save(voiture3);
                vehicleRepository.save(moto1);
                vehicleRepository.save(voiture4);
                vehicleRepository.save(camion1);
                vehicleRepository.save(camion2);
                vehicleRepository.save(moto2);
                vehicleRepository.save(voitureV1);
                vehicleRepository.save(voitureV2);

                // Liaison Agent -> Véhicule
                agentPart1.addVehicle(voiture1);
                agentPart1.addVehicle(voiture2);
                agentPart2.addVehicle(voiture3);
                agentPart2.addVehicle(moto1);
                agentPro1.addVehicle(voiture4);
                agentPro1.addVehicle(camion1);
                agentPro1.addVehicle(camion2);
                agentPro2.addVehicle(moto2);

                // === 6. Initialiser des options pour les agents (Exemples) ===
                // Option 1: Acceptation manuelle pour Alice
                OptionAcceptationManuelle optManuelle = new OptionAcceptationManuelle();
                agentPart1.ajouterOption(optManuelle);

                // Option 2: Entretien automatique pour Durand SA
                OptionEntretien optEntretien = new OptionEntretien(true);
                agentPro1.ajouterOption(optEntretien);

                // Option 3: Parking Vienci pour Bob (test US parking)
                fr.miage.groupe2projetpoo.entity.infrastructure.Parking parkingVienci = new fr.miage.groupe2projetpoo.entity.infrastructure.Parking(
                                1, "Vienci Parking Montparnasse", "15 Boulevard du Montparnasse", "Paris", 200, 8.0);
                parkingVienci.setCodeAcces("4521#");
                parkingVienci.setProcedureAcces(
                                "1. Entrez le code 4521# sur le digicode à l'entrée\n2. Suivez les panneaux 'Location Véhicules'\n3. Garez le véhicule sur un emplacement marqué 'VIENCI'\n4. Déposez les clés dans la boîte aux lettres n°12");
                parkingVienci.setInstructionsSpeciales(
                                "Parking ouvert 24h/24. En cas de problème, contactez le 01 23 45 67 89.");

                fr.miage.groupe2projetpoo.entity.assurance.OptionParking optParking = new fr.miage.groupe2projetpoo.entity.assurance.OptionParking(
                                "Option Vienci Parking", 25.0, 7, 3, 5.0, parkingVienci);
                optParking.setEstActive(true);
                agentPart2.ajouterOption(optParking);

                loueurJean.addVehicle(voitureV1); // C'est un loueur mais il agit comme agent ici dans l'ancien code ?
                loueurMarie.addVehicle(voitureV2);

                // === 5. Créer les Contrats ===
                // Assurances (récupérées via repoUser car stockées là pour l'instant)
                Assurance assuranceBasic = new Assurance(1, "Assurance Basic", 15.0);
                Assurance assuranceComplete = new Assurance(2, "Assurance Tous Risques", 35.0);
                Assurance assurancePremium = new Assurance(3, "Assurance Premium", 50.0);

                Calendar cal = Calendar.getInstance();
                Date today_date = cal.getTime();
                cal.add(Calendar.DAY_OF_MONTH, 7);
                Date nextWeek = cal.getTime();
                cal.add(Calendar.DAY_OF_MONTH, 7);
                Date twoWeeksLater = cal.getTime();
                cal.setTime(today_date);
                cal.add(Calendar.DAY_OF_MONTH, -5);
                Date fiveDaysAgo = cal.getTime();

                // Contrat 1: Loueur1 loue voiture1 (Alice) - Même lieu
                RentalContract contract1 = new RentalContract(loueur1, voiture1, today_date, nextWeek,
                                "Paris - Gare du Nord",
                                "Paris - Gare du Nord", assuranceBasic, agentPart1);
                contract1.setIdC(1);

                // Contrat 2: Loueur2 loue voiture3 (Bob) - Même lieu
                RentalContract contract2 = new RentalContract(loueur2, voiture3, fiveDaysAgo, today_date,
                                "Marseille Centre",
                                "Marseille Centre", assuranceComplete, agentPart2);
                contract2.setIdC(2);
                contract2.setStatut(true);

                // Contrat 3: Loueur1 loue camion1 (Durand SA) - Agent Pro → lieux différents OK
                RentalContract contract3 = new RentalContract(loueur1, camion1, today_date, twoWeeksLater,
                                "Paris - Entrepôt",
                                "Lyon - Centre", assurancePremium, agentPro1);
                contract3.setIdC(3);

                // Contrat 4: Loueur2 loue moto2 (Moreau SARL) - Même lieu
                RentalContract contract4 = new RentalContract(loueur2, moto2, nextWeek, twoWeeksLater,
                                "Bordeaux - Centre Ville", "Bordeaux - Centre Ville", assuranceBasic, agentPro2);
                contract4.setIdC(4);

                rentalRepository.save(contract1);
                rentalRepository.save(contract2);
                rentalRepository.save(contract3);
                rentalRepository.save(contract4);

                // Contrat Historique: Loueur1 a loué voiture1 (Alice) il y a 2 mois
                Calendar calHist = Calendar.getInstance();
                calHist.add(Calendar.MONTH, -2);
                Date historyStart = calHist.getTime();
                calHist.add(Calendar.DAY_OF_MONTH, 5);
                Date historyEnd = calHist.getTime();

                RentalContract contractHistorique = new RentalContract(loueur1, voiture1, historyStart, historyEnd,
                                "Paris - Centre", "Paris - Centre", assuranceBasic, agentPart1);
                contractHistorique.setIdC(100);
                contractHistorique.setStatut(true);
                contractHistorique.setSignatureLoueur(true);
                contractHistorique.setSignatureAgent(true);

                rentalRepository.save(contractHistorique);

                // Liaison Loueur/Agent -> Contrat
                loueur1.addContract(contract1);
                loueur1.addVehicle(voiture1);
                agentPart1.addContract(contract1);
                loueur2.addContract(contract2);
                loueur2.addVehicle(voiture3);
                agentPart2.addContract(contract2);
                loueur1.addContract(contract3);
                loueur1.addVehicle(camion1);
                agentPro1.addContract(contract3);
                loueur2.addContract(contract4);
                loueur2.addVehicle(moto2);
                loueur2.addContract(contract4);
                loueur2.addVehicle(moto2);
                agentPro2.addContract(contract4);

                // Liaison Pour Historique
                loueur1.addContract(contractHistorique);
                agentPart1.addContract(contractHistorique);
                voiture1.ajouterContrat(contractHistorique);
        }
}
