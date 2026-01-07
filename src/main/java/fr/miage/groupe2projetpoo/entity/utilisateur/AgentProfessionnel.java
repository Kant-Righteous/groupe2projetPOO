package fr.miage.groupe2projetpoo.entity.utilisateur;

/**
 * Agent professionnel (entreprise)
 */
public class AgentProfessionnel extends Agent {

    private String nomEntreprise;
    private String siret;

    public AgentProfessionnel() {
        super();
    }

    public AgentProfessionnel(String email, String password, String nom, String prenom,
            String nomEntreprise, String siret) {
        super(email, password, nom, prenom);
        this.nomEntreprise = nomEntreprise;
        this.siret = siret;
    }

    @Override
    public Role getRole() {
        return Role.AGENT_PROFESSIONNEL;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }
}
