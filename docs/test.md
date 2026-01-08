# Test des API

## Table des matières

- [login and register](#login-and-register)
  - [Register](#register)
  - [Login](#login)
- [Affiche les trucs par Utilisateur](#affiche-les-trucs-par-utilisateur)
  - [Affiche Vehicles par Loueur](#affiche-vehicles-par-loueur)
  - [Affiche Contracts par Loueur](#affiche-contracts-par-loueur)
- [Comptes de test](#comptes-de-test)

---

## login and register

### Register
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

### Login
```
POST http://localhost:8080/api/users/login
Content-Type: application/json

{
    "email": "alice@test.com",
    "password": "123456"
}
```

---

## Affiche les trucs par Utilisateur

### Affiche Vehicles par Loueur
```
GET http://localhost:8080/api/users/alice@test.com/vehicles
```

### Affiche Contracts par Loueur
```
GET http://localhost:8080/api/users/loueur1@test.com/contracts
```

---

## Comptes de test

| Email | Mot de passe | Rôle |
|-------|--------------|------|
| alice@test.com | 123456 | AGENT_PARTICULIER |
| bob@test.com | 123456 | AGENT_PARTICULIER |
| loueur1@test.com | 123456 | LOUEUR |
| loueur2@test.com | 123456 | LOUEUR |
