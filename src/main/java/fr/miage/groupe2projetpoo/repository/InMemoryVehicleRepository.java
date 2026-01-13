package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.vehicule.Disponibilite;
import fr.miage.groupe2projetpoo.entity.vehicule.TypeVehicule;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;

import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryVehicleRepository implements VehicleRepository {

    // Map de vehicule <id,vehicule>
    private final Map<String, Vehicle> vehicules = new ConcurrentHashMap<>();

    // Enregistre vehicule
    @Override
    public Vehicle save(Vehicle v) {
        vehicules.put(v.getIdVehicule(), v);
        return v;
    }

    // recupere tous les vehicules
    @Override
    public Collection<Vehicle> findAll(){
        return vehicules.values();
    }

    /***************************** Filtre ************************************/

    // Chercher par ID
    @Override
    public Optional<Vehicle> findById(String id) {
        return Optional.ofNullable(vehicules.get(id));
    }
    // Chercher par ville
    @Override
    public List<Vehicle> findByVille(String ville) {
        return vehicules.values().stream()
                .filter(v -> v.getVilleVehicule().equalsIgnoreCase(ville))
                .toList();
    }
    // Chercher par type
    @Override
    public List<Vehicle> findByType(String type) {
        TypeVehicule typeEnum;
        try {
            typeEnum = TypeVehicule.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return List.of();
        }
        return vehicules.values().stream()
                .filter(v -> v.getType() == typeEnum) .toList();

    }
    @Override
    public List<Vehicle> findByEnPause(){
        return vehicules.values().stream()
                .filter(v -> !v.getEstEnpause()) // veicule disponible sur le marche
                .toList();
    }

    // Verifier l'existance de l'ID
    @Override
    public boolean existsById(String id) {
        return vehicules.containsKey(id);
    }

    // Chercher par disponibilité
    @Override
    public Optional<Vehicle> findByDisponibility(LocalDate debut, LocalDate fin) {
        return vehicules.values().stream()
                .filter(v -> v.estDisponibleMap(debut, fin))
                .findFirst();
    }

    /************************************** Autres Methodes ******************************************/
    // Suppression by Id
    @Override
    public void deleteById(String id){
        if(!vehicules.containsKey(id)){
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        vehicules.remove(id);
    }

    // Modifier un vehicule
    @Override
    public void modifVehicule(String id, Vehicle modif) {
        if (!vehicules.containsKey(id)) {
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        vehicules.put(id, modif);
    }


    /************************************* Planning ****************************************/

    // Récupèrer le planning complet d'un vehicule
    @Override
    public List<Disponibilite> getPlanning(String id){
        Vehicle v = vehicules.get(id);
        if(v == null){
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        return v.getPlanningDisponible();
    }

    // Modifier planning
    @Override
    public void addPlanning(String id, LocalDate debut, LocalDate fin) {
        Vehicle v = vehicules.get(id);
        if(v == null){
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        v.addPlanningDispo(debut,fin);
    }

    // DELETE planning
    @Override
    public void removeCreneau(String id, int index){
        Vehicle v = vehicules.get(id);
        if(v == null){
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        v.removeCreneauPlanning(index);
    }

    // verifier la disponibilité d'un vehicule a un creneaux
    @Override
    public boolean estDisponible(String id, LocalDate debut, LocalDate fin){
        Vehicle v = vehicules.get(id);
        if(v == null){
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        return v.estDisponible(debut,fin);
    }
}