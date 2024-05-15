import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class Testing {
    @Test
    @DisplayName("EXAMPLE TEST CASE - Small TicTacToe Example")
    public void firstCaseTest() {
        AbstractStrategyGame g = new TicTacToe();

        // You can add optional error messages that will be displayed if a test fails
        assertEquals(1, g.getNextPlayer(), "Player 1 not next player after construction");
        assertEquals(-1, g.getWinner(), "Winner incorrectly declared after construction");
        assertFalse(g.isGameOver(), "Game over immediately after construction");

        // Weird way we're going to make moves - make our own scanners NOT
        // connected to System.in. Since we can make Scanners over strings this will
        // work the exact way and allow us to control input!
        g.makeMove(new Scanner("0 0"));
        assertEquals(2, g.getNextPlayer(), "Player 2 not next player after a single move");
        assertEquals(-1, g.getWinner(), "Winner incorrectly declared after a single move");
        assertFalse(g.isGameOver(), "Game over immediately after construction");

        assertThrows(IllegalArgumentException.class, () -> {
            // -1 is an illegal move so our code should throw an IllegalArgumentException
            g.makeMove(new Scanner("-1 2"));
        }, "IllegalArgumentException not thrown for illegal move");
    }

    @Test
    @DisplayName("EXAMPLE TEST CASE - Large TicTacToe Example")
    public void secondCaseTest() {
        // You definitely don't have to get this fancy in your tests!
        AbstractStrategyGame g = new TicTacToe();

        // Going to play a whole game where 1 plays in first row, 2 plays in second row
        // No optional error messages - up to you if you want your code to be easier to debug!
        for (int i = 0; i < 5; i++) {
            int player = (i % 2) + 1;
            assertEquals(player, g.getNextPlayer());
            assertFalse(g.isGameOver());

            int col = i / 2;
            g.makeMove(new Scanner(player + " " + col));
        }

        // At this point, 5 moves have been played, player 1 should have three in a row and
        // player 2 should have two
        assertTrue(g.isGameOver());
        assertEquals(1, g.getWinner());
        assertEquals(-1, g.getNextPlayer());
    }
}
