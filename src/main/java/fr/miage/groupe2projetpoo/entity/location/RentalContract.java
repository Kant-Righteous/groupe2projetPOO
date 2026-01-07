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
        //this.calculerPrix();
    }

}
