package fr.miage.groupe2projetpoo.entity.utilisateur;

import fr.miage.groupe2projetpoo.entity.notation.NoteLoueur;
import java.util.ArrayList;
import java.util.List;

/**
 * Loueur - propriétaire qui met en location ses véhicules
 */
public class Loueur extends Utilisateur {

    private String iban;
    private String nomSociete;
    private List<NoteLoueur> notations = new ArrayList<>();

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

    public List<NoteLoueur> getNotations() {
        return notations;
    }

    public void setNotations(List<NoteLoueur> notations) {
        this.notations = notations;
    }

    // Méthodes pour les notations
    public void ajouterNotation(NoteLoueur notation) {
        this.notations.add(notation);
    }

    public double calculerNoteMoyenne() {
        if (notations.isEmpty()) {
            return 0.0;
        }
        double somme = 0.0;
        for (NoteLoueur note : notations) {
            somme += note.calculerNoteGlobale();
        }
        return somme / notations.size();
    }
}
