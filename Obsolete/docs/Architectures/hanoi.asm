     LDSPi @stack@
     DECSP          ; On réserve de la place dans la pile pour la valeur de retour
     LDAi  0004
     PUSHA          ; On charge l'opérande
     CALL hanoi    ; On appelle la routine
     POPA           ; On enlève l'opérande
     POPA           ; On récupère le résultat dans A
     STA  1000    ; On l'affiche
     END
hanoi:	PEEKA 0002         ; On récupère n
     LDBi 0001
     SUBB           ; On calcule B:= n - 1
     JZB  end    ; Si B = 0, i.e. n=1, on branche en 0034
     DECSP          ; Sinon on invoque h(n-1)
     PUSHB          ; On empile n-1
     CALL hanoi    ; On appelle la routine h(n-1)
     INCSP          ; On enlève l'opérande
     POPA           ; On récupère le résultat h(n-1) dans A
     LDBi 0002    
     MULA           ; On calcule A := 2*h(n-1)
     LDBi 0001   
     ADDA           ; On calcule A:= 2*h(n-1) + 1
     POKEA 0003   ; On sauvegarde le résultat dans la pile
     RET            ; On retourne à l'appelant
end:	LDAi  0001
     POKEA 0003
     RET
