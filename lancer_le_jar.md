# README – Projet JVM

## Informations générales

Ce projet est fourni sous forme de fichier exécutable `.jar`.
Il a été développé et testé avec **Java 21**.

## Prérequis

1. **Java 21** doit être installé sur votre machine.
2. **JavaFX 17.0.18 [LTS]** doit être téléchargé et installé.

### Téléchargement de JavaFX

Rendez-vous sur le site officiel : [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)

- Faites défiler jusqu’en bas de la page et dans la section **Download**.
- Sélectionnez la **version 17.0.18 [LTS]** pour la "JavaFX version".
- Téléchargez la version correspondant à votre système d’exploitation avec le **type SDK**.
- Décompressez l’archive téléchargée dans un dossier de votre choix.

## Exécution du projet

Pour lancer l’application, utilisez la commande suivante dans un terminal ou une invite de commande, en remplaçant `<chemin_vers_javafx>` par le chemin où vous avez décompressé JavaFX et `<chemin_vers_jar>` par le chemin vers le fichier `.jar` du projet :

```bash
java --module-path <chemin_vers_javafx>/lib --add-modules javafx.controls,javafx.fxml -jar <chemin_vers_jar>/projet_jvm-1.0-SNAPSHOT-shaded.jar
```

### Exemple générique

```bash
java --module-path C:/chemin/vers/javafx-sdk-17.0.18/lib --add-modules javafx.controls,javafx.fxml -jar C:/chemin/vers/projet/projet_jvm-1.0-SNAPSHOT-shaded.jar
```

⚠️ Veillez à bien utiliser les chemins correspondant à votre installation.

