# Backend – Projet POO (MIAGE)

Ce dépôt contient le **backend Spring Boot** du projet POO (MIAGE).  
Ce README explique **pas à pas** comment initialiser et lancer le projet, même pour un débutant.

---

## 1. Prérequis (le plus simple possible)

### Option recommandée (la plus simple)

👉 **Installer IntelliJ IDEA** (Community Edition suffit)

- IntelliJ IDEA permet de :
  - créer le projet Spring Boot
  - **installer automatiquement le JDK**
  - gérer Maven
  - lancer le projet sans configuration manuelle

🔹 Autrement dit : **si IntelliJ IDEA est installé, presque tout peut se faire dedans**.

---

## 2. Installation avec IntelliJ IDEA (recommandé)

### 2.1 Installer IntelliJ IDEA

Télécharger IntelliJ IDEA (Community) :
https://www.jetbrains.com/idea/download/

---

### 2.2 Ouvrir le projet

```text
File → Open → sélectionner le dossier du projet
```

IntelliJ détecte automatiquement :

- le projet Maven
- le fichier `pom.xml`

---

### 2.3 Installer Java 17 directement depuis IntelliJ (important)

Si Java n’est pas encore installé, IntelliJ proposera automatiquement :

> **Download JDK**

Choisir :

- Version : **17**
- Distribution : **Eclipse Temurin (AdoptOpenJDK)**

👉 **Aucune installation manuelle n’est nécessaire**, IntelliJ s’en occupe.

---

### 2.4 Synchroniser Maven

Clic droit sur `pom.xml` →

```text
Maven → Sync Project
```

Cela télécharge automatiquement toutes les dépendances Spring Boot.

---

### 2.5 Lancer le projet

Ouvrir la classe :

```text
Groupe2projetPooApplication.java
```

Cliquer sur ▶️ **Run**.

Si tout est correct, la console affiche :

```text
Tomcat started on port 8080
Started Groupe2projetPooApplication
```

Le backend est alors accessible sur :

```text
http://localhost:8080
```

---

## 3. Lancer le projet sans IntelliJ IDEA (optionnel)

Il est possible de ne pas utiliser IntelliJ IDEA, mais cela demande plus de configuration.

### Prérequis obligatoires

- Java 17 installé manuellement
- Maven fonctionnel dans le terminal

### Commande

```bash
./mvnw spring-boot:run
```

Sous Windows :

```bash
mvnw.cmd spring-boot:run
```

⚠️ Si vous n’utilisez pas IntelliJ IDEA :

- assurez-vous que le projet démarre correctement
- évitez de modifier `pom.xml` sans prévenir l’équipe

---

## 4. Organisation du projet

```text
src/
 └─ main/
    ├─ java/
    │   └─ fr.miage.groupe2projetpoo
    │       ├─ Groupe2projetPooApplication.java
    │       └─ (controllers, services, models)
    └─ resources/
```

---

## 5. Convention de commits (obligatoire)

Pour garder un historique clair, chaque commit doit commencer par un type.

### Format

```text
[NOM] type: message clair
```

👉 **Chaque commit doit commencer par le nom du membre de l'équipe entre crochets**, suivi du type et du message.

### Types autorisés

- `feat:` nouvelle fonctionnalité  
- `fix:` correction de bug  
- `test:` ajout ou modification de tests  
- `docs:` documentation  
- `refactor:` amélioration du code sans changement fonctionnel  

### Exemples

```bash
[Alice] feat: add hello endpoint
[Bob] fix: correct server port
[Charlie] docs: add README setup instructions
[David] refactor: simplify user validation logic
```

### À éviter absolument

- `first commit`
- `test`
- `ok`
- `update`

---

## 6. Organisation du travail en équipe (Git & Branches)

### Règle générale (très importante)

👉 **Personne ne travaille directement sur la branche `master` (ou `main`)**.  
La branche `master` doit toujours rester **stable, exécutable et présentable**.

Chaque membre de l’équipe travaille sur **sa propre branche**.

---

### Convention de nommage des branches

Merci d’utiliser la convention suivante :

- `feature/nom-fonctionnalite` → nouvelle fonctionnalité
- `fix/nom-bug` → correction de bug
- `test/nom-test` → ajout ou modification de tests
- `docs/nom-doc` → documentation uniquement

**Exemples :**

- `feature/user-controller`
- `feature/authentication`
- `fix/startup-error`
- `docs/readme-update`

---

### Workflow recommandé (pas à pas)

1. Se placer sur `master` et récupérer la dernière version :

   ```bash
   git checkout master
   git pull origin master
   ```

2. Créer une nouvelle branche pour votre travail :

   ```bash
   git checkout -b feature/ma-fonctionnalite
   ```

3. Développer, puis commit :

   ```bash
   git add .
   git commit -m "feat: description claire de la fonctionnalité"
   ```

4. Pousser la branche sur GitHub :

   ```bash
   git push origin feature/ma-fonctionnalite
   ```

5. Fusion dans `master` (par le responsable du projet ou via Pull Request).

---

### Convention de messages de commit

Les messages de commit doivent **obligatoirement** commencer par le **nom du membre entre crochets**, suivi d'un mot-clé :

```text
[NOM] type: message clair
```

- `feat:` → nouvelle fonctionnalité
- `fix:` → correction de bug
- `test:` → ajout ou modification de tests
- `docs:` → documentation
- `refactor:` → amélioration du code sans changement fonctionnel

**Exemples :**

```text
[Alice] feat: add user REST controller
[Bob] fix: resolve port configuration issue
[Charlie] test: add unit tests for service layer
[David] docs: update README with setup instructions
```

👉 Cette convention est importante pour la lisibilité et sera appréciée lors de l’évaluation du projet.

## 7. Règles de travail en équipe

- Toujours **pull** avant de travailler
- Ne pas casser la branche principale
- En cas de doute → demander avant de modifier la configuration

---

## 8. État du projet

- ✔ Projet Spring Boot initialisé
- ✔ Java 17 validé
- ✔ Maven synchronisé
- ✔ Prêt pour le développement métier

---

Bon travail à tous 🚀
