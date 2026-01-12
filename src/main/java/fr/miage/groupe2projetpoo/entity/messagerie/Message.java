package fr.miage.groupe2projetpoo.entity.messagerie;

import java.time.LocalDateTime;

/**
 * Classe représentant un message interne
 */
public class Message {

    private int idM;
    private String senderEmail;
    private String receiverEmail;
    private String contenu;
    private LocalDateTime dateEnvoi;

    public Message() {
        this.dateEnvoi = LocalDateTime.now();
    }

    public Message(int idM, String senderEmail, String receiverEmail, String contenu) {
        this.idM = idM;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.contenu = contenu;
        this.dateEnvoi = LocalDateTime.now();
    }

    // Getters et Setters

    public int getIdM() {
        return idM;
    }

    public void setIdM(int idM) {
        this.idM = idM;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
}
