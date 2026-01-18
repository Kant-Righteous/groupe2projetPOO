package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.location.RentalContract;
import fr.miage.groupe2projetpoo.entity.utilisateur.*;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des utilisateurs (inscription et connexion)
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Inscription d'un utilisateur
     */
    public Utilisateur register(String nom, String prenom, String password, String email, String tel, Role role) {
        if (userRepository.existsByEmail(email)) {
            return null;
        }

        Utilisateur user;
        switch (role) {
            case AGENT_PARTICULIER:
                user = new AgentParticulier(nom, prenom, password, email, tel);
                break;
            case AGENT_PROFESSIONNEL:
                user = new AgentProfessionnel(nom, prenom, password, email, tel, null, null);
                break;
            case LOUEUR:
                user = new Loueur(nom, prenom, password, email, tel, null, null);
                break;
            default:
                return null;
        }

        return userRepository.save(user);
    }

    /**
     * Connexion d'un utilisateur
     */
    public Utilisateur login(String email, String password) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return null;
        }

        Utilisateur user = userOpt.get();

        if (!user.getPassword().equals(password)) {
            return null;
        }

        return user;
    }

    /**
     * Trouver un utilisateur par email (pour vérification)
     */
    public Optional<Utilisateur> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Récupérer les véhicules d'un utilisateur (Loueur ou Agent)
     */
    public List<Vehicle> getVehiclesByUserEmail(String email) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Utilisateur user = userOpt.get();

        if (user instanceof Loueur) {
            return ((Loueur) user).getVehicles();
        } else if (user instanceof Agent) {
            return ((Agent) user).getVehicleList();
        }

        return new ArrayList<>();
    }

    /**
     * Récupérer les contrats d'un utilisateur (Loueur ou Agent)
     */
    public List<RentalContract> getContractsByUserEmail(String email) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Utilisateur user = userOpt.get();

        if (user instanceof Loueur) {
            return ((Loueur) user).getContracts();
        } else if (user instanceof Agent) {
            return ((Agent) user).getContracts();
        }

        return new ArrayList<>();
    }

    /**
     * Récupérer tous les agents (pour les visiteurs)
     */
    public List<Agent> getAllAgents() {
        return userRepository.getAllAgents();
    }

    public Utilisateur updateUserInfo(String email, java.util.Map<String, String> updates) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return null;
        }

        Utilisateur user = userOpt.get();

        // Vérifier que l'utilisateur est un Loueur ou un Agent
        if (!(user instanceof Loueur) && !(user instanceof Agent)) {
            return null;
        }

        // Mettre à jour les champs communs (Utilisateur)
        if (updates.containsKey("nom")) {
            user.setNom(updates.get("nom"));
        }
        if (updates.containsKey("prenom")) {
            user.setPrenom(updates.get("prenom"));
        }
        if (updates.containsKey("tel")) {
            user.setTel(updates.get("tel"));
        }
        if (updates.containsKey("password")) {
            user.setPassword(updates.get("password"));
        }

        // Mettre à jour les champs spécifiques au Loueur
        if (user instanceof Loueur) {
            Loueur loueur = (Loueur) user;
            if (updates.containsKey("iban")) {
                loueur.setIban(updates.get("iban"));
            }
            if (updates.containsKey("nomSociete")) {
                loueur.setNomSociete(updates.get("nomSociete"));
            }
        }

        // Mettre à jour les champs spécifiques à l'AgentProfessionnel
        if (user instanceof AgentProfessionnel) {
            AgentProfessionnel agentPro = (AgentProfessionnel) user;
            if (updates.containsKey("nomEntreprise")) {
                agentPro.setNomEntreprise(updates.get("nomEntreprise"));
            }
            if (updates.containsKey("siret")) {
                agentPro.setSiret(updates.get("siret"));
            }
        }

        return userRepository.save(user);
    }

    // ===== Parrainage (US.L.9) =====

    /**
     * Montant de la récompense de parrainage (configurable)
     */
    public static final double MONTANT_RECOMPENSE_PARRAINAGE = 20.0;

    /**
     * Enregistrer un parrainage - le filleul désigne son parrain
     * 
     * @param filleulEmail Email du filleul (celui qui est parrainé)
     * @param parrainEmail Email du parrain (celui qui parraine)
     * @return true si le parrainage a été enregistré, false sinon
     */
    public boolean parrainerLoueur(String filleulEmail, String parrainEmail) {
        // Vérifier que les deux emails sont différents
        if (filleulEmail.equals(parrainEmail)) {
            return false;
        }

        // Récupérer le filleul
        Optional<Utilisateur> filleulOpt = userRepository.findByEmail(filleulEmail);
        if (filleulOpt.isEmpty() || !(filleulOpt.get() instanceof Loueur)) {
            return false;
        }
        Loueur filleul = (Loueur) filleulOpt.get();

        // Vérifier que le filleul n'a pas déjà un parrain
        if (filleul.getParrain() != null) {
            return false;
        }

        // Récupérer le parrain
        Optional<Utilisateur> parrainOpt = userRepository.findByEmail(parrainEmail);
        if (parrainOpt.isEmpty() || !(parrainOpt.get() instanceof Loueur)) {
            return false;
        }
        Loueur parrain = (Loueur) parrainOpt.get();

        // Enregistrer le parrainage
        filleul.setParrain(parrain);
        parrain.addFilleul(filleul);

        // Sauvegarder les modifications
        userRepository.save(filleul);
        userRepository.save(parrain);

        System.out.println("INFO: Parrainage enregistre: " + parrainEmail + " parraine " + filleulEmail);
        return true;
    }

    /**
     * Récupérer les informations de parrainage d'un loueur
     * 
     * @param email Email du loueur
     * @return Map contenant les informations de parrainage
     */
    public java.util.Map<String, Object> getParrainageInfo(String email) {
        java.util.Map<String, Object> info = new java.util.HashMap<>();

        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Loueur)) {
            info.put("error", "Utilisateur non trouvé ou n'est pas un Loueur");
            return info;
        }

        Loueur loueur = (Loueur) userOpt.get();

        info.put("email", loueur.getEmail());
        info.put("parrainEmail", loueur.getParrainEmail());
        info.put("soldeParrainage", loueur.getSoldeParrainage());
        info.put("nombreFilleuls", loueur.getNombreFilleuls());
        info.put("aCompletePremiereLocation", loueur.isACompletePremiereLocation());

        // Liste des emails des filleuls
        List<String> filleulsEmails = new ArrayList<>();
        for (Loueur filleul : loueur.getFilleuls()) {
            filleulsEmails.add(filleul.getEmail());
        }
        info.put("filleuls", filleulsEmails);

        return info;
    }

    /**
     * Déclencher la récompense de parrainage quand le filleul complète sa première
     * location
     * 
     * @param filleulEmail Email du filleul qui a complété une location
     */
    public void declencherRecompenseParrainage(String filleulEmail) {
        Optional<Utilisateur> filleulOpt = userRepository.findByEmail(filleulEmail);
        if (filleulOpt.isEmpty() || !(filleulOpt.get() instanceof Loueur)) {
            return;
        }

        Loueur filleul = (Loueur) filleulOpt.get();

        // Vérifier que c'est bien la première location et qu'il a un parrain
        if (filleul.isACompletePremiereLocation()) {
            // Déjà récompensé, ne rien faire
            return;
        }

        if (filleul.getParrain() == null) {
            // Pas de parrain, juste marquer la première location comme complétée
            filleul.setACompletePremiereLocation(true);
            userRepository.save(filleul);
            return;
        }

        // Marquer la première location comme complétée
        filleul.setACompletePremiereLocation(true);

        // Ajouter la récompense au parrain
        Loueur parrain = filleul.getParrain();
        parrain.ajouterRecompenseParrainage(MONTANT_RECOMPENSE_PARRAINAGE);

        // Sauvegarder les modifications
        userRepository.save(filleul);
        userRepository.save(parrain);

        System.out.println(
                "INFO: Recompense de parrainage de " + MONTANT_RECOMPENSE_PARRAINAGE + " euros ajoutee a " + parrain.getEmail());
    }

    /**
     * Récupérer tous les loueurs
     */
    public List<Loueur> getAllLoueurs() {
        return userRepository.getAllLoueurs();
    }

    // ===== Parrainage Agent =====

    /**
     * Montant de la récompense de parrainage pour les agents (configurable)
     */
    public static final double MONTANT_RECOMPENSE_PARRAINAGE_AGENT = 30.0;

    /**
     * Enregistrer un parrainage entre agents - le filleul désigne son parrain
     * 
     * @param filleulEmail Email du filleul (agent qui est parrainé)
     * @param parrainEmail Email du parrain (agent qui parraine)
     * @return true si le parrainage a été enregistré, false sinon
     */
    public boolean parrainerAgent(String filleulEmail, String parrainEmail) {
        // Vérifier que les deux emails sont différents
        if (filleulEmail.equals(parrainEmail)) {
            return false;
        }

        // Récupérer le filleul
        Optional<Utilisateur> filleulOpt = userRepository.findByEmail(filleulEmail);
        if (filleulOpt.isEmpty() || !(filleulOpt.get() instanceof Agent)) {
            return false;
        }
        Agent filleul = (Agent) filleulOpt.get();

        // Vérifier que le filleul n'a pas déjà un parrain
        if (filleul.getParrain() != null) {
            return false;
        }

        // Récupérer le parrain
        Optional<Utilisateur> parrainOpt = userRepository.findByEmail(parrainEmail);
        if (parrainOpt.isEmpty() || !(parrainOpt.get() instanceof Agent)) {
            return false;
        }
        Agent parrain = (Agent) parrainOpt.get();

        // Enregistrer le parrainage
        filleul.setParrain(parrain);
        parrain.addFilleul(filleul);

        // Sauvegarder les modifications
        userRepository.save(filleul);
        userRepository.save(parrain);

        System.out.println("INFO: Parrainage Agent enregistre: " + parrainEmail + " parraine " + filleulEmail);
        return true;
    }

    /**
     * Récupérer les informations de parrainage d'un agent
     * 
     * @param email Email de l'agent
     * @return Map contenant les informations de parrainage
     */
    public java.util.Map<String, Object> getParrainageAgentInfo(String email) {
        java.util.Map<String, Object> info = new java.util.HashMap<>();

        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Agent)) {
            info.put("error", "Utilisateur non trouvé ou n'est pas un Agent");
            return info;
        }

        Agent agent = (Agent) userOpt.get();

        info.put("email", agent.getEmail());
        info.put("parrainEmail", agent.getParrainEmail());
        info.put("soldeParrainage", agent.getSoldeParrainage());
        info.put("nombreFilleuls", agent.getNombreFilleuls());
        info.put("aEuVehiculeLoue", agent.isAEuVehiculeLoue());

        // Liste des emails des filleuls
        List<String> filleulsEmails = new ArrayList<>();
        for (Agent filleul : agent.getFilleuls()) {
            filleulsEmails.add(filleul.getEmail());
        }
        info.put("filleuls", filleulsEmails);

        return info;
    }

    /**
     * Déclencher la récompense de parrainage quand le filleul (Agent) a un véhicule
     * loué
     * Appelé lorsqu'un contrat est terminé et que l'agent propriétaire du véhicule
     * est un filleul qui n'a pas encore déclenché la récompense
     * 
     * @param agentEmail Email de l'agent propriétaire du véhicule loué
     */
    public void declencherRecompenseParrainageAgent(String agentEmail) {
        Optional<Utilisateur> agentOpt = userRepository.findByEmail(agentEmail);
        if (agentOpt.isEmpty() || !(agentOpt.get() instanceof Agent)) {
            return;
        }

        Agent agent = (Agent) agentOpt.get();

        // Vérifier que c'est la première fois qu'un véhicule est loué
        if (agent.isAEuVehiculeLoue()) {
            // Déjà récompensé, ne rien faire
            return;
        }

        if (agent.getParrain() == null) {
            // Pas de parrain, juste marquer comme ayant eu un véhicule loué
            agent.setAEuVehiculeLoue(true);
            userRepository.save(agent);
            return;
        }

        // Marquer comme ayant eu un véhicule loué
        agent.setAEuVehiculeLoue(true);

        // Ajouter la récompense au parrain
        Agent parrain = agent.getParrain();
        parrain.ajouterRecompenseParrainage(MONTANT_RECOMPENSE_PARRAINAGE_AGENT);

        // Sauvegarder les modifications
        userRepository.save(agent);
        userRepository.save(parrain);

        System.out.println("INFO: Recompense de parrainage Agent de " + MONTANT_RECOMPENSE_PARRAINAGE_AGENT
                + " euros ajoutee a " + parrain.getEmail());
    }
}
