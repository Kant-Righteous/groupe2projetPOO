package fr.miage.groupe2projetpoo.repository;

import fr.miage.groupe2projetpoo.entity.utilisateur.Utilisateur;
import fr.miage.groupe2projetpoo.entity.vehicule.Vehicle;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class VehicleRepository {
    //Map de vehicule
    private final Map<String, Vehicle> vehicules = new ConcurrentHashMap<>();

    // Récupérer tous les véhicules
    public Collection<Vehicle> findAll() {
        return vehicules.values();
    }

    // Récupérer toutes les informations de tous les véhicules
    public String getAllVehicleInfo() {
        if (vehicules.isEmpty()) {
            return "Aucun véhicule dans le repository.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Liste de tous les véhicules ===\n\n");

        for (Vehicle v : vehicules.values()) {
            sb.append("ID: ").append(v.getIdVehicule()).append("\n");
            sb.append("Type: ").append(v.getTypeVehicule()).append("\n");
            sb.append("Marque: ").append(v.getMarqueVehicule()).append("\n");
            sb.append("Modèle: ").append(v.getModeleVehicule()).append("\n");
            sb.append("Couleur: ").append(v.getCouleurVehicule()).append("\n");
            sb.append("Ville: ").append(v.getVilleVehicule()).append("\n");
            sb.append("Prix/Jour: ").append(v.getPrixVehiculeParJour()).append(" €\n");
            sb.append("Disponible: ").append(v.getEstDisponible() ? "Oui" : "Non").append("\n");
            sb.append("Propriétaire: ")
                    .append(v.getProprietaire() != null ? v.getProprietaire().toString() : "Non défini").append("\n");
            sb.append("----------------------------------------\n");
        }

        return sb.toString();
    }

    public Vehicle save(Vehicle v) {
        vehicules.put(v.getIdVehicule(), v);
        return v;
    }

    public Optional<Vehicle> findById(String id) {
        return Optional.ofNullable(vehicules.get(id));
    }
    public boolean existsById(String id) {
        return vehicules.containsKey(id);
    }
    /*public Optional<Vehicle> findByDisponibility(LocalDate debut, LocalDate fin) {
        return Optional.ofNullable(vehicules.get(estdispo));
    }*/

    public void MettreAjourVehicule() {
    }

    public boolean suppVehicule(String id) {
        vehicules.remove(id);
        return true;
    }

}
