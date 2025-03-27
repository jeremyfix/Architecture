# -*- coding: utf-8 -*-

import sys

if len(sys.argv) != 3:
    print("Usage : %s <code.asm> <code.mem>" % sys.argv[0])
    sys.exit(-1)

instructions = {
    "END": {"code": 0x0C, "nop": 0},
    "LDAi": {"code": 0x10, "nop": 1},
    "LDAd": {"code": 0x14, "nop": 1},
    "STAB": {"code": 0x18, "nop": 0},
    "STA": {"code": 0x1C, "nop": 1},
    "LDBi": {"code": 0x20, "nop": 1},
    "LDBd": {"code": 0x24, "nop": 1},
    "STB": {"code": 0x2C, "nop": 1},
    "STBA": {"code": 0x28, "nop": 0},
    "ADDA": {"code": 0x30, "nop": 0},
    "ADDB": {"code": 0x34, "nop": 0},
    "SUBA": {"code": 0x38, "nop": 0},
    "SUBB": {"code": 0x3C, "nop": 0},
    "MULA": {"code": 0x40, "nop": 0},
    "MULB": {"code": 0x44, "nop": 0},
    "DIVA": {"code": 0x48, "nop": 0},
    "ANDA": {"code": 0x50, "nop": 0},
    "ANDB": {"code": 0x54, "nop": 0},
    "ORA": {"code": 0x58, "nop": 0},
    "ORB": {"code": 0x5C, "nop": 0},
    "NOTA": {"code": 0x60, "nop": 0},
    "NOTB": {"code": 0x64, "nop": 0},
    "JMP": {"code": 0x70, "nop": 1},
    "JZA": {"code": 0x74, "nop": 1},
    "JZB": {"code": 0x78, "nop": 1},
    "LDSPi": {"code": 0x80, "nop": 1},
    "LDSPd": {"code": 0x84, "nop": 1},
    "STSP": {"code": 0x8C, "nop": 1},
    "INCSP": {"code": 0x90, "nop": 0},
    "DECSP": {"code": 0x94, "nop": 0},
    "CALL": {"code": 0xA0, "nop": 1},
    "RET": {"code": 0xA8, "nop": 0},
    "PUSHA": {"code": 0xB0, "nop": 0},
    "POPA": {"code": 0xB4, "nop": 0},
    "POKEA": {"code": 0xB8, "nop": 1},
    "PEEKA": {"code": 0xBC, "nop": 1},
    "PUSHB": {"code": 0xC0, "nop": 0},
    "POPB": {"code": 0xC4, "nop": 0},
    "POKEB": {"code": 0xC8, "nop": 1},
    "PEEKB": {"code": 0xCC, "nop": 1},
    "CLI": {"code": 0xD0, "nop": 0},
    "STI": {"code": 0xD4, "nop": 0},
    "INT": {"code": 0xE0, "nop": 0},
    "RTI": {"code": 0xE8, "nop": 0},
}


def strip_str(mystr):
    if mystr.startswith("0x"):
        return mystr[2:]
    else:
        return mystr


memptr = 0x00
asm_filename = sys.argv[1]
asm = open(asm_filename).readlines()
asm = [l.strip() for l in asm]
asm_lnumber = 1
bytecode_filename = sys.argv[2]

labels = {}
variables = []

print("")
print("Génération du code machine")
bytecode = ""
header_code = True
for l in asm:
    l = l.strip()  # On supprime les espaces de chaque côté
    if len(l) == 0 or l[0] == ";":
        asm_lnumber += 1
        continue

    # On enlève les éventuels commentaires
    fields = l.split(";")
    l_wo_comments = fields[0].strip()

    # On s'assure qu'on n'a pas une étiquette seule
    if len(l_wo_comments) != 0 and l_wo_comments[-1] == ":":
        print(
            "[Erreur] une étiquette ne peut pas apparaître seule sur une ligne, elle doit forcément être suivie d'une instruction, %s ligne %i"
            % (asm_filename, asm_lnumber)
        )
        sys.exit(-1)

    # On vérifie si il y a une étiquette
    fields = l_wo_comments.split(":")
    if len(fields) > 1:
        # Il y a effectivement une étiquette
        # On s'assure que l'étiquette n'est pas interprétable en hexadécimal
        # Ca facilite le travail de substitution
        # Il pourrait apparaitre des problèmes si quelqu'un fait par exemple :
        # a: LDAi a
        #    JMP a
        try:
            val = int("0x" + fields[0].strip(), 16)
            print(
                "[Erreur] une étiquette ne doit pas être interprétable comme une valeur hexadécimale; l'étiquette \"%s\" peut l'être;  %s ligne %i"
                % (fields[0].strip(), asm_filename, asm_lnumber)
            )
            sys.exit(-1)
        except ValueError:
            pass
        labels[fields[0].strip()] = hex(memptr)
        l_instruction = fields[1].strip()
    else:
        l_instruction = fields[0].strip()

    # On récupère l'instruction et éventuellement l'opérande
    fields = l_instruction.split()
    inst_name = fields[0]
    inst_op = None
    if len(fields) > 1:
        inst_op = fields[1]

    # Est ce qu'on est à l'entête du programme:
    # Les DSW ne sont pas autorisés dés qu'on croise une instruction autre que DSW ou JMP

    # Est ce que l'instruction est une réservation mémoire ?
    if inst_name == "DSW":
        if not inst_op:
            print(
                "[ERREUR] l'instruction %s attends un nom de variable, %s ligne %i"
                % ("DSW", asm_filename, asm_lnumber)
            )
            sys.exit(-1)
        if not header_code:
            print(
                "[ERREUR] les instructions DSW doivent se trouver en tête du programme, après éventuellement des JMP, %s ligne %i"
                % (asm_filename, asm_lnumber)
            )
            sys.exit(-1)
        # ON s'assure que le nom de variable ne peut pas être reconnu comme une valeur hexadécimale
        # ca facilite le travail de substitution
        # Sinon, on pourrait avoir des soucis du style:
        # DSW a
        # LDAd a
        # LDAi a
        try:
            val = int("0x" + inst_op, 16)
            print(
                '[Erreur] un nom de variable ne doit pas être interprétable comme une valeur hexadécimale; la variable "%s" peut l\'être;  %s ligne %i'
                % (inst_op, asm_filename, asm_lnumber)
            )
            sys.exit(-1)
        except ValueError:
            pass
        variables.append(inst_op)
    # Sinon ce doit être une instruction machine
    else:
        if inst_name not in instructions:
            print(
                "[Erreur] l'instruction %s n'est pas reconnue, %s ligne %i "
                % (inst_name, asm_filename, asm_lnumber)
            )
            sys.exit(-1)

        if header_code and inst_name != "JMP":
            header_code = False

        # On vérifie le nombre d'opérandes
        expected_n_op = instructions[inst_name]["nop"]
        if (inst_op and expected_n_op == 0) or ((not inst_op) and expected_n_op == 1):
            if inst_op and expected_n_op == 0:
                print(
                    "[Erreur] l'instruction %s n'attend pas d'argument, %s ligne %i"
                    % (inst_name, asm_filename, asm_lnumber)
                )
                sys.exit(-1)
            else:
                print(
                    "[Erreur] l'instruction %s attends un argument, %s ligne %i"
                    % (inst_name, asm_filename, asm_lnumber)
                )
                sys.exit(-1)

        bytecode += strip_str(hex(instructions[inst_name]["code"] * 16**2)) + " "
        memptr += 1
        if inst_op:
            # print("Inst_op : " + inst_op + "," + strip_str(inst_op))

            bytecode += strip_str(inst_op) + "\n"
            memptr += 1
        else:
            bytecode += "\n"
    asm_lnumber += 1


# On place la pile "tout au bout" de la RAM, avant les afficheurs
stack = int(0x0FFF) - len(variables)
labels["@stack@"] = hex(stack)
# On gère le placement des variables globales
lcl_var = stack + 1
for v in variables:
    labels[v] = hex(lcl_var)
    lcl_var += 1

# et on opère maintenant aux substitutions d'étiquettes
for k, l in labels.items():
    bytecode = bytecode.replace(" " + k + "\n", " " + strip_str(l) + "\n")

print("Les substitutions suivantes ont été opérées: " + str(labels))

# Avant de sauvegarder le code machine, on s'assure qu'il n'y a que
# du code interprétable en hexadécimal; Sinon cela voudrait dire que, par exemple, des étiquettes ou variables sont utilisées et non déclarées
print("Vérification de la validité du code")
for l in bytecode.split():
    try:
        val = int(l, 16)
    except:
        print(
            "[Erreur] la chaîne %s ne corresponds pas un code hexadécimal valide. Avez vous fait référence à des étiquettes ou variables non définies ?"
            % l
        )
        print("J'étais entrain d'analyser le code ci-dessous : \n" + bytecode)
        print("[Erreur]")
        sys.exit(-1)

f = open(bytecode_filename, "w")
f.write("v2.0 raw\n" + bytecode)
f.close()
print(
    "Le code machine a été généré et sauvegardé dans le fichier %s" % bytecode_filename
)

print("")
print("Code machine : ")
print(bytecode)

print("[Succès]")
