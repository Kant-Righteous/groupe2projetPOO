package fr.miage.groupe2projetpoo.entity.vehicule;

public enum TypeVehicule {
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

    // Methode utilitaire pour convertir une string de manière safe si besoin
    public static TypeVehicule fromString(String type) {
        try {
            return TypeVehicule.valueOf(type.toUpperCase());
        } catch (Exception e) {
            return CITADINE; // Valeur par défaut
        }
    }
}
