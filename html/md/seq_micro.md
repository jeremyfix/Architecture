# Séquence micro-programmé (TP 2)

## Introduction

Le but de ce TL est de réaliser un séquenceur micro-programmé pour piloter notre architecture représentée ci-dessous. Nous allons voir qu'un nouveau composant est introduit, une ROM, et votre travail consiste alors à écrire le micro-code de quelques instructions dans cette ROM. Plusieurs programmes vous sont fournis pour tester votre réalisation et vous aurez également à écrire vos propres programmes en code machine. Lancez logisim et chargez l'architecture [archi_sequenceur.circ]() ainsi que le fichier [csmetz.jar](assets/csmetz.jar) à placer dans le même répertoire que [archi_sequenceur.circ](). Lisez ensuite la présentation ci-dessous avec l'architecture sous les yeux.

!!! danger

	Mettre le fichier archi_sequenceur.circ

!!! danger

	Mettre l'illustration de l'archi

La mémoire RAM contient des mots de 16 bits et est adressable sur 16 bits et a donc une capacité de 128 Ko. Les opérandes et les adresses sont codées sur 16 bits. Les instructions sont codées sur un ou deux mots de 16 bits : le premier mot contient le code de l'opération et l'éventuel mot suivant contient l'opérande. Le code de l'opération est codé dans les 8 bits de poids fort d'un mot. Ce qui se résumé sur le tableau ci-dessous donnant la sémantique des bits d'un ou deux mots de 16 bits.

<table class="codeop">
	  <thead>
	    <tr><th>15</th><th>14</th><th>13</th><th>12</th><th>11</th><th>10</th><th> 9</th><th> 8</th><th> 7</th><th> 6</th><th> 5</th><th> 4</th><th> 3</th><th> 2</th><th> 1</th><th> 0</th></tr>
	  </thead>
	  <tr><td colspan="8">Code de l'opération</td><td colspan="8"> -- bits inutilisés -- </td></tr>
</table>
<table class="codeop">
	  <thead>
	    <tr><th>15</th><th>14</th><th>13</th><th>12</th><th>11</th><th>10</th><th> 9</th><th> 8</th><th> 7</th><th> 6</th><th> 5</th><th> 4</th><th> 3</th><th> 2</th><th> 1</th><th> 0</th></tr>
	  </thead>
	  <tr><td colspan=16>Opérande (optionelle)</td></tr>
</table>

Les instructions à disposition sont listées dans la table ci-dessous :

| Code opération (8 bits) | Nom de l'opération | Nombre de mots | Description |
|--|--|--|--|
| 0x0c | END | 1 | Fin de programme |
| 0x10 | LDAi | 2 | Charge la valeur de l'opérande dans le registre A `[A := operande]` |

## Présentation du chemin de données

### Mémoire RAM

## Adressage des afficheurs 7 segments

## Unité Arithmétique et Logique

## Registres d'opérandes et Program Counter

## Séquence microprogrammé

## Travail à réaliser 

!!! Tip

	Vous avez la possibilité de modifier le contenu des RAM et ROM depuis logisim. Néanmoins, je vous conseille de procéder différemment. Vous avez en effet la possibilité de charger la mémoire à partir d'un fichier. Je vous propose donc de télécharger le fichier microcode_seq.rom, de le modifier avec un éditeur de texte (gedit ou emacs par example) et de le charger ensuite en ROM. Le texte commençant par "#" sont des commentaires pour vous aider à vous repérer dans la ROM. 

Pour faciliter votre travail

<div id="genmicrocode" data-id="0" ></div><br/><br/>
