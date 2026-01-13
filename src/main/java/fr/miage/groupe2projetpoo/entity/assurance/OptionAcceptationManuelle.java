package fr.miage.groupe2projetpoo.entity.assurance;

import java.util.Date;

/**
 * Option payante permettant à l'agent d'accepter manuellement les contrats.
 * L'agent dispose d'un délai de 6 heures pour accepter ou refuser un contrat
 * après la pré-signature du loueur.
 * 
 * Tarif fixe : 10€/mois
 */
public class OptionAcceptationManuelle extends OptionPayante {

    // Tarif fixe défini par la plateforme
    private static final double TARIF_MENSUEL = 10.0;
    
    // Délai d'acceptation : 6 heures en millisecondes
    private static final long DELAI_EXPIRATION_MS = 6 * 60 * 60 * 1000;

    public OptionAcceptationManuelle() {
        super("Acceptation manuelle des contrats", TARIF_MENSUEL);
    }

    /**
     * Calcule la date d'expiration à partir de la date de signature du loueur.
     * @param dateSignatureLoueur Date à laquelle le loueur a signé
     * @return Date limite pour que l'agent accepte le contrat
     */
    public Date calculerDateExpiration(Date dateSignatureLoueur) {
        return new Date(dateSignatureLoueur.getTime() + DELAI_EXPIRATION_MS);
    }

    /**
     * Vérifie si le délai d'acceptation est dépassé.
     * @param dateSignatureLoueur Date de signature du loueur
     * @return true si le délai est expiré
     */
    public boolean estExpire(Date dateSignatureLoueur) {
        Date expiration = calculerDateExpiration(dateSignatureLoueur);
        return new Date().after(expiration);
    }

    /**
     * Retourne le délai d'expiration en heures.
     */
    public static int getDelaiEnHeures() {
        return 6;
    }
}
