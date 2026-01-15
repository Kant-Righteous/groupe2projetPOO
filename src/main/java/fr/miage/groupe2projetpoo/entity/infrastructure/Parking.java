package fr.miage.groupe2projetpoo.entity.infrastructure;

public class Parking {
    private int idParking;
    private String nom;
    private String adresse;
    private String ville;
    private int capacite;
    private double tarifJournalierAgent;

    // Informations d'accès au parking (US Vienci)
    private String codeAcces; // Code d'accès / digicode
    private String procedureAcces; // Procédure d'accès détaillée
    private String instructionsSpeciales; // Instructions spéciales (horaires, contact, etc.)

    // Constructeur complet
    public Parking(int idParking, String nom, String adresse, String ville, int capacite, double tarifJournalierAgent) {
        this.idParking = idParking;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.capacite = capacite;
        this.tarifJournalierAgent = tarifJournalierAgent;
    }

    // Constructeur simplifié demandé : Parking(id: int, nom: String, ville: String,
    // tarif: double)
    public Parking(int idParking, String nom, String ville, double tarifJournalierAgent) {
        this.idParking = idParking;
        this.nom = nom;
        this.ville = ville;
        this.tarifJournalierAgent = tarifJournalierAgent;
        this.capacite = 100; // Valeur par défaut
        this.adresse = "Adresse inconnue";
    }

    public boolean estComplet() {
        return false; // Logique par défaut (0 voiture garée) ou à implémenter si gestion places
    }

    // Getters et Setters
    public int getIdParking() {
        return idParking;
    }

    public void setIdParking(int idParking) {
        this.idParking = idParking;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public double getTarifJournalierAgent() {
        return tarifJournalierAgent;
    }

    public void setTarifJournalierAgent(double tarifJournalierAgent) {
        this.tarifJournalierAgent = tarifJournalierAgent;
    }

    // Getters et Setters pour les informations d'accès

    public String getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(String codeAcces) {
        this.codeAcces = codeAcces;
    }

    public String getProcedureAcces() {
        return procedureAcces;
    }

    public void setProcedureAcces(String procedureAcces) {
        this.procedureAcces = procedureAcces;
    }

    public String getInstructionsSpeciales() {
        return instructionsSpeciales;
    }

    public void setInstructionsSpeciales(String instructionsSpeciales) {
        this.instructionsSpeciales = instructionsSpeciales;
    }

    @Override
    public String toString() {
        return "Parking{" +
                "id=" + idParking +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", tarif=" + tarifJournalierAgent +
                '}';
    }
}
