package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.entity.assurance.OptionEntretien;
import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.Loueur;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.repository.ParkingRepository;
import fr.miage.groupe2projetpoo.repository.RentalRepository;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import fr.miage.groupe2projetpoo.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingRepository parkingRepository;
    private final MaintenanceService maintenanceService;
    private final UserService userService;

    @Autowired
    public RentalService(RentalRepository rentalRepository, UserRepository userRepository,
            VehicleRepository vehicleRepository, ParkingRepository parkingRepository,
            MaintenanceService maintenanceService, UserService userService) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.parkingRepository = parkingRepository;
        this.maintenanceService = maintenanceService;
        this.userService = userService;
    }

    // ===== MÉTHODES CRUD =====

    /**
     * Créer un nouveau contrat de location
     * 
     * @param loueurEmail  Email du loueur
     * @param vehiculeId   ID du véhicule
     * @param dateDebut    Date de début
     * @param dateFin      Date de fin
     * @param lieuPrise    Lieu de prise
     * @param lieuDepose   Lieu de dépôt
     * @param assuranceNom Nom de l'assurance
     * @param parkingId    ID du parking (optionnel, null si pas de parking)
     */
    public RentalContract creerContrat(String loueurEmail, String vehiculeId,
            Date dateDebut, Date dateFin,
            String lieuPrise, String lieuDepose,
            String assuranceNom, Integer parkingId) {

        Utilisateur user = userRepository.findByEmail(loueurEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + loueurEmail));

        if (!(user instanceof Loueur)) {
            throw new RuntimeException("L'utilisateur n'est pas un loueur: " + loueurEmail);
        }
        Loueur loueur = (Loueur) user;

        Vehicle vehicule = vehicleRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec l'ID: " + vehiculeId));

        Assurance assurance = getAssuranceByNom(assuranceNom);
        if (assurance == null) {
            throw new RuntimeException("Assurance non trouvée avec le nom: " + assuranceNom);
        }

        // Récupérer l'agent propriétaire du véhicule pour vérifier ses options
        String proprietaireEmail = vehicule.getProprietaire();
        Agent agentProprietaire = null;
        Optional<Utilisateur> proprietaireOpt = userRepository.findByEmail(proprietaireEmail);
        if (proprietaireOpt.isPresent() && proprietaireOpt.get() instanceof Agent) {
            agentProprietaire = (Agent) proprietaireOpt.get();
        }

        // Création du contrat avec l'agent propriétaire (pour auto-sélection assurance)
        RentalContract contrat = new RentalContract(
                loueur, vehicule, dateDebut, dateFin, lieuPrise, lieuDepose, assurance, agentProprietaire);

        // Lier le contrat à l'agent
        if (agentProprietaire != null) {
            contrat.setAgent(agentProprietaire);
            agentProprietaire.addContract(contrat);

            // GESTION OPTION PARKING - Loueur choisit un parking si l'agent a l'option
            // activée
            if (parkingId != null) {
                fr.miage.groupe2projetpoo.entity.assurance.OptionParking optParking = agentProprietaire
                        .getOption(fr.miage.groupe2projetpoo.entity.assurance.OptionParking.class);

                if (optParking != null && optParking.isEstActive()) {
                    // Récupérer le parking choisi par le loueur
                    Parking parkingChoisi = parkingRepository.findById(parkingId)
                            .orElseThrow(() -> new RuntimeException("Parking non trouvé avec l'ID: " + parkingId));

                    // Associer le parking à l'option et au contrat
                    optParking.setParkingPartenaire(parkingChoisi);
                    contrat.setLieuDepose(parkingChoisi.getNom());
                    contrat.setOptionParkingSelectionnee(true);

                    // Remplir les informations d'accès au parking pour le loueur
                    contrat.setParkingNom(parkingChoisi.getNom());
                    contrat.setParkingAdresse(parkingChoisi.getAdresse());
                    contrat.setParkingVille(parkingChoisi.getVille());
                    contrat.setParkingCodeAcces(parkingChoisi.getCodeAcces());
                    contrat.setParkingProcedureAcces(parkingChoisi.getProcedureAcces());
                    contrat.setParkingInstructionsSpeciales(parkingChoisi.getInstructionsSpeciales());
                } else {
                    throw new RuntimeException(
                            "L'agent n'a pas activé l'option Parking. Le loueur ne peut pas choisir de parking.");
                }
            }
        }

        return rentalRepository.save(contrat);
    }

    /**
     * Récupérer tous les contrats
     */
    public List<RentalContract> getTousLesContrats() {
        return rentalRepository.findAll();
    }

    /**
     * Récupérer un contrat par son ID
     */
    public Optional<RentalContract> getContratById(int id) {
        return rentalRepository.findById(id);
    }

    /**
     * Signer un contrat (par le loueur)
     */
    public RentalContract signerContrat(int contratId) {
        RentalContract contrat = rentalRepository.findById(contratId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé avec l'ID: " + contratId));

        contrat.signerLoueur();
        return rentalRepository.save(contrat);
    }

    /**
     * Supprimer un contrat
     */
    public void supprimerContrat(int id) {
        rentalRepository.deleteById(id);
    }

    /**
     * Récupérer les contrats d'un loueur par son email
     */
    public List<RentalContract> getContratsParLoueur(String email) {
        return rentalRepository.findByLoueurEmail(email);
    }

    /**
     * Récupérer les contrats d'un véhicule
     */
    public List<RentalContract> getContratsParVehicule(String vehiculeId) {
        return rentalRepository.findByVehiculeId(vehiculeId);
    }

    // ===== MÉTHODES UTILITAIRES =====

    public List<Loueur> getTousLesLoueurs() {
        return userRepository.getAllLoueurs();
    }

    public List<Vehicle> getTousLesVehicules() {
        return new ArrayList<>(vehicleRepository.findAll());
    }

    public List<Assurance> getToutesLesAssurances() {
        return userRepository.getAllAssurances();
    }

    private Assurance getAssuranceByNom(String nom) {
        return userRepository.getAllAssurances().stream()
                .filter(a -> a.getNom().equals(nom))
                .findFirst()
                .orElse(null);
    }

    // ===== MÉTHODES POUR L'ACCEPTATION MANUELLE =====

    /**
     * L'agent accepte manuellement un contrat
     */
    public RentalContract accepterContratParAgent(int contratId) {
        RentalContract contrat = rentalRepository.findById(contratId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé avec l'ID: " + contratId));

        contrat.signerAgent();
        return rentalRepository.save(contrat);
    }

    /**
     * Récupérer les contrats en attente d'acceptation par l'agent
     */
    public List<RentalContract> getContratsEnAttente() {
        return rentalRepository.findAll().stream()
                .filter(c -> c.estEnAttenteAgent())
                .toList();
    }

    /**
     * Terminer un contrat et déclencher l'entretien automatique si l'option est
     * active
     */
    public RentalContract terminerContrat(int contratId) {
        RentalContract contrat = rentalRepository.findById(contratId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé avec l'ID: " + contratId));

        // 1. Marquer comme terminé
        // TODO: Vérifier comment marquer un contrat comme terminé dans RentalContract
        // contrat.setStatut(false); // Cette méthode n'existe pas
        // Possibilité : contrat.setStatutLocation(StatutLocation.TERMINEE);

        // 2. Déclencher l'entretien automatique
        Agent agent = contrat.getAgent();

        // Sécurité : si l'agent n'est pas lié, on le cherche via le propriétaire du
        // véhicule
        if (agent == null && contrat.getVehicule() != null) {
            String ownerEmail = contrat.getVehicule().getProprietaire();
            Utilisateur u = userRepository.findByEmail(ownerEmail).orElse(null);
            if (u instanceof Agent)
                agent = (Agent) u;
        }

        if (agent != null && agent.aOptionActive(OptionEntretien.class)) {
            OptionEntretien opt = agent.getOption(OptionEntretien.class);
            if (opt.isAutomatique() && agent.getEntrepriseEntretienPreferee() != null) {
                // Planifier pour le lendemain
                java.time.LocalDate dateEntretien = java.time.LocalDate.now().plusDays(1);
                maintenanceService.requestMaintenance(
                        agent.getEmail(),
                        contrat.getVehicule().getIdVehicule(),
                        agent.getEntrepriseEntretienPreferee().getEmail(),
                        dateEntretien);
                System.out.println("INFO: Entretien automatique planifie pour le " + dateEntretien);
            }
        }

        // 3. Déclencher la récompense de parrainage (US.L.9)
        // Si le loueur a un parrain et c'est sa première location terminée
        Loueur loueur = contrat.getLoueur();
        if (loueur != null) {
            userService.declencherRecompenseParrainage(loueur.getEmail());
        }

        // 4. Déclencher la récompense de parrainage Agent
        // Si l'agent propriétaire du véhicule a un parrain et c'est le premier véhicule
        // loué
        if (agent != null) {
            userService.declencherRecompenseParrainageAgent(agent.getEmail());
        }

        return rentalRepository.save(contrat);
    }

    // ===== MÉTHODE POUR CHANGER LE STATUT DU CONTRAT =====

    /**
     * Mettre à jour le statut d'un contrat de location
     * 
     * @param contratId     ID du contrat
     * @param nouveauStatut Nouveau statut (EN_ATTENTE_SIGNATURE, SIGNE, EN_COURS,
     *                      TERMINEE, ANNULEE)
     */
    public RentalContract updateContratStatus(int contratId,
            fr.miage.groupe2projetpoo.entity.location.StatutLocation nouveauStatut) {
        RentalContract contrat = rentalRepository.findById(contratId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé avec l'ID: " + contratId));

        fr.miage.groupe2projetpoo.entity.location.StatutLocation ancienStatut = contrat.getStatutLocation();
        contrat.setStatutLocation(nouveauStatut);

        System.out.println("Statut du contrat " + contratId + " modifié: " + ancienStatut + " → " + nouveauStatut);

        return rentalRepository.save(contrat);
    }

    // ===== MÉTHODE POUR RENSEIGNER LE KILOMÉTRAGE FIN ET DÉCLENCHER LES
    // RÉCOMPENSES =====

    /**
     * Renseigner le kilométrage de fin et déclencher automatiquement les
     * récompenses de parrainage
     * 
     * @param contratId ID du contrat
     * @param km        Kilométrage au retour
     * @param photoNom  Nom du fichier photo de preuve
     * @return Le contrat mis à jour
     */
    public RentalContract renseignerKilometrageFin(int contratId, int km, String photoNom) {
        RentalContract contrat = rentalRepository.findById(contratId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé avec l'ID: " + contratId));

        // 1. Enregistrer le kilométrage de fin (cela change le statut à TERMINEE)
        contrat.renseignerKilometrageFin(km, photoNom);

        // 2. Déclencher la récompense de parrainage Loueur (US.L.9)
        Loueur loueur = contrat.getLoueur();
        if (loueur != null) {
            userService.declencherRecompenseParrainage(loueur.getEmail());
        }

        // 3. Déclencher la récompense de parrainage Agent
        // Récupérer l'agent propriétaire du véhicule
        String proprietaireEmail = contrat.getVehicule().getProprietaire();
        Agent agent = null;
        if (proprietaireEmail != null) {
            var userOpt = userRepository.findByEmail(proprietaireEmail);
            if (userOpt.isPresent() && userOpt.get() instanceof Agent) {
                agent = (Agent) userOpt.get();
                userService.declencherRecompenseParrainageAgent(agent.getEmail());
            }
        }

        return rentalRepository.save(contrat);
    }
}
