package fr.miage.groupe2projetpoo.entity.location;

/**
 * Énumération des différents statuts possibles d'un contrat de location.
 * Représente le cycle de vie complet d'une location de véhicule.
 */
public enum StatutLocation {
    /**
     * Contrat créé, en attente de la signature du loueur
     */
    EN_ATTENTE_SIGNATURE,

    /**
     * Contrat signé par les deux parties, en attente de la prise du véhicule
     * Le loueur doit maintenant récupérer le véhicule et renseigner le kilométrage de départ
     */
    SIGNE,

    /**
     * Location en cours : le véhicule a été récupéré (kilométrage départ renseigné)
     * Le loueur utilise actuellement le véhicule
     */
    EN_COURS,

    /**
     * Location terminée : le véhicule a été rendu (kilométrage fin renseigné)
     * Le contrat est clôturé
     */
    TERMINEE,

    /**
     * Contrat annulé (par le loueur ou l'agent)
     */
    ANNULEE
}
