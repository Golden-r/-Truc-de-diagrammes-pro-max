#!/bin/bash

# Arrêter le script en cas d’erreur
set -e

# Forcer l'encodage UTF-8
export LANG=C.UTF-8
export LC_ALL=C.UTF-8

# Créer le dossier class s'il n'existe pas
mkdir -p class

# Exécuter le script de compilation
./compileList.sh

# Compiler les fichiers Java
javac -encoding UTF-8 -cp "lib/*" -d class @compile.list

# Lancer l'application Java
java -Dfile.encoding=UTF-8 -cp "class:lib/*" src.ControleurCUI "$@"
