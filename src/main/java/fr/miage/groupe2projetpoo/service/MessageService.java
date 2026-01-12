package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.messagerie.Message;
import fr.miage.groupe2projetpoo.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service pour la gestion de la messagerie interne
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Envoyer un message avec vérification automatique du contenu
     */
    public Message sendMessage(String senderEmail, String receiverEmail, String contenu) {
        String contenuVerifie = censurerContenu(contenu);
        Message message = new Message(0, senderEmail, receiverEmail, contenuVerifie);
        return messageRepository.save(message);
    }

    /**
     * Récupérer la conversation entre deux utilisateurs
     */
    public List<Message> getConversation(String email1, String email2) {
        return messageRepository.findConversation(email1, email2);
    }

    /**
     * Filtre le contenu pour remplacer les emails et numéros de téléphone par [CENSURÉ]
     */
    private String censurerContenu(String contenu) {
        if (contenu == null) return "";

        // Regex pour les emails
        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        
        // Regex pour les numéros de téléphone (formats français courants)
        // 0612345678, 06 12 34 56 78, +33 6 12 34 56 78, 01.23.45.67.89, etc.
        String telRegex = "(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}";

        String contenuCensure = contenu.replaceAll(emailRegex, "[CENSURÉ]");
        contenuCensure = contenuCensure.replaceAll(telRegex, "[CENSURÉ]");

        return contenuCensure;
    }
}
