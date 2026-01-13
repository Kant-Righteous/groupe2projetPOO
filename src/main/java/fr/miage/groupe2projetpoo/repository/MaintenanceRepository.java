package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.entretien.MaintenanceIntervention;
import fr.miage.groupe2projetpoo.entity.utilisateur.MaintenanceCompany;

import java.util.List;
import java.util.Optional;

public interface MaintenanceRepository {
    MaintenanceCompany saveCompany(MaintenanceCompany company);
    Optional<MaintenanceCompany> findCompanyByEmail(String email);
    List<MaintenanceCompany> findAllCompanies();
    List<MaintenanceCompany> findAllRefereCompanies();
    
    MaintenanceIntervention saveIntervention(MaintenanceIntervention intervention);
    Optional<MaintenanceIntervention> findInterventionById(String id);
    List<MaintenanceIntervention> findInterventionsByAgent(String agentEmail);
    List<MaintenanceIntervention> findInterventionsByVehicle(String vehicleId);
    List<MaintenanceIntervention> findInterventionsByCompany(String companyEmail);
}
