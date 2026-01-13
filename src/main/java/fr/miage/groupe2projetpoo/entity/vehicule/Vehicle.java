package fr.miage.groupe2projetpoo.entity.vehicule;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.notation.NoteVehicule;

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

    /*
     * public String getTypeVehicule() {
     * return typeVehicule;
     * }
     */

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

    /*
     * public boolean getEstDisponible() {
     * return estDisponible;
     * }
     */

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

    /*
     * public void setEstDisponible(boolean estDisponible) {
     * this.estDisponible = estDisponible;
     * }
     */

    public void setPrixVehiculeParJour(double prixVehiculeJour) {
        this.prixVehiculeParJour = prixVehiculeJour;
    }

    public void setProprietaire(String proprietaire) {
        this.Proprietaire = proprietaire;
    }

    public boolean estDisponible(LocalDate debut, LocalDate fin) {
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

    public void setNotations(List<NoteVehicule> notations) {
        this.notations = notations;
    }

    public void setEstEnpause(boolean estEnpause) {
        this.estEnpause = estEnpause;
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

    // === Gestion de l'historique des contrats ===
    public List<RentalContract> getHistoriqueContrats() {
        return historiqueContrats;
    }

    public void ajouterContrat(RentalContract contrat) {
        this.historiqueContrats.add(contrat);
    }

    // === Ajouter planning de disponibilité
    public void AddPlanningDispo(LocalDate debut, LocalDate fin){
        for(Disponibilite d : planningDisponible){
            if(d.chevauchement(debut, fin)){
                throw new IllegalArgumentException("Créneau déjà occupé");
            }
        }
        planningDisponible.add(new Disponibilite(debut,fin));
    }

    // verifier la disponibilité dans planning
    public boolean estDiponible(LocalDate debut, LocalDate fin) {
        for(Disponibilite d: planningDisponible){
            if(d.chevauchement(debut, fin)){
                return false;
            }
        }
        return true;
    }
}
