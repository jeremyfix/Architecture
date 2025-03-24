	JMP	init
	JMP	clavier
init:	LDSPi	100		
	STI
	LDAi	0
	STA	101
loop:	LDAd	101		
	LDBi	1
	ADDA
	STA	101
	STA	1000
	JMP 	loop
clavier:LDAd	1003		
	STA	102
	STA	1004
	RTI

	
