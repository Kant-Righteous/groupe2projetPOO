package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import fr.miage.groupe2projetpoo.repository.ParkingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller REST pour la gestion des Parkings
 */
@RestController
@RequestMapping("/api/parkings")
public class ParkingController {

    private final ParkingRepository parkingRepository;

    public ParkingController(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    /**
     * GET /api/parkings - Récupérer tous les parkings disponibles
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllParkings() {
        List<Parking> parkings = parkingRepository.findAll();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Parking p : parkings) {
            Map<String, Object> parkingInfo = new LinkedHashMap<>();
            parkingInfo.put("id", p.getIdParking());
            parkingInfo.put("nom", p.getNom());
            parkingInfo.put("adresse", p.getAdresse());
            parkingInfo.put("ville", p.getVille());
            parkingInfo.put("capacite", p.getCapacite());
            parkingInfo.put("tarifJournalier", p.getTarifJournalierAgent());
            parkingInfo.put("codeAcces", p.getCodeAcces() != null ? p.getCodeAcces() : "Non défini");
            result.add(parkingInfo);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/parkings/{id} - Récupérer un parking par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getParkingById(@PathVariable int id) {
        return parkingRepository.findById(id)
                .map(p -> {
                    Map<String, Object> parkingInfo = new LinkedHashMap<>();
                    parkingInfo.put("id", p.getIdParking());
                    parkingInfo.put("nom", p.getNom());
                    parkingInfo.put("adresse", p.getAdresse());
                    parkingInfo.put("ville", p.getVille());
                    parkingInfo.put("capacite", p.getCapacite());
                    parkingInfo.put("tarifJournalier", p.getTarifJournalierAgent());
                    parkingInfo.put("codeAcces", p.getCodeAcces());
                    parkingInfo.put("procedureAcces", p.getProcedureAcces());
                    parkingInfo.put("instructionsSpeciales", p.getInstructionsSpeciales());
                    return ResponseEntity.ok(parkingInfo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
