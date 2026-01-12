package fr.miage.groupe2projetpoo.entity.infrastructure;

public class Parking {
    private int idParking;
    private String nom;
    private String adresse;
    private String ville;
    private int capacite;
    private double tarifJournalierAgent;

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

    @Override
    public String toString() {
        return "Parking{" +
                "id=" + idParking +
                ", nom='" + nom + '\'' +
                ", ville='" + ville + '\'' +
                ", tarif=" + tarifJournalierAgent +
                '}';
    }
}
