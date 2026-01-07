package fr.miage.groupe2projetpoo.entity.utilisateur;

/**
 * Classe abstraite représentant un agent
 */
public abstract class Agent extends Utilisateur {

    public Agent() {
        super();
    }

    public Agent(String email, String password, String nom, String prenom) {
        super(email, password, nom, prenom);
    }
}
