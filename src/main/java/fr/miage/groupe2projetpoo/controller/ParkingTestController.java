package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/parking-test")
public class ParkingTestController {

    @GetMapping("/disponibles")
    public List<Parking> getParkings() {
        List<Parking> parkings = new ArrayList<>();
        // Construction avec le nouveau constructeur (avec Ville)
        parkings.add(new Parking(1, "Parking Gare Part-Dieu", "Place de la Gare", "Lyon", 500, 25.0));
        parkings.add(new Parking(2, "Parking Opéra", "Place de l'Opéra", "Paris", 300, 35.0));
        return parkings;
    }
}
