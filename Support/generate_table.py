

import sys

def code_v0():
    return '&\\multicolumn{4}{c|}{0} &\\multicolumn{3}{c|}{0}&\\multicolumn{2}{c|}{CodeMux}&\\multicolumn{4}{c|}{UAL}&\multicolumn{2}{c|}{0} & SetB & ReadB & SetA& ReadA & SetPC & ReadPC& SetRADM & SetMem & ReadMem & \multicolumn{8}{c}{\\@Adr}'


str += '\\begin{footnotesize}\n'
str += '\\begin{tabular}{c'
for i in range(8):
    str += '|' + 'c'*4
str += '}\n'
str += "bit :"
for i in range(32):
    str += '&$b_{%i}$' % (31-i)

str += '\\\\ \n'
str += 'valeur : ' + code_v0()


str += '\n'
str += '\\end{tabular}\n'
print str
