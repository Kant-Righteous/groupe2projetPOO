package fr.miage.groupe2projetpoo.entity.utilisateur;

/**
 * Visiteur - utilisateur non connecté
 */
public class Visiteur extends Utilisateur {

    public Visiteur() {
        super();
        this.email = "anonymous";
        this.nom = "Visiteur";
        this.prenom = "Anonyme";
    }

    @Override
    public Role getRole() {
        return Role.VISITEUR;
    }
}
