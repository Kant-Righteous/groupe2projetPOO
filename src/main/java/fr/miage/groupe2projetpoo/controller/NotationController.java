package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.notation.Notation;
import fr.miage.groupe2projetpoo.entity.notation.NoteAgent;
import fr.miage.groupe2projetpoo.entity.notation.NoteLoueur;
import fr.miage.groupe2projetpoo.service.NotationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des notations
 */
@RestController
@RequestMapping("/api/notations")
public class NotationController {

    private final NotationService notationService;

    public NotationController(NotationService notationService) {
        this.notationService = notationService;
    }

    /**
     * Ajouter une note à un agent - POST /api/notations/agent
     */
    @PostMapping("/agent")
    public ResponseEntity<Map<String, Object>> addNoteAgent(@RequestBody Map<String, Object> request) {
        try {
            String authorEmail = (String) request.get("authorEmail");
            String targetEmail = (String) request.get("targetEmail");
            String commentaire = (String) request.get("commentaire");
            double ponctualite = Double.parseDouble(request.get("ponctualite").toString());
            double communication = Double.parseDouble(request.get("communication").toString());

            NoteAgent note = notationService.addNoteAgent(authorEmail, targetEmail, commentaire, ponctualite, communication);

            if (note != null) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Note ajoutée à l'agent", "id", note.getId()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Erreur : agent introuvable"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Erreur format de données"));
        }
    }

    /**
     * Ajouter une note à un loueur - POST /api/notations/loueur
     */
    @PostMapping("/loueur")
    public ResponseEntity<Map<String, Object>> addNoteLoueur(@RequestBody Map<String, Object> request) {
        try {
            String authorEmail = (String) request.get("authorEmail");
            String targetEmail = (String) request.get("targetEmail");
            String commentaire = (String) request.get("commentaire");
            double respect = Double.parseDouble(request.get("respect").toString());

            NoteLoueur note = notationService.addNoteLoueur(authorEmail, targetEmail, commentaire, respect);

            if (note != null) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Note ajoutée au loueur", "id", note.getId()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Erreur : loueur introuvable"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Erreur format de données"));
        }
    }

    /**
     * Consulter les notes d'un utilisateur - GET /api/notations/user/{email}
     */
    @GetMapping("/user/{email}")
    public ResponseEntity<List<Notation>> getNotationsForUser(@PathVariable String email) {
        return ResponseEntity.ok(notationService.getNotationsForUser(email));
    }

    /**
     * Consulter la note moyenne - GET /api/notations/user/{email}/average
     */
    @GetMapping("/user/{email}/average")
    public ResponseEntity<Map<String, Object>> getAverageRating(@PathVariable String email) {
        double average = notationService.getAverageRating(email);
        return ResponseEntity.ok(Map.of("email", email, "average", average));
    }

    /**
     * Supprimer une note - DELETE /api/notations/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteNotation(@PathVariable int id) {
        boolean deleted = notationService.deleteNotation(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Note supprimée"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
