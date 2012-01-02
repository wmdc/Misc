#!/usr/bin/python2.7

import fileinput

FILENAME = "elements.txt"

chem_symbols = []

for line in fileinput.input(FILENAME):
    chem_symbols.append(line.split("\t")[1])

# can call this as elements("xenon", chem_symbols) for example to use the chemical symbols

def elements(word, symbols, soFar = ""):
    """Produces a tree of symbols representing the possibilities for spelling the given word."""
    if len(word) == 0: return [soFar]
    
    result = []
    
    for e in symbols:
        if word.lower().find(e.lower()) == 0:
            result += elements(word[len(e):], symbols, soFar + e)
    
    if len(result) == 0: return [soFar]
    
    return result


import sys

for word in sys.argv[1:]:
    print elements(word, chem_symbols)
