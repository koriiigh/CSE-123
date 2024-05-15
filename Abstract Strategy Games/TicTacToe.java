// **THIS IS AN EXAMPLE IMPLEMENTATION!**
// Brett Wortzman
// CSE 123
// C0: Abstract Strategy Games
//
// A class to represent a game of tic-tac-toe that implements the 
// AbstractStrategyGame interface.
import java.util.*;

public class TicTacToe extends AbstractStrategyGame {
    private char[][] board;
    private boolean isXTurn;

    // Constructs a new TicTacToe game.
    public TicTacToe() {
        board = new char[][]{{'-', '-', '-'},
                             {'-', '-', '-'},
                             {'-', '-', '-'}};
        isXTurn = true;
    }

    // Returns whether or not the game is over.
    public boolean isGameOver() {
        return getWinner() >= 0;
    }

    // Returns the index of the winner of the game.
    // 1 if player 1 (X), 2 if player 2 (O), 0 if a tie occurred,
    // and -1 if the game is not over.
    public int getWinner() {
        for (int i = 0; i < board.length; i++) {
            // check row i
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] != '-') {
                return board[i][0] == 'X' ? 1 : 2;
            }

            // check col i
            if (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] != '-') {
                return board[0][i] == 'X' ? 1 : 2;
            }
        }

        // check diagonals
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != '-') {
            return board[0][0] == 'X' ? 1 : 2;
        }        
        if (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != '-') {
            return board[0][2] == 'X' ? 1 : 2;
        }

        // check for tie
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == '-') {
                    // unfilled space; game not over
                    return -1;
                }
            }
        }

        // it's a tie!
        return 0;
    }

    // Returns the index of which player's turn it is.
    // 1 if player 1 (X), 2 if player 2 (O), -1 if the game is over
    public int getNextPlayer() {
        if (isGameOver()) {
            return -1;
        }
        return isXTurn ? 1 : 2;
    }

    // Given the input, places an X or an O where
    // the player specifies.
    // Throws an IllegalArgumentException if the position is
    // invalid, whether that be out of bounds or already occupied.
    // Board bounds are [0, 2] for both rows and cols.
    public void makeMove(Scanner input) {
        char currPlayer = isXTurn ? 'X' : 'O';

        System.out.print("Row? ");
        int row = input.nextInt();
        System.out.print("Column? ");
        int col = input.nextInt();

        makeMove(row, col, currPlayer);
        isXTurn = !isXTurn;
    }

    // Private helper method for makeMove.
    // Given a row and col, as well as player index,
    // places an X or an O in that row and col.
    // Throws an IllegalArgumentException if the position is
    // invalid, whether that be out of bounds or already occupied.
    // Board bounds are [0, 2] for both rows and cols.
    private void makeMove(int row, int col, char player) {
        if (row < 0 || row >= board.length ||
            col < 0 || col >= board[0].length) {
                throw new IllegalArgumentException("Invalid board position: " + row + "," + col);
        }

        if (board[row][col] != '-') {
            throw new IllegalArgumentException("Space already occupied: " + row + "," + col);
        }
        
        board[row][col] = player;
    }

    // Returns a String containing instructions to play the game.
    public String instructions() {
        String result = "";
        result += "Player 1 is X and goes first. Choose where to play by entering a row and\n";
        result += "column number, where (0, 0) is the upper left and (2, 2) is the lower right.\n";
        result += "Spaces show as a - are empty. The game ends when one player marks three spaces\n";
        result += "in a row, in which case that player wins, or when the board is full, in which\n";
        result += "case the game ends in a tie.";
        return result;
    }

    // Returns a String representation of the current state of the board.
    public String toString() {
        String result = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                result += board[i][j] + " ";
            }
            result += "\n";
        }
        return result;
    }
}
