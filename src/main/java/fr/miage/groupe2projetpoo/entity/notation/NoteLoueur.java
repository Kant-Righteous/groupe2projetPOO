package fr.miage.groupe2projetpoo.entity.notation;

/**
 * Classe représentant la notation d'un loueur
 */
public class NoteLoueur extends Notation {

    private double respect;

    public NoteLoueur() {
        super();
    }

    public NoteLoueur(int id, String authorEmail, String targetEmail, String commentaire, double respect) {
        super(id, authorEmail, targetEmail, commentaire);
        this.respect = respect;
    }

    @Override
    public double calculerNoteGlobale() {
        return respect;
    }

    // Getters et Setters

    public double getRespect() {
        return respect;
    }

    public void setRespect(double respect) {
        this.respect = respect;
    }
}