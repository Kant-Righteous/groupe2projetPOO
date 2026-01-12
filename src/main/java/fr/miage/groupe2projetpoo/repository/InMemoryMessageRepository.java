package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.messagerie.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Implémentation en mémoire du repository de messagerie
 */
@Repository
public class InMemoryMessageRepository implements MessageRepository {

    private final List<Message> messages = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public Message save(Message message) {
        if (message.getIdM() == 0) {
            message.setIdM(idGenerator.getAndIncrement());
        }
        messages.add(message);
        return message;
    }

    @Override
    public List<Message> findBySenderEmail(String email) {
        return messages.stream()
                .filter(m -> email.equals(m.getSenderEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findByReceiverEmail(String email) {
        return messages.stream()
                .filter(m -> email.equals(m.getReceiverEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findConversation(String email1, String email2) {
        return messages.stream()
                .filter(m -> (email1.equals(m.getSenderEmail()) && email2.equals(m.getReceiverEmail())) ||
                             (email2.equals(m.getSenderEmail()) && email1.equals(m.getReceiverEmail())))
                .sorted((m1, m2) -> m1.getDateEnvoi().compareTo(m2.getDateEnvoi()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findAllForUser(String email) {
        return messages.stream()
                .filter(m -> email.equals(m.getSenderEmail()) || email.equals(m.getReceiverEmail()))
                .collect(Collectors.toList());
    }
}
