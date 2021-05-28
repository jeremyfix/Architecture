	JMP	init		; Vecteur d'interrution de reset
	JMP	sched		; Vecteur d'interrupteur du scheduler
	DSW	sp0
	DSW 	sp1
	DSW	idx
init:	LDAi	400		; On initialise les variables du scheduler
	STA	sp0		; avec le pointeur de pile 0
	LDAi	700	        ; 
	STA	sp1		; avec le pointeur de pile 1
	LDAi	0		; on définit l'identifiant du programme en cours d'exécution, disons le premier
	STA	idx		
	LDSPd 	sp0		; Initialisation de la pile 0 en empilant un contexte à dépiler
	LDAi	0		; A	
	PUSHA
	LDAi	0		; B
	PUSHA
	LDAi	main0		; et PC = main0
	PUSHA
	STSP	sp0		; on mémorise la valeur courante de SP sur la pile 0
	LDSPd   sp1		; Initialisation de la pile 1 en empilant un contexte à dépiler
	LDAi	0		; A
	PUSHA
	LDAi	0		; B
	PUSHA
	LDAi	main1		; et PC = main1
	PUSHA
	STSP	sp1		; on mémorise la valeur courante de SP sur la pile 1
	LDSPd	sp0		; on charge le pointeur de pile sp0, pour qu'en retour d'interruption, on lance main0
	RTI			; on sort de l'interruption
	
main0:	LDAi	0
	PUSHA
loop0:	POPA
	LDBi	1
	ADDA
	PUSHA
	STA	1000
	JMP	loop0

	
main1:	LDAi	0
	PUSHA
loop1:	POPA
	LDBi	2
	ADDA
	PUSHA
	STA	1001
	JMP	loop1

	
sched:	LDAd	idx		; identifiant du programme en cours
				; est ce que le premier ou deuxième programme ?
	JZA	trig1		; si id == 0, on charge le contexte du programme 1
; Chargement du contexte du programme 0
trig0:	STSP	sp1		; Sauvegarde du SP du programme 1
	LDAi	0		; Changement d'identifiant de programme exécuté
	STA 	idx
	LDSPd	sp0		; Chargement du SP du programme 0
	RTI
; Chargement du contexte du programme1
trig1:	STSP	sp0		; Sauvegarde du SP du programme 0
	LDAi	1		; Changement d'identifiant de programme exécuté
	STA 	idx
	LDSPd	sp1		; Chargement du SP du programme 1
	RTI
