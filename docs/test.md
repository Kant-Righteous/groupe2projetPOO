# Test des API

Ce document contient tous les endpoints API disponibles dans l'application avec des exemples de tests.

## Table des matières

- [Utilisateurs (UserController)](#utilisateurs-usercontroller)
  - [Register](#register)
  - [Login](#login)
  - [Récupérer le profil](#récupérer-le-profil)
  - [Modifier le profil](#modifier-le-profil)
  - [Récupérer les véhicules d'un utilisateur](#récupérer-les-véhicules-dun-utilisateur)
  - [Récupérer les contrats d'un utilisateur](#récupérer-les-contrats-dun-utilisateur)
  - [Liste des agents](#liste-des-agents)
  - [Informations détaillées d'un agent](#informations-détaillées-dun-agent)
  - [Parrainage Loueur](#parrainage-loueur)
  - [Parrainage Agent](#parrainage-agent)
- [Véhicules (VehicleController)](#véhicules-vehiclecontroller)
  - [Ajouter un véhicule](#ajouter-un-véhicule)
  - [Modifier un véhicule](#modifier-un-véhicule)
  - [Supprimer un véhicule](#supprimer-un-véhicule)
  - [Modifier le statut (pause)](#modifier-le-statut-pause)
  - [Voir tous les véhicules](#voir-tous-les-véhicules)
  - [Récupérer les infos d'un véhicule](#récupérer-les-infos-dun-véhicule)
  - [Rechercher par ville](#rechercher-par-ville)
  - [Rechercher par type](#rechercher-par-type)
  - [Rechercher par disponibilité](#rechercher-par-disponibilité)
  - [Rechercher par prix](#rechercher-par-prix)
  - [Rechercher par modèle](#rechercher-par-modèle)
  - [Rechercher par marque](#rechercher-par-marque)
  - [Multi-filtrage](#multi-filtrage)
  - [Gestion du planning](#gestion-du-planning)
  - [Véhicules proches](#véhicules-proches)
  - [Historique et notations](#historique-et-notations)
- [Location (RentalController)](#location-rentalcontroller)
  - [Créer un contrat](#créer-un-contrat)
  - [Récupérer tous les contrats](#récupérer-tous-les-contrats)
  - [Récupérer un contrat par ID](#récupérer-un-contrat-par-id)
  - [Signer un contrat](#signer-un-contrat)
  - [Accepter un contrat (agent)](#accepter-un-contrat-agent)
  - [Terminer une location](#terminer-une-location)
  - [Supprimer un contrat](#supprimer-un-contrat)
  - [Contrats d'un loueur](#contrats-dun-loueur)
  - [Contrats en attente](#contrats-en-attente)
  - [Kilométrage](#kilométrage)
  - [Statut du contrat](#statut-du-contrat)
  - [Informations parking Vienci](#informations-parking-vienci)
  - [Données utilitaires](#données-utilitaires)
- [Notations (NotationController)](#notations-notationcontroller)
  - [Noter un agent](#noter-un-agent)
  - [Noter un loueur](#noter-un-loueur)
  - [Noter un véhicule](#noter-un-véhicule)
  - [Consulter les notes](#consulter-les-notes)
  - [Modifier une note](#modifier-une-note)
  - [Supprimer une note](#supprimer-une-note)
  - [Répondre à une note](#répondre-à-une-note)
- [Messagerie (MessageController)](#messagerie-messagecontroller)
  - [Envoyer un message](#envoyer-un-message)
  - [Voir une conversation](#voir-une-conversation)
- [Options Payantes (OptionPayanteController)](#options-payantes-optionpayantecontroller)
  - [Voir les options d'un agent](#voir-les-options-dun-agent)
  - [Ajouter une option](#ajouter-une-option)
  - [Supprimer les options](#supprimer-les-options)
  - [Voir la facture](#voir-la-facture)
- [Contrôle Technique (ControleTechniqueController)](#contrôle-technique-controletechniquecontroller)
  - [Enregistrer un CT](#enregistrer-un-ct)
  - [Consulter un CT](#consulter-un-ct)
  - [Supprimer un CT](#supprimer-un-ct)
- [Maintenance (MaintenanceController)](#maintenance-maintenancecontroller)
  - [Entreprises d'entretien](#entreprises-dentretien)
  - [Demandes d'entretien](#demandes-dentretien)
  - [Historique des entretiens](#historique-des-entretiens)
- [Gestion Agent (GestionAgentController)](#gestion-agent-gestionagentcontroller)
  - [Alertes maintenance](#alertes-maintenance)
  - [Recommandations entretien](#recommandations-entretien)
  - [Simulations](#simulations)
- [Prix (PrixController)](#prix-prixcontroller)
  - [Simuler un prix](#simuler-un-prix)
  - [Barèmes de réduction](#barèmes-de-réduction)
- [Parkings (ParkingController)](#parkings-parkingcontroller)
  - [Liste des parkings](#liste-des-parkings)
  - [Détails d'un parking](#détails-dun-parking)
- [Assurances (AssuranceController)](#assurances-assurancecontroller)
  - [Liste des assurances](#liste-des-assurances)
  - [Créer une assurance](#créer-une-assurance)
- [Comptes de test](#comptes-de-test)

---

## Utilisateurs (UserController)

### Register

Inscription d'un nouvel utilisateur.

```
POST http://localhost:8080/api/users/register
Content-Type: application/json

{
    "nom": "Dupont",
    "prenom": "Jean",
    "password": "123456",
    "email": "jean.dupont@test.com",
    "tel": "0612345678",
    "role": "LOUEUR"
}
```

Rôles disponibles: `LOUEUR`, `AGENT_PARTICULIER`, `AGENT_PROFESSIONNEL`, `VISITEUR`

### Login

Connexion d'un utilisateur existant.

```
POST http://localhost:8080/api/users/login
Content-Type: application/json

{
    "email": "alice@test.com",
    "password": "123456"
}
```

### Récupérer le profil

Récupérer les informations d'un utilisateur.

```
GET http://localhost:8080/api/users/alice@test.com
```

### Modifier le profil

Mettre à jour les informations d'un utilisateur.

```
PUT http://localhost:8080/api/users/loueur1@test.com/update
Content-Type: application/json

{
    "nom": "NouveauNom",
    "prenom": "NouveauPrenom",
    "tel": "0698765432"
}
```

Champs modifiables:
- Communs: `nom`, `prenom`, `tel`, `password`
- Loueur: `iban`, `nomSociete`
- AgentProfessionnel: `nomEntreprise`, `siret`

### Récupérer les véhicules d'un utilisateur

```
GET http://localhost:8080/api/users/alice@test.com/vehicles
```

### Récupérer les contrats d'un utilisateur

```
GET http://localhost:8080/api/users/loueur1@test.com/contracts
```

### Liste des agents

Récupérer la liste de tous les agents (public pour les visiteurs).

```
GET http://localhost:8080/api/users/agents
```

### Informations détaillées d'un agent

Récupérer les informations complètes d'un agent, incluant ses options payantes.

```
GET http://localhost:8080/api/users/agents/alice@test.com/info
```

### Parrainage Loueur

#### Enregistrer un parrainage

```
POST http://localhost:8080/api/users/nouveau.loueur@test.com/parrainage
Content-Type: application/json

{
    "parrainEmail": "loueur1@test.com"
}
```

#### Consulter les informations de parrainage

```
GET http://localhost:8080/api/users/loueur1@test.com/parrainage
```

### Parrainage Agent

#### Enregistrer un parrainage entre agents

```
POST http://localhost:8080/api/users/agents/nouvel.agent@test.com/parrainage
Content-Type: application/json

{
    "parrainEmail": "alice@test.com"
}
```

#### Consulter les informations de parrainage d'un agent

```
GET http://localhost:8080/api/users/agents/alice@test.com/parrainage
```

---

## Véhicules (VehicleController)

### Ajouter un véhicule

```
POST http://localhost:8080/api/vehicules/add
Content-Type: application/json

{
    "idVehicule": "V100",
    "type": "VOITURE",
    "marqueVehicule": "Toyota",
    "couleurVehicule": "Bleu",
    "modeleVehicule": "Yaris",
    "villeVehicule": "Paris",
    "prixVehiculeParJour": "45.0",
    "proprietaire": "alice@test.com",
    "estEnPause": "false"
}
```

Types disponibles: `VOITURE`, `MOTO`, `CAMION`

### Modifier un véhicule

```
PUT http://localhost:8080/api/vehicules/update
Content-Type: application/json

{
    "idVehicule": "V001",
    "marqueVehicule": "Renault",
    "couleurVehicule": "Rouge",
    "modeleVehicule": "Clio",
    "villeVehicule": "Lyon",
    "prixVehiculeParJour": "35.0",
    "proprietaire": "alice@test.com",
    "estEnPause": "false"
}
```

### Supprimer un véhicule

```
DELETE http://localhost:8080/api/vehicules/delete
Content-Type: application/json

{
    "idVehicule": "V100"
}
```

### Modifier le statut (pause)

```
PUT http://localhost:8080/api/vehicules/updateEstEnPause
Content-Type: application/json

{
    "idVehicule": "V001",
    "estEnPause": "true"
}
```

### Voir tous les véhicules

```
GET http://localhost:8080/api/vehicules/all
```

### Récupérer les infos d'un véhicule

#### Informations simples

```
GET http://localhost:8080/api/vehicules/info
Content-Type: application/json

{
    "id": "V001"
}
```

#### Informations complètes (avec notations et historique)

```
GET http://localhost:8080/api/vehicules/infoCompleted
Content-Type: application/json

{
    "id": "V001"
}
```

### Rechercher par ville

```
GET http://localhost:8080/api/vehicules/parVille
Content-Type: application/json

{
    "ville": "Paris"
}
```

### Rechercher par type

```
GET http://localhost:8080/api/vehicules/parType
Content-Type: application/json

{
    "type": "VOITURE"
}
```

### Rechercher par disponibilité

Récupérer tous les véhicules qui ne sont pas en pause.

```
GET http://localhost:8080/api/vehicules/parDisponibilité
```

### Rechercher par prix

```
GET http://localhost:8080/api/vehicules/parPrix
Content-Type: application/json

{
    "min": "20.0",
    "max": "50.0"
}
```

### Rechercher par modèle

```
GET http://localhost:8080/api/vehicules/parModele
Content-Type: application/json

{
    "modele": "Clio"
}
```

### Rechercher par marque

```
GET http://localhost:8080/api/vehicules/parMarque
Content-Type: application/json

{
    "marque": "Renault"
}
```

### Multi-filtrage

Filtrage combiné avec plusieurs critères optionnels.

```
POST http://localhost:8080/api/vehicules/MultiFiltrage
Content-Type: application/json

{
    "ville": "Paris",
    "type": "VOITURE",
    "marque": "Renault",
    "estEnPause": "false",
    "min": "20.0",
    "max": "100.0",
    "debut": "2026-01-20",
    "fin": "2026-01-25"
}
```

Tous les champs sont optionnels.

### Gestion du planning

#### Consulter le planning d'un véhicule

```
GET http://localhost:8080/api/vehicules/planning
Content-Type: application/json

{
    "id": "V001"
}
```

#### Ajouter un créneau de disponibilité

```
POST http://localhost:8080/api/vehicules/planning/add
Content-Type: application/json

{
    "id": "V001",
    "debut": "2026-02-01",
    "fin": "2026-02-15"
}
```

#### Supprimer un créneau

```
DELETE http://localhost:8080/api/vehicules/planning/delete
Content-Type: application/json

{
    "id": "V001",
    "index": "1"
}
```

#### Vérifier la disponibilité sur une période

```
POST http://localhost:8080/api/vehicules/planning/verifDisponible
Content-Type: application/json

{
    "id": "V001",
    "debut": "2026-01-20",
    "fin": "2026-01-25"
}
```

### Véhicules proches

#### Par ville

```
POST http://localhost:8080/api/vehicules/proches
Content-Type: application/json

{
    "ville": "Paris",
    "rayonKm": 50
}
```

Rayon:
- 0-49 km : même ville uniquement
- 50-99 km : même région
- 100+ km : toutes les régions

#### Par coordonnées GPS

```
POST http://localhost:8080/api/vehicules/proches/gps
Content-Type: application/json

{
    "latitude": 48.8566,
    "longitude": 2.3522,
    "rayonKm": 50
}
```

#### Suggestions pour un loueur

```
GET http://localhost:8080/api/vehicules/suggestions/loueur1@test.com?rayonKm=50
```

### Historique et notations

#### Historique des contrats d'un véhicule

```
GET http://localhost:8080/api/vehicules/historiqueContrat
Content-Type: application/json

{
    "id": "V001"
}
```

#### Liste des notations d'un véhicule

```
GET http://localhost:8080/api/vehicules/notations
Content-Type: application/json

{
    "id": "V001"
}
```

---

## Location (RentalController)

### Créer un contrat

```
POST http://localhost:8080/api/rentals
Content-Type: application/json

{
    "loueurEmail": "loueur1@test.com",
    "vehiculeId": "V001",
    "dateDebut": "2026-01-20",
    "dateFin": "2026-01-25",
    "lieuPrise": "Paris",
    "lieuDepose": "Paris",
    "assuranceNom": "AZA Classique"
}
```

Avec option parking (optionnel):

```
POST http://localhost:8080/api/rentals
Content-Type: application/json

{
    "loueurEmail": "loueur1@test.com",
    "vehiculeId": "V001",
    "dateDebut": "2026-01-20",
    "dateFin": "2026-01-25",
    "lieuPrise": "Paris",
    "lieuDepose": "Lille",
    "assuranceNom": "AZA Classique",
    "parkingId": 1
}
```

### Récupérer tous les contrats

```
GET http://localhost:8080/api/rentals
```

### Récupérer un contrat par ID

```
GET http://localhost:8080/api/rentals/1
```

### Signer un contrat

```
POST http://localhost:8080/api/rentals/1/sign
```

### Accepter un contrat (agent)

Pour les agents ayant l'option d'acceptation manuelle.

```
POST http://localhost:8080/api/rentals/1/accept
```

### Terminer une location

Termine la location et déclenche l'entretien automatique si l'option est active.

```
PUT http://localhost:8080/api/rentals/1/termine
```

### Supprimer un contrat

```
DELETE http://localhost:8080/api/rentals/1
```

### Contrats d'un loueur

```
GET http://localhost:8080/api/rentals/loueur/loueur1@test.com
```

### Contrats en attente

Contrats en attente d'acceptation par l'agent.

```
GET http://localhost:8080/api/rentals/pending
```

### Kilométrage

#### Renseigner le kilométrage au départ

```
POST http://localhost:8080/api/rentals/1/kilometrage-debut
Content-Type: application/json

{
    "kilometrage": 45230,
    "photoNom": "photo_km_debut_contrat1_20260114.jpg"
}
```

#### Renseigner le kilométrage au retour

Note: Cette action termine automatiquement la location et déclenche les récompenses de parrainage.

```
POST http://localhost:8080/api/rentals/1/kilometrage-fin
Content-Type: application/json

{
    "kilometrage": 45580,
    "photoNom": "photo_km_fin_contrat1_20260120.jpg"
}
```

#### Consulter les informations kilométriques

```
GET http://localhost:8080/api/rentals/1/kilometrage
```

### Statut du contrat

Mettre à jour le statut d'un contrat.

```
PUT http://localhost:8080/api/rentals/1/status
Content-Type: application/json

{
    "statutLocation": "EN_COURS"
}
```

Valeurs possibles: `EN_ATTENTE_SIGNATURE`, `SIGNE`, `EN_COURS`, `TERMINEE`, `ANNULEE`

### Informations parking Vienci

Obtenir les informations d'accès au parking partenaire.

```
GET http://localhost:8080/api/rentals/1/parking-info
```

### Données utilitaires

#### Liste des loueurs disponibles

```
GET http://localhost:8080/api/rentals/loueurs
```

#### Liste des véhicules disponibles

```
GET http://localhost:8080/api/rentals/vehicules
```

#### Liste des assurances disponibles

```
GET http://localhost:8080/api/rentals/assurances
```

---

## Notations (NotationController)

### Noter un agent

```
POST http://localhost:8080/api/notations/agent
Content-Type: application/json

{
    "authorEmail": "loueur1@test.com",
    "targetEmail": "alice@test.com",
    "commentaire": "Très bon service, agent ponctuel et communicatif",
    "ponctualite": 5.0,
    "communication": 4.5
}
```

### Noter un loueur

```
POST http://localhost:8080/api/notations/loueur
Content-Type: application/json

{
    "authorEmail": "alice@test.com",
    "targetEmail": "loueur1@test.com",
    "commentaire": "Loueur respectueux du véhicule",
    "respect": 4.8
}
```

### Noter un véhicule

```
POST http://localhost:8080/api/notations/vehicle
Content-Type: application/json

{
    "authorEmail": "loueur1@test.com",
    "vehicleId": "V001",
    "commentaire": "Véhicule propre et confortable",
    "confort": 4.5,
    "proprete": 5.0
}
```

### Consulter les notes

#### Notes d'un utilisateur

```
GET http://localhost:8080/api/notations/user/alice@test.com
```

#### Note moyenne d'un utilisateur

```
GET http://localhost:8080/api/notations/user/alice@test.com/average
```

#### Notes d'un véhicule

```
GET http://localhost:8080/api/notations/vehicle/1
```

#### Note moyenne d'un véhicule

```
GET http://localhost:8080/api/notations/vehicle/V001/average
```

### Modifier une note

#### Modifier une note d'agent

```
PUT http://localhost:8080/api/notations/agent/1
Content-Type: application/json

{
    "commentaire": "Mise à jour du commentaire",
    "ponctualite": 5.0,
    "communication": 5.0
}
```

#### Modifier une note de loueur

```
PUT http://localhost:8080/api/notations/loueur/1
Content-Type: application/json

{
    "commentaire": "Mise à jour du commentaire",
    "respect": 5.0
}
```

#### Modifier une note de véhicule

```
PUT http://localhost:8080/api/notations/vehicle/1
Content-Type: application/json

{
    "commentaire": "Mise à jour du commentaire",
    "confort": 5.0,
    "proprete": 5.0
}
```

### Supprimer une note

```
DELETE http://localhost:8080/api/notations/1
```

### Répondre à une note

```
PUT http://localhost:8080/api/notations/1/reponse
Content-Type: application/json

{
    "responderEmail": "alice@test.com",
    "reponse": "Merci pour votre retour positif !"
}
```

---

## Messagerie (MessageController)

### Envoyer un message

```
POST http://localhost:8080/api/messages
Content-Type: application/json

{
    "senderEmail": "loueur1@test.com",
    "receiverEmail": "alice@test.com",
    "contenu": "Bonjour, je suis intéressé par votre véhicule."
}
```

### Voir une conversation

```
GET http://localhost:8080/api/messages/conversation/loueur1@test.com/alice@test.com
```

---

## Options Payantes (OptionPayanteController)

### Voir les options d'un agent

```
GET http://localhost:8080/api/options?agentEmail=alice@test.com
```

### Ajouter une option

Types d'options disponibles: `MANUEL`, `ASSURANCE`, `ENTRETIEN_PONCTUEL`, `ENTRETIEN_AUTO`, `PARKING`

#### Option Acceptation Manuelle

```
POST http://localhost:8080/api/options/add
Content-Type: application/json

{
    "agentEmail": "alice@test.com",
    "typeOption": "MANUEL"
}
```

#### Option Assurance Personnalisée

```
POST http://localhost:8080/api/options/add
Content-Type: application/json

{
    "agentEmail": "alice@test.com",
    "typeOption": "ASSURANCE"
}
```

#### Option Entretien Ponctuel

```
POST http://localhost:8080/api/options/add
Content-Type: application/json

{
    "agentEmail": "alice@test.com",
    "typeOption": "ENTRETIEN_PONCTUEL"
}
```

#### Option Entretien Automatique

```
POST http://localhost:8080/api/options/add
Content-Type: application/json

{
    "agentEmail": "alice@test.com",
    "typeOption": "ENTRETIEN_AUTO"
}
```

#### Option Parking

```
POST http://localhost:8080/api/options/add
Content-Type: application/json

{
    "agentEmail": "alice@test.com",
    "typeOption": "PARKING",
    "tarifMensuel": 15.0
}
```

### Supprimer les options

Supprimer toutes les options d'un agent.

```
DELETE http://localhost:8080/api/options/reset?agentEmail=alice@test.com
```

### Voir la facture

Voir la facture mensuelle détaillée.

```
GET http://localhost:8080/api/options/facture?agentEmail=alice@test.com
```

---

## Contrôle Technique (ControleTechniqueController)

### Enregistrer un CT

```
POST http://localhost:8080/api/controle-technique/V001
Content-Type: application/json

{
    "datePassage": "2025-12-15",
    "dateExpiration": "2027-12-15",
    "resultat": "Favorable",
    "observations": "Aucune anomalie détectée"
}
```

### Consulter un CT

```
GET http://localhost:8080/api/controle-technique/V001
```

### Supprimer un CT

```
DELETE http://localhost:8080/api/controle-technique/V001
```

---

## Maintenance (MaintenanceController)

### Entreprises d'entretien

#### Inscrire une entreprise d'entretien

```
POST http://localhost:8080/api/maintenance/companies/register
Content-Type: application/json

{
    "nom": "Speedy Auto",
    "prenom": "Contact",
    "password": "123456",
    "email": "speedy@auto.com",
    "tel": "0100000000"
}
```

#### Définir une entreprise comme référencée

```
PUT http://localhost:8080/api/maintenance/companies/speedy@auto.com/refere?refere=true
```

#### Ajouter un tarif

```
POST http://localhost:8080/api/maintenance/companies/speedy@auto.com/prices
Content-Type: application/json

{
    "typeVehicule": "VOITURE",
    "modele": "Clio",
    "prix": 89.0
}
```

#### Importer plusieurs tarifs

```
POST http://localhost:8080/api/maintenance/companies/speedy@auto.com/prices/import
Content-Type: application/json

[
    {"typeVehicule": "VOITURE", "modele": "Clio", "prix": 89.0},
    {"typeVehicule": "VOITURE", "modele": "208", "prix": 95.0},
    {"typeVehicule": "MOTO", "modele": "CB500", "prix": 65.0}
]
```

#### Liste des entreprises disponibles

```
GET http://localhost:8080/api/maintenance/companies/available
```

#### Détails d'une entreprise

```
GET http://localhost:8080/api/maintenance/companies/speedy@auto.com
```

### Demandes d'entretien

#### Créer une demande d'entretien

```
POST http://localhost:8080/api/maintenance/request
Content-Type: application/json

{
    "agentEmail": "alice@test.com",
    "vehicleId": "V001",
    "companyEmail": "speedy@auto.com",
    "date": "2026-02-01"
}
```

#### Définir l'entreprise préférée d'un agent

```
PUT http://localhost:8080/api/maintenance/agent/alice@test.com/preferred-company
Content-Type: application/json

{
    "companyEmail": "speedy@auto.com"
}
```

#### S'abonner à l'option entretien

```
POST http://localhost:8080/api/maintenance/agent/alice@test.com/subscribe-option
Content-Type: application/json

{
    "automatique": true
}
```

#### Mettre à jour le statut d'une intervention

```
PUT http://localhost:8080/api/maintenance/interventions/INT001/status
Content-Type: application/json

{
    "statut": "TERMINEE"
}
```

Valeurs possibles: `EN_ATTENTE`, `EN_COURS`, `TERMINEE`, `ANNULEE`

### Historique des entretiens

#### Historique d'un agent

```
GET http://localhost:8080/api/maintenance/agent/alice@test.com/history
```

#### Historique d'un véhicule

```
GET http://localhost:8080/api/maintenance/vehicle/V001/history
```

#### Test des rappels

```
GET http://localhost:8080/api/maintenance/rappels-test
```

#### Test US.A.10 (historique entretiens)

```
GET http://localhost:8080/api/maintenance/test-us-a10
```

#### Historique de démonstration

```
GET http://localhost:8080/api/maintenance/historique
```

---

## Gestion Agent (GestionAgentController)

### Alertes maintenance

Voir les alertes et recommandations de maintenance pour la flotte (données simulées).

```
GET http://localhost:8080/api/gestion-agent/maintenance/alertes
```

### Recommandations entretien

Obtenir les recommandations d'entretien basées sur le kilométrage pour un agent réel (US.A.11).

```
GET http://localhost:8080/api/gestion-agent/maintenance/recommandations/alice@test.com
```

### Simulations

#### Simuler un renseignement de CT

```
GET http://localhost:8080/api/gestion-agent/maintenance/info-ct
```

#### Simuler une facture mensuelle

```
GET http://localhost:8080/api/gestion-agent/finance/facture
```

#### Simuler une location avec retour en parking partenaire

```
GET http://localhost:8080/api/gestion-agent/location/retour-parking
```

---

## Prix (PrixController)

### Simuler un prix

Simuler le prix d'une location sans créer de contrat.
Teste les réductions longue durée (US.A.7).

```
POST http://localhost:8080/api/prix/simuler
Content-Type: application/json

{
    "vehiculeId": "V001",
    "dateDebut": "2026-01-15",
    "dateFin": "2026-01-20",
    "assuranceNom": "AZA Classique"
}
```

Réductions longue durée:
- 7-13 jours : -5% sur commission variable
- 14-29 jours : -10% sur commission variable
- 30+ jours : -15% sur commission variable

### Barèmes de réduction

```
GET http://localhost:8080/api/prix/baremes
```

---

## Parkings (ParkingController)

### Liste des parkings

```
GET http://localhost:8080/api/parkings
```

### Détails d'un parking

```
GET http://localhost:8080/api/parkings/1
```

---

## Assurances (AssuranceController)

### Liste des assurances

Récupérer les données d'assurances de démonstration.

```
GET http://localhost:8080/api/mes-assurances
```

### Créer une assurance

```
POST http://localhost:8080/api/mes-assurances
Content-Type: application/json

{
    "idA": 3,
    "nom": "Assurance Premium",
    "tarifBase": 25.0
}
```

---

## Comptes de test

### Agents Particuliers

| Email | Nom | Prénom | Mot de passe | Téléphone | Options |
|-------|-----|--------|--------------|-----------|---------|
| alice@test.com | Martin | Alice | 123456 | 0612345678 | Acceptation Manuelle |
| bob@test.com | Dupont | Bob | 123456 | 0698765432 | Option Parking Vienci |

### Agents Professionnels

| Email | Nom | Prénom | Entreprise | SIRET | Mot de passe | Options |
|-------|-----|--------|------------|-------|--------------|---------|
| enterprise1@test.com | Durand | Pierre | Durand SA | 12345678901234 | 123456 | Entretien Automatique |
| enterprise2@test.com | Moreau | Marie | Moreau SARL | 98765432109876 | 123456 | - |

### Loueurs

| Email | Nom | Prénom | Mot de passe | Ville | IBAN |
|-------|-----|--------|--------------|-------|------|
| loueur1@test.com | Bernard | Luc | 123456 | Paris | FR7630001007941234567890185 |
| loueur2@test.com | Petit | Sophie | 123456 | Marseille | FR7630004000031234567890143 |
| jean@email.com | Dupont | Jean | pass123 | Lyon | - |
| marie@email.com | Martin | Marie | pass456 | Nice | - |

---

## Véhicules de test

### Véhicules disponibles

| ID | Type | Marque | Modèle | Couleur | Ville | Prix/jour | Propriétaire | Kilométrage | CT |
|----|------|--------|--------|---------|-------|-----------|--------------|-------------|-----|
| 1 | VOITURE | Renault | Clio | Bleu | Paris | 45€ | alice@test.com | 15200 km | Valide |
| 2 | VOITURE | Peugeot | 308 | Noir | Lyon | 55€ | alice@test.com | 40500 km | Expire bientôt |
| 3 | VOITURE | BMW | Serie 3 | Blanc | Marseille | 85€ | bob@test.com | 102000 km | Valide |
| 4 | MOTO | Yamaha | MT-07 | Rouge | Nice | 60€ | bob@test.com | 8500 km | Non enregistré |
| 5 | VOITURE | Mercedes | Classe A | Gris | Paris | 90€ | enterprise1@test.com | 25000 km | Valide |
| 6 | CAMION | Renault | Master | Blanc | Paris | 120€ | enterprise1@test.com | 75000 km | Expiré |
| 7 | CAMION | Mercedes | Sprinter | Jaune | Lyon | 135€ | enterprise1@test.com | 30200 km | Valide |
| 8 | MOTO | Honda | CB650R | Noir | Bordeaux | 65€ | enterprise2@test.com | 12000 km | Contre-visite |
| V001 | VOITURE | Renault | Clio | Bleu | Paris | 30€ | jean@email.com | - | - |
| V002 | VOITURE | Peugeot | 3008 | Noir | Lyon | 50€ | marie@email.com | - | - |

---

## Contrats de test

| ID | Loueur | Véhicule | Lieu départ | Lieu retour | Assurance | Agent | Statut |
|----|--------|----------|-------------|-------------|-----------|-------|--------|
| 1 | loueur1@test.com | 1 (Clio) | Paris - Gare du Nord | Paris - Gare du Nord | Basic | alice@test.com | En cours |
| 2 | loueur2@test.com | 3 (Serie 3) | Marseille Centre | Marseille Centre | Tous Risques | bob@test.com | Terminé |
| 3 | loueur1@test.com | 6 (Master) | Paris - Entrepôt | Lyon - Centre | Premium | enterprise1@test.com | En cours |
| 4 | loueur2@test.com | 8 (CB650R) | Bordeaux - Centre | Bordeaux - Centre | Basic | enterprise2@test.com | En cours |
| 100 | loueur1@test.com | 1 (Clio) | Paris - Centre | Paris - Centre | Basic | alice@test.com | Historique |

---

## Parkings partenaires Vienci

| ID | Nom | Adresse | Ville | Capacité | Tarif agent/jour | Code accès |
|----|-----|---------|-------|----------|------------------|------------|
| 1 | Parking Gare de Lyon | 18 Boulevard Diderot | Paris | 350 | 8€ | LYON2025# |
| 2 | Parking Aéroport CDG T2 | Terminal 2E | Roissy-en-France | 500 | 12€ | CDG2026* |
| 3 | Parking Centre Commercial Lyon Part-Dieu | 17 Rue du Docteur Bouchut | Lyon | 400 | 6.50€ | PARTDIEU# |

---

## Assurances disponibles

| ID | Nom | Tarif de base |
|----|-----|---------------|
| 1 | Assurance Basic | 15€ |
| 2 | Assurance Tous Risques | 35€ |
| 3 | Assurance Premium | 50€ |

---

## Notes d'utilisation

### Format des dates
Toutes les dates doivent être au format `yyyy-MM-dd` (ex: `2026-01-15`).

### Authentification
L'API ne nécessite pas d'authentification pour les tests. En production, un système de token serait nécessaire.

### Erreurs courantes

| Code | Description |
|------|-------------|
| 200 | Succès |
| 400 | Requête invalide (paramètres manquants ou incorrects) |
| 404 | Ressource non trouvée |
| 500 | Erreur serveur |

### Outils recommandés pour les tests

- **Hoppscotch** : https://hoppscotch.io/
- **Postman** : https://www.postman.com/
- **cURL** : Ligne de commande

### Exemple avec cURL

```bash
# Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@test.com","password":"123456"}'

# Récupérer tous les véhicules
curl http://localhost:8080/api/vehicules/all
```
