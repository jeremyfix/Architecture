	JMP 	init
	JMP 	int
init:	LDSPi	100
	LDAi	0
	STA	102
	STI
	LDAi	0
	STA	101
loop:	LDAd	101
	LDBi	1
	ADDA
	STA	101
	STA	1000
	JMP	loop
int:	LDAi	1
	LDBd	102
	SUBA
	STA	102
	STA	1001
	RTI
