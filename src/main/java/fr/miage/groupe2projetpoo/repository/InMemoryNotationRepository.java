package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.notation.Notation;
import fr.miage.groupe2projetpoo.entity.notation.NoteAgent;
import fr.miage.groupe2projetpoo.entity.notation.NoteLoueur;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Implémentation en mémoire du repository de notation
 */
@Repository
public class InMemoryNotationRepository implements NotationRepository {

    private final Map<Integer, Notation> notations = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public Notation save(Notation notation) {
        if (notation.getId() == 0) {
            notation.setId(idGenerator.getAndIncrement());
        }
        notations.put(notation.getId(), notation);
        return notation;
    }

    @Override
    public List<Notation> findAll() {
        return new ArrayList<>(notations.values());
    }

    @Override
    public Optional<Notation> findById(int id) {
        return Optional.ofNullable(notations.get(id));
    }

    @Override
    public List<Notation> findByTargetEmail(String targetEmail) {
        return notations.values().stream()
                .filter(n -> targetEmail.equals(n.getTargetEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(int id) {
        notations.remove(id);
    }
}
