# Accueil

## Structure du cours

Le cours est structuré de la manière suivante :

- 3 curs magistraux (3 x 1h30)
- 2 TP (4h30) 
- 2 cours magistraux (2 x 1h30) 
- 1 TP (4h30)
- 2 cours magistraux (2 x 1h30) 
- 2 TP (4h30) 

## Sujets de BE/TP

Tout au long de ces TP, vous allez construire votre propre ordinateur. Cet ordinateur sera simulé à l'aide du logiciel [logisim](http://www.cburch.com/logisim/). Logisim est un simulateur de circuits logiques initialement développé par C. Burch, qui a interrompu son développement et nous utiliserons le fork [logisim-evolution](https://github.com/logisim-evolution/logisim-evolution).

Ce simulatateur dispose de tout les éléments nécessaires à la compréhension du fonctionnement d'un microprocesseur ou microcontrôleur. En construisant petit à petit une architecture de plus en plus complexe, on abordera les questions suivantes:

- les composants constituants un microprocesseur : mémoire, registres, unité arithmétique et logique et leur coordination : <a href="seq_man.html">Séquenceur manuel (BE)</a>
- le séquencement automatique du chemin de données : <a href="seq.html">Séquenceur microprogrammé (TL)</a>
- la pile et l'appel de routines: <a href="routines.html">Pile et appel de routines (BE)</a>
- la gestion des entrées par interruption :<a href="irq.html">Interruptions (BE)</a>
- la programmation assembleur et l'ordonnancement dans un système multiprocessus : <a href="ordonnanceur.html">Programmation assembleur et ordonnanceur (TL)</a>

Ci-dessous, une petite vidéo illutre en fonctionnement l'architecture que nous allons construire et étudier.
<video controls autoplay loop width="100%">
<source src="/videos/archi-all.webm" type="video/webm" />

Download the <a href="/videos/archi-all.webm">WEBM</a>
video.
</video>

Logisim **est installé** sur les postes de travail. Si vous souhaitez l'utiliser **chez vous**, il vous faut récupérer l'installeur mis à disposition dans les releases de logisim evolution : [https://github.com/logisim-evolution/logisim-evolution/releases]()

> Notez au passage qu'il existe plusieurs versions de logisim sur Internet. La version initiale développé par C. Burch, dont il a arrêté le
> développement en 2014. Il y a eu le fork de jlawrence qui a fixé quelques bugs résiduels mais qui ne reçoit plus de mise à jour	depuis 2016. Et il y a logisim-evolution qui continue à être mis à jour.

## Démarrer la version JAR de logisim sous Ubuntu

 Si vous utilisez la version jar de logisim, pour la démarrer, il suffit de taper la commande suivante dans un terminal :

- Se connecter sur une machine ubuntu
- Lancer un terminal en appuyant **Ctrl + Alt + T**
- Saisir, dans le terminal, la commande : **java -jar /opt/logisim-evolution-3.9.0-all.jar**
- le simulateur devrait se lancer

Pour vous simplifier l'exécution ultérieure de logisim, lorsque logisim est lancé pour la première, une icone apparaît dans la liste des applications à gauche de l'écran. Cliquez avec le bouton droit sur cette icône et sélectionnez "L'attacher au lanceur". Les prochaines fois, vous n'aurez plus besoin de démarrer de terminal pour lancer logisim, il vous suffira de cliquer sur l'icône.

Pour démarrer un éditeur de texte, il faut:

- Lancer un terminal (**Ctrl + Alt + T**) ou ouvrir un nouvel onglet dans un terminal déjà ouvert : sélectionner le terminal et presser **Ctrl + Shift + T**
- Saisir, dans le terminal, la commande : **gedit**
- l'éditeur de texte devrait se lancer


