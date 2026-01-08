package fr.miage.groupe2projetpoo.entity.notation;

/**
 * Classe représentant la notation d'un véhicule
 */
public class NoteVehicule extends Notation {

    private double confort;
    private double proprete;

    public NoteVehicule() {
        super();
    }

    public NoteVehicule(int id, String authorEmail, String targetEmail, String commentaire, double confort, double proprete) {
        super(id, authorEmail, targetEmail, commentaire);
        this.confort = confort;
        this.proprete = proprete;
    }

    @Override
    public double calculerNoteGlobale() {
        return (confort + proprete) / 2.0;
    }

    // Getters et Setters

    public double getConfort() {
        return confort;
    }

    public void setConfort(double confort) {
        this.confort = confort;
    }

    public double getProprete() {
        return proprete;
    }

    public void setProprete(double proprete) {
        this.proprete = proprete;
    }
}
