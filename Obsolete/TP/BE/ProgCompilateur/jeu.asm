	JMP 	init
	JMP	clavier
	DSW 	positiononehot	; 0000 0001 0000 0000
	DSW 	positionx	; 7 (index parmi les 16 colonnes)
	DSW	tir		; y a t'il un tir en cours ?
	DSW	tirpositionx	; position x du tir en onehot 0000 0001 0000 0000
	DSW	tirpositiony	; position y en index parmi les 16 lignes possibles
	DSW	idxdbuff
init:	LDSPi	@stack@
	STI
	JMP	splash
splash:	DECSP
	LDAi	0		;i = 0
	POKEA	1	
lsplu:	PEEKA	1		; A:= i
	LDBi	10		; B := 10
	SUBB			; B := i - 10 ?
	JZB 	elsplu		; then goto end while (elspl)
	CALL	cls
	PEEKA	1		; A := i
	PUSHA
	LDBi	FFFF
	PUSHB
	CALL 	disl		; on affiche
	INCSP			; on enlève les opérandes
	INCSP
	CALL	swapbuf
	PEEKA	1
	LDBi	1
	ADDA
	POKEA	1		; i = i + 1
	JMP 	lsplu
elsplu:	LDAi	0		;i = 0
	POKEA	1	
lspld:	PEEKA	1		; A:= i
	LDBi	10		; B := 10
	SUBB			; B := i - 10 ?
	JZB 	elspld		; then goto end while (elspld)
	CALL	cls
	PEEKB	1		; A := i
	LDAi	F
	SUBA
	PUSHA
	LDBi	FFFF
	PUSHB
	CALL 	disl		; on affiche
	INCSP			; on enlève les opérandes
	INCSP
	CALL	swapbuf
	PEEKA	1
	LDBi	1
	ADDA
	POKEA	1		; i = i + 1
	JMP 	lspld
elspld:	LDAi	0
	PUSHA
	PUSHA
	CALL	disl
	INCSP
	INCSP			; Fin du splash screen
	JMP	main			
main:	LDAi	0
	STA	tir
	STA	tirpositionx
	STA	tirpositiony
	LDAi	100		; One hot encoding !
	STA	positiononehot
	LDAi	7
	STA	positionx
mainl:	CALL	steptir
	CALL	affiche
	JMP	mainl
	END

	;; If any, move the shot upward until getting out of the screen
steptir:LDAd	tir
	JZA	etir 		; un tir ? sinon on a terminé
	LDAd	tirpositiony
	LDBi	1
	ADDA
	STA	tirpositiony
	LDBi	10		; est ce qu'on est sorti de l'écran ?
	SUBB
	JZB	outtir
	JMP	etir
outtir:	LDAi	0
	STA	tir
	STA	tirpositiony
	STA	tirpositionx
etir:	RET

	;; Display the game
affiche:CALL	cls
	LDAd	tirpositiony	; Affichage du tir
	LDBi	2000
	ADDB
	LDAd	tirpositionx	;
	STAB			;
	LDBd	positiononehot	; Affichage de la base du joueur
	LDAi	2
	MULA
	ADDB
	LDAd	positiononehot
	DIVA
	ADDB
	LDAi	2000
	STBA
	LDAd	positiononehot	; Petit canon
	LDBi	2001
	STAB
	CALL	swapbuf
	RET
	;; Clean the screen
cls:	DECSP
	LDAi	0
	POKEA	1
lcls:	LDAi	10
	PEEKB	1	
	SUBB
	JZB	elcls		; i = 16 ?
	PEEKA	1
	LDBi	2000
	ADDB
	LDAi	0
	STAB
	PEEKA	1
	LDBi	1
	ADDA
	POKEA 	1
	JMP lcls
elcls:	INCSP
	RET
	;; disl(line_number, value)
disl:	PEEKA	3
	LDBi	2000
	ADDB			; B contient l'adresse de la ligne
	PEEKA	2		; A contient la valeur à afficher
	STAB			; B -> RAM[A]
	RET
	
clavier:LDAd	1003
	LDBi	71
	SUBB
	JZB	qkey
	LDBi	64
	SUBB
	JZB	dkey
	LDBi	6C
	SUBB
	JZB	lkey
	JMP	eclavier
qkey:	LDAd	positionx	;Move left the plateform
	JZA	eqkey		; already at max left
	LDBi	1
	SUBA
	STA	positionx
	LDAd	positiononehot
	LDBi	2
	MULA
	STA	positiononehot
eqkey:	JMP	eclavier
dkey:	LDAd	positionx	;Move right the plateform
	LDBi	F
	SUBB
	JZB	edkey		; plateform already at max right
	LDBi	1
	ADDA
	STA	positionx
	LDAd	positiononehot
	DIVA
	STA 	positiononehot
edkey:	JMP 	eclavier
lkey:	LDAi	1
	STA	tir
	LDAd	positiononehot
	STA	tirpositionx
	LDAi	1
	STA	tirpositiony
elkey:	JMP	eclavier
eclavier:RTI

tdbuff:	LDAi	0
	STA	2010
	CALL	cls
	LDAi	1
	STA	2010
	CALL	cls
	DECSP
	LDAi	0
	POKEA	1
ltdbuf: PEEKA	1		; A:= i
	LDBi	10		; B := 10
	SUBB			; B := i - 10 ?
	JZB 	eltdbuf		; then goto end while (elspl)
	PEEKA	1
	LDBi	2000
	ADDB
	LDAi	FFFF
	STAB
	PEEKA	1
	LDBi	1
	ADDA
	POKEA	1
	JMP 	ltdbuf
eltdbuf:LDAi	0
	STA	2010
	END
swapbuf:LDAi	1
	LDBd	idxdbuff
	SUBA
	STA	idxdbuff
	STA	2010
	RET
