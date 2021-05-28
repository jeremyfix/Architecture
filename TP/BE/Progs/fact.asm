		LDSPi	100	; Initialisation du Stack Pointer
		DECSP		; On réserve de la place pour le résultat
		LDAi	8	; On empile l'opérande
		PUSHA
		CALL	d	; Appel de routine
		INCSP		; Suppression de l'opérande
		POPA		; On charge le résultat en registre A
		STA	1002	; On affiche le résultat
endmain:	END	 	;	On s'arrête                 
facto:		PEEKA	2	; On charge l'opérande n dans A    
		STA	1000	; On affiche l'opérande n
		JZA	ifzero	; Si n=0 on saute
		DECSP		; On réserve de la place pour le résultat
		LDBi	1	;
		SUBA		; A = n - 1
		PUSHA		; On empile l'opérande
		CALL	d	; on appelle fac(n-1)
		INCSP		; Suppression de l'opérande
		POPB		; On met le résultat dans B
		PEEKA	2	; On met l'opérande n dans A
		STA	1000
		MULA	       	;   on calcule   n * fac(n-1)
		STA	1001	; On affiche fac(n) = n * fac(n-1) dans l'afficheur 2
		POKEA	3	; On sauvegarde le résultat
		RET		; On finit la routine
ifzero:		LDAi	1	; fac(0) = 1 donc on charge 1 dans A   
		POKEA	3	; qu'on met sauvegarde comme résultat
		STA	1001	; On affiche fac(0) dans l'afficheur 2   
		RET		; 
	
	
