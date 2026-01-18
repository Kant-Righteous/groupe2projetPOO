package fr.miage.groupe2projetpoo.entity.vehicule;

import java.time.LocalDate;

public class Disponibilite {
    // Propriétes
    private LocalDate debut;
    private LocalDate fin;

    // Construteur
    public Disponibilite(LocalDate debut, LocalDate fin) {
        this.debut = debut;
        this.fin = fin;
    }

    // Verifier le chevauchement de 2 dates
    public boolean chevauchement(LocalDate d, LocalDate f){
        return !(f.isBefore(debut) || d.isAfter(fin) );
    }

    // getter
    public LocalDate getDebut() {
        return debut;
    }
    public LocalDate getFin() {
        return fin;
    }
}
