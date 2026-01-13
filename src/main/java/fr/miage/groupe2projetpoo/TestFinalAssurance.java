package fr.miage.groupe2projetpoo;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.assurance.OptionParking;
import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import fr.miage.groupe2projetpoo.entity.utilisateur.AgentParticulier;
import fr.miage.groupe2projetpoo.entity.vehicule.Camion;
import fr.miage.groupe2projetpoo.entity.vehicule.Voiture;

import java.time.LocalDate;
import java.util.ArrayList;

public class TestFinalAssurance {
        public static void main(String[] args) {
                System.out.println("=== DEBUT DU TEST ASSURANCE & OPTION ===");

                // 1. Test Assurance
                System.out.println("\n--- Test Assurance ---");
                Assurance assuranceBase = new Assurance(1, "Assurance Basic", 50.0);

                // Création de véhicules
                Voiture voiture = new Voiture("1", "Renault", "Rouge", "Clio", "Paris", 30.0, "Jean",
                               false);
                Camion camion = new Camion("2", "Volvo", "Blanc", "FH16", "Lyon", 100.0, "Marie",
                                false);

                double primeVoiture = assuranceBase.calculerPrime(voiture);
                // Tarif Base 50 + Supplement Voiture 10 = 60
                System.out.println("Prime pour Voiture (Base 50 + 10) : " + primeVoiture + " -> "
                                + (primeVoiture == 60.0 ? "OK" : "ERREUR"));

                double primeCamion = assuranceBase.calculerPrime(camion);
                // Tarif Base 50 + Supplement Camion 20 = 70
                System.out.println(
                                "Prime pour Camion (Base 50 + 20) : " + primeCamion + " -> "
                                                + (primeCamion == 70.0 ? "OK" : "ERREUR"));

                // 2. Test Option Parking
                System.out.println("\n--- Test Option Parking ---");
                Parking parkingParis = new Parking(1, "Parking Gare du Nord", "18 Rue de Dunkerque", "Paris", 200,
                                15.0);
                OptionParking optionParking = new OptionParking("Abonnement Parking VIP", 15.0, true, 24, 5, 8.0,
                                parkingParis);

                System.out.println("Option créée : " + optionParking.getNom());
                System.out.println("Liée au parking : " + optionParking.getParkingPartenaire().getNom());

                // Test Eligibilité
                AgentParticulier agentSansVehicule = new AgentParticulier("Dubois", "Jean", "pass", "jean@test.com",
                                "0600000000");
                boolean eligibleSans = optionParking.estEligible(agentSansVehicule);
                System.out.println(
                                "Agent sans véhicule éligible ? " + eligibleSans + " -> "
                                                + (!eligibleSans ? "OK (Non)" : "ERREUR"));

                AgentParticulier agentAvecVehicule = new AgentParticulier("Durand", "Marie", "pass", "marie@test.com",
                                "0600000000");
                agentAvecVehicule.addVehicle(voiture);
                boolean eligibleAvec = optionParking.estEligible(agentAvecVehicule);
                System.out.println(
                                "Agent avec véhicule éligible ? " + eligibleAvec + " -> "
                                                + (eligibleAvec ? "OK (Oui)" : "ERREUR"));

                // Test coût
                System.out.println("Coût mensuel option (Active=true) : " + optionParking.calculerCoutMensuel() + " -> "
                                + (optionParking.calculerCoutMensuel() == 15.0 ? "OK" : "ERREUR"));
                System.out.println("Tarif présentiel hors forfait : " + optionParking.calculerTarifPresentiel());

                System.out.println("\n=== FIN DU TEST ===");
        }
}
