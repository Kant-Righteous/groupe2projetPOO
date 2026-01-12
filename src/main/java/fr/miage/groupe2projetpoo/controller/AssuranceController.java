package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.assurance.OptionParking;
import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/mes-assurances")
public class AssuranceController {

    // Cette méthode crée les données "à la volée" sans toucher à votre base de
    // données réelle
    @GetMapping
    public List<Object> getMesDonnees() {
        List<Object> resultat = new ArrayList<>();

        // 1. Je crée mes Assurances (juste pour l'affichage API)
        resultat.add(new Assurance(1, "Assurance Basic (Test API)", 15.0));
        resultat.add(new Assurance(2, "Assurance Premium (Test API)", 50.0));

        // 2. Je crée mes Options (juste pour l'affichage API)
        Parking parking = new Parking(1, "Parking API Test", "Rue du Web", "Internet", 100, 5.0);
        OptionParking option = new OptionParking("Option Parking VIP", 20.0, true, 24, 10, 5.0, parking);
        resultat.add(option);

        return resultat;
    }

    // POST http://localhost:8080/api/mes-assurances
    // Cette méthode simule la création d'une assurance reçue depuis Hoppscotch
    @org.springframework.web.bind.annotation.PostMapping
    public Assurance creerAssurance(@org.springframework.web.bind.annotation.RequestBody Assurance nouvelleAssurance) {
        // En vrai, on l'enregistrerait en base de données.
        // Ici, on renvoie juste l'objet pour confirmer qu'on a bien reçu les données.
        System.out.println("Reçu via POST : " + nouvelleAssurance.getNom());
        return nouvelleAssurance;
    }
}
