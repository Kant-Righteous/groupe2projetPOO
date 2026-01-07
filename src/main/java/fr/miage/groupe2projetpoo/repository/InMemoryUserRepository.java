package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implémentation en mémoire du repository utilisateur
 */
@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, Utilisateur> users = new ConcurrentHashMap<>();

    /**
     * Initialisation des données de test
     */
    @PostConstruct
    public void init() {
        // 2 AgentParticulier
        save(new AgentParticulier("alice@test.com", "123456", "Martin", "Alice"));
        save(new AgentParticulier("bob@test.com", "123456", "Dupont", "Bob"));

        // 2 AgentProfessionnel
        save(new AgentProfessionnel("enterprise1@test.com", "123456", "Durand", "Pierre", "Durand SA",
                "12345678901234"));
        save(new AgentProfessionnel("enterprise2@test.com", "123456", "Moreau", "Marie", "Moreau SARL",
                "98765432109876"));

        // 2 Loueur
        save(new Loueur("loueur1@test.com", "123456", "Bernard", "Luc", "FR7630001007941234567890185", null));
        save(new Loueur("loueur2@test.com", "123456", "Petit", "Sophie", "FR7630004000031234567890143", null));
    }

    @Override
    public Utilisateur save(Utilisateur user) {
        users.put(user.getEmail(), user);
        return user;
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.containsKey(email);
    }
}
