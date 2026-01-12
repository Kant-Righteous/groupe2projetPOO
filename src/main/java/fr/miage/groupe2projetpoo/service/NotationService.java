package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.notation.Notation;
import fr.miage.groupe2projetpoo.entity.notation.NoteAgent;
import fr.miage.groupe2projetpoo.entity.notation.NoteLoueur;
import fr.miage.groupe2projetpoo.entity.notation.NoteVehicule;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.repository.NotationRepository;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import fr.miage.groupe2projetpoo.repository.VehicleRepository;
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
    private final VehicleRepository vehicleRepository;

    public NotationService(NotationRepository notationRepository, UserRepository userRepository,
            VehicleRepository vehicleRepository) {
        this.notationRepository = notationRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Ajouter une note à un agent
     */
    public NoteAgent addNoteAgent(String authorEmail, String targetEmail, String commentaire, double ponctualite,
            double communication) {
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
     * Modifier une note d'un agent
     */
    public NoteAgent updateNoteAgent(int id, String commentaire, double ponctualite, double communication) {
        Optional<Notation> noteOpt = notationRepository.findById(id);
        if (noteOpt.isPresent() && noteOpt.get() instanceof NoteAgent) {
            NoteAgent note = (NoteAgent) noteOpt.get();
            note.setCommentaire(commentaire);
            note.setPonctualite(ponctualite);
            note.setCommunication(communication);
            return (NoteAgent) notationRepository.save(note);
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
     * Modifier une note d'un loueur
     */
    public NoteLoueur updateNoteLoueur(int id, String commentaire, double respect) {
        Optional<Notation> noteOpt = notationRepository.findById(id);
        if (noteOpt.isPresent() && noteOpt.get() instanceof NoteLoueur) {
            NoteLoueur note = (NoteLoueur) noteOpt.get();
            note.setCommentaire(commentaire);
            note.setRespect(respect);
            return (NoteLoueur) notationRepository.save(note);
        }
        return null;
    }

    /**
     * Ajouter une note à un véhicule
     */
    public NoteVehicule addNoteVehicule(String authorEmail, String vehicleId, String commentaire, double confort,
            double proprete) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            // On utilise l'ID du véhicule comme targetEmail pour la cohérence de la
            // structure Notation
            NoteVehicule note = new NoteVehicule(0, authorEmail, vehicleId, commentaire, confort, proprete);
            note = (NoteVehicule) notationRepository.save(note);
            vehicle.ajouterNotation(note);
            return note;
        }
        return null;
    }

    /**
     * Modifier une note d'un véhicule
     */
    public NoteVehicule updateNoteVehicule(int id, String commentaire, double confort, double proprete) {
        Optional<Notation> noteOpt = notationRepository.findById(id);
        if (noteOpt.isPresent() && noteOpt.get() instanceof NoteVehicule) {
            NoteVehicule note = (NoteVehicule) noteOpt.get();
            note.setCommentaire(commentaire);
            note.setConfort(confort);
            note.setProprete(proprete);
            return (NoteVehicule) notationRepository.save(note);
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

            // Retirer la note de la liste de l'utilisateur ou du véhicule
            Optional<Utilisateur> userOpt = userRepository.findByEmail(targetEmail);
            if (userOpt.isPresent()) {
                Utilisateur user = userOpt.get();
                if (user instanceof Agent && note instanceof NoteAgent) {
                    ((Agent) user).getNotations().remove((NoteAgent) note);
                } else if (user instanceof Loueur && note instanceof NoteLoueur) {
                    ((Loueur) user).getNotations().remove((NoteLoueur) note);
                }
            } else {
                // Si ce n'est pas un utilisateur, c'est peut-être un véhicule
                try {
                    String vehicleId = targetEmail;
                    Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
                    if (vehicleOpt.isPresent() && note instanceof NoteVehicule) {
                        vehicleOpt.get().getNotations().remove((NoteVehicule) note);
                    }
                } catch (NumberFormatException e) {
                    // Pas un ID de véhicule valide
                }
            }

            notationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Calculer la note globale d'un véhicule
     */
    public double getAverageRatingForVehicle(String vehicleId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isPresent()) {
            return vehicleOpt.get().calculerNoteMoyenne();
        }
        return 0.0;
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
