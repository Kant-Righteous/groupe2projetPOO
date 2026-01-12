package fr.miage.groupe2projetpoo.entity.notation;

import java.time.LocalDateTime;

/**
 * Classe abstraite représentant une notation
 */
public abstract class Notation {

    private int id;
    private String authorEmail;
    private String targetEmail;
    private LocalDateTime date;
    private String commentaire;
    private String reponse;
    private LocalDateTime dateReponse;

    public Notation() {
        this.date = LocalDateTime.now();
    }

    public Notation(int id, String authorEmail, String targetEmail, String commentaire) {
        this.id = id;
        this.authorEmail = authorEmail;
        this.targetEmail = targetEmail;
        this.commentaire = commentaire;
        this.date = LocalDateTime.now();
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

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getTargetEmail() {
        return targetEmail;
    }

    public void setTargetEmail(String targetEmail) {
        this.targetEmail = targetEmail;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public LocalDateTime getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(LocalDateTime dateReponse) {
        this.dateReponse = dateReponse;
    }
}