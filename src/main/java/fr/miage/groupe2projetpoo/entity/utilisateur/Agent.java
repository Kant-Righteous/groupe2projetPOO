package fr.miage.groupe2projetpoo.entity.utilisateur;

import fr.miage.groupe2projetpoo.entity.notation.NoteAgent;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant un agent
 */
public abstract class Agent extends Utilisateur {

    private List<NoteAgent> notations = new ArrayList<>();

    public Agent() {
        super();
    }

    public Agent(String email, String password, String nom, String prenom) {
        super(email, password, nom, prenom);
    }

    public List<NoteAgent> getNotations() {
        return notations;
    }

    public void setNotations(List<NoteAgent> notations) {
        this.notations = notations;
    }

    // Méthodes pour les notations
    public void ajouterNotation(NoteAgent notation) {
        this.notations.add(notation);
    }

    public double calculerNoteMoyenne() {
        if (notations.isEmpty()) {
            return 0.0;
        }
        double somme = 0.0;
        for (NoteAgent note : notations) {
            somme += note.calculerNoteGlobale();
        }
        return somme / notations.size();
    }
}
