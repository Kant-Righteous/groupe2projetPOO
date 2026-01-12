package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.Role;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des utilisateurs
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Inscription - POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String nom = request.get("nom");
        String prenom = request.get("prenom");
        String roleStr = request.get("role");
        String tel = request.get("tel");

        Role role = Role.valueOf(roleStr);
        Utilisateur user = userService.register(nom, prenom, password, email, tel, role);

        if (user != null) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Inscription réussie",
                    "email", user.getEmail()));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Échec de l'inscription, email déjà existant"));
        }
    }

    /**
     * Connexion - POST /api/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        Utilisateur user = userService.login(email, password);

        if (user != null) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Connexion réussie",
                    "email", user.getEmail(),
                    "role", user.getRole().toString()));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Échec de la connexion, email ou mot de passe incorrect"));
        }
    }

    /**
     * Récupérer les véhicules d'un utilisateur - GET /api/users/{email}/vehicles
     */
    @GetMapping("/{email}/vehicles")
    public ResponseEntity<Map<String, Object>> getVehiclesByUser(@PathVariable String email) {
        List<Vehicle> vehicles = userService.getVehiclesByUserEmail(email);

        // Transformer les véhicules en Map pour la réponse JSON
        List<Map<String, Object>> vehicleList = vehicles.stream().map(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", v.getIdVehicule());
            map.put("type", v.getTypeVehicule());
            map.put("marque", v.getMarqueVehicule());
            map.put("modele", v.getModeleVehicule());
            map.put("couleur", v.getCouleurVehicule());
            map.put("ville", v.getVilleVehicule());
            // map.put("disponible", v.getEstDisponible());
            map.put("prixJour", v.getPrixVehiculeParJour());
            return map;
        }).toList();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "email", email,
                "count", vehicleList.size(),
                "vehicles", vehicleList));
    }

    /**
     * Récupérer les contrats d'un utilisateur - GET /api/users/{email}/contracts
     */
    @GetMapping("/{email}/contracts")
    public ResponseEntity<Map<String, Object>> getContractsByUser(@PathVariable String email) {
        List<RentalContract> contracts = userService.getContractsByUserEmail(email);

        // Transformer les contrats en Map pour la réponse JSON
        List<Map<String, Object>> contractList = contracts.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getIdC());
            map.put("dateCreation", c.getDateCréationContrat());
            map.put("dateDebut", c.getDateDebut());
            map.put("dateFin", c.getDateFin());
            map.put("lieuPrise", c.getLieuPrise());
            map.put("lieuDepose", c.getLieuDepose());
            map.put("statut", c.isStatut());
            map.put("prixTotal", c.getPrixTotal());
            map.put("signatureLoueur", c.isSignatureLoueur());
            map.put("signatureAgent", c.isSignatureAgent());

            // Info véhicule
            if (c.getVehicule() != null) {
                Map<String, Object> vehicleInfo = new HashMap<>();
                vehicleInfo.put("id", c.getVehicule().getIdVehicule());
                vehicleInfo.put("marque", c.getVehicule().getMarqueVehicule());
                vehicleInfo.put("modele", c.getVehicule().getModeleVehicule());
                map.put("vehicle", vehicleInfo);
            }

            // Info assurance
            if (c.getAssurance() != null) {
                Map<String, Object> assuranceInfo = new HashMap<>();
                assuranceInfo.put("id", c.getAssurance().getIdA());
                assuranceInfo.put("nom", c.getAssurance().getNom());
                assuranceInfo.put("tarif", c.getAssurance().getTarifBase());
                map.put("assurance", assuranceInfo);
            }

            return map;
        }).toList();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "email", email,
                "count", contractList.size(),
                "contracts", contractList));
    }
}
