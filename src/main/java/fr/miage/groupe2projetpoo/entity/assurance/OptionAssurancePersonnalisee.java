package fr.miage.groupe2projetpoo.entity.assurance;

/**
 * Option payante permettant aux agents professionnels de proposer
 * leur propre assurance pour leurs véhicules au lieu de l'assurance
 * AZA par défaut de la plateforme.
 * 
 * Note : Réservée aux agents professionnels uniquement.
 * 
 * Tarif fixe : 15€/mois
 */
public class OptionAssurancePersonnalisee extends OptionPayante {

    // Tarif fixe défini par la plateforme
    private static final double TARIF_MENSUEL = 15.0;

    // L'assurance personnalisée de l'agent
    private Assurance assuranceAgent;

    public OptionAssurancePersonnalisee() {
        super("Assurance personnalisée", TARIF_MENSUEL);
    }

    public OptionAssurancePersonnalisee(Assurance assuranceAgent) {
        super("Assurance personnalisée", TARIF_MENSUEL);
        this.assuranceAgent = assuranceAgent;
    }

    // Getters et Setters
    public Assurance getAssuranceAgent() {
        return assuranceAgent;
    }

    public void setAssuranceAgent(Assurance assuranceAgent) {
        this.assuranceAgent = assuranceAgent;
    }

    /**
     * Vérifie si une assurance personnalisée a été définie.
     */
    public boolean aAssuranceDefinie() {
        return assuranceAgent != null;
    }
}
