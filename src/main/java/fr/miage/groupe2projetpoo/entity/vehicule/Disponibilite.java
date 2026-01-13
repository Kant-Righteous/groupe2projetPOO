package fr.miage.groupe2projetpoo.entity.vehicule;

import java.time.LocalDate;

public class Disponibilite {
    private LocalDate debut;
    private LocalDate fin;

    public Disponibilite(LocalDate debut, LocalDate fin) {
        this.debut = debut;
        this.fin = fin;
    }

    public boolean chevauchement(LocalDate d, LocalDate f){
        return !(f.isBefore(debut) || d.isAfter(fin) );
    }
}
