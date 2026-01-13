package fr.miage.groupe2projetpoo.entity.assurance;

/**
 * Option payante permettant à l'agent de faire appel à une entreprise
 * d'entretien pour nettoyer ses véhicules.
 * 
 * Deux modes possibles :
 * - Ponctuel : l'agent demande un entretien quand il le souhaite
 * - Automatique : un entretien est planifié après chaque location
 * 
 * Tarif fixe : 20€/mois (automatique) ou 5€/mois (ponctuel)
 */
public class OptionEntretien extends OptionPayante {

    // Tarifs fixes définis par la plateforme
    private static final double TARIF_AUTOMATIQUE = 20.0;
    private static final double TARIF_PONCTUEL = 5.0;

    // Mode d'entretien
    private boolean automatique;

    /**
     * Constructeur pour entretien ponctuel (par défaut)
     */
    public OptionEntretien() {
        super("Entretien ponctuel", TARIF_PONCTUEL);
        this.automatique = false;
    }

    /**
     * Constructeur avec choix du mode
     * @param automatique true pour entretien automatique après chaque location
     */
    public OptionEntretien(boolean automatique) {
        super(automatique ? "Entretien automatique" : "Entretien ponctuel",
              automatique ? TARIF_AUTOMATIQUE : TARIF_PONCTUEL);
        this.automatique = automatique;
    }

    // Getters
    public boolean isAutomatique() {
        return automatique;
    }

    /**
     * Passe en mode automatique (entretien après chaque location)
     */
    public void activerModeAutomatique() {
        this.automatique = true;
        this.nom = "Entretien automatique";
        // Note: le tarif reste celui défini à la création pour cohérence de facturation
    }

    /**
     * Passe en mode ponctuel
     */
    public void activerModePonctuel() {
        this.automatique = false;
        this.nom = "Entretien ponctuel";
    }

    /**
     * Retourne le tarif pour le mode automatique
     */
    public static double getTarifAutomatique() {
        return TARIF_AUTOMATIQUE;
    }

    /**
     * Retourne le tarif pour le mode ponctuel
     */
    public static double getTarifPonctuel() {
        return TARIF_PONCTUEL;
    }
}
