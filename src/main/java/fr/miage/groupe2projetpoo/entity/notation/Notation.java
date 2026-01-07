package fr.miage.groupe2projetpoo.entity.notation;

/**
 * Classe abstraite représentant une notation
 */
public abstract class Notation {

    private int id;

    public Notation() {
    }

    public Notation(int id) {
        this.id = id;
    }

    /**
     * Calcule et retourne la note globale
     */
    public abstract double calculerNoteGlobale();

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
