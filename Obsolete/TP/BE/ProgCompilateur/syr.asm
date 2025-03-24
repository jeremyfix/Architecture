	DSW 	u		; on passe par u plutot que d'utiliser A comme accumulateur
	LDSPi 	@stack@
	LDAi  	7f
	STA 	u
loop:	DECSP			; on reserve de la place pour le resultat
	LDAd	u
	PUSHA
	CALL	syr		; appel de la routine syr(u)
	INCSP			; on supprime l'oprande
	POPA			; on rcupre le rultat
	STA	u		; qu'on sauvegarde
	STA	1000		; et qu'on affiche
	JMP	loop
syr:	PEEKA	2		; on recupre l'oprande u
	LDBi	1
	ANDB			; on test la parit
	JZB	pair
	LDBi	3		; si u impair
	MULA
	LDBi	1
	ADDA			; A = 3 * u + 1
	POKEA	3		; on sauvegarde le rsultat
	RET
pair:	DIVA			; A = u / 2
	POKEA 	3
	RET
