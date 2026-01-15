package fr.miage.groupe2projetpoo.entity.utilisateur;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.assurance.OptionAcceptationManuelle;
import fr.miage.groupe2projetpoo.entity.assurance.OptionAssurancePersonnalisee;
import fr.miage.groupe2projetpoo.entity.assurance.OptionPayante;
import fr.miage.groupe2projetpoo.entity.notation.NoteAgent;
import java.util.ArrayList;
import java.util.List;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.entity.utilisateur.MaintenanceCompany;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classe abstraite représentant un agent
 */
public abstract class Agent extends Utilisateur {

    private List<NoteAgent> notations = new ArrayList<>();

    // Liste des véhicules gérés par l'agent (relation un-à-plusieurs)
    private List<Vehicle> vehicleList;

    // Liste des contrats de location gérés par l'agent (relation un-à-plusieurs)
    private List<RentalContract> contracts;

    // Liste des options payantes souscrites par l'agent
    private List<OptionPayante> optionsPayantes = new ArrayList<>();

    // Entreprise d'entretien préférée (pour l'automatisation)
    private MaintenanceCompany entrepriseEntretienPreferee;

    // === Parrainage (US.A - parrainage agent) ===

    // Référence au parrain (qui m'a parrainé)
    @JsonIgnore
    private Agent parrain;

    // Liste des filleuls (ceux que j'ai parrainés)
    @JsonIgnore
    private List<Agent> filleuls = new ArrayList<>();

    // Solde de parrainage (utilisable pour les options payantes)
    private double soldeParrainage = 0.0;

    // Indique si l'agent a eu au moins un véhicule loué (pour déclencher la
    // récompense)
    private boolean aEuVehiculeLoue = false;

    // Constructeur par défaut
    public Agent() {
        super();
        this.vehicleList = new ArrayList<>();
        this.contracts = new ArrayList<>();
        this.optionsPayantes = new ArrayList<>();
        this.entrepriseEntretienPreferee = null;
    }

    // Constructeur avec paramètres
    public Agent(String nom, String prenom, String password, String email, String tel) {
        super(nom, prenom, password, email, tel);
        this.vehicleList = new ArrayList<>();
        this.contracts = new ArrayList<>();
        this.optionsPayantes = new ArrayList<>();
        this.entrepriseEntretienPreferee = null;
    }

    // === Gestion des véhicules ===

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public void addVehicle(Vehicle vehicle) {
        if (this.vehicleList == null) {
            this.vehicleList = new ArrayList<>();
        }
        this.vehicleList.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        if (this.vehicleList != null) {
            this.vehicleList.remove(vehicle);
        }
    }

    // === Gestion des contrats ===

    public List<RentalContract> getContracts() {
        return contracts;
    }

    public void setContracts(List<RentalContract> contracts) {
        this.contracts = contracts;
    }

    public void addContract(RentalContract contract) {
        if (this.contracts == null) {
            this.contracts = new ArrayList<>();
        }
        this.contracts.add(contract);
        contract.setAgent(this); // Assurer la liaison bidirectionnelle
    }

    public void removeContract(RentalContract contract) {
        if (this.contracts != null) {
            this.contracts.remove(contract);
        }
    }

    // === Gestion des options payantes ===

    public List<OptionPayante> getOptionsPayantes() {
        return optionsPayantes;
    }

    public void setOptionsPayantes(List<OptionPayante> optionsPayantes) {
        this.optionsPayantes = optionsPayantes;
    }

    /**
     * Ajoute une option payante à l'agent
     */
    public void ajouterOption(OptionPayante option) {
        this.optionsPayantes.add(option);
    }

    /**
     * Retire une option payante de l'agent
     */
    public void retirerOption(OptionPayante option) {
        this.optionsPayantes.remove(option);
    }

    /**
     * Vérifie si l'agent a une option spécifique active
     * 
     * @param typeOption La classe de l'option à vérifier
     */
    public boolean aOptionActive(Class<? extends OptionPayante> typeOption) {
        for (OptionPayante opt : optionsPayantes) {
            if (typeOption.isInstance(opt) && opt.isEstActive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupère une option spécifique si elle existe
     */
    @SuppressWarnings("unchecked")
    public <T extends OptionPayante> T getOption(Class<T> typeOption) {
        for (OptionPayante opt : optionsPayantes) {
            if (typeOption.isInstance(opt)) {
                return (T) opt;
            }
        }
        return null;
    }

    /**
     * Vérifie si l'agent a l'option d'acceptation manuelle active
     */
    public boolean aAcceptationManuelle() {
        return aOptionActive(OptionAcceptationManuelle.class);
    }

    /**
     * Vérifie si l'agent a l'option assurance personnalisée active
     */
    public boolean aAssurancePersonnalisee() {
        return aOptionActive(OptionAssurancePersonnalisee.class);
    }

    /**
     * Récupère l'assurance personnalisée de l'agent si disponible
     * 
     * @return L'assurance personnalisée ou null si non définie
     */
    public Assurance getAssurancePersonnalisee() {
        OptionAssurancePersonnalisee option = getOption(OptionAssurancePersonnalisee.class);
        if (option != null && option.aAssuranceDefinie()) {
            return option.getAssuranceAgent();
        }
        return null;
    }

    /**
     * Calcule le total de la facture mensuelle pour toutes les options actives
     */
    public double calculerFactureMensuelle() {
        double total = 0;
        for (OptionPayante option : optionsPayantes) {
            total += option.calculerCoutMensuel();
        }
        return total;
    }

    public MaintenanceCompany getEntrepriseEntretienPreferee() {
        return entrepriseEntretienPreferee;
    }

    public void setEntrepriseEntretienPreferee(MaintenanceCompany entrepriseEntretienPreferee) {
        this.entrepriseEntretienPreferee = entrepriseEntretienPreferee;
    }

    // === Gestion des notations ===

    public List<NoteAgent> getNotations() {
        return notations;
    }

    public void setNotations(List<NoteAgent> notations) {
        this.notations = notations;
    }

    public void ajouterNotation(NoteAgent notation) {
        this.notations.add(notation);
    }

    public double calculerNoteMoyenne() {
        if (notations.isEmpty()) {
            return 0.0;
        }
        double somme = 0.0;
        for (NoteAgent note : notations) {
            somme += note.calculerNoteGlobale();
        }
        return somme / notations.size();
    }

    // === Gestion du parrainage (Agent) ===

    public Agent getParrain() {
        return parrain;
    }

    public void setParrain(Agent parrain) {
        this.parrain = parrain;
    }

    public List<Agent> getFilleuls() {
        if (this.filleuls == null) {
            this.filleuls = new ArrayList<>();
        }
        return filleuls;
    }

    public void setFilleuls(List<Agent> filleuls) {
        this.filleuls = filleuls;
    }

    public void addFilleul(Agent filleul) {
        if (this.filleuls == null) {
            this.filleuls = new ArrayList<>();
        }
        this.filleuls.add(filleul);
    }

    public double getSoldeParrainage() {
        return soldeParrainage;
    }

    public void setSoldeParrainage(double soldeParrainage) {
        this.soldeParrainage = soldeParrainage;
    }

    /**
     * Ajouter une récompense au solde de parrainage
     * 
     * @param montant Montant à ajouter
     */
    public void ajouterRecompenseParrainage(double montant) {
        this.soldeParrainage += montant;
    }

    /**
     * Utiliser le solde de parrainage pour une option payante
     * 
     * @param montant Montant à utiliser
     * @return true si le montant a pu être utilisé, false sinon
     */
    public boolean utiliserSoldeParrainage(double montant) {
        if (montant <= this.soldeParrainage) {
            this.soldeParrainage -= montant;
            return true;
        }
        return false;
    }

    public boolean isAEuVehiculeLoue() {
        return aEuVehiculeLoue;
    }

    public void setAEuVehiculeLoue(boolean aEuVehiculeLoue) {
        this.aEuVehiculeLoue = aEuVehiculeLoue;
    }

    /**
     * Obtenir l'email du parrain (pour éviter la sérialisation récursive)
     */
    public String getParrainEmail() {
        return parrain != null ? parrain.getEmail() : null;
    }

    /**
     * Obtenir le nombre de filleuls
     */
    public int getNombreFilleuls() {
        return filleuls != null ? filleuls.size() : 0;
    }
}
