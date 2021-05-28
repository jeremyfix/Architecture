	JMP 	init
	JMP	clavier
	DSW 	position
	DSW	line
	DSW	regline
init:	LDSPi	@stack@
	STI
	JMP	splash

splash:	LDAi	0
	STA	line
	LDAi	0		;i = 0
	PUSHA	
lspl:	PEEKA	1
	LDBi	F
	SUBB			; i = 16 ?
	JZB 	elspl		; goto end while (elspl)
	JZA	spl
	LDBi	1
	SUBB
	CALL LLLL procédure display(linenumber, value)
spl2:	


	
	JZA	elspl		; i = 0?
	LDAi	
elspl:	LDAi	0
	POKEA	1
	
main:	LDAi	
	STA	2000
bouton:	
	
