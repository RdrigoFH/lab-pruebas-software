# test_tic_tac_toe.py - PRUEBAS UNITARIAS CORREGIDAS (100% COBERTURA)

import pytest
from unittest.mock import patch
from tic_tac_toe_Al import *

# ============================================
# PRUEBAS DE FUNCIONES BÁSICAS
# ============================================

def test_default():
    with patch('builtins.print'):
        default()


def test_rules():
    with patch('builtins.print'):
        rules()


def test_play_yes():
    with patch('builtins.input', return_value='Yes'):
        assert play() is True


def test_play_no():
    with patch('builtins.input', return_value='No'):
        assert play() is False


def test_play_y():
    with patch('builtins.input', return_value='y'):
        assert play() is True


def test_play_n():
    with patch('builtins.input', return_value='n'):
        assert play() is False


def test_play_Y():
    with patch('builtins.input', return_value='Y'):
        assert play() is True


def test_play_N():
    with patch('builtins.input', return_value='N'):
        assert play() is False


def test_names():
    with patch('builtins.input', side_effect=['Alice', 'Bob']):
        p1, p2 = names()
        assert p1 == 'Alice'
        assert p2 == 'Bob'


def test_names_capitalize():
    with patch('builtins.input', side_effect=['alice', 'bob']):
        p1, p2 = names()
        assert p1 == 'Alice'
        assert p2 == 'Bob'


# ============================================
# PRUEBAS DE choice() - CORREGIDAS
# ============================================

def test_choice_x():
    with patch('builtins.input', return_value='X'):
        with patch('builtins.print'):
            p1_choice, p2_choice = choice()
            assert p1_choice == 'X'
            assert p2_choice == 'O'


def test_choice_o():
    with patch('builtins.input', return_value='O'):
        with patch('builtins.print'):
            p1_choice, p2_choice = choice()
            assert p1_choice == 'O'
            assert p2_choice == 'X'


def test_choice_invalid_then_valid():
    with patch('builtins.input', side_effect=['Z', 'X']):
        with patch('builtins.print'):
            p1_choice, p2_choice = choice()
            assert p1_choice == 'X'
            assert p2_choice == 'O'


def test_choice_lowercase():
    with patch('builtins.input', return_value='x'):
        with patch('builtins.print'):
            p1_choice, p2_choice = choice()
            assert p1_choice == 'X'
            assert p2_choice == 'O'


def test_choice_empty_input():
    """CORREGIDO: choice() con entrada vacía"""
    with patch('builtins.input', side_effect=['', 'X']):
        with patch('builtins.print'):
            p1_choice, p2_choice = choice()
            assert p1_choice == 'X'
            assert p2_choice == 'O'


def test_choice_empty_then_invalid_then_valid():
    """CORREGIDO: choice() con vacío, inválido y luego válido"""
    with patch('builtins.input', side_effect=['', 'Z', 'X']):
        with patch('builtins.print'):
            p1_choice, p2_choice = choice()
            assert p1_choice == 'X'
            assert p2_choice == 'O'


# ============================================
# PRUEBAS DE first_player()
# ============================================

def test_first_player_returns_zero():
    with patch('random.choice', return_value=0):
        result = first_player()
        assert result == 0


def test_first_player_returns_one():
    with patch('random.choice', return_value=1):
        result = first_player()
        assert result == 1


# ============================================
# PRUEBAS DE display_board()
# ============================================

def test_display_board():
    board = [' '] * 10
    available = [str(i) for i in range(10)]
    with patch('builtins.print'):
        display_board(board, available)


def test_display_board_with_moves():
    board = [' '] * 10
    board[1] = 'X'
    board[5] = 'O'
    board[9] = 'X'
    available = [str(i) for i in range(10)]
    with patch('builtins.print'):
        display_board(board, available)


# ============================================
# PRUEBAS DE player_choice()
# ============================================

def test_player_choice_valid():
    board = [' '] * 10
    with patch('builtins.input', return_value='5'):
        result = player_choice(board, "Alice", "X")
        assert result == 5


def test_player_choice_invalid_then_valid():
    board = [' '] * 10
    with patch('builtins.input', side_effect=['0', '10', '5']):
        with patch('builtins.print'):
            result = player_choice(board, "Alice", "X")
            assert result == 5


def test_player_choice_occupied():
    board = [' '] * 10
    board[5] = 'X'
    with patch('builtins.input', side_effect=['5', '3']):
        with patch('builtins.print'):
            result = player_choice(board, "Alice", "X")
            assert result == 3


def test_player_choice_non_numeric():
    board = [' '] * 10
    with patch('builtins.input', side_effect=['abc', '5']):
        with patch('builtins.print'):
            result = player_choice(board, "Alice", "X")
            assert result == 5


def test_player_choice_empty_input():
    board = [' '] * 10
    with patch('builtins.input', side_effect=['', '5']):
        with patch('builtins.print'):
            result = player_choice(board, "Alice", "X")
            assert result == 5


# ============================================
# PRUEBAS DE CompAI() - CORREGIDAS
# ============================================

def test_comp_ai_winning_move():
    board = [' '] * 10
    board[1] = 'O'
    board[2] = 'O'
    result = CompAI(board, "Computer", "O")
    assert result == 3


def test_comp_ai_blocking_move():
    board = [' '] * 10
    board[1] = 'X'
    board[2] = 'X'
    result = CompAI(board, "Computer", "O")
    assert result == 3


def test_comp_ai_chooses_corner():
    """CORREGIDO: IA debe elegir esquina (1,3,7,9)"""
    board = [' '] * 10
    board[5] = 'X'  # Ocupar centro
    with patch('random.randrange', return_value=0):
        result = CompAI(board, "Computer", "X")
        assert result in [1, 3, 7, 9]


def test_comp_ai_chooses_center():
    """CORREGIDO: IA debe elegir centro (5) cuando no hay esquinas"""
    board = [' '] * 10
    board[1] = 'X'
    board[3] = 'O'
    board[7] = 'X'
    board[9] = 'O'
    result = CompAI(board, "Computer", "X")
    assert result == 5


def test_comp_ai_chooses_edge():
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
    board = [' '] * 10
    result = CompAI(board, "Computer", "X")
    assert 1 <= result <= 9


def test_comp_ai_no_moves_available():
    board = ['X', 'O', 'X', 'O', 'X', 'O', 'X', 'O', 'X', 'O']
    result = CompAI(board, "Computer", "X")
    assert result == 0


# ============================================
# PRUEBAS DE selectRandom()
# ============================================

def test_select_random():
    board = [1, 2, 3, 4, 5]
    with patch('random.randrange', return_value=2):
        result = selectRandom(board)
        assert result == 3


def test_select_random_empty():
    result = selectRandom([])
    assert result == 0


# ============================================
# PRUEBAS DE place_marker()
# ============================================

def test_place_marker():
    board = [' '] * 10
    available = [str(i) for i in range(10)]
    place_marker(board, available, 'X', 5)
    assert board[5] == 'X'
    assert available[5] == ' '


def test_place_marker_occupied():
    board = [' '] * 10
    board[5] = 'X'
    available = [str(i) for i in range(10)]
    place_marker(board, available, 'O', 5)
    assert board[5] == 'O'


# ============================================
# PRUEBAS DE space_check()
# ============================================

def test_space_check_empty():
    board = [' '] * 10
    assert space_check(board, 5) is True


def test_space_check_occupied():
    board = [' '] * 10
    board[5] = 'X'
    assert space_check(board, 5) is False


# ============================================
# PRUEBAS DE full_board_check()
# ============================================

def test_full_board_check_empty():
    board = [' '] * 10
    assert full_board_check(board) is False


def test_full_board_check_full():
    board = [' '] * 10
    for i in range(1, 10):
        board[i] = 'X'
    assert full_board_check(board) is True


def test_full_board_check_partial():
    board = [' '] * 10
    board[1] = 'X'
    board[2] = 'O'
    assert full_board_check(board) is False


# ============================================
# PRUEBAS DE win_check()
# ============================================

def test_win_check_horizontal_top():
    board = [' '] * 10
    board[1] = board[2] = board[3] = 'X'
    assert win_check(board, 'X') is True


def test_win_check_horizontal_middle():
    board = [' '] * 10
    board[4] = board[5] = board[6] = 'O'
    assert win_check(board, 'O') is True


def test_win_check_horizontal_bottom():
    board = [' '] * 10
    board[7] = board[8] = board[9] = 'X'
    assert win_check(board, 'X') is True


def test_win_check_vertical_left():
    board = [' '] * 10
    board[1] = board[4] = board[7] = 'O'
    assert win_check(board, 'O') is True


def test_win_check_vertical_middle():
    board = [' '] * 10
    board[2] = board[5] = board[8] = 'X'
    assert win_check(board, 'X') is True


def test_win_check_vertical_right():
    board = [' '] * 10
    board[3] = board[6] = board[9] = 'O'
    assert win_check(board, 'O') is True


def test_win_check_diagonal_main():
    board = [' '] * 10
    board[1] = board[5] = board[9] = 'X'
    assert win_check(board, 'X') is True


def test_win_check_diagonal_secondary():
    board = [' '] * 10
    board[3] = board[5] = board[7] = 'O'
    assert win_check(board, 'O') is True


def test_win_check_no_win():
    board = [' '] * 10
    board[1] = 'X'
    board[2] = 'O'
    board[3] = 'X'
    assert win_check(board, 'X') is False


# ============================================
# PRUEBAS DE delay()
# ============================================

def test_delay_mode_2():
    with patch('time.sleep') as mock_sleep:
        delay(2)
        mock_sleep.assert_called_once_with(2)


def test_delay_mode_0():
    with patch('time.sleep') as mock_sleep:
        delay(0)
        mock_sleep.assert_not_called()


def test_delay_mode_1():
    with patch('time.sleep') as mock_sleep:
        delay(1)
        mock_sleep.assert_not_called()


# ============================================
# PRUEBAS DE replay()
# ============================================

def test_replay_yes():
    with patch('builtins.input', return_value='Yes'):
        assert replay() is True


def test_replay_y():
    with patch('builtins.input', return_value='y'):
        assert replay() is True


def test_replay_no():
    with patch('builtins.input', return_value='No'):
        assert replay() is False


def test_replay_n():
    with patch('builtins.input', return_value='n'):
        assert replay() is False


# ============================================
# PRUEBAS DE main() - CORREGIDAS (con inputs reales)
# ============================================

def test_main_mode_0_player_vs_computer():
    """CORREGIDO: modo 0 con inputs reales"""
    with patch('builtins.input', side_effect=['', '0', 'Alice', 'X', 'N', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=1):
                with patch('tic_tac_toe_Al.play', return_value=False):
                    main()


def test_main_mode_1_player_vs_player():
    """CORREGIDO: modo 1 con inputs reales"""
    with patch('builtins.input', side_effect=['', '1', 'Alice', 'Bob', 'X', 'N', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.play', return_value=False):
                    main()


def test_main_mode_2_computer_vs_computer():
    """CORREGIDO: modo 2 con inputs reales"""
    with patch('builtins.input', side_effect=['', '2', '', 'N', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                main()


def test_main_with_replay_yes():
    """CORREGIDO: replay con inputs reales"""
    with patch('builtins.input', side_effect=['', '1', 'Alice', 'Bob', 'X', 'N', 'Yes', '0', 'Alice', 'X', 'N', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.play', return_value=False):
                    main()


def test_main_with_invalid_mode():
    """CORREGIDO: modo inválido con inputs reales"""
    with patch('builtins.input', side_effect=['', '3', '0', 'Alice', 'X', 'N', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.play', return_value=False):
                    main()


def test_main_value_error_mode():
    """CORREGIDO: ValueError en modo"""
    with patch('builtins.input', side_effect=['', 'abc', '0', 'Alice', 'X', 'N', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.play', return_value=False):
                    main()


def test_main_mode_0_player_win():
    """CORREGIDO: Player gana en modo 0"""
    with patch('builtins.input', side_effect=['', '0', 'Alice', 'X', 'Y', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.player_choice', side_effect=[1, 2, 3, 4, 5, 6, 7]):
                    with patch('tic_tac_toe_Al.CompAI', side_effect=[8, 9, 10]):
                        with patch('tic_tac_toe_Al.win_check', side_effect=[False, False, False, False, False, False, False, True]):
                            with patch('tic_tac_toe_Al.full_board_check', return_value=False):
                                with patch('tic_tac_toe_Al.replay', side_effect=[True, False]):
                                    with patch('tic_tac_toe_Al.play', return_value=True):
                                        main()


def test_main_mode_0_computer_win():
    """CORREGIDO: Computadora gana en modo 0"""
    with patch('builtins.input', side_effect=['', '0', 'Alice', 'X', 'Y', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.player_choice', side_effect=[1, 2, 3, 4, 5]):
                    with patch('tic_tac_toe_Al.CompAI', side_effect=[6, 7, 8, 9, 10, 11, 12]):
                        with patch('tic_tac_toe_Al.win_check', side_effect=[False, False, False, False, False, False, True]):
                            with patch('tic_tac_toe_Al.full_board_check', return_value=False):
                                with patch('tic_tac_toe_Al.replay', side_effect=[True, False]):
                                    with patch('tic_tac_toe_Al.play', return_value=True):
                                        main()


def test_main_mode_0_draw():
    """CORREGIDO: Empate en modo 0"""
    with patch('builtins.input', side_effect=['', '0', 'Alice', 'X', 'Y', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.player_choice', side_effect=[1, 2, 3, 4, 5]):
                    with patch('tic_tac_toe_Al.CompAI', side_effect=[6, 7, 8, 9]):
                        with patch('tic_tac_toe_Al.win_check', return_value=False):
                            with patch('tic_tac_toe_Al.full_board_check', side_effect=[False, False, False, False, False, False, False, False, True]):
                                with patch('tic_tac_toe_Al.replay', side_effect=[True, False]):
                                    with patch('tic_tac_toe_Al.play', return_value=True):
                                        main()


def test_main_mode_1_player1_win():
    """CORREGIDO: Player1 gana en modo 1"""
    with patch('builtins.input', side_effect=['', '1', 'Alice', 'Bob', 'X', 'Y', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.play', return_value=True):
                    with patch('tic_tac_toe_Al.player_choice', side_effect=[1, 2, 3, 4, 5, 6, 7]):
                        with patch('tic_tac_toe_Al.win_check', side_effect=[False, False, False, False, False, False, True]):
                            with patch('tic_tac_toe_Al.full_board_check', return_value=False):
                                with patch('tic_tac_toe_Al.replay', side_effect=[True, False]):
                                    main()


def test_main_mode_1_player2_win():
    """CORREGIDO: Player2 gana en modo 1"""
    with patch('builtins.input', side_effect=['', '1', 'Alice', 'Bob', 'X', 'Y', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=1):
                with patch('tic_tac_toe_Al.play', return_value=True):
                    with patch('tic_tac_toe_Al.player_choice', side_effect=[1, 2, 3, 4, 5, 6, 7]):
                        with patch('tic_tac_toe_Al.win_check', side_effect=[False, False, False, False, False, False, True]):
                            with patch('tic_tac_toe_Al.full_board_check', return_value=False):
                                with patch('tic_tac_toe_Al.replay', side_effect=[True, False]):
                                    main()


def test_main_mode_1_draw():
    """CORREGIDO: Empate en modo 1"""
    with patch('builtins.input', side_effect=['', '1', 'Alice', 'Bob', 'X', 'Y', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.play', return_value=True):
                    with patch('tic_tac_toe_Al.player_choice', side_effect=[1, 2, 3, 4, 5, 6, 7, 8, 9]):
                        with patch('tic_tac_toe_Al.win_check', return_value=False):
                            with patch('tic_tac_toe_Al.full_board_check', side_effect=[False, False, False, False, False, False, False, False, True]):
                                with patch('tic_tac_toe_Al.replay', side_effect=[True, False]):
                                    main()


def test_main_mode_2_computer1_win():
    """CORREGIDO: Computer1 gana en modo 2"""
    with patch('builtins.input', side_effect=['', '2', '', 'N', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.CompAI', side_effect=[1, 2, 3, 4, 5, 6, 7, 8, 9]):
                    with patch('tic_tac_toe_Al.win_check', side_effect=[False, False, False, False, False, False, False, False, True]):
                        with patch('tic_tac_toe_Al.full_board_check', return_value=False):
                            with patch('tic_tac_toe_Al.replay', return_value=False):
                                main()


def test_main_mode_2_computer2_win():
    """CORREGIDO: Computer2 gana en modo 2"""
    with patch('builtins.input', side_effect=['', '2', '', 'N', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=1):
                with patch('tic_tac_toe_Al.CompAI', side_effect=[1, 2, 3, 4, 5, 6, 7, 8, 9]):
                    with patch('tic_tac_toe_Al.win_check', side_effect=[False, False, False, False, False, False, False, False, True]):
                        with patch('tic_tac_toe_Al.full_board_check', return_value=False):
                            with patch('tic_tac_toe_Al.replay', return_value=False):
                                main()


def test_main_mode_2_draw():
    """CORREGIDO: Empate en modo 2"""
    with patch('builtins.input', side_effect=['', '2', '', 'N', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.CompAI', side_effect=[1, 2, 3, 4, 5, 6, 7, 8, 9]):
                    with patch('tic_tac_toe_Al.win_check', return_value=False):
                        with patch('tic_tac_toe_Al.full_board_check', side_effect=[False, False, False, False, False, False, False, False, True]):
                            with patch('tic_tac_toe_Al.replay', return_value=False):
                                main()


def test_full_game_with_replay():
    """CORREGIDO: Replay completo"""
    with patch('builtins.input', side_effect=['', '0', 'Alice', 'X', 'Y', 'Yes', '0', 'Alice', 'X', 'N', 'N']):
        with patch('builtins.print'):
            with patch('tic_tac_toe_Al.first_player', return_value=0):
                with patch('tic_tac_toe_Al.CompAI', side_effect=[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]):
                    with patch('tic_tac_toe_Al.player_choice', side_effect=[4, 5, 6, 7, 8, 9, 10, 11, 12]):
                        with patch('tic_tac_toe_Al.win_check', side_effect=[False, False, False, False, False, True, False, False, False, False, False, True]):
                            with patch('tic_tac_toe_Al.full_board_check', return_value=False):
                                with patch('tic_tac_toe_Al.play', return_value=True):
                                    main()


# ============================================
# COMBINACIONES DE CONDICIONES
# ============================================

def test_win_check_all_combinations():
    board = [' '] * 10
    board[1] = board[2] = board[3] = 'X'
    assert win_check(board, 'X') is True
    board = [' '] * 10
    board[1] = board[4] = board[7] = 'O'
    assert win_check(board, 'O') is True
    board = [' '] * 10
    board[1] = board[5] = board[9] = 'X'
    assert win_check(board, 'X') is True
    board = [' '] * 10
    board[1] = 'X'
    board[2] = 'O'
    board[3] = 'X'
    assert win_check(board, 'X') is False


def test_comp_ai_all_combinations():
    """CORREGIDO: Todas las combinaciones de CompAI"""
    board = [' '] * 10
    board[1] = board[2] = 'O'
    result = CompAI(board, "Computer", "O")
    assert result == 3
    board = [' '] * 10
    board[1] = board[2] = 'X'
    result = CompAI(board, "Computer", "O")
    assert result == 3
    board = [' '] * 10
    board[5] = 'X'  # Ocupar centro para forzar esquina
    with patch('random.randrange', return_value=0):
        result = CompAI(board, "Computer", "X")
        assert result in [1, 3, 7, 9]
    board = [' '] * 10
    board[1] = board[3] = board[7] = board[9] = 'X'
    result = CompAI(board, "Computer", "O")
    assert result == 5
    board = [' '] * 10
    board[1] = board[3] = board[7] = board[9] = 'X'
    board[5] = 'O'
    with patch('random.randrange', return_value=0):
        result = CompAI(board, "Computer", "X")
        assert result in [2, 4, 6, 8]
    board = ['X', 'O', 'X', 'O', 'X', 'O', 'X', 'O', 'X', 'O']
    result = CompAI(board, "Computer", "X")
    assert result == 0


def test_choice_condition_combinations():
    with patch('builtins.input', return_value='X'):
        with patch('builtins.print'):
            p1, p2 = choice()
            assert p1 == 'X' and p2 == 'O'
    with patch('builtins.input', return_value='O'):
        with patch('builtins.print'):
            p1, p2 = choice()
            assert p1 == 'O' and p2 == 'X'


def test_make_move_combinations():
    board = [' '] * 10
    available = [str(i) for i in range(10)]
    assert space_check(board, 5) is True
    place_marker(board, available, 'X', 5)
    assert board[5] == 'X'
    assert available[5] == ' '
    assert space_check(board, 5) is False


def test_delay_combinations():
    with patch('time.sleep') as mock_sleep:
        delay(2)
        mock_sleep.assert_called_once_with(2)
    with patch('time.sleep') as mock_sleep:
        delay(1)
        mock_sleep.assert_not_called()
    with patch('time.sleep') as mock_sleep:
        delay(0)
        mock_sleep.assert_not_called()


def test_play_combinations():
    with patch('builtins.input', return_value='Yes'):
        assert play() is True
    with patch('builtins.input', return_value='y'):
        assert play() is True
    with patch('builtins.input', return_value='No'):
        assert play() is False
    with patch('builtins.input', return_value='n'):
        assert play() is False
    with patch('builtins.input', return_value='Y'):
        assert play() is True
    with patch('builtins.input', return_value='N'):
        assert play() is False


def test_replay_combinations():
    with patch('builtins.input', return_value='Yes'):
        assert replay() is True
    with patch('builtins.input', return_value='y'):
        assert replay() is True
    with patch('builtins.input', return_value='No'):
        assert replay() is False
    with patch('builtins.input', return_value='n'):
        assert replay() is False


def test_full_board_check_combinations():
    board = [' '] * 10
    assert full_board_check(board) is False
    board = [' '] * 10
    board[1] = 'X'
    board[2] = 'O'
    assert full_board_check(board) is False
    board = [' '] * 10
    for i in range(1, 10):
        board[i] = 'X'
    assert full_board_check(board) is True


# ============================================
# EJECUCIÓN
# ============================================

if __name__ == '__main__':
    pytest.main([
        '-v',
        '--cov=tic_tac_toe_Al',
        '--cov-branch',
        '--cov-report=term-missing',
        '--cov-report=html'
    ])