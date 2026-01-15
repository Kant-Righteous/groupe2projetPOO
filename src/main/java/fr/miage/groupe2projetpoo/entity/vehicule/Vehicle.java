package fr.miage.groupe2projetpoo.entity.vehicule;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.notation.NoteVehicule;

import fr.miage.groupe2projetpoo.entity.maintenance.ControleTechnique;
import fr.miage.groupe2projetpoo.entity.maintenance.Entretien;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import org.slf4j.spi.LocationAwareLogger;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public abstract class Vehicle {
    // Propriétés
    private String idVehicule;
    // private String typeVehicule;
    private String marqueVehicule;
    private String couleurVehicule;
    private String modeleVehicule;
    private String villeVehicule;
    private boolean estEnpause;
    private double prixVehiculeParJour;
    private String Proprietaire;
    private int kilometrageActuel; // US.A.11 et US.L.10

    // Coordonnées GPS du véhicule (pour la recherche par proximité)
    private Double latitude;
    private Double longitude;

    // Maintenance (US.A.8, US.A.10)
    private ControleTechnique controleTechnique;
    private List<Entretien> historiqueEntretiens = new ArrayList<>();
    @JsonIgnore
    private Map<LocalDate, Boolean> disponibilites = new HashMap<>();
    @JsonIgnore
    private List<RentalContract> historiqueContrats = new ArrayList<>();
    private List<NoteVehicule> notations = new ArrayList<>();
    private List<Disponibilite> planningDisponible = new ArrayList<>();

    // Constructeur
    public Vehicle(String idVehicule, String marqueVehicule,
            String couleurVehicule, String modeleVehicule, String villeVehicule,
            double prixVehiculeJour, String proprietaire, Boolean estEnpause) {
        this.idVehicule = idVehicule;
        this.marqueVehicule = marqueVehicule;
        this.couleurVehicule = couleurVehicule;
        this.modeleVehicule = modeleVehicule;
        this.villeVehicule = villeVehicule;
        this.estEnpause = estEnpause;
        this.prixVehiculeParJour = prixVehiculeJour;
        this.Proprietaire = proprietaire;
        this.kilometrageActuel = 0; // Défaut
        initiliserDisponibilites();
    }

    private void initiliserDisponibilites() {
        LocalDate dateDeb = LocalDate.now().withDayOfYear(1);
        LocalDate dateFin = dateDeb.plusYears(1);

        while (!dateDeb.isAfter(dateFin)) {
            disponibilites.put(dateDeb, true);
            dateDeb = dateDeb.plusDays(1);
        }
    }

    // Getters

    public abstract TypeVehicule getType();

    public String getIdVehicule() {
        return idVehicule;
    }

    public String getMarqueVehicule() {
        return marqueVehicule;
    }

    public String getCouleurVehicule() {
        return couleurVehicule;
    }

    public String getModeleVehicule() {
        return modeleVehicule;
    }

    public String getVilleVehicule() {
        return villeVehicule;
    }

    public List<NoteVehicule> getNotations() {
        return notations;
    }

    public double getPrixVehiculeParJour() {
        return prixVehiculeParJour;
    }

    public String getProprietaire() {
        return Proprietaire;
    }

    public Map<LocalDate, Boolean> getDisponibilites() {
        return disponibilites;
    }

    public boolean getEstEnpause() {
        return estEnpause;
    }

    public List<Disponibilite> getPlanningDisponible() {
        return planningDisponible;
    }

    public int getKilometrageActuel() {
        return kilometrageActuel;
    }

    /********************** SETTER **********************/
    public void setIdVehicule(String idV) {
        this.idVehicule = idV;
    }

    /*
     * public void setTypeVehicule(String type) {
     * this.typeVehicule = type;
     * }
     */
    public void setMarqueVehicule(String marque) {
        this.marqueVehicule = marque;
    }

    public void setCouleurVehicule(String couleur) {
        this.couleurVehicule = couleur;
    }

    public void setModeleVehicule(String modele) {
        this.modeleVehicule = modele;
    }

    public void setVilleVehicule(String ville) {
        this.villeVehicule = ville;
    }

    public void setPrixVehiculeParJour(double prixVehiculeJour) {
        this.prixVehiculeParJour = prixVehiculeJour;
    }

    public void setProprietaire(String proprietaire) {
        this.Proprietaire = proprietaire;
    }

    public void setNotations(List<NoteVehicule> notations) {
        this.notations = notations;
    }

    public void setEstEnpause(boolean estEnpause) {
        this.estEnpause = estEnpause;
    }

    public void setKilometrageActuel(int kilometrageActuel) {
        this.kilometrageActuel = kilometrageActuel;
    }

    /************************** Methodes ******************************/
    // Méthodes pour les notations
    public void ajouterNotation(NoteVehicule notation) {
        this.notations.add(notation);
    }

    public double calculerNoteMoyenne() {
        if (notations.isEmpty()) {
            return 0.0;
        }
        double somme = 0.0;
        for (NoteVehicule note : notations) {
            somme += note.calculerNoteGlobale();
        }
        return somme / notations.size();
    }

    // === Gestion Maintenance ===
    public ControleTechnique getControleTechnique() {
        return controleTechnique;
    }

    public void setControleTechnique(ControleTechnique controleTechnique) {
        this.controleTechnique = controleTechnique;
    }

    public List<Entretien> getHistoriqueEntretiens() {
        return historiqueEntretiens;
    }

    public void setHistoriqueEntretiens(
            List<Entretien> historiqueEntretiens) {
        this.historiqueEntretiens = historiqueEntretiens;
    }

    public void ajouterEntretien(Entretien entretien) {
        this.historiqueEntretiens.add(entretien);
    }

    // === Gestion de l'historique des contrats ===
    public List<RentalContract> getHistoriqueContrats() {
        return historiqueContrats;
    }

    public void ajouterContrat(RentalContract contrat) {
        this.historiqueContrats.add(contrat);
    }

    // === Calendrier === a ne pas utiliser pour l'instant
    public boolean estDisponibleMap(LocalDate debut, LocalDate fin) {
        LocalDate d = debut;
        while (!d.isAfter(fin)) {
            Boolean dispo = disponibilites.get(d);
            if (dispo == null || !dispo) {
                return false;
            }
            d = d.plusDays(1);
        }
        return true;
    }

    // === Ajouter planning de disponibilité ===
    public void addPlanningDispo(LocalDate debut, LocalDate fin) {
        for (Disponibilite d : planningDisponible) {
            if (d.chevauchement(debut, fin)) {
                throw new IllegalArgumentException("Créneau déjà occupé");
            }
        }
        planningDisponible.add(new Disponibilite(debut, fin));
    }

    // verifier la disponibilité dans planning
    public boolean estDisponiblePlanning(LocalDate debut, LocalDate fin) {
        for (Disponibilite d : planningDisponible) {
            if (d.chevauchement(debut, fin)) {
                return false;
            }
        }
        return true;
    }

    // Suprimer Planning
    public void removeCreneauPlanning(int index) {
        planningDisponible.remove(index);
    }

    /**
     * Récupère le dernier lieu de dépose du véhicule basé sur l'historique des
     * contrats.
     * Si aucun contrat terminé n'est trouvé, retourne la ville du véhicule.
     */
    public String getDernierLieuDepose() {
        if (historiqueContrats == null || historiqueContrats.isEmpty()) {
            return this.villeVehicule;
        }

        RentalContract dernierContrat = null;
        for (RentalContract c : historiqueContrats) {
            // On cherche le contrat terminé le plus récent
            if (c.estTerminee()) {
                if (dernierContrat == null || c.getDateFin().after(dernierContrat.getDateFin())) {
                    dernierContrat = c;
                }
            }
        }

        if (dernierContrat != null) {
            return dernierContrat.getLieuDepose();
        }

        return this.villeVehicule;
    }

    // === Coordonnées GPS ===

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Définit les coordonnées GPS en une seule opération
     */
    public void setCoordonnees(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
