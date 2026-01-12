package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.messagerie.Message;
import java.util.List;

/**
 * Interface pour l'accès aux données de messagerie
 */
public interface MessageRepository {

    Message save(Message message);

    List<Message> findBySenderEmail(String email);

    List<Message> findByReceiverEmail(String email);

    List<Message> findConversation(String email1, String email2);

    List<Message> findAllForUser(String email);
}
