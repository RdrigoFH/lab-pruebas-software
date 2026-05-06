# Algoritmo: Shell Sort mejorado (Donald Knuth, 1973)
# Recibe números como argumentos desde la línea de comandos,
# los ordena usando el algoritmo Shell Sort y los imprime ordenados.

import sys


def shell_sort(a, size):
    h = 1
    while h <= size // 3:   # ← corregido
        h = h * 3 + 1
    while h != 1:
        h //= 3
        for i in range(h, size):   # ← corregido
            v = a[i]
            j = i
            while j >= h and a[j - h] > v:
                a[j] = a[j - h]
                j -= h
            if i != j:
                a[j] = v

# Es una mejora del Insertion Sort clásico. En vez de mover elementos de uno en uno,
# los mueve en saltos grandes primero, lo que lo hace significativamente
# más eficiente con listas grandes.


def main():
    args = sys.argv[1:]
    a = []
    for arg in args:
        try:
            a.append(int(arg))
        except ValueError:
            a.append(0)

    size = len(a) -1
    shell_sort(a, size)
    print("Output:", end=" ")
    for num in a:
        print(num, end=" ")
    print()


if __name__ == "__main__":
    main()
