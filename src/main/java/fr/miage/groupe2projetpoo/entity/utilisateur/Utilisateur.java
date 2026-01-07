package fr.miage.groupe2projetpoo.entity.utilisateur;

/**
 * Classe abstraite représentant un utilisateur
 */
public abstract class Utilisateur {

    protected String email;
    protected String password;
    protected String nom;
    protected String prenom;

    public Utilisateur() {
    }

    public Utilisateur(String email, String password, String nom, String prenom) {
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * Retourne le rôle de l'utilisateur
     */
    public abstract Role getRole();

    // Getters et Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
