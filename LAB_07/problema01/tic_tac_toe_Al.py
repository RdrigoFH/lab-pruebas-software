# tic_tac_toe_Al.py - VERSIÓN CORREGIDA (100% COBERTURA)
#### TIC TAC TOE ####

# FUNCIONES

def default():
    """Imprime el mensaje de bienvenida."""
    print("\nWelcome! Let's play TIC TAC TOE!\n")


def rules():
    """Imprime las reglas y la disposición del tablero."""
    print("The board will look like this!")
    print("The positions of this 3 x 3 board is same as the right side of your key board.\n")
    print(" 7 | 8 | 9 ")
    print("-----------")
    print(" 4 | 5 | 6 ")
    print("-----------")
    print(" 1 | 2 | 3 ")
    print("\nYou just have to input the position(1-9).")


def play():
    """Pregunta si el jugador está listo para jugar."""
    return input("\nAre you ready to play the game? Enter [Y]es or [N]o.\t").upper().startswith('Y')


def names():
    """Solicita los nombres de los jugadores."""
    p1_name = input("\nEnter NAME of PLAYER 1:\t").capitalize()
    p2_name = input("Enter NAME of PLAYER 2:\t").capitalize()
    return (p1_name, p2_name)


def choice():
    """
    Solicita la elección de X o O para el Jugador 1.
    Retorna una tupla con las elecciones de ambos jugadores.
    """
    p1_choice = ' '
    p2_choice = ' '
    while True:
        p1_choice = input("\nPlayer 1, Do you want to be X or O?\t")[0].upper()
        if p1_choice == 'X' or p1_choice == 'O':
            break
        print("INVALID INPUT! Please Try Again!")
    
    if p1_choice == 'X':
        p2_choice = 'O'
    else:
        p2_choice = 'X'
    
    return (p1_choice, p2_choice)


def first_player():
    """Determina aleatoriamente quién juega primero."""
    import random
    return random.choice((0, 1))


def display_board(board, avail):
    """
    Muestra el tablero actual y las posiciones disponibles.
    board: lista con el estado actual del tablero
    avail: lista con las posiciones disponibles
    """
    print("    " + " {} | {} | {} ".format(board[7], board[8], board[9]) + "            " + " {} | {} | {} ".format(avail[7], avail[8], avail[9]))
    print("    " + "-----------" + "            " + "-----------")
    print("    " + " {} | {} | {} ".format(board[4], board[5], board[6]) + "            " + " {} | {} | {} ".format(avail[4], avail[5], avail[6]))
    print("    " + "-----------" + "            " + "-----------")
    print("    " + " {} | {} | {} ".format(board[1], board[2], board[3]) + "            " + " {} | {} | {} ".format(avail[1], avail[2], avail[3]))


def player_choice(board, name, choice):
    """
    Solicita al jugador su siguiente movimiento.
    Valida que la posición sea válida y esté disponible.
    """
    position = 0
    while position not in [1, 2, 3, 4, 5, 6, 7, 8, 9] or not space_check(board, position):
        try:
            position = int(input(f'\n{name} ({choice}), Choose your next position: (1-9) \t'))
        except ValueError:
            position = 0
        if position not in [1, 2, 3, 4, 5, 6, 7, 8, 9] or not space_check(board, position):
            print(f"INVALID INPUT. Please Try Again!\n")
    print("\n")
    return position


def CompAI(board, name, choice):
    """
    Inteligencia Artificial para el Tic Tac Toe.
    Sigue la estrategia óptima:
    1. Ganar si es posible
    2. Bloquear al oponente
    3. Tomar el centro (prioridad alta)
    4. Tomar esquinas
    5. Tomar bordes
    """
    import random
    
    # Obtener posiciones disponibles
    possibilities = [x for x, letter in enumerate(board) if letter == ' ' and x != 0]
    
    # Si no hay movimientos disponibles, retornar 0
    if not possibilities:
        return 0
    
    # 1. INTENTAR GANAR (Prioridad máxima)
    for i in possibilities:
        boardCopy = board[:]
        boardCopy[i] = choice
        if win_check(boardCopy, choice):
            return i
    
    # 2. BLOQUEAR AL OPONENTE (Prioridad alta)
    opponent = 'O' if choice == 'X' else 'X'
    for i in possibilities:
        boardCopy = board[:]
        boardCopy[i] = opponent
        if win_check(boardCopy, opponent):
            return i
    
    # 3. TOMAR EL CENTRO (Prioridad media-alta) - ¡CORREGIDO!
    if 5 in possibilities:
        return 5
    
    # 4. TOMAR ESQUINAS (Prioridad media)
    corners = [x for x in possibilities if x in [1, 3, 7, 9]]
    if corners:
        return selectRandom(corners)
    
    # 5. TOMAR BORDES (Última opción)
    edges = [x for x in possibilities if x in [2, 4, 6, 8]]
    if edges:
        return selectRandom(edges)
    
    # Si no hay movimientos (caso extremo)
    return 0


def selectRandom(board):
    """Selecciona un elemento aleatorio de una lista."""
    import random
    if not board:  # CORREGIDO: manejar lista vacía
        return 0
    ln = len(board)
    r = random.randrange(0, ln)
    return board[r]


def place_marker(board, avail, choice, position):
    """Coloca la marca del jugador en la posición indicada."""
    board[position] = choice
    avail[position] = ' '


def space_check(board, position):
    """Verifica si una posición está vacía en el tablero."""
    return board[position] == ' '


def full_board_check(board):
    """Verifica si el tablero está completamente lleno."""
    for i in range(1, 10):
        if space_check(board, i):
            return False
    return True


def win_check(board, choice):
    """
    Verifica si el jugador ha ganado.
    Comprueba todas las combinaciones ganadoras posibles.
    """
    return (
        # Horizontal
        (board[1] == choice and board[2] == choice and board[3] == choice) or
        (board[4] == choice and board[5] == choice and board[6] == choice) or
        (board[7] == choice and board[8] == choice and board[9] == choice) or
        # Vertical
        (board[1] == choice and board[4] == choice and board[7] == choice) or
        (board[2] == choice and board[5] == choice and board[8] == choice) or
        (board[3] == choice and board[6] == choice and board[9] == choice) or
        # Diagonal
        (board[1] == choice and board[5] == choice and board[9] == choice) or
        (board[3] == choice and board[5] == choice and board[7] == choice)
    )


def delay(mode):
    """Introduce una pausa si el modo es 2 (Computer vs Computer)."""
    if mode == 2:
        import time
        time.sleep(2)


def replay():
    """Pregunta si los jugadores quieren jugar de nuevo."""
    return input('\nDo you want to play again? Enter [Y]es or [N]o: ').lower().startswith('y')


def main():
    """Función principal del juego."""
    print("\n\t\t NAMASTE! \n")
    input("Press ENTER to start!")
    default()
    rules()
    
    while True:
        # Inicializar tablero
        theBoard = [' '] * 10
        available = [str(num) for num in range(0, 10)]
        
        # Selección del modo de juego
        print("\n[0]. Player vs. Computer")
        print("[1]. Player vs. Player")
        print("[2]. Computer vs. Computer")
        mode = int(input("\nSelect an option [0]-[2]: "))
        
        # Configuración según el modo
        if mode == 1:  # Player vs Player
            p1_name, p2_name = names()
            p1_choice, p2_choice = choice()
            print(f"\n{p1_name}: {p1_choice}")
            print(f"{p2_name}: {p2_choice}")
        elif mode == 0:  # Player vs Computer
            p1_name = input("\nEnter NAME of PLAYER who will go against the Computer:\t").capitalize()
            p2_name = "Computer"
            p1_choice, p2_choice = choice()
            print(f"\n{p1_name}: {p1_choice}")
            print(f"{p2_name}: {p2_choice}")
        else:  # Computer vs Computer
            p1_name = "Computer1"
            p2_name = "Computer2"
            p1_choice, p2_choice = "X", "O"
            print(f"\n{p1_name}: {p1_choice}")
            print(f"{p2_name}: {p2_choice}")
        
        # Determinar quién empieza
        if first_player():
            turn = p2_name
        else:
            turn = p1_name
        
        print(f"\n{turn} will go first!")
        
        # Preparar el juego
        if mode == 2:
            input("\nThis is going to be fast! Press Enter for the battle to begin!\n")
            play_game = 1
        else:
            play_game = play()
        
        # Bucle principal del juego
        while play_game:
            # Turno del Jugador 1
            if turn == p1_name:
                display_board(theBoard, available)
                
                if mode != 2:
                    position = player_choice(theBoard, p1_name, p1_choice)
                else:
                    position = CompAI(theBoard, p1_name, p1_choice)
                    print(f'\n{p1_name} ({p1_choice}) has placed on {position}\n')
                
                place_marker(theBoard, available, p1_choice, position)
                
                if win_check(theBoard, p1_choice):
                    display_board(theBoard, available)
                    print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                    if mode:
                        print(f'\n\nCONGRATULATIONS {p1_name}! YOU HAVE WON THE GAME!\n\n')
                    else:
                        print('\n\nTHE Computer HAS WON THE GAME!\n\n')
                    print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                    play_game = False
                else:
                    if full_board_check(theBoard):
                        display_board(theBoard, available)
                        print("~~~~~~~~~~~~~~~~~~")
                        print('\nThe game is a DRAW!\n')
                        print("~~~~~~~~~~~~~~~~~~")
                        break
                    else:
                        turn = p2_name
            
            # Turno del Jugador 2
            else:
                display_board(theBoard, available)
                
                if mode == 1:
                    position = player_choice(theBoard, p2_name, p2_choice)
                else:
                    position = CompAI(theBoard, p2_name, p2_choice)
                    print(f'\n{p2_name} ({p2_choice}) has placed on {position}\n')
                
                place_marker(theBoard, available, p2_choice, position)
                
                if win_check(theBoard, p2_choice):
                    display_board(theBoard, available)
                    print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                    if mode:
                        print(f'\n\nCONGRATULATIONS {p2_name}! YOU HAVE WON THE GAME!\n\n')
                    else:
                        print('\n\nTHE Computer HAS WON THE GAME!\n\n')
                    print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                    play_game = False
                else:
                    if full_board_check(theBoard):
                        display_board(theBoard, available)
                        print("~~~~~~~~~~~~~~~~~~")
                        print('\nThe game is a DRAW!\n')
                        print("~~~~~~~~~~~~~~~~~~")
                        break
                    else:
                        turn = p1_name
        
        # Preguntar si jugar de nuevo
        if not replay():
            break
    
    print("\n\n\t\t\tTHE END!")


# EJECUCIÓN DEL JUEGO (solo si se ejecuta directamente)
if __name__ == '__main__':
    main()