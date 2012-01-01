import fileinput

FILENAME = "elements.txt"

symbols = []
slower = []

for line in fileinput.input(FILENAME):
    s = line.split("\t")[1]
    symbols.append(s)
    slower.append(s.lower())


# This doesn't properly handle cases where there are multiple solutions
# for example, "sc" can be Sc (scandium) or SC (sulfur + carbon)

# To fix this I will need to build sets of results to recurse on

def elements(word, soFar = ""):
     s = word.lower()
     
     for e in slower:
             if s.find(e) == 0:
                     return elements(s.replace(e, "", 1), soFar + e.capitalize())
     
     return soFar



def elemt(word, soFar = ""):
    """Produces a tree of chemical symbols representing the possibilities for spelling the given word."""
    
    result = []
    
    s = word.lower()
    
    for e in slower:
        if s.find(e) == 0:
            result.append(e.capitalize())
            rest = elemt(s.replace(e, "", 1), soFar + e.capitalize())
            if rest: result.append(rest)
    
    return result
