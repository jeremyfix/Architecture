import sys
sys.setrecursionlimit(10000000) 

def f(n):
    x = f(n-1)
    return 2*x+1

f(4)
