package fr.miage.groupe2projetpoo.entity.location;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.assurance.OptionAcceptationManuelle;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RentalContract {
    private int idC;
    private Date dateCréationContrat;
    private boolean statut;
    private String fichierPDF;
    private boolean SignatureLoueur;
    private boolean SignatureAgent;
    private Date dateSignatureLoueur;
    private Date dateSignatureAgent;
    private Date dateDebut;
    private Date dateFin;
    private String lieuPrise;
    private String lieuDepose;
    private double prixTotal;
    private double montantAgent;
    private double montantPlatforme;
    private double commissionPourcentage;
    private double commissionFixeParJour;
    @JsonIgnoreProperties({ "historiqueContrats", "disponibilites", "notations", "proprietaire" })
    private Vehicle Vehicule;

    // Use JsonIgnoreProperties to avoid infinite recursion AND to keep the output
    // clean
    @JsonIgnoreProperties({ "vehicles", "contracts", "password", "notations", "iban", "nomSociete" })
    private Loueur loueur;
    private Agent agent;
    private Assurance assurance;

    // Attribut pour l'acceptation manuelle (option payante)
    private Date dateExpiration;  // Date limite pour que l'agent accepte (6h après signature loueur)

    // US.L.10 - Kilométrage et preuves photos
    private Integer kilometrageDebut;       // KM à la prise du véhicule
    private String photoKilometrageDebut;   // Nom du fichier photo de preuve (départ)
    private Date dateRenseignementDebut;    // Date/heure de renseignement du KM départ

    private Integer kilometrageFin;         // KM au retour du véhicule
    private String photoKilometrageFin;     // Nom du fichier photo de preuve (retour)
    private Date dateRenseignementFin;      // Date/heure de renseignement du KM retour

    // Constructeur par défaut (nécessaire pour Jackson JSON)
    public RentalContract() {
    }

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public Date getDateCréationContrat() {
        return dateCréationContrat;
    }

    public void setDateCréationContrat(Date dateCréationContrat) {
        this.dateCréationContrat = dateCréationContrat;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    public String getFichierPDF() {
        return fichierPDF;
    }

    public void setFichierPDF(String fichierPDF) {
        this.fichierPDF = fichierPDF;
    }

    public boolean isSignatureLoueur() {
        return SignatureLoueur;
    }

    public void setSignatureLoueur(boolean signatureLoueur) {
        SignatureLoueur = signatureLoueur;
    }

    public boolean isSignatureAgent() {
        return SignatureAgent;
    }

    public void setSignatureAgent(boolean signatureAgent) {
        SignatureAgent = signatureAgent;
    }

    public Date getDateSignatureLoueur() {
        return dateSignatureLoueur;
    }

    public void setDateSignatureLoueur(Date dateSignatureLoueur) {
        this.dateSignatureLoueur = dateSignatureLoueur;
    }

    public Date getDateSignatureAgent() {
        return dateSignatureAgent;
    }

    public void setDateSignatureAgent(Date dateSignatureAgent) {
        this.dateSignatureAgent = dateSignatureAgent;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getLieuPrise() {
        return lieuPrise;
    }

    public void setLieuPrise(String lieuPrise) {
        this.lieuPrise = lieuPrise;
    }

    public String getLieuDepose() {
        return lieuDepose;
    }

    public void setLieuDepose(String lieuDepose) {
        this.lieuDepose = lieuDepose;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public double getMontantAgent() {
        return montantAgent;
    }

    public void setMontantAgent(double montantAgent) {
        this.montantAgent = montantAgent;
    }

    public double getMontantPlatforme() {
        return montantPlatforme;
    }

    public void setMontantPlatforme(double montantPlatforme) {
        this.montantPlatforme = montantPlatforme;
    }

    public double getCommissionPourcentage() {
        return commissionPourcentage;
    }

    public void setCommissionPourcentage(double commissionPourcentage) {
        this.commissionPourcentage = commissionPourcentage;
    }

    public double getCommissionFixeParJour() {
        return commissionFixeParJour;
    }

    public void setCommissionFixeParJour(double commissionFixeParJour) {
        this.commissionFixeParJour = commissionFixeParJour;
    }

    public Vehicle getVehicule() {
        return Vehicule;
    }

    public void setVehicule(Vehicle vehicule) {
        Vehicule = vehicule;
    }

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }

    public Loueur getLoueur() {
        return loueur;
    }

    public void setLoueur(Loueur loueur) {
        this.loueur = loueur;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    // Constructeur avec Agent (recommandé - permet la sélection automatique de l'assurance)
    public RentalContract(Loueur loueur, Vehicle vehicule, Date dateDebut, Date dateFin, String lieuPrise,
            String lieuDepose, Assurance assurance, Agent agent) {
        // 1. Affectation des données saisies par le loueur
        this.loueur = loueur;
        this.Vehicule = vehicule;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieuPrise = lieuPrise;
        this.lieuDepose = lieuDepose;
        this.agent = agent;

        // 2. Sélection automatique de l'assurance selon l'option de l'agent
        // Si l'agent a l'option assurance personnalisée, on l'utilise
        // Sinon, on utilise l'assurance fournie (AZA par défaut)
        if (agent != null && agent.aAssurancePersonnalisee()) {
            Assurance assuranceAgent = agent.getAssurancePersonnalisee();
            if (assuranceAgent != null) {
                this.assurance = assuranceAgent;
            } else {
                this.assurance = assurance; // Fallback sur l'assurance fournie
            }
        } else {
            this.assurance = assurance;
        }

        // 3. Initialisation automatique des champs système
        this.dateCréationContrat = new Date();
        this.statut = false;
        this.SignatureLoueur = false;
        this.SignatureAgent = false;
        this.dateSignatureLoueur = null;
        this.dateSignatureAgent = null;
        this.fichierPDF = null;

        // 4. Calcul automatique des montants
        this.calculerPrix();
    }

    // Constructeur sans Agent (pour rétro-compatibilité)
    public RentalContract(Loueur loueur, Vehicle vehicule, Date dateDebut, Date dateFin, String lieuPrise,
            String lieuDepose, Assurance assurance) {
        this(loueur, vehicule, dateDebut, dateFin, lieuPrise, lieuDepose, assurance, null);
    }

    @Override
    public String toString() {
        return "RentalContract{" +
                "idC=" + idC +
                ", dateCréationContrat=" + dateCréationContrat +
                ", statut=" + statut +
                ", fichierPDF='" + fichierPDF + '\'' +
                ", SignatureLoueur=" + SignatureLoueur +
                ", SignatureAgent=" + SignatureAgent +
                ", dateSignatureLoueur=" + dateSignatureLoueur +
                ", dateSignatureAgent=" + dateSignatureAgent +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", lieuPrise='" + lieuPrise + '\'' +
                ", lieuDepose='" + lieuDepose + '\'' +
                ", prixTotal=" + prixTotal +
                ", montantAgent=" + montantAgent +
                ", montantPlatforme=" + montantPlatforme +
                ", commissionPourcentage=" + commissionPourcentage +
                ", commissionFixeParJour=" + commissionFixeParJour +
                ", Vehicule=" + Vehicule +
                ", loueur=" + loueur +
                ", assurance=" + assurance +
                '}';
    }

    private void calculerPrix() {
        // 1. Calcul de la durée en jours
        long diffInMillies = Math.abs(this.dateFin.getTime() - this.dateDebut.getTime());
        // On ajoute +1 car une location du lundi au lundi compte pour 1 jour complet
        // (ou règle métier à définir)
        // Ici on assume des jours pleins, convertis depuis millisecondes
        long diffInDays = (diffInMillies / (1000 * 60 * 60 * 24)) + 1;

        // 2. Récupération des Tarifs
        // IL MANQUE CECI DANS VOTRE CLASSE VEHICLE :
        double prixJournalierVehicule = this.Vehicule.getPrixVehiculeParJour();

        // IL MANQUE CECI DANS VOTRE CLASSE ASSURANCE (si applicable) :
        // Le sujet dit que le prix dépend du véhicule, on imagine que l'objet assurance
        // stocke le prix calculé
        double prixAssurance = (this.assurance != null) ? this.assurance.calculerPrime(this.Vehicule) : 0.0;

        // 3. Définition des règles de la plateforme [cite: 281]
        this.commissionPourcentage = 0.10; // 10%
        this.commissionFixeParJour = 2.0; // 2€

        // 4. Calcul de la part Agent
        // Ex: 30€ * 5 jours = 150€
        this.montantAgent = prixJournalierVehicule * diffInDays;

        // 5. Calcul de la part Plateforme [cite: 284-285]
        // Ex: (150€ * 10%) + (5 jours * 2€) = 15€ + 10€ = 25€
        double partVariable = this.montantAgent * this.commissionPourcentage;
        double partFixe = diffInDays * this.commissionFixeParJour;
        this.montantPlatforme = partVariable + partFixe;

        // 6. Calcul du Total Final à payer par le loueur
        // Total = Part Agent + Commission Plateforme + Assurance (+ Options si vous les
        // activez)
        this.prixTotal = this.montantAgent + this.montantPlatforme + prixAssurance;
    }

    /**
     * Enregistre la signature du loueur sur le contrat.
     * Si l'agent a l'option "acceptation manuelle", le contrat reste en attente.
     * Sinon, l'agent signe automatiquement.
     */
    public void signerLoueur() {
        // 1. Vérification de sécurité : on ne signe pas deux fois
        if (this.SignatureLoueur) {
            System.out.println("Erreur : Ce contrat a déjà été signé par le loueur le " + this.dateSignatureLoueur);
            return;
        }

        // 2. Vérification que les dates appartiennent aux disponibilités du véhicule
        LocalDate debutLocal = this.dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate finLocal = this.dateFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        if (!this.Vehicule.estDisponible(debutLocal, finLocal)) {
            System.out.println("Erreur : Le véhicule n'est pas disponible pour la période du " + debutLocal + " au " + finLocal);
            return;
        }

        // 3. Application de la signature du loueur
        this.SignatureLoueur = true;
        this.dateSignatureLoueur = new Date();

        // 4. Vérifier si l'agent a l'option "acceptation manuelle"
        if (this.agent != null && this.agent.aAcceptationManuelle()) {
            // L'agent doit accepter manuellement dans les 6 heures
            OptionAcceptationManuelle option = this.agent.getOption(OptionAcceptationManuelle.class);
            this.dateExpiration = option.calculerDateExpiration(this.dateSignatureLoueur);
            this.statut = false;
            System.out.println("Signature Loueur enregistrée. En attente de validation par l'Agent...");
            System.out.println("L'agent a jusqu'au " + this.dateExpiration + " pour accepter le contrat.");
        } else {
            // Signature automatique de l'agent
            this.SignatureAgent = true;
            this.dateSignatureAgent = new Date();
            validerContrat();
        }
    }

    /**
     * L'agent accepte manuellement le contrat (si option payante activée).
     * Doit être appelée avant l'expiration du délai de 6h.
     */
    public void signerAgent() {
        // 1. Vérifier que le loueur a signé
        if (!this.SignatureLoueur) {
            System.out.println("Erreur : Le loueur n'a pas encore signé ce contrat.");
            return;
        }

        // 2. Vérifier que l'agent n'a pas déjà signé
        if (this.SignatureAgent) {
            System.out.println("Erreur : L'agent a déjà signé ce contrat.");
            return;
        }

        // 3. Vérifier que le délai de 6h n'est pas dépassé
        if (estExpire()) {
            System.out.println("Erreur : Le délai de 6h est dépassé. Le contrat est expiré.");
            return;
        }

        // 4. Signature de l'agent
        this.SignatureAgent = true;
        this.dateSignatureAgent = new Date();
        validerContrat();
        System.out.println("Succès : Contrat accepté par l'agent.");
    }

    /**
     * Valide le contrat : ajoute aux historiques du loueur et du véhicule.
     */
    private void validerContrat() {
        this.statut = true;
        this.loueur.addContract(this);
        this.Vehicule.ajouterContrat(this);
        System.out.println("Contrat VALIDÉ et ACTIF.");
    }

    /**
     * Vérifie si le délai d'acceptation de 6h est dépassé.
     */
    public boolean estExpire() {
        if (this.dateExpiration == null) {
            return false;
        }
        return new Date().after(this.dateExpiration);
    }

    /**
     * Retourne la date d'expiration pour l'acceptation manuelle.
     */
    public Date getDateExpiration() {
        return dateExpiration;
    }

    /**
     * Vérifie si le contrat est en attente d'acceptation de l'agent.
     */
    public boolean estEnAttenteAgent() {
        return this.SignatureLoueur && !this.SignatureAgent && !estExpire();
    }

    public String genererPdf() {
        // On simule la création d'un nom de fichier unique
        String nomFichier = "Contrat_Location_" + this.idC + ".pdf";

        // Dans la vraie vie, ici on écrirait dans un fichier sur le disque dur.
        // Pour l'exercice, on remplit juste l'attribut.
        this.fichierPDF = nomFichier;

        System.out.println("--- Génération du PDF ---");
        System.out.println("Fichier créé : " + nomFichier);

        return nomFichier;
    }

    // ===== GESTION DU KILOMÉTRAGE (US.L.10) =====

    /**
     * Enregistrer le kilométrage au départ (prise du véhicule)
     * @param km Kilométrage relevé
     * @param photoNom Nom du fichier photo de preuve
     */
    public void renseignerKilometrageDebut(int km, String photoNom) {
        if (photoNom == null || photoNom.trim().isEmpty()) {
            throw new IllegalArgumentException("La photo de preuve est obligatoire");
        }
        if (km < 0) {
            throw new IllegalArgumentException("Le kilométrage ne peut pas être négatif");
        }
        this.kilometrageDebut = km;
        this.photoKilometrageDebut = photoNom;
        this.dateRenseignementDebut = new Date();
        System.out.println("Kilométrage départ enregistré: " + km + " km (Photo: " + photoNom + ")");
    }

    /**
     * Enregistrer le kilométrage au retour du véhicule
     * @param km Kilométrage relevé
     * @param photoNom Nom du fichier photo de preuve
     */
    public void renseignerKilometrageFin(int km, String photoNom) {
        if (this.kilometrageDebut == null) {
            throw new IllegalStateException("Le kilométrage de départ n'a pas encore été renseigné");
        }
        if (photoNom == null || photoNom.trim().isEmpty()) {
            throw new IllegalArgumentException("La photo de preuve est obligatoire");
        }
        if (km < this.kilometrageDebut) {
            throw new IllegalArgumentException("Le kilométrage de fin (" + km + " km) ne peut pas être inférieur au kilométrage de départ (" + this.kilometrageDebut + " km)");
        }
        this.kilometrageFin = km;
        this.photoKilometrageFin = photoNom;
        this.dateRenseignementFin = new Date();
        System.out.println("Kilométrage retour enregistré: " + km + " km (Photo: " + photoNom + ")");
        System.out.println("Distance parcourue: " + (km - this.kilometrageDebut) + " km");
    }

    /**
     * Calcule la distance parcourue pendant la location
     * @return Distance en km, ou null si non renseignée
     */
    public Integer calculerDistanceParcourue() {
        if (this.kilometrageDebut != null && this.kilometrageFin != null) {
            return this.kilometrageFin - this.kilometrageDebut;
        }
        return null;
    }

    // Getters pour le kilométrage
    public Integer getKilometrageDebut() {
        return kilometrageDebut;
    }

    public String getPhotoKilometrageDebut() {
        return photoKilometrageDebut;
    }

    public Date getDateRenseignementDebut() {
        return dateRenseignementDebut;
    }

    public Integer getKilometrageFin() {
        return kilometrageFin;
    }

    public String getPhotoKilometrageFin() {
        return photoKilometrageFin;
    }

    public Date getDateRenseignementFin() {
        return dateRenseignementFin;
    }
}
