package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.entretien.MaintenanceIntervention;
import fr.miage.groupe2projetpoo.entity.utilisateur.MaintenanceCompany;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryMaintenanceRepository implements MaintenanceRepository {

    private final Map<String, MaintenanceCompany> companies = new ConcurrentHashMap<>();
    private final Map<String, MaintenanceIntervention> interventions = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Ajouter une entreprise de test référencée
        MaintenanceCompany cleanCar = new MaintenanceCompany("CleanCar", "Jean", "password", "cleancar@test.com", "0123456789");
        cleanCar.setEstRefere(true);
        cleanCar.getGrilleTarifaire().add(new fr.miage.groupe2projetpoo.entity.entretien.MaintenancePrice(fr.miage.groupe2projetpoo.entity.vehicule.TypeVehicule.VOITURE, null, 40.0));
        cleanCar.getGrilleTarifaire().add(new fr.miage.groupe2projetpoo.entity.entretien.MaintenancePrice(fr.miage.groupe2projetpoo.entity.vehicule.TypeVehicule.MOTO, null, 25.0));
        saveCompany(cleanCar);

        // Ajouter une autre entreprise non référencée pour tester
        MaintenanceCompany dirtyWash = new MaintenanceCompany("DirtyWash", "Marc", "password", "dirtywash@test.com", "0987654321");
        saveCompany(dirtyWash);
    }

    @Override
    public MaintenanceCompany saveCompany(MaintenanceCompany company) {
        companies.put(company.getEmail(), company);
        return company;
    }

    @Override
    public Optional<MaintenanceCompany> findCompanyByEmail(String email) {
        return Optional.ofNullable(companies.get(email));
    }

    @Override
    public List<MaintenanceCompany> findAllCompanies() {
        return new ArrayList<>(companies.values());
    }

    @Override
    public List<MaintenanceCompany> findAllRefereCompanies() {
        return companies.values().stream()
                .filter(MaintenanceCompany::isEstRefere)
                .collect(Collectors.toList());
    }

    @Override
    public MaintenanceIntervention saveIntervention(MaintenanceIntervention intervention) {
        if (intervention.getId() == null || intervention.getId().isEmpty()) {
            intervention.setId(UUID.randomUUID().toString());
        }
        interventions.put(intervention.getId(), intervention);
        return intervention;
    }

    @Override
    public Optional<MaintenanceIntervention> findInterventionById(String id) {
        return Optional.ofNullable(interventions.get(id));
    }

    @Override
    public List<MaintenanceIntervention> findInterventionsByAgent(String agentEmail) {
        return interventions.values().stream()
                .filter(i -> i.getAgent().getEmail().equals(agentEmail))
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceIntervention> findInterventionsByVehicle(String vehicleId) {
        return interventions.values().stream()
                .filter(i -> i.getVehicule().getIdVehicule().equals(vehicleId))
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceIntervention> findInterventionsByCompany(String companyEmail) {
        return interventions.values().stream()
                .filter(i -> i.getEntreprise().getEmail().equals(companyEmail))
                .collect(Collectors.toList());
    }
}
