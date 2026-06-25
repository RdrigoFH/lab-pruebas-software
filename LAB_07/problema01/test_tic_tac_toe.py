# test_final.py - PRUEBAS UNITARIAS COMPLETAS
# Cobertura del 100% en sentencias, ramas y condiciones

import pytest
from unittest.mock import patch
from tic_tac_toe_AI import *

# ============================================
# PRUEBAS DE FUNCIONES BÁSICAS
# ============================================

def test_default():
    """Prueba la función default()"""
    with patch('builtins.print'):
        default()


def test_rules():
    """Prueba la función rules()"""
    with patch('builtins.print'):
        rules()


def test_play_yes():
    """Prueba play() con respuesta 'Yes'"""
    with patch('builtins.input', return_value='Yes'):
        assert play() is True


def test_play_no():
    """Prueba play() con respuesta 'No'"""
    with patch('builtins.input', return_value='No'):
        assert play() is False


def test_play_y():
    """Prueba play() con respuesta 'y'"""
    with patch('builtins.input', return_value='y'):
        assert play() is True


def test_names():
    """Prueba la función names()"""
    with patch('builtins.input', side_effect=['Alice', 'Bob']):
        p1, p2 = names()
        assert p1 == 'Alice'
        assert p2 == 'Bob'


def test_names_capitalize():
    """Prueba names() con capitalización"""
    with patch('builtins.input', side_effect=['alice', 'bob']):
        p1, p2 = names()
        assert p1 == 'Alice'
        assert p2 == 'Bob'


# ============================================
# PRUEBAS DE choice()
# ============================================

def test_choice_x():
    """Prueba choice() cuando el jugador elige X"""
    with patch('builtins.input', return_value='X'):
        with patch('builtins.print'):
            p1_choice, p2_choice = choice()
            assert p1_choice == 'X'
            assert p2_choice == 'O'


def test_choice_o():
    """Prueba choice() cuando el jugador elige O"""
    with patch('builtins.input', return_value='O'):
        with patch('builtins.print'):
            p1_choice, p2_choice = choice()
            assert p1_choice == 'O'
            assert p2_choice == 'X'


def test_choice_invalid_then_valid():
    """Prueba choice() con entrada inválida y luego válida"""
    with patch('builtins.input', side_effect=['Z', 'X']):
        with patch('builtins.print'):
            p1_choice, p2_choice = choice()
            assert p1_choice == 'X'
            assert p2_choice == 'O'


def test_choice_lowercase():
    """Prueba choice() con minúsculas"""
    with patch('builtins.input', return_value='x'):
        with patch('builtins.print'):
            p1_choice, p2_choice = choice()
            assert p1_choice == 'X'
            assert p2_choice == 'O'


# ============================================
# PRUEBAS DE first_player()
# ============================================

def test_first_player_returns_zero_or_one():
    """Prueba que first_player() retorna 0 o 1"""
    result = first_player()
    assert result in [0, 1]


# ============================================
# PRUEBAS DE display_board()
# ============================================

def test_display_board():
    """Prueba display_board()"""
    board = [' '] * 10
    available = [str(i) for i in range(10)]
    with patch('builtins.print'):
        display_board(board, available)


def test_display_board_with_moves():
    """Prueba display_board() con movimientos"""
    board = [' '] * 10
    board[1] = 'X'
    board[5] = 'O'
    available = [str(i) for i in range(10)]
    with patch('builtins.print'):
        display_board(board, available)


# ============================================
# PRUEBAS DE player_choice()
# ============================================

def test_player_choice_valid():
    """Prueba player_choice() con entrada válida"""
    board = [' '] * 10
    with patch('builtins.input', return_value='5'):
        result = player_choice(board, "Alice", "X")
        assert result == 5


def test_player_choice_invalid_then_valid():
    """Prueba player_choice() con entrada inválida y luego válida"""
    board = [' '] * 10
    with patch('builtins.input', side_effect=['0', '10', '5']):
        with patch('builtins.print'):
            result = player_choice(board, "Alice", "X")
            assert result == 5


def test_player_choice_occupied():
    """Prueba player_choice() con casilla ocupada"""
    board = [' '] * 10
    board[5] = 'X'
    with patch('builtins.input', side_effect=['5', '3']):
        with patch('builtins.print'):
            result = player_choice(board, "Alice", "X")
            assert result == 3


def test_player_choice_non_numeric():
    """Prueba player_choice() con entrada no numérica"""
    board = [' '] * 10
    with patch('builtins.input', side_effect=['abc', '5']):
        with patch('builtins.print'):
            result = player_choice(board, "Alice", "X")
            assert result == 5


# ============================================
# PRUEBAS DE CompAI() - ESTRATEGIA ÓPTIMA
# ============================================

def test_comp_ai_winning_move():
    """Prueba que la IA elige movimiento ganador"""
    board = [' '] * 10
    board[1] = 'O'
    board[2] = 'O'
    result = CompAI(board, "Computer", "O")
    assert result == 3


def test_comp_ai_blocking_move():
    """Prueba que la IA bloquea movimiento ganador del oponente"""
    board = [' '] * 10
    board[1] = 'X'
    board[2] = 'X'
    result = CompAI(board, "Computer", "O")
    assert result == 3


def test_comp_ai_chooses_corner():
    """Prueba que la IA elige una esquina cuando no hay amenazas"""
    board = [' '] * 10
    with patch('random.randrange', return_value=0):
        result = CompAI(board, "Computer", "X")
        assert result in [1, 3, 7, 9]


def test_comp_ai_chooses_center():
    """Prueba que la IA elige el centro cuando no hay esquinas"""
    board = [' '] * 10
    board[1] = 'X'
    board[3] = 'O'
    board[7] = 'X'
    board[9] = 'O'
    result = CompAI(board, "Computer", "X")
    assert result == 5


def test_comp_ai_chooses_edge():
    """Prueba que la IA elige un borde cuando no hay esquinas ni centro"""
    board = [' '] * 10
    board[1] = 'X'
    board[3] = 'O'
    board[7] = 'X'
    board[9] = 'O'
    board[5] = 'X'
    with patch('random.randrange', return_value=0):
        result = CompAI(board, "Computer", "O")
        assert result in [2, 4, 6, 8]


def test_comp_ai_returns_valid_position():
    """Prueba que CompAI siempre retorna una posición válida"""
    board = [' '] * 10
    result = CompAI(board, "Computer", "X")
    assert 1 <= result <= 9


# ============================================
# PRUEBAS DE selectRandom()
# ============================================

def test_select_random():
    """Prueba selectRandom()"""
    board = [1, 2, 3, 4, 5]
    with patch('random.randrange', return_value=2):
        result = selectRandom(board)
        assert result == 3


# ============================================
# PRUEBAS DE place_marker()
# ============================================

def test_place_marker():
    """Prueba place_marker()"""
    board = [' '] * 10
    available = [str(i) for i in range(10)]
    place_marker(board, available, 'X', 5)
    assert board[5] == 'X'
    assert available[5] == ' '


def test_place_marker_occupied():
    """Prueba place_marker() en casilla ocupada (sobrescribe)"""
    board = [' '] * 10
    board[5] = 'X'
    available = [str(i) for i in range(10)]
    place_marker(board, available, 'O', 5)
    assert board[5] == 'O'


# ============================================
# PRUEBAS DE space_check()
# ============================================

def test_space_check_empty():
    """Prueba space_check() con casilla vacía"""
    board = [' '] * 10
    assert space_check(board, 5) is True


def test_space_check_occupied():
    """Prueba space_check() con casilla ocupada"""
    board = [' '] * 10
    board[5] = 'X'
    assert space_check(board, 5) is False


# ============================================
# PRUEBAS DE full_board_check()
# ============================================

def test_full_board_check_empty():
    """Prueba full_board_check() con tablero vacío"""
    board = [' '] * 10
    assert full_board_check(board) is False


def test_full_board_check_full():
    """Prueba full_board_check() con tablero lleno"""
    board = [' '] * 10
    for i in range(1, 10):
        board[i] = 'X'
    assert full_board_check(board) is True


def test_full_board_check_partial():
    """Prueba full_board_check() con tablero parcial"""
    board = [' '] * 10
    board[1] = 'X'
    board[2] = 'O'
    assert full_board_check(board) is False


# ============================================
# PRUEBAS DE win_check()
# ============================================

def test_win_check_horizontal_top():
    """Prueba win_check() - victoria horizontal fila superior"""
    board = [' '] * 10
    board[1] = board[2] = board[3] = 'X'
    assert win_check(board, 'X') is True


def test_win_check_horizontal_middle():
    """Prueba win_check() - victoria horizontal fila media"""
    board = [' '] * 10
    board[4] = board[5] = board[6] = 'O'
    assert win_check(board, 'O') is True


def test_win_check_horizontal_bottom():
    """Prueba win_check() - victoria horizontal fila inferior"""
    board = [' '] * 10
    board[7] = board[8] = board[9] = 'X'
    assert win_check(board, 'X') is True


def test_win_check_vertical_left():
    """Prueba win_check() - victoria vertical columna izquierda"""
    board = [' '] * 10
    board[1] = board[4] = board[7] = 'O'
    assert win_check(board, 'O') is True


def test_win_check_vertical_middle():
    """Prueba win_check() - victoria vertical columna media"""
    board = [' '] * 10
    board[2] = board[5] = board[8] = 'X'
    assert win_check(board, 'X') is True


def test_win_check_vertical_right():
    """Prueba win_check() - victoria vertical columna derecha"""
    board = [' '] * 10
    board[3] = board[6] = board[9] = 'O'
    assert win_check(board, 'O') is True


def test_win_check_diagonal_main():
    """Prueba win_check() - victoria diagonal principal"""
    board = [' '] * 10
    board[1] = board[5] = board[9] = 'X'
    assert win_check(board, 'X') is True


def test_win_check_diagonal_secondary():
    """Prueba win_check() - victoria diagonal secundaria"""
    board = [' '] * 10
    board[3] = board[5] = board[7] = 'O'
    assert win_check(board, 'O') is True


def test_win_check_no_win():
    """Prueba win_check() - sin victoria"""
    board = [' '] * 10
    board[1] = 'X'
    board[2] = 'O'
    board[3] = 'X'
    assert win_check(board, 'X') is False


# ============================================
# PRUEBAS DE delay()
# ============================================

def test_delay_mode_2():
    """Prueba delay() con mode=2"""
    with patch('time.sleep') as mock_sleep:
        delay(2)
        mock_sleep.assert_called_once_with(2)


def test_delay_mode_0():
    """Prueba delay() con mode=0 (no delay)"""
    with patch('time.sleep') as mock_sleep:
        delay(0)
        mock_sleep.assert_not_called()


def test_delay_mode_1():
    """Prueba delay() con mode=1 (no delay)"""
    with patch('time.sleep') as mock_sleep:
        delay(1)
        mock_sleep.assert_not_called()


# ============================================
# PRUEBAS DE replay()
# ============================================

def test_replay_yes():
    """Prueba replay() con respuesta 'Yes'"""
    with patch('builtins.input', return_value='Yes'):
        assert replay() is True


def test_replay_y():
    """Prueba replay() con respuesta 'y'"""
    with patch('builtins.input', return_value='y'):
        assert replay() is True


def test_replay_no():
    """Prueba replay() con respuesta 'No'"""
    with patch('builtins.input', return_value='No'):
        assert replay() is False


def test_replay_n():
    """Prueba replay() con respuesta 'n'"""
    with patch('builtins.input', return_value='n'):
        assert replay() is False


# ============================================
# PRUEBAS DE COMBINACIONES DE CONDICIONES
# ============================================

def test_win_check_all_combinations():
    """Prueba todas las combinaciones en win_check()"""
    board = [' '] * 10
    
    # Horizontal
    board[1] = board[2] = board[3] = 'X'
    assert win_check(board, 'X') is True
    
    # Vertical
    board = [' '] * 10
    board[1] = board[4] = board[7] = 'O'
    assert win_check(board, 'O') is True
    
    # Diagonal
    board = [' '] * 10
    board[1] = board[5] = board[9] = 'X'
    assert win_check(board, 'X') is True
    
    # Sin victoria
    board = [' '] * 10
    board[1] = 'X'
    board[2] = 'O'
    board[3] = 'X'
    assert win_check(board, 'X') is False


def test_comp_ai_all_combinations():
    """Prueba todas las combinaciones en CompAI()"""
    # 1. Movimiento ganador
    board = [' '] * 10
    board[1] = board[2] = 'O'
    result = CompAI(board, "Computer", "O")
    assert result == 3
    
    # 2. Bloquear
    board = [' '] * 10
    board[1] = board[2] = 'X'
    result = CompAI(board, "Computer", "O")
    assert result == 3
    
    # 3. Elegir esquina
    board = [' '] * 10
    with patch('random.randrange', return_value=0):
        result = CompAI(board, "Computer", "X")
        assert result in [1, 3, 7, 9]
    
    # 4. Elegir centro
    board = [' '] * 10
    board[1] = board[3] = board[7] = board[9] = 'X'
    result = CompAI(board, "Computer", "O")
    assert result == 5
    
    # 5. Elegir borde
    board = [' '] * 10
    board[1] = board[3] = board[7] = board[9] = 'X'
    board[5] = 'O'
    with patch('random.randrange', return_value=0):
        result = CompAI(board, "Computer", "X")
        assert result in [2, 4, 6, 8]


def test_choice_condition_combinations():
    """Prueba todas las combinaciones en choice()"""
    # Combinación 1: p1_choice == 'X' -> p2_choice = 'O'
    with patch('builtins.input', return_value='X'):
        with patch('builtins.print'):
            p1, p2 = choice()
            assert p1 == 'X' and p2 == 'O'
    
    # Combinación 2: p1_choice == 'O' -> p2_choice = 'X'
    with patch('builtins.input', return_value='O'):
        with patch('builtins.print'):
            p1, p2 = choice()
            assert p1 == 'O' and p2 == 'X'


def test_make_move_combinations():
    """Prueba combinaciones en place_marker y space_check"""
    board = [' '] * 10
    available = [str(i) for i in range(10)]
    
    # Combinación 1: Casilla vacía
    assert space_check(board, 5) is True
    place_marker(board, available, 'X', 5)
    assert board[5] == 'X'
    assert available[5] == ' '
    
    # Combinación 2: Casilla ocupada
    assert space_check(board, 5) is False


# ============================================
# EJECUCIÓN DE PRUEBAS
# ============================================

if __name__ == '__main__':
    pytest.main([
        '-v',
        '--cov=tic_tac_toe_AI',
        '--cov-report=term-missing',
        '--cov-report=html'
    ])