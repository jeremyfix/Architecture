	LDSPi	@stack@
	DECSP
	LDAi	A
	PUSHA
	CALL	somme
	INCSP
	POPA
	STA	1000
	END
somme:	DECSP			;totottitiijcbcjbc
	DECSP
	PEEKA	4
	LDBi	1
	SUBA
	POKEA	2
	LDAi	0
	POKEA	1
loop:	PEEKA	2
	JZA	end
	PEEKA	1
	PEEKB	2
	ADDA
	POKEA	1
	PEEKA	2
	LDBi	1
	SUBA
	POKEA	2
	JMP	loop
end:	PEEKA	1
	POKEA	5
	INCSP
	INCSP
	RET