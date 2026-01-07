package fr.miage.groupe2projetpoo.entity.utilisateur;

/**
 * Agent particulier (personne physique)
 */
public class AgentParticulier extends Agent {

    public AgentParticulier() {
        super();
    }

    public AgentParticulier(String email, String password, String nom, String prenom) {
        super(email, password, nom, prenom);
    }

    @Override
    public Role getRole() {
        return Role.AGENT_PARTICULIER;
    }
}
