	DSW 	compteur1
	DSW	compteur2
	JMP 	init
	JMP 	int
init:	LDSPi	@stack@
	LDAi	0
	STA	compteur1
	STA	compteur2
	STI
loop:	LDAd	compteur1
	LDBi	1
	ADDA
	STA	compteur1
	STA	1000
	JMP	loop
int:	LDAd	compteur2	
	LDBi	2
	ADDA
	STA	compteur2
	STA	1001
	RTI
