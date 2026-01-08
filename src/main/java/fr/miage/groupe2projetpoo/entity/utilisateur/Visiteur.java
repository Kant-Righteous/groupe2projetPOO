package fr.miage.groupe2projetpoo.entity.utilisateur;

/**
 * Visiteur - utilisateur non connecté
 */
public class Visiteur extends Utilisateur {

    public Visiteur() {
        super();
        setEmail("anonymous");
        setNom("Visiteur");
        setPrenom("Anonyme");
        setTel("");
    }

    @Override
    public Role getRole() {
        return Role.VISITEUR;
    }
}
