	JMP 	0x0004
	JMP 	0x0022
init:	LDSPi	0x0100
	STI
	LDAi	0x0000
	STA	0x0101
	STA	0x0102
loop:	LDAd 	0x0102
	JZA	0x0017
	LDAi	0x0000
	STA	0x0101
	STA	0x0102
	LDAd	0x0101
	LDBi	0x0001
	ADDA
	STA	0x0101
	STA	0x1000
	JMP	0x000d
int:	LDAi	0x0001
	STA	0x0102
	RTI
