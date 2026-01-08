package fr.miage.groupe2projetpoo.entity.location;
import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;

import java.util.Date;

public class RentalContract {private int idC;
private Date dateCréationContrat ;
private boolean statut ;
private  String fichierPDF ;
private boolean SignatureLoueur;
private boolean SignatureAgent;
private Date dateSignatureLoueur ;
private Date dateSignatureAgent ;
private Date dateDebut ;
private Date dateFin ;
private String lieuPrise ;
private String lieuDepose ;
private double prixTotal ;
private double montantAgent ;
private double montantPlatforme ;
private double commissionPourcentage ;
private double commissionFixeParJour ;
private Vehicle Vehicule;
private Loueur loueur;
private Assurance assurance;
//private listeOptions : List<OptionPayante>

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
    // Constructeur
    public RentalContract(Loueur loueur, Vehicle vehicule, Date dateDebut, Date dateFin, String lieuPrise, String lieuDepose, Assurance assurance) {
        // 1. Affectation des données saisies par le loueur
        this.loueur = loueur;
        this.Vehicule = vehicule; // Attention à la majuscule 'V' dans votre attribut
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieuPrise = lieuPrise;
        this.lieuDepose = lieuDepose;
        this.assurance= assurance;

        // 2. Initialisation automatique des champs système
        this.dateCréationContrat = new Date(); // Date d'aujourd'hui
        this.statut = false; // false = En attente / Non validé
        this.SignatureLoueur = false;
        this.SignatureAgent = false;
        this.dateSignatureLoueur = null;
        this.dateSignatureAgent = null;
        this.fichierPDF = null; // Sera généré plus tard

        // 3. Calcul automatique des montants
        // On appelle la méthode privée pour remplir prixTotal, montantAgent, etc.
        this.calculerPrix();
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
        // On ajoute +1 car une location du lundi au lundi compte pour 1 jour complet (ou règle métier à définir)
        // Ici on assume des jours pleins, convertis depuis millisecondes
        long diffInDays = (diffInMillies / (1000 * 60 * 60 * 24)) + 1;

        // 2. Récupération des Tarifs (Vérifiez que ces méthodes existent dans vos autres classes !)
        // IL MANQUE CECI DANS VOTRE CLASSE VEHICLE :
        double prixJournalierVehicule = this.Vehicule.getPrixVehiculeJour();

        // IL MANQUE CECI DANS VOTRE CLASSE ASSURANCE (si applicable) :
        // Le sujet dit que le prix dépend du véhicule, on imagine que l'objet assurance stocke le prix calculé
        double prixAssurance = (this.assurance != null) ? this.assurance.getTarif() : 0.0;

        // 3. Définition des règles de la plateforme [cite: 281]
        this.commissionPourcentage = 0.10; // 10%
        this.commissionFixeParJour = 2.0;  // 2€

        // 4. Calcul de la part Agent
        // Ex: 30€ * 5 jours = 150€
        this.montantAgent = prixJournalierVehicule * diffInDays;

        // 5. Calcul de la part Plateforme [cite: 284-285]
        // Ex: (150€ * 10%) + (5 jours * 2€) = 15€ + 10€ = 25€
        double partVariable = this.montantAgent * this.commissionPourcentage;
        double partFixe = diffInDays * this.commissionFixeParJour;
        this.montantPlatforme = partVariable + partFixe;

        // 6. Calcul du Total Final à payer par le loueur
        // Total = Part Agent + Commission Plateforme + Assurance (+ Options si vous les activez)
        this.prixTotal = this.montantAgent + this.montantPlatforme + prixAssurance;
    }

    /**
     * Enregistre la signature du loueur sur le contrat.
     * Met à jour la date de signature et vérifie si le contrat est complet.
     */
    public void signerLoueur() {
        // 1. Vérification de sécurité : on ne signe pas deux fois
        if (this.SignatureLoueur==true) {
            System.out.println("Erreur : Ce contrat a déjà été signé par le loueur le " + this.dateSignatureLoueur);
            return;
        }

        // 2. Application de la signature
        this.SignatureLoueur = true;
        this.dateSignatureLoueur = new Date(); // Enregistre la date et l'heure actuelle
        this.SignatureAgent=true;//Pour l'instant pas d'option payante donc l'agent signe automatiquement .
        this.dateSignatureAgent = new Date();
        // 3. Mise à jour du statut global du contrat
        // Le statut devient TRUE (Validé) seulement si l'Agent a AUSSI signé.
        if (this.SignatureAgent==true) {
            this.statut = true; // Le contrat est totalement validé
            System.out.println("Succès : Contrat signé par le Loueur. Le contrat est désormais VALIDÉ et ACTIF.");
        } else {
            this.statut = false; // Toujours en attente de l'agent
            System.out.println("Succès : Signature Loueur enregistrée. En attente de validation par l'Agent...");
        }
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

}
