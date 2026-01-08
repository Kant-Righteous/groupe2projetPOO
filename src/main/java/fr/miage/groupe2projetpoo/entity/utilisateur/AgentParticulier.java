package fr.miage.groupe2projetpoo.entity.utilisateur;

/**
 * Agent particulier (personne physique)
 */
public class AgentParticulier extends Agent {

    public AgentParticulier() {
        super();
    }

    public AgentParticulier(String nom, String prenom, String password, String email, String tel) {
        super(nom, prenom, password, email, tel);
    }

    @Override
    public Role getRole() {
        return Role.AGENT_PARTICULIER;
    }
}
