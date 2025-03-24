	LDSPi 	4f	; initialisation de SP = 0x004f
	LDAi  	3	; valeur de a
	STA	50	; stockée à 0x0050
	STA	1000	; affichée
	LDAi	1	; valeur de b
	STA	51	; stockée à 0x0051
	STA	1001	; affichée
	LDAi	7F	; valeur de u0 = 127
	STA	52	; stockée à 0x0052
	STA	1002	; affichée
loop:	DECSP		; Place pour le résultat u(n)
	LDAd	52  	; empile u(n-1)
	PUSHA
	LDAd	50  	; empile a
	PUSHA
	LDAd	51  	; empile b
	PUSHA
	CALL	gensyr
	POPA		; dépile b
	POPA		; dépile a
	POPA		; dépile u(n-1)
	POPA		; dépile u(n)
	STA	52 	; on sauvegarde u(n)
	STA	1002 	; on l'affiche  --> 40 mots
	JMP    	loop	; on reboucle
; Routines syr(u, a, b) 
gensyr:	PEEKA	4	; Test de parité de u
	LDBi	1
	ANDA
	JZA	3c
	PEEKA	3	; si impair : on charge a dans A
	PEEKB	4	;             on charge u dans B
	MULA		;             on calcule A := a * u
	PEEKB	2	;             on charge b dans B
	ADDA		;             on calcule A := a * u + b
	POKEA	5	;             on sauvegarde le résultat dans la pile
	RET		;             on sort de la routine
	PEEKA	4	; si pair   : on charge u dans A
	DIVA		;             on calcule A := u / 2
	POKEA	5	;             on sauvegarde le résultat
	RET		; 	      on sort de la routine 
	
