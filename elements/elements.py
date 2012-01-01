import fileinput

FILENAME = "elements.txt"

symbols = []
slower = []

for line in fileinput.input(FILENAME):
    s = line.split("\t")[1]
    symbols.append(s)
    slower.append(s.lower())

# can call this as elements("xenon", symbols) for example to use the chemical symbols

def elements(word, symbols, soFar = ""):
    """Produces a tree of symbols representing the possibilities for spelling the given word."""
    
    if len(word) == 0: return [soFar]
    
    result = []
    
    s = word.lower()
    
    for e in symbols:
        if s.find(e.lower()) == 0:
            result += elemt2(s.replace(e.lower(), "", 1), soFar + e)
    
    return result
