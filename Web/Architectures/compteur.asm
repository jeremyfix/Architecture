	JMP	init
	JMP	bouton
	DSW	compteur
	DSW	compteurbouton
init:	LDSPi	@stack@
	LDAi	0
	STA 	compteur
	LDAi	0
	STA 	compteurbouton
	LDAd	compteur
	LDBi	1
	STI
loop:	ADDA
	STA	compteur
	STA	1000
	JMP	loop
bouton:	LDAd	compteurbouton
	LDBi	2
	ADDA
	STA	compteurbouton
	STA	1002
	RTI
