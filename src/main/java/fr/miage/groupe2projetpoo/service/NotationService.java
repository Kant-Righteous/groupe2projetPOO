package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.notation.Notation;
import fr.miage.groupe2projetpoo.entity.notation.NoteAgent;
import fr.miage.groupe2projetpoo.entity.notation.NoteLoueur;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.repository.NotationRepository;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des notations
 */
@Service
public class NotationService {

    private final NotationRepository notationRepository;
    private final UserRepository userRepository;

    public NotationService(NotationRepository notationRepository, UserRepository userRepository) {
        this.notationRepository = notationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Ajouter une note à un agent
     */
    public NoteAgent addNoteAgent(String authorEmail, String targetEmail, String commentaire, double ponctualite, double communication) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(targetEmail);
        if (userOpt.isPresent() && userOpt.get() instanceof Agent) {
            Agent agent = (Agent) userOpt.get();
            NoteAgent note = new NoteAgent(0, authorEmail, targetEmail, commentaire, ponctualite, communication);
            note = (NoteAgent) notationRepository.save(note);
            agent.ajouterNotation(note);
            return note;
        }
        return null;
    }

    /**
     * Ajouter une note à un loueur
     */
    public NoteLoueur addNoteLoueur(String authorEmail, String targetEmail, String commentaire, double respect) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(targetEmail);
        if (userOpt.isPresent() && userOpt.get() instanceof Loueur) {
            Loueur loueur = (Loueur) userOpt.get();
            NoteLoueur note = new NoteLoueur(0, authorEmail, targetEmail, commentaire, respect);
            note = (NoteLoueur) notationRepository.save(note);
            loueur.ajouterNotation(note);
            return note;
        }
        return null;
    }

    /**
     * Consulter les notes d'un utilisateur
     */
    public List<Notation> getNotationsForUser(String email) {
        return notationRepository.findByTargetEmail(email);
    }

    /**
     * Supprimer une note
     */
    public boolean deleteNotation(int id) {
        Optional<Notation> noteOpt = notationRepository.findById(id);
        if (noteOpt.isPresent()) {
            Notation note = noteOpt.get();
            String targetEmail = note.getTargetEmail();
            
            // Retirer la note de la liste de l'utilisateur
            Optional<Utilisateur> userOpt = userRepository.findByEmail(targetEmail);
            if (userOpt.isPresent()) {
                Utilisateur user = userOpt.get();
                if (user instanceof Agent && note instanceof NoteAgent) {
                    ((Agent) user).getNotations().remove((NoteAgent) note);
                } else if (user instanceof Loueur && note instanceof NoteLoueur) {
                    ((Loueur) user).getNotations().remove((NoteLoueur) note);
                }
            }
            
            notationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Calculer la note globale d'un utilisateur
     * Note: Les entités Agent et Loueur ont déjà une méthode calculerNoteMoyenne()
     */
    public double getAverageRating(String email) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            if (user instanceof Agent) {
                return ((Agent) user).calculerNoteMoyenne();
            } else if (user instanceof Loueur) {
                return ((Loueur) user).calculerNoteMoyenne();
            }
        }
        return 0.0;
    }
}
