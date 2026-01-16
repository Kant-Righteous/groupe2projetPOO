package fr.miage.groupe2projetpoo.entity.location;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.assurance.OptionAcceptationManuelle;
import fr.miage.groupe2projetpoo.entity.assurance.OptionParking;
import fr.miage.groupe2projetpoo.entity.maintenance.ControleTechnique;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.AgentProfessionnel;
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

    @JsonIgnoreProperties({ "contracts", "vehicleList", "optionsPayantes", "notations", "password" })
    private Agent agent;
    private Assurance assurance;

    // Attribut pour l'acceptation manuelle (option payante)
    private Date dateExpiration; // Date limite pour que l'agent accepte (6h après signature loueur)

    // US.L.10 - Kilométrage et preuves photos
    private Integer kilometrageDebut; // KM à la prise du véhicule
    private String photoKilometrageDebut; // Nom du fichier photo de preuve (départ)
    private Date dateRenseignementDebut; // Date/heure de renseignement du KM départ

    private Integer kilometrageFin; // KM au retour du véhicule
    private String photoKilometrageFin; // Nom du fichier photo de preuve (retour)
    private Date dateRenseignementFin; // Date/heure de renseignement du KM retour

    // Statut du contrat de location (cycle de vie)
    private StatutLocation statutLocation = StatutLocation.EN_ATTENTE_SIGNATURE;

    // Informations d'accès au parking Vienci (remplies lors de la validation si
    // optionParking active)
    private String parkingNom;
    private String parkingAdresse;
    private String parkingVille;
    private String parkingCodeAcces;
    private String parkingProcedureAcces;
    private String parkingInstructionsSpeciales;

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

    // Constructeur avec Agent (recommandé - permet la sélection automatique de
    // l'assurance)
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

        // 3. Validation des lieux de prise et dépose (US.L.2)
        validerLieuxPriseDepose();

        // 4. Initialisation automatique des champs système
        this.dateCréationContrat = new Date();
        this.statut = false;
        this.SignatureLoueur = false;
        this.SignatureAgent = false;
        this.dateSignatureLoueur = null;
        this.dateSignatureAgent = null;
        this.fichierPDF = null;

        // 5. Calcul automatique des montants
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

    // Option Vienci : Si true, le loueur a choisi de déposer le véhicule au parking
    // partenaire
    private boolean optionParkingSelectionnee = false;

    // Nouveaux attributs pour le détail du prix
    private double prixSansReduction;
    private String detailsReduction;

    private void calculerPrix() {
        // 1. Calcul de la durée en jours
        long diffInMillies = Math.abs(this.dateFin.getTime() - this.dateDebut.getTime());
        long diffInDays = (diffInMillies / (1000 * 60 * 60 * 24)) + 1;

        // 2. Définition des règles de la plateforme
        this.commissionPourcentage = 0.10; // 10%
        this.commissionFixeParJour = 2.0; // 2€

        // 2.1 Application de la réduction longue durée (US.A.7)
        appliquerReductionLongueDuree(diffInDays);

        // 3. Gestion des Tarifs et Réductions Vienci
        double prixJournalierBase = this.Vehicule.getPrixVehiculeParJour();

        // Calcul du prix sans réduction (pour référence)
        double montantAgentSansReduction = prixJournalierBase * diffInDays;
        double partVariableSansReduction = montantAgentSansReduction * this.commissionPourcentage;
        double partFixe = diffInDays * this.commissionFixeParJour;
        double prixAssurance = (this.assurance != null) ? this.assurance.calculerPrime(this.Vehicule) : 0.0;

        // prixSansReduction = Prix total AVANT réduction parking (车租 + 佣金 + 保险)
        this.prixSansReduction = montantAgentSansReduction + partVariableSansReduction + partFixe + prixAssurance;

        double multiplicateur = 1.0;
        StringBuilder details = new StringBuilder();

        // Vérification des options de l'agent pour les réductions
        if (this.agent != null) {
            fr.miage.groupe2projetpoo.entity.assurance.OptionParking optParking = this.agent
                    .getOption(fr.miage.groupe2projetpoo.entity.assurance.OptionParking.class);

            if (optParking != null && optParking.isEstActive()) {
                double reduction = optParking.getTauxReduction();

                // Cas A : Réduction si le Véhicule est récupéré depuis un Parking Partenaire
                String lieuActuelVehicule = this.Vehicule.getDernierLieuDepose();

                // Vérifier si dernierLieuDepose est un parking partenaire
                boolean estDansParking = lieuActuelVehicule != null
                        && fr.miage.groupe2projetpoo.config.DataInitializer.PARKINGS_PARTENAIRES
                                .contains(lieuActuelVehicule);

                if (estDansParking) {
                    multiplicateur *= (1.0 - reduction);
                    if (details.length() > 0)
                        details.append(" + ");
                    details.append("Départ du Parking Partenaire (-").append((int) (reduction * 100)).append("%)");
                }

                // Cas B : Réduction si le Loueur choisit de déposer au Parking Partenaire
                // (Incitation au retour)
                if (this.optionParkingSelectionnee) {
                    multiplicateur *= (1.0 - reduction);
                    if (details.length() > 0)
                        details.append(" + ");
                    details.append("Retour au Parking Partenaire (-").append((int) (reduction * 100)).append("%)");
                }
            }
        }

        if (details.length() == 0) {
            this.detailsReduction = "Aucune réduction appliquée";
        } else {
            this.detailsReduction = details.toString();
        }

        double prixJournalierFinal = prixJournalierBase * multiplicateur;

        // 4. Calcul de la part Agent (avec réduction appliquée)
        this.montantAgent = prixJournalierFinal * diffInDays;

        // 5. Calcul de la part Plateforme (basée sur montantAgent réduit)
        double partVariable = this.montantAgent * this.commissionPourcentage;
        this.montantPlatforme = partVariable + partFixe;

        // 6. Calcul du Total Final à payer par le loueur
        this.prixTotal = this.montantAgent + this.montantPlatforme + prixAssurance;
    }

    /**
     * Applique une réduction sur la commission variable en fonction de la durée de
     * location.
     * US.A.7 - Réduction longue durée pour fidéliser les agents.
     * 
     * Barème de réduction sur la commission variable :
     * - 7 à 13 jours : -5% (commission passe de 10% à 9.5%)
     * - 14 à 29 jours : -10% (commission passe de 10% à 9%)
     * - 30 jours et plus : -15% (commission passe de 10% à 8.5%)
     * 
     * @param nbJours Nombre de jours de location
     */
    private void appliquerReductionLongueDuree(long nbJours) {
        if (nbJours >= 30) {
            // Location mensuelle : -15% sur la commission
            this.commissionPourcentage = 0.10 * 0.85; // 10% × 85% = 8.5%
        } else if (nbJours >= 14) {
            // Location bi-hebdomadaire : -10% sur la commission
            this.commissionPourcentage = 0.10 * 0.90; // 10% × 90% = 9%
        } else if (nbJours >= 7) {
            // Location hebdomadaire : -5% sur la commission
            this.commissionPourcentage = 0.10 * 0.95; // 10% × 95% = 9.5%
        }
        // Sinon (< 7 jours), pas de réduction : commission reste à 10%
    }

    public boolean isOptionParkingSelectionnee() {
        return optionParkingSelectionnee;
    }

    public void setOptionParkingSelectionnee(boolean optionParkingSelectionnee) {
        this.optionParkingSelectionnee = optionParkingSelectionnee;
        // Recalculer le prix si l'option change
        calculerPrix();
    }

    public double getPrixSansReduction() {
        return prixSansReduction;
    }

    public String getDetailsReduction() {
        return detailsReduction;
    }

    /**
     * Valide que le lieu de dépose est autorisé (US.L.2).
     * 
     * Règle simplifiée :
     * - Si option parking active OU agent professionnel → lieu dépose peut être
     * différent
     * - Sinon → lieu dépose DOIT être identique au lieu de prise
     * 
     * @throws IllegalStateException si le lieu de dépose n'est pas autorisé
     */
    private void validerLieuxPriseDepose() {
        // Si même lieu, toujours OK
        if (this.lieuPrise.equalsIgnoreCase(this.lieuDepose)) {
            return;
        }

        // Si lieux différents, vérifier les autorisations
        if (this.agent == null) {
            throw new IllegalStateException(
                    "❌ Lieu de dépose différent non autorisé sans agent. " +
                            "Choisissez le même lieu : '" + this.lieuPrise + "'");
        }

        // CAS 1 : Agent a l'option Parking active
        OptionParking optParking = this.agent.getOption(OptionParking.class);
        if (optParking != null && optParking.isEstActive()) {
            return;
        }

        // CAS 2 : Agent est professionnel
        if (this.agent instanceof AgentProfessionnel) {
            return;
        }

        // Aucune autorisation → ERREUR
        throw new IllegalStateException(
                "❌ Le lieu de dépose '" + this.lieuDepose + "' doit être identique au lieu de prise '" + this.lieuPrise
                        + "'.\n" +
                        "Pour déposer ailleurs, activez l'option Parking Vienci ou utilisez un agent professionnel.");
    }

    /**
     * Vérifie que le véhicule a un contrôle technique valide avant la location.
     * US.A.8 - Renseigner les informations sur le contrôle technique
     * 
     * @throws IllegalStateException si le CT est absent ou expiré
     */
    private void verifierControleTechnique() {
        // 1. Récupérer le contrôle technique du véhicule
        ControleTechnique ct = this.Vehicule.getControleTechnique();

        // 2. Vérifier qu'un CT existe
        if (ct == null) {
            throw new IllegalStateException(
                    "❌ Le véhicule " + this.Vehicule.getIdVehicule() +
                            " ne peut pas être loué : aucun contrôle technique enregistré");
        }

        // 3. Vérifier que le CT n'est pas expiré
        if (ct.estExpire()) {
            throw new IllegalStateException(
                    "❌ Le véhicule " + this.Vehicule.getIdVehicule() +
                            " ne peut pas être loué : contrôle technique expiré depuis le " +
                            ct.getDateExpiration());
        }

        // 4. Tout est OK !
        System.out.println("✅ Contrôle technique valide jusqu'au " + ct.getDateExpiration());
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

        // 2. Vérification du contrôle technique (US.A.8)
        verifierControleTechnique();

        // 3. Vérification que les dates appartiennent aux disponibilités du véhicule
    LocalDate debutLocal = this.dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate finLocal = this.dateFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    if (!this.Vehicule.estDisponiblePlanning(debutLocal, finLocal)) {
        throw new IllegalStateException(
                "❌ Le véhicule n'est pas disponible pour la période du " + debutLocal + " au " + finLocal);
    }
        // 4. Application de la signature du loueur
        this.SignatureLoueur = true;
        this.dateSignatureLoueur = new Date();

        // 5. Vérifier si l'agent a l'option "acceptation manuelle"
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
            // Transition : EN_ATTENTE_SIGNATURE → SIGNE
            this.statutLocation = StatutLocation.SIGNE;
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
        // Transition : EN_ATTENTE_SIGNATURE → SIGNE
        this.statutLocation = StatutLocation.SIGNE;
        System.out.println("Succès : Contrat accepté par l'agent.");
    }

    /**
     * Valide le contrat : ajoute aux historiques du loueur et du véhicule.
     * Bloque automatiquement la période dans le planning du véhicule.
     */
    private void validerContrat() {
        this.statut = true;
        this.loueur.addContract(this);
        this.Vehicule.ajouterContrat(this);
        
        // Bloquer la période dans le planning du véhicule
        LocalDate debutLocal = this.dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate finLocal = this.dateFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        try {
            this.Vehicule.addPlanningDispo(debutLocal, finLocal);
            System.out.println("Contrat VALIDÉ et ACTIF.");
            System.out.println("✅ Véhicule " + this.Vehicule.getIdVehicule() + " bloqué du " 
                               + debutLocal + " au " + finLocal);
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Attention : période déjà réservée (doublon détecté)");
            // Le contrat est déjà validé, on affiche juste un warning
        }
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
     * 
     * @param km       Kilométrage relevé
     * @param photoNom Nom du fichier photo de preuve
     */
    public void renseignerKilometrageDebut(int km, String photoNom) {
        // Validation du statut
        if (this.statutLocation != StatutLocation.SIGNE) {
            throw new IllegalStateException(
                    "Le contrat doit être signé avant de prendre le véhicule. Statut actuel: " + this.statutLocation);
        }
        if (photoNom == null || photoNom.trim().isEmpty()) {
            throw new IllegalArgumentException("La photo de preuve est obligatoire");
        }
        if (km < 0) {
            throw new IllegalArgumentException("Le kilométrage ne peut pas être négatif");
        }
        this.kilometrageDebut = km;
        this.photoKilometrageDebut = photoNom;
        this.dateRenseignementDebut = new Date();
        // Transition : SIGNE → EN_COURS
        this.statutLocation = StatutLocation.EN_COURS;
        System.out.println("Kilométrage départ enregistré: " + km + " km (Photo: " + photoNom + ")");
        System.out.println("Statut du contrat: " + this.statutLocation);
    }

    /**
     * Enregistrer le kilométrage au retour du véhicule
     * 
     * @param km       Kilométrage relevé
     * @param photoNom Nom du fichier photo de preuve
     */
    public void renseignerKilometrageFin(int km, String photoNom) {
        // Validation du statut
        if (this.statutLocation != StatutLocation.EN_COURS) {
            throw new IllegalStateException(
                    "Le véhicule doit être en cours de location. Statut actuel: " + this.statutLocation);
        }
        if (this.kilometrageDebut == null) {
            throw new IllegalStateException("Le kilométrage de départ n'a pas encore été renseigné");
        }
        if (photoNom == null || photoNom.trim().isEmpty()) {
            throw new IllegalArgumentException("La photo de preuve est obligatoire");
        }
        if (km < this.kilometrageDebut) {
            throw new IllegalArgumentException("Le kilométrage de fin (" + km
                    + " km) ne peut pas être inférieur au kilométrage de départ (" + this.kilometrageDebut + " km)");
        }
        this.kilometrageFin = km;
        this.photoKilometrageFin = photoNom;
        this.dateRenseignementFin = new Date();
        // Transition : EN_COURS → TERMINEE
        this.statutLocation = StatutLocation.TERMINEE;
        System.out.println("Kilométrage retour enregistré: " + km + " km (Photo: " + photoNom + ")");
        System.out.println("Distance parcourue: " + (km - this.kilometrageDebut) + " km");
        System.out.println("Statut du contrat: " + this.statutLocation + " - Location terminée");
    }

    /**
     * Calcule la distance parcourue pendant la location
     * 
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

    // ===== GESTION DU STATUT DE LOCATION =====

    public StatutLocation getStatutLocation() {
        return statutLocation;
    }

    public void setStatutLocation(StatutLocation statutLocation) {
        this.statutLocation = statutLocation;
    }

    /**
     * Vérifie si la location est en cours (véhicule actuellement loué)
     */
    public boolean estEnCours() {
        return this.statutLocation == StatutLocation.EN_COURS;
    }

    /**
     * Vérifie si la location est terminée
     */
    public boolean estTerminee() {
        return this.statutLocation == StatutLocation.TERMINEE;
    }

    /**
     * Vérifie si le contrat est signé et prêt pour la prise du véhicule
     */
    public boolean estPretPourPrise() {
        return this.statutLocation == StatutLocation.SIGNE;
    }

    /**
     * Annuler le contrat de location
     */
    public void annulerContrat() {
        if (this.statutLocation == StatutLocation.EN_COURS) {
            throw new IllegalStateException(
                    "Impossible d'annuler un contrat en cours. Le véhicule doit d'abord être rendu.");
        }
        if (this.statutLocation == StatutLocation.TERMINEE) {
            throw new IllegalStateException("Impossible d'annuler un contrat déjà terminé.");
        }
        this.statutLocation = StatutLocation.ANNULEE;
    }

    // ===== GESTION DES INFORMATIONS PARKING VIENCI =====

    /**
     * Remplit les informations d'accès au parking partenaire Vienci.
     * Cette méthode doit être appelée lors de la validation du contrat
     * si l'option parking est sélectionnée.
     */
    public void remplirInfoParking() {
        if (this.optionParkingSelectionnee && this.agent != null) {
            fr.miage.groupe2projetpoo.entity.assurance.OptionParking optParking = this.agent
                    .getOption(fr.miage.groupe2projetpoo.entity.assurance.OptionParking.class);

            if (optParking != null && optParking.isEstActive()) {
                fr.miage.groupe2projetpoo.entity.infrastructure.Parking parking = optParking.getParkingPartenaire();
                if (parking != null) {
                    this.parkingNom = parking.getNom();
                    this.parkingAdresse = parking.getAdresse();
                    this.parkingVille = parking.getVille();
                    this.parkingCodeAcces = parking.getCodeAcces();
                    this.parkingProcedureAcces = parking.getProcedureAcces();
                    this.parkingInstructionsSpeciales = parking.getInstructionsSpeciales();
                }
            }
        }
    }

    /**
     * Vérifie si les informations de parking sont disponibles
     */
    public boolean hasParkingInfo() {
        return this.parkingNom != null && !this.parkingNom.isEmpty();
    }

    // Getters pour les informations de parking

    public String getParkingNom() {
        return parkingNom;
    }

    public String getParkingAdresse() {
        return parkingAdresse;
    }

    public String getParkingVille() {
        return parkingVille;
    }

    public String getParkingCodeAcces() {
        return parkingCodeAcces;
    }

    public String getParkingProcedureAcces() {
        return parkingProcedureAcces;
    }

    public String getParkingInstructionsSpeciales() {
        return parkingInstructionsSpeciales;
    }

    // Setters pour les informations de parking (si besoin de modification manuelle)

    public void setParkingNom(String parkingNom) {
        this.parkingNom = parkingNom;
    }

    public void setParkingAdresse(String parkingAdresse) {
        this.parkingAdresse = parkingAdresse;
    }

    public void setParkingVille(String parkingVille) {
        this.parkingVille = parkingVille;
    }

    public void setParkingCodeAcces(String parkingCodeAcces) {
        this.parkingCodeAcces = parkingCodeAcces;
    }

    public void setParkingProcedureAcces(String parkingProcedureAcces) {
        this.parkingProcedureAcces = parkingProcedureAcces;
    }

    public void setParkingInstructionsSpeciales(String parkingInstructionsSpeciales) {
        this.parkingInstructionsSpeciales = parkingInstructionsSpeciales;
    }
}
