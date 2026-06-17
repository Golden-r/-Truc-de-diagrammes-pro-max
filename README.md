# SAE 3.01 - Projet Équipe 1

## 👥 Membres de l'équipe

- Mateo CHEVEAU
- Ethan DAMESTOY
- Joshua HERMILLY
- Lucas LAFOSSE
- Jonathan LECLERC

---

## ⚙️ Configuration du projet

### Prérequis
- **Java JDK** (version recommandée : 8 ou supérieure)
- **Encodage UTF-8** : Assurez-vous que votre environnement est configuré en conséquence

### Dépendances
Ce projet utilise la bibliothèque **JDOM 2.0.6** (`jdom-2.0.6.jar`) présente dans le dossier `lib/` pour l'export et la sauvegarde en XML.

---

## 🚀 Lancement de l'application

### Windows
Double-cliquez sur le script de votre choix ou lancez-le via l'invite de commande :
```batch
# Interface Graphique
.\runGUI.bat

# Interface Console
.\runCUI.bat
```

### Linux / macOS
Ouvrez un terminal et exécutez le script :
```bash
# Interface Graphique
./runGUI.sh

# Interface Console
./runCUI.sh
```

> **Note** : Pensez à rendre les scripts exécutables si nécessaire :
> ```bash
> chmod +x runGUI.sh runCUI.sh
> ```

---

## 🛠️ Compilation et exécution manuelle

Si vous ne souhaitez pas utiliser les scripts fournis :

### 1. Compilation
```bash
javac -encoding UTF-8 -d class @compile.list
```

### 2. Exécution

**Interface Graphique (GUI) :**
```bash
java -Dfile.encoding=UTF-8 -cp class src.ControleurGUI
```

**Interface Console (CUI) :**
```bash
java -Dfile.encoding=UTF-8 -cp class src.ControleurCUI
```

---

## 📖 Manuel d'utilisation (Interface Console)

L'application permet de générer un affichage UML à partir de code source Java.

### Au lancement
Choisissez le mode d'entrée :
- **1** → Fichier unique (chemin absolu ou relatif)
- **2** → Répertoire complet (chemin absolu ou relatif)

> ⚠️ **Important** : L'ouverture d'un nouveau répertoire efface les classes précédemment chargées.

---

## 📚 Génération de la Javadoc

Pour générer la documentation dans le dossier `javadoc/` :
```bash
javadoc -d javadoc -sourcepath . -classpath "lib/jdom-2.0.6.jar" -subpackages src
```

---

## 📂 Structure du projet
```
.
├── lib/
│   └── jdom-2.0.6.jar
├── src/
│   ├── ControleurGUI.java
│   ├── ControleurCUI.java
│   └── ...
├── class/              # Classes compilées
├── javadoc/            # Documentation générée
├── compile.list        # Liste des fichiers à compiler
├── runGUI.bat          # Script Windows (GUI)
├── runCUI.bat          # Script Windows (CUI)
├── runGUI.sh           # Script Linux/macOS (GUI)
└── runCUI.sh           # Script Linux/macOS (CUI)
```

---

## 📝 Licence

Ce projet est réalisé dans le cadre de la SAE 3.01.
