package fr.miage.groupe2projetpoo.entity.assurance;

import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;

public class NHOptionParking extends OptionPayante {

    private int dureeMaxStationnement;
    private int nbJoursInclus;
    private double tarifPreference;
    private Parking parkingPartenaire;

    public OptionParking() {
        super();
    }

    public OptionParking(String nom, double tarifMensuel, boolean estActive,
            int dureeMaxStationnement, int nbJoursInclus, double tarifPreference, Parking parkingPartenaire) {
        super(nom, tarifMensuel, estActive);
        this.dureeMaxStationnement = dureeMaxStationnement;
        this.nbJoursInclus = nbJoursInclus;
        this.tarifPreference = tarifPreference;
        this.parkingPartenaire = parkingPartenaire;
    }

    public int getDureeMaxStationnement() {
        return dureeMaxStationnement;
    }

    public void setDureeMaxStationnement(int dureeMaxStationnement) {
        this.dureeMaxStationnement = dureeMaxStationnement;
    }

    public int getNbJoursInclus() {
        return nbJoursInclus;
    }

    public void setNbJoursInclus(int nbJoursInclus) {
        this.nbJoursInclus = nbJoursInclus;
    }

    public double getTarifPreference() {
        return tarifPreference;
    }

    public void setTarifPreference(double tarifPreference) {
        this.tarifPreference = tarifPreference;
    }

    public Parking getParkingPartenaire() {
        return parkingPartenaire;
    }

    public void setParkingPartenaire(Parking parkingPartenaire) {
        this.parkingPartenaire = parkingPartenaire;
    }

    // Méthodes métier

    public double calculerTarifPresentiel() {
        // Exemple de logique : si on dépasse les jours inclus, on paie le tarif
        // préférence
        // Ici, on retourne simplement le tarif préférentiel comme base de calcul pour
        // l'exemple
        return tarifPreference;
    }

    public boolean estEligible(Agent agent) {
        // Exemple de règle : l'agent doit avoir au moins un véhicule pour bénéficier du
        // parking
        return agent != null && agent.getVehicleList() != null && !agent.getVehicleList().isEmpty();
    }

    @Override
    public String toString() {
        return "OptionParking{" +
                "nom='" + getNom() + '\'' +
                ", tarifMensuel=" + getTarifMensuel() +
                ", dureeMaxStationnement=" + dureeMaxStationnement +
                ", nbJoursInclus=" + nbJoursInclus +
                ", tarifPreference=" + tarifPreference +
                ", parkingPartenaire=" + parkingPartenaire +
                '}';
    }
}
