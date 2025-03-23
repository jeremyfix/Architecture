# Séquenceur manuel (TP 1)

## Introduction

 Le but de ce premier TP est d'introduire un chemin de données qu'on va utiliser tout au long du cours et des TPs. On introduit progressivement des éléments de logique combinatoire et de logique séquentielle avec le but utiltime de cette séance d'être capable de piloter à la main le chemin de données pour lui faire réaliser quelques opérations. Nous verrons durant les séances suivantes comment automatiser le pilotage du chemin de données ainsi que quelques extensions de ce chemin de données. On va voir notamment:

- l'unité arithmétique et logique
- les registres
- la mémoire RAM (Random Access Memory)
- le séquencement du chemin de données

Logisim est installé sur les postes de travail. Lisez la [page principale](index.md) pour savoir comment le démarrer plus 2/3 autres indications.

## Charger des registres et effectuer des opérations arithmétiques

### Lancement de logisim et chargement de l'architecture

On considère dans un premier temps l'architecture ci-dessous. Téléchargez les fichiers [csmetz.jar](assets/csmetz.jar) et [archi_seq_man_regual.circ](assets/circuits/archi_seq_man_regual.circ) et placez les dans le même répertoire. Lancez logisim et chargez le fichier archi_seq_man_regual.circ. L'architecture chargée est représentée ci-dessous.

!!! danger

	- Add the archi_seq_man_regual.circ circuit

Avant de jouer avec cette architecture, je vous propose de décrire rapidement quelques éléments de l'interface logisim. 

!!! danger

	- Add the illustration of the architecture

Vos outils principaux dans ce TP sont le "poke tool" et l'horloge. Le poke tool, la petite main en haut à gauche de l'interface, permet de modifier la valeur des entrées, cliquer sur les boutons, .... L'horloge est activée en appuyant sur les touches **Ctrl + t**, le petit symbole de l'horloge indiquant si elle est à un niveau haut ou bas. Appuyer sur Ctrl + t déclenche une transition de niveau bas vers niveau haut ou niveau haut vers niveau bas, il faut donc presser deux fois ces touches pour produire un cycle d'horloge.

### Présentation de l'architecture

 L'architecture proposée est constituée :

- de signaux de contrôle : des entrées modifiables avec le `poke tool`, et un bouton `Clear` pour réinitialiser le contenu des registres,
- d'une entrée pour les données labelisée Data, tout à gauche. Son contenu est modifié avec le `poke tool`,
- de deux registres A et B actifs sur front montant d'horloge,
- d'une Unité Arithmétique et Logique,
- d'une sortie avec quelques afficheurs 7 ségments et un registre mémorisant la valeur à afficher (le convertisseur ToBCD est un circuit de logique combinatoire convertissant un nombre binaire en binaire codé décimal pour l'affichage).

La circulation des données sur les bus A, B et S est autorisée par des buffers contrôlés par un signal de contrôle particulier. 

!!! example

	Pour charger le registre A, il faut :

	- placer des données sur le bus S, 
	- autoriser le chargement du registre (SetA), 
	- déclencher un front montant d'horloge.

En plus des signaux Zero (Z), Retenue (Carry) et de débordement (V), l'UAL fournit 12 opérations décrites dans la table ci-dessous: 

| U3  | U2  | U1  | U0  | Opération |
| --- | --- | --- | --- | --------- |
| 0 | 0 | 0 | 0 | S = A |
| 0 | 0 | 0 | 1 | S = B |
| 0 | 0 | 1 | 0 | S = A ET B |
| 0 | 0 | 1 | 1 | S = A OU B |
| 0 | 1 | 0 | 0 | S = non(A) |
| 0 | 1 | 0 | 1 | S = non(B) |
| 0 | 1 | 1 | 0 | S = A + B |
| 0 | 1 | 1 | 1 | S = A - B |
| 1 | 0 | 0 | 0 | S = A + 1 |
| 1 | 0 | 0 | 1 | S = A - 1 |
| 1 | 0 | 1 | 0 | S = A * B |
| 1 | 0 | 1 | 1 | S = A >> 1 |
| :fontawesome-solid-ban: | :fontawesome-solid-ban: | :fontawesome-solid-ban: | :fontawesome-solid-ban: | S = Erreur |

!!! Question

	**Travail à réaliser**

	En utilisant les signaux de contrôle et l'entrée Data **uniquement** (!), réalisez les opérations suivantes les unes après les autres. Je vous conseille de remettre à zéro tous les signaux de contrôle avant de passer à l'instruction suivante.

	- charger la valeur décimale 16 dans le registre A
	- charger la valeur décimale 12 dans le registre B
	- afficher le contenu du registre A sur les afficheurs 7 ségments
	- afficher le contenu du registre B sur les afficheurs 7 ségments
	- sommer le contenu des registres A et B et stocker le résultat dans le registre A
	- stocker dans A le résultat de la division entière par deux (une opération de l'UAL permet de le faire, si si) du contenu du registre A et l'afficher simultanément sur les afficheurs 7 ségments


## Connecter le chemin de données avec une mémoire RAM

On introduit maintenant une mémoire dans le chemin de données depuis laquelle on va charger des données et dans laquelle on va également écrire des données. Cette nouvelle architecture est [archi_seq_man.circ](assets/circuits/archi_seq_man.circ). Le chemin de données est modifié en ajoutant une RAM et deux registres : 

- PC (Program counter) et,
- RADM (Registre d'ADresse Mémoire).

Quelques afficheurs 7-segments sont également disponibles.

!!! danger

	- Add the archi_seq_man.circ


!!! danger

	- Add the illustration of archi_seq_man.circ


La RAM ainsi que les afficheurs sont adressables. Cela veut dire qu'on peut lire/écrire dans la RAM ainsi qu'écrire dans les afficheurs. Pour une lecture, on réaliser les étapes suivantes :

1. placer l'adresse du mot à lire dans le registre RADM, 
2. déclencher la lecture avec le signal ReadMem. 

Pour une écriture, on procédera par :

1. placer l'adresse du mot à écrire dans le registre RADM, 
2. placer les données à écrire sur le bus S et
3. déclencher l'écriture avec le signal SetMem.

Les afficheurs sont dits *mappés en mémoire* (on verra un plus en détails ce que cela signifie plus tard), c'est à dire qu'ils ont des adresses particulières:

- Si le contenu de RADM est inférieur strict à 0x1000, on adresse la RAM,
- Si le contenu de RADM vaut 0x1000, 0x1001 ou 0x1002 on adresse respectivement le premier, deuxième et troisième afficheur.


!!! example

	Ne vous laissez pas dérouter par le plat de spaghettis de connexions autour de la mémoire et des afficheurs.

	En pratique, si je veux afficher le contenu du registre A sur le premier afficheur, je procéderais ainsi :

	1. mettre 0x1000 dans RADM pour adresser le premier afficheur
	2. Lire A (ReadA), rediriger A vers S (UAL=0000), et écrire en mémoire (SetMem)

	Si je veux charger, dans le registre A, le contenu de la RAM à l'adresse 0x0010, je procéderais ainsi :

	1. mettre 0x0010 dans RADM
	2. Lire la mémoire (ReadMem), rediriger B vers S (UAL=0001), et écrire dans le registre A (SetA)

	Si je veux écrire le contenu du registre A dans la RAM à l'adresse 0x0010, je procéderais ainsi :

	1. mettre 0x0010 dans RADM
	2. Lire A (ReadA), rediriger A vers S (UAL=0000), et écrire en mémoire (SetMem)

Dans le travail que je propose de réaliser, nous allons charger des opérandes en mémoire (plus tard, on ajoutera également les instructions) à partir d'un fichier (bouton droit sur la RAM, "charger l'image" et choisir par example [seq_man.mem](assets/programs/seq_man.mem) ou bien en éditant directement la RAM (bouton droit sur la RAM, "éditer le contenu"). 

!!! question
 
	Chargez l'image en mémoire [seq_man.mem](assets/programs/seq_man.mem) et regardez ensuite le contenu de la RAM. Il doit être :

	0010 1000 000c 1001 1002

	Ces valeurs correspondent aux opérandes dont vous allez avoir besoin par la
	suite.

Les opérandes vont être considérées les unes après les autres (dans la question
suivante, vous comprendrez pourquoi nous avons mis ces valeurs particulières).
Pour savoir quelle est l'adresse de l'opérande en mémoire actuellement
considérée, nous utilisons le regisgtre **Program Counter** (PC). Pour récupérer une opérande dont l'adresse est dans le PC, il suffit de transférer le PC dans le registre RADM et de lire la mémoire. Chaque fois qu'une opérande est utilisée, on **devra incrémenter le program counter**. 


!!! question

	**Travail à réaliser**

	En utilisant uniquement les signaux de contrôle (donc on ne modifie pas les registres "à la main"), et après avoir chargé l'image seq_man.mem dans la RAM, effectuez les opérations ci-dessous. Je vous conseille de prendre des notes des signaux de contrôle que vous utilisez pour chaque étape. Notez que pour chacune des opérations ci-dessous, les opérandes sont placées dans la RAM.

    1. charger la valeur décimale 16 dans le registre A
    2. afficher le contenu du registre A sur le premier afficheur
    3. charger la valeur décimale 12 dans le registre B
    4. afficher le contenu du registre B sur le second afficheur
    5. soustraire le contenu du registre B au contenu du registre A et stocker le résultat dans le registre A
    6. afficher le contenu du registre A sur le troisème afficheur

On se propose désormai de travailler sur un deuxième exemple. Je vous propose de
procède comme précédemment, en définissant votre contenu de RAM et en
orchestrant manuellement le chemin de données via les signaux de contrôle. 

!!! question

	**Travail à réaliser**

	Modifiez le contenu de la mémoire et exécutez le programme avec les signaux de contrôle permettant de faire les divisions par deux successives de 128 jusqu'à un résultat nul. Affichez le résultat de chaque étape sur l'afficheur 7 segments. 

	Quel signal me permet de détecter qu'il faut s'arrêter ? 

Dans ce dernier exemple, on commence à voir apparaître une boucle. Nous
introduirons prochainement des instructions, et en particulier des instructions
de saut conditionnel (`JZA` et `JZB`) qui utilisent les signaux émis par l'UAL pour dérouter le fil d'exécution séquentiel d'un programme.
