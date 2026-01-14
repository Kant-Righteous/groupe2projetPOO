package fr.miage.groupe2projetpoo.entity.vehicule;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.notation.NoteVehicule;

import fr.miage.groupe2projetpoo.entity.maintenance.ControleTechnique;
import fr.miage.groupe2projetpoo.entity.maintenance.Entretien;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;

import java.time.LocalDate;
import java.util.List;

public abstract class Vehicle {
    // Propriétés
    private final String idVehicule;
    private String marqueVehicule;
    private String couleurVehicule;
    private String modeleVehicule;
    private String villeVehicule;
    private boolean estEnpause;
    private double prixVehiculeParJour;
    private String Proprietaire;
    private int kilometrageActuel; // US.A.11 et US.L.10
    // Maintenance (US.A.8, US.A.10)
    private ControleTechnique controleTechnique;
    private List<Entretien> historiqueEntretiens = new ArrayList<>();
    @JsonIgnore
    private Map<LocalDate, Boolean> disponibilites = new HashMap<>();
    @JsonIgnore
    private List<RentalContract> historiqueContrats = new ArrayList<>();
    private List<NoteVehicule> notations = new ArrayList<>();
    private List<Disponibilite> planningDisponible = new ArrayList<>();
    private String dernierLieuDepose; // Dernier lieu où le véhicule a été déposé

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

    /*
     * public Map<LocalDate, Boolean> getDisponibilites() {
     * return disponibilites;
     * }
     */

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

    public String getDernierLieuDepose() {
        return dernierLieuDepose;
    }

    public void setDernierLieuDepose(String dernierLieuDepose) {
        this.dernierLieuDepose = dernierLieuDepose;
    }

    /************************** Methodes ******************************/
    // === Méthodes pour les notations ===
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

    // === Calendrier === (utilise la Map de disponibilités)
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

    // Vérifier la disponibilité dans le planning
    public boolean estDisponible(LocalDate debut, LocalDate fin) {
        for (Disponibilite d : planningDisponible) {
            if (d.chevauchement(debut, fin)) {
                return false;
            }
        }
        return true;
    }

    // Alias pour compatibilité
    public boolean estDisponiblePlanning(LocalDate debut, LocalDate fin) {
        return estDisponible(debut, fin);
    }

    // Supprimer un créneau du planning
    public void removeCreneauPlanning(int index) {
        planningDisponible.remove(index);
    }
}
