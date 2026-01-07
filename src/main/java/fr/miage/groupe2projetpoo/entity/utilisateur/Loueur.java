package fr.miage.groupe2projetpoo.entity.utilisateur;

/**
 * Loueur - propriétaire qui met en location ses véhicules
 */
public class Loueur extends Utilisateur {

    private String iban;
    private String nomSociete;

    public Loueur() {
        super();
    }

    public Loueur(String email, String password, String nom, String prenom,
            String iban, String nomSociete) {
        super(email, password, nom, prenom);
        this.iban = iban;
        this.nomSociete = nomSociete;
    }

    @Override
    public Role getRole() {
        return Role.LOUEUR;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }
}
