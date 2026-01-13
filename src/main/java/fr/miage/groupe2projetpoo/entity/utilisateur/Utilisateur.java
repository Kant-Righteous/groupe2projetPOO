package fr.miage.groupe2projetpoo.entity.utilisateur;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classe abstraite représentant un utilisateur
 */
public abstract class Utilisateur {

    private String nom;
    private String prenom;
    @JsonIgnore
    private String password;
    private String email;
    private String tel;

    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String password, String email, String tel) {
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.email = email;
        this.tel = tel;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
