
IPE_SOURCES=$(wildcard Figs/*.ipe)
IPE_PDF=$(patsubst %.ipe,%.pdf,$(IPE_SOURCES))
SOURCES=$(wildcard *.tex)

support.pdf: $(SOURCES) ipe_pdf
	pdflatex support.tex
	bibtex support
	pdflatex support.tex
	pdflatex support.tex

clean:
	rm -f *~ *.aux *.log

ipe_pdf:$(IPE_PDF)
	@echo "ipe to pdf conversion done"

Figs/%.pdf: Figs/%.ipe
	@ipetoipe -pdf $^ 2> /dev/null
	@echo "$@ generated"

ascii_table.pdf:ascii_pdf.tex
	pdflatex ascii_pdf.tex
	pdfcrop ascii_pdf.pdf
	pdftk A=ascii_pdf-crop.pdf cat A2 output ascii_table.pdf
