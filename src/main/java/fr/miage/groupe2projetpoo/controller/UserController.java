package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.utilisateur.Role;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        Role role = Role.valueOf(roleStr);
        Utilisateur user = userService.register(email, password, nom, prenom, role);

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
}
