package fr.miage.groupe2projetpoo.entity.vehicule;

public enum TypeVehicule {
    VOITURE,
    SUV,
    UTILITAIRE,
    CITADINE,
    BERLINE,
    CABRIOLET,
    BREAK,
    MONOSPACE,
    MOTO,
    CAMION,
    VOITURE;

    public static TypeVehicule fromString(String type) {
        try {
            return TypeVehicule.valueOf(type.toUpperCase());
        } catch (Exception e) {
            return VOITURE; // Valeur par défaut
        }
    }
}
