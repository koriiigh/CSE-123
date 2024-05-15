// Kourosh Ghahramani
// 04/17/2024
// CSE 123
// Creative Project 1: Abstract Strategy Games
// TA: Benoit Le


// This class implements the Connect Four game, a two-player abstract strategy game.
// Players take turns dropping colored discs into a grid, aiming to form a horizontal,
// vertical, or diagonal line of four discs of their color to win the game.

import java.util.*;

public class ConnectFour extends AbstractStrategyGame {
    private char[][] grid;
    private char currentPlayer;
    private boolean winner;


    // Constructor:
    // Initializes a Connect Four game with a standard 6-row by 7-column grid.
    // The game starts with no winner and sets the initial state for the game board.
    public ConnectFour() {
        this.grid = new char[7][6];
        currentPlayer = '*';
        winner = false;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                grid[row][col] = ' ';
            }
        }
    }


    // Provides a description of how to play Connect Four. It details the game objective,
    // the mechanics of making moves, and the win condition.
    // Returns:
    //   A string containing the rules and instructions for playing the game.
    @Override
    public String instructions() {
        return "Instructions: Connect Four is a two-player game. Players" +
            " take turns dropping colored discs into one of the seven columns on a vertically" +
            " suspended 6x7 grid. The objective is to be the first to form a horizontal, " +
            "vertical, or diagonal line of four of one's own discs. Moves are made by " +
            "choosing a column from 0 to 6 where the disc will be dropped. The game"+
            " ends with a win or a tie if the board fills up completely without a winner.";
    }



    
    
    // Provides a textual representation of the current state of the game board.
    // Each cell is displayed with either a space if empty, or the player's token.
    // Useful for visualizing the game state in text format.
    // Returns:
    //   A formatted string representing the game board.
    @Override
    public String toString() {
        String result = " 0 1 2 3 4 5\n---------------\n";
        for (int row = 0; row < grid.length; row++) {
            result += "|";
            for (int col = 0; col < grid[0].length; col++) {
                result += grid[row][col] + "|";
            }
            result += "\n---------------\n";
        }
        return result;
    }





    // Checks if the game has concluded, either by a win or a full board.
    // Returns:
    //   true if the game is over, otherwise false.
    public boolean isGameOver() {
        return winner;
    }


    // Determines the winner of the game if there is one.
    // Returns:
    //   1 if Player 1 has won, 2 if Player 2 has won, -1 if there's no winner yet.
    @Override
    public int getWinner() {
        if (winner) {
            return currentPlayer == '*' ? 1 : 2;
        } else {
            return -1;
        }
    }


    // Identifies which player's turn is next.
    // Returns:
    //   The number representing the next player to make a move
    @Override
    public int getNextPlayer() {
        return currentPlayer == '*' ? 1 : 2;
    }



    // Processes a player's move based on column selection from input.
    // This method changes the state of the game by placing a disc in the chosen column.
    // Parameters:
    //   - input: Scanner object to take player's input.
    // Exceptions:
    //   - IllegalArgumentException if the chosen column is invalid or full.
    public void makeMove(Scanner input) {
        boolean validPlay = false;
        int play = 0;
        while (!validPlay) {
            System.out.print("Player ");
            System.out.print(currentPlayer == '*' ? "Amir" : "Ali");
            System.out.print(", choose a column: ");
            play = input.nextInt();
            validPlay = validate(play);
            if (!validPlay) {
                throw new IllegalArgumentException();
            }
        }
        for (int row = grid.length - 1; row >= 0; row--) {
            if (grid[row][play] == ' ') {
                grid[row][play] = currentPlayer;
                break;
            }
        }
        checkForWinner();
        if (!winner) {
            currentPlayer = (currentPlayer == '*') ? 'O' : '*';
        }
    }




    // Validates the selected column for a move to ensure it is within the board and not full.
    // Parameters:
    //   - column: The chosen column for the move.
    // Returns:
    //   - true if the column can accept a disc, false otherwise.
    private boolean validate(int column) {
        if (column < 0 || column >= grid[0].length) {
            return false;
        }
        return grid[0][column] == ' ';
    }




    // Checks all possible four-disc alignments to determine if the current player has won.
    // Updates the game status if a winning condition is met.
    private void checkForWinner() {
        char currentToken = currentPlayer;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length - 3; col++) {
                if (grid[row][col] == currentToken && grid[row][col + 1] == currentToken &&
                    grid[row][col + 2] == currentToken && grid[row][col + 3] == currentToken) {
                    winner = true;
                    return;
                }
            }
        }

        for (int col = 0; col < grid[0].length; col++) {
            for (int row = 0; row < grid.length - 3; row++) {
                if (grid[row][col] == currentToken && grid[row + 1][col] == currentToken &&
                    grid[row + 2][col] == currentToken && grid[row + 3][col] == currentToken) {
                    winner = true;
                    return;
                }
            }
        }

        for (int row = 0; row < grid.length - 3; row++) {
            for (int col = 0; col < grid[0].length - 3; col++) {
                if (grid[row][col] == currentToken && grid[row + 1][col + 1] == currentToken &&
                    grid[row + 2][col + 2] == currentToken &&
                    grid[row + 3][col + 3] == currentToken) {
                    winner = true;
                    return;
                }
            }
        }

        for (int row = 3; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length - 3; col++) {
                if (grid[row][col] == currentToken && grid[row - 1][col + 1] == currentToken &&
                    grid[row - 2][col + 2] == currentToken &&
                    grid[row - 3][col + 3] == currentToken) {
                    winner = true;
                    return;
                }
            }
        }
    }

}

