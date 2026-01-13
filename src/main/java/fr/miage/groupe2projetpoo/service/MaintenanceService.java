package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.entretien.MaintenanceIntervention;
import fr.miage.groupe2projetpoo.entity.entretien.MaintenancePrice;
import fr.miage.groupe2projetpoo.entity.entretien.MaintenanceStatus;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.MaintenanceCompany;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.repository.MaintenanceRepository;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import fr.miage.groupe2projetpoo.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository, UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public MaintenanceCompany registerCompany(MaintenanceCompany company) {
        return maintenanceRepository.saveCompany(company);
    }

    public void setCompanyRefere(String email, boolean refere) {
        MaintenanceCompany company = maintenanceRepository.findCompanyByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Entreprise introuvable"));
        company.setEstRefere(refere);
        maintenanceRepository.saveCompany(company);
    }

    public void addPrice(String companyEmail, MaintenancePrice price) {
        MaintenanceCompany company = maintenanceRepository.findCompanyByEmail(companyEmail)
                .orElseThrow(() -> new IllegalArgumentException("Entreprise introuvable"));
        company.getGrilleTarifaire().add(price);
        maintenanceRepository.saveCompany(company);
    }

    public void importPrices(String companyEmail, List<MaintenancePrice> prices) {
        MaintenanceCompany company = maintenanceRepository.findCompanyByEmail(companyEmail)
                .orElseThrow(() -> new IllegalArgumentException("Entreprise introuvable"));
        company.getGrilleTarifaire().addAll(prices);
        maintenanceRepository.saveCompany(company);
    }

    public List<MaintenanceCompany> getAvailableCompanies() {
        return maintenanceRepository.findAllRefereCompanies();
    }

    public MaintenanceIntervention requestMaintenance(String agentEmail, String vehicleId, String companyEmail, LocalDate date) {
        // 1. Trouver l'agent
        Utilisateur user = userRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Agent introuvable"));
        if (!(user instanceof Agent)) {
            throw new IllegalArgumentException("L'utilisateur n'est pas un agent");
        }
        Agent agent = (Agent) user;

        // 2. Trouver le véhicule
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable"));

        // 3. Vérifier disponibilité (pas loué)
        if (!vehicle.estDisponible(date, date)) {
            throw new IllegalStateException("Le véhicule est déjà loué à cette date");
        }

        // 4. Trouver l'entreprise
        MaintenanceCompany company = maintenanceRepository.findCompanyByEmail(companyEmail)
                .orElseThrow(() -> new IllegalArgumentException("Entreprise introuvable"));
        if (!company.isEstRefere()) {
            throw new IllegalStateException("L'entreprise n'est pas référencée");
        }

        // 5. Déterminer le prix
        double prix = findPrice(company, vehicle);

        // 6. Créer l'intervention
        MaintenanceIntervention intervention = new MaintenanceIntervention(
                null, agent, vehicle, company, date, prix, MaintenanceStatus.PREVU
        );

        // 7. Marquer le véhicule comme indisponible à cette date pour l'entretien
        vehicle.getDisponibilites().put(date, false);
        vehicleRepository.save(vehicle);

        return maintenanceRepository.saveIntervention(intervention);
    }

    private double findPrice(MaintenanceCompany company, Vehicle vehicle) {
        return company.getGrilleTarifaire().stream()
                .filter(p -> p.getTypeVehicule() == vehicle.getType())
                .filter(p -> p.getModele() == null || p.getModele().isEmpty() || p.getModele().equalsIgnoreCase(vehicle.getModeleVehicule()))
                .map(MaintenancePrice::getPrix)
                .findFirst()
                .orElse(50.0); // Prix par défaut si non trouvé
    }

    public void updateInterventionStatus(String id, MaintenanceStatus status) {
        MaintenanceIntervention intervention = maintenanceRepository.findInterventionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Intervention introuvable"));
        
        // Si on annule, on libère le véhicule
        if (status == MaintenanceStatus.ANNULE && intervention.getStatut() != MaintenanceStatus.ANNULE) {
            Vehicle v = intervention.getVehicule();
            v.getDisponibilites().put(intervention.getDateIntervention(), true);
            vehicleRepository.save(v);
        }
        
        intervention.setStatut(status);
        maintenanceRepository.saveIntervention(intervention);
    }

    public List<MaintenanceIntervention> getHistoryByVehicle(String vehicleId) {
        return maintenanceRepository.findInterventionsByVehicle(vehicleId);
    }

    public List<MaintenanceIntervention> getInterventionsByAgent(String agentEmail) {
        return maintenanceRepository.findInterventionsByAgent(agentEmail);
    }

    public MaintenanceCompany getCompanyByEmail(String email) {
        return maintenanceRepository.findCompanyByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Entreprise introuvable"));
    }
}
