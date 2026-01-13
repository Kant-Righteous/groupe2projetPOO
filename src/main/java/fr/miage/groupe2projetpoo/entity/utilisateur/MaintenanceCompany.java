package fr.miage.groupe2projetpoo.entity.utilisateur;

import fr.miage.groupe2projetpoo.entity.entretien.MaintenancePrice;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceCompany extends Utilisateur {
    
    private boolean estRefere;
    private List<MaintenancePrice> grilleTarifaire;

    public MaintenanceCompany() {
        super();
        this.grilleTarifaire = new ArrayList<>();
        this.estRefere = false;
    }

    public MaintenanceCompany(String nom, String prenom, String password, String email, String tel) {
        super(nom, prenom, password, email, tel);
        this.grilleTarifaire = new ArrayList<>();
        this.estRefere = false;
    }

    @Override
    public Role getRole() {
        return Role.SERVICE_ENTRETIEN;
    }

    public boolean isEstRefere() {
        return estRefere;
    }

    public void setEstRefere(boolean estRefere) {
        this.estRefere = estRefere;
    }

    public List<MaintenancePrice> getGrilleTarifaire() {
        return grilleTarifaire;
    }

    public void setGrilleTarifaire(List<MaintenancePrice> grilleTarifaire) {
        this.grilleTarifaire = grilleTarifaire;
    }
}
