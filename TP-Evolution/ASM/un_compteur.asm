	JMP	init
	JMP	bouton
	DSW	compteur
init:	LDSPi	@stack@
	LDAi	0
	STA 	compteur
	LDAd	compteur
	LDBi	1
	STI
loop:	ADDA
	STA	compteur
	STA	1000
	JMP	loop
bouton:	RTI
