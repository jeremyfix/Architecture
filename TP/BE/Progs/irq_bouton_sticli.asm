	JMP 	init
	JMP 	int
init:	LDSPi	100
	STI
	LDAi	0
	STA	101
loop:	CLI
	LDAd	101
	LDBi	1
	ADDA
	STA	101
	STI
	STA	1000
	JMP	loop
int:	LDAi	0
	STA	101
	RTI
