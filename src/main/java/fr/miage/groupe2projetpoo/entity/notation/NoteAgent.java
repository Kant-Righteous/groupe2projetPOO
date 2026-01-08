package fr.miage.groupe2projetpoo.entity.notation;

/**
 * Classe représentant la notation d'un agent
 */
public class NoteAgent extends Notation {

    private double ponctualite;
    private double communication;

    public NoteAgent() {
        super();
    }

    public NoteAgent(int id, String authorEmail, String targetEmail, String commentaire, double ponctualite, double communication) {
        super(id, authorEmail, targetEmail, commentaire);
        this.ponctualite = ponctualite;
        this.communication = communication;
    }

    @Override
    public double calculerNoteGlobale() {
        return (ponctualite + communication) / 2.0;
    }

    // Getters et Setters

    public double getPonctualite() {
        return ponctualite;
    }

    public void setPonctualite(double ponctualite) {
        this.ponctualite = ponctualite;
    }

    public double getCommunication() {
        return communication;
    }

    public void setCommunication(double communication) {
        this.communication = communication;
    }
}