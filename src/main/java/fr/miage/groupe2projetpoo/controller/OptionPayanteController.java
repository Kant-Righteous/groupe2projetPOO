package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.assurance.OptionEntretien;
import fr.miage.groupe2projetpoo.entity.assurance.OptionParking;
import fr.miage.groupe2projetpoo.entity.assurance.OptionPayante;
import fr.miage.groupe2projetpoo.entity.infrastructure.Parking;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/options")
public class OptionPayanteController {

    private final UserRepository userRepository;

    public OptionPayanteController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Récupérer les options payantes d'un agent via son email
     */
    @GetMapping("/agent/{email}")
    public List<OptionPayante> getOptionsAgent(@PathVariable String email) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get() instanceof Agent) {
            return ((Agent) userOpt.get()).getOptionsPayantes();
        }
        throw new RuntimeException("Agent non trouvé ou l'utilisateur n'est pas un agent");
    }

    /**
     * Souscrire à l'option Entretien Automatique pour un agent
     */
    @PostMapping("/souscrire/entretien/{email}")
    public Map<String, String> souscrireOptionEntretien(@PathVariable String email) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get() instanceof Agent) {
            Agent agent = (Agent) userOpt.get();
            // Création de l'option
            OptionEntretien option = new OptionEntretien(true);
            agent.ajouterOption(option);

            // Note: En mode InMemory, l'objet agent est modifié en mémoire directement.
            // Si JPA, il faudrait userRepository.save(agent);

            return Map.of("message", "Option Entretien ajoutée avec succès à " + agent.getNom());
        }
        return Map.of("error", "Agent introuvable");
    }

    /**
     * Souscrire à l'option Parking pour un agent
     */
    @PostMapping("/souscrire/parking/{email}")
    public Map<String, String> souscrireOptionParking(@PathVariable String email) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get() instanceof Agent) {
            Agent agent = (Agent) userOpt.get();

            // Création d'un parking fictif pour l'exemple (ou à récupérer d'un repo
            // Parking)
            Parking parking = new Parking(1, "Parking Central", "10 Rue de la Paix", "Paris", 200, 15.0);

            // Utilisation du nouveau constructeur complet
            OptionParking option = new OptionParking("Parking VIP", 25.0, 24, 30, 5.0, parking);

            agent.ajouterOption(option);

            return Map.of("message", "Option Parking ajoutée avec succès à " + agent.getNom());
        }
        return Map.of("error", "Agent introuvable");
    }
}
