
IPE_SOURCES=$(wildcard Figs/*.ipe)
IPE_PDF=$(patsubst %.ipe,%.pdf,$(IPE_SOURCES))
SOURCES=$(wildcard *.tex)


all:
	make slides-1.pdf 
	make slides-2.pdf
	make slides-3.pdf

slides%.pdf: slides%.tex ipe_pdf
	pdflatex $^
	pdflatex $^

clean:
	rm -f *~ *.aux *.log *.nav *.out *.snm *.toc *.vrb *.bbl *.blg

ipe_pdf:$(IPE_PDF)
	@echo "ipe to pdf conversion done"

Figs/%.pdf: Figs/%.ipe
	@ipetoipe -pdf $^ 2> /dev/null
	@echo "$@ generated"
