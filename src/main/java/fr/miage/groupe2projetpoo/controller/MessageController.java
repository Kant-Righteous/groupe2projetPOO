package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.messagerie.Message;
import fr.miage.groupe2projetpoo.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion de la messagerie
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Envoyer un message - POST /api/messages
     */
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Map<String, String> request) {
        String senderEmail = request.get("senderEmail");
        String receiverEmail = request.get("receiverEmail");
        String contenu = request.get("contenu");

        if (senderEmail == null || receiverEmail == null || contenu == null) {
            return ResponseEntity.badRequest().build();
        }

        Message message = messageService.sendMessage(senderEmail, receiverEmail, contenu);
        return ResponseEntity.ok(message);
    }

    /**
     * Voir la conversation entre deux utilisateurs - GET /api/messages/conversation/{email1}/{email2}
     */
    @GetMapping("/conversation/{email1}/{email2}")
    public ResponseEntity<List<Message>> getConversation(@PathVariable String email1, @PathVariable String email2) {
        List<Message> conversation = messageService.getConversation(email1, email2);
        return ResponseEntity.ok(conversation);
    }
}
