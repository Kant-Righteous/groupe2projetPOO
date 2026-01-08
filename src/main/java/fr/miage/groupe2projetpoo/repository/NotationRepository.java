package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.notation.Notation;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour l'accès aux données de notation
 */
public interface NotationRepository {

    Notation save(Notation notation);

    List<Notation> findAll();

    Optional<Notation> findById(int id);

    List<Notation> findByTargetEmail(String targetEmail);

    void deleteById(int id);
}
