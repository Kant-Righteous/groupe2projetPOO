package fr.miage.groupe2projetpoo.controller;

import fr.miage.groupe2projetpoo.entity.assurance.OptionAcceptationManuelle;
import fr.miage.groupe2projetpoo.entity.assurance.OptionEntretien;
import fr.miage.groupe2projetpoo.entity.utilisateur.Agent;
import fr.miage.groupe2projetpoo.entity.utilisateur.AgentParticulier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/options-test")
public class OptionTestController {

    @GetMapping("/demo")
    public Map<String, Object> demoOptions() {
        Agent agent = new AgentParticulier("Test", "Agent", "pass", "test@agent.com", "0102030405");

        // Ajout Option Manuelle
        OptionAcceptationManuelle opt1 = new OptionAcceptationManuelle();
        agent.ajouterOption(opt1);

        // Ajout Option Entretien
        OptionEntretien opt2 = new OptionEntretien(true);
        agent.ajouterOption(opt2);

        Map<String, Object> result = new HashMap<>();
        result.put("agent", agent.getNom());
        result.put("options", agent.getOptionsPayantes());
        result.put("cout_mensuel_total", agent.calculerFactureMensuelle());

        return result;
    }
}
