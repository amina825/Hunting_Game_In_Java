
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.mycompany.huntinga.HuntingGui;
import com.mycompany.huntinga.Player;
import org.junit.jupiter.api.BeforeEach;


public class HuntingTest {
    private HuntingGui game;

    @BeforeEach
    public void setUp() {
        game = new HuntingGui();
    }

    @Test
    public void testInitializeBoard() {
        // Check fugitive position
        assertEquals("F", game.getCells()[game.boardSize() / 2][game.boardSize() / 2].getText());

        // Check hunters positions
        assertEquals("H", game.getCells()[0][0].getText());
        assertEquals("H", game.getCells()[0][game.boardSize() - 1].getText());
        assertEquals("H", game.getCells()[game.boardSize() - 1][0].getText());
        assertEquals("H", game.getCells()[game.boardSize() - 1][game.boardSize()- 1].getText());
    }

    @Test
    public void testSelectBoardSize() {
        game.boardSizeSelector().setSelectedItem("5x5");
        assertEquals(5, game.boardSize());
        assertEquals(5, game.getCells().length);
        assertEquals(5, game.getCells()[0].length);
    }

    @Test
    public void testMoveFugitive() {
        int initialX = game.fugitive().getX();
        int initialY = game.fugitive().getY();
        game.handleCellClick(initialX + 1, initialY); // Move down
        assertEquals("F", game.getCells()[initialX + 1][initialY].getText());
        assertEquals("", game.getCells()[initialX][initialY].getText());
    }

    @Test
    public void testMoveHunter() {
        int initialX = game.hunters()[0].getX();
        int initialY = game.hunters()[0].getY();
        game.fugitiveTurn = false; // Set turn to hunter
        game.handleCellClick(initialX + 1, initialY); // Move down
        assertEquals("H", game.getCells()[initialX + 1][initialY].getText());
        assertEquals("", game.getCells()[initialX][initialY].getText());
    }

    @Test
    public void testInvalidMoveFugitive() {
        int initialX = game.fugitive().getX();
        int initialY = game.fugitive().getY();
        game.handleCellClick(initialX , initialY); // Invalid move
        assertEquals("F", game.getCells()[initialX][initialY].getText());
        assertEquals("", game.getCells()[initialX + 1][initialY].getText());
    }

    @Test
    public void testInvalidMoveHunter() {
        int initialX = game.hunters()[0].getX();
        int initialY = game.hunters()[0].getY();
        game.fugitiveTurn = false; // Set turn to hunter
        game.handleCellClick(initialX + 2, initialY); // Invalid move
        assertEquals("H", game.getCells()[initialX][initialY].getText());
        assertEquals("", game.getCells()[initialX + 1][initialY].getText());
    }

    @Test
    public void testFugitiveCannotMoveToOccupiedCell() {
        int initialX = game.fugitive().getX();
        int initialY = game.fugitive().getY();
        game.handleCellClick(0, 0); // Cell occupied by hunter
        assertEquals("F", game.getCells()[initialX][initialY].getText());
        assertEquals("H", game.getCells()[0][0].getText());
    }

    @Test
    public void testHunterCannotMoveToOccupiedCell() {
        int initialX = game.hunters()[0].getX();
        int initialY = game.hunters()[0].getY();
        game.fugitiveTurn = false; // Set turn to hunter
        game.handleCellClick(game.fugitive().getX(), game.fugitive().getY()); // Cell occupied by fugitive
        assertEquals("H", game.getCells()[initialX][initialY].getText());
        assertEquals("F", game.getCells()[game.fugitive().getX()][game.fugitive().getY()].getText());
    }

    @Test
    public void testGameEndsWhenFugitiveIsSurrounded() {
        // Surround fugitive
        game.moveCharacter(game.hunters()[0], game.fugitive().getX() - 1, game.fugitive().getY());
        game.moveCharacter(game.hunters()[1], game.fugitive().getX() + 1, game.fugitive().getY());
        game.moveCharacter(game.hunters()[2], game.fugitive().getX(), game.fugitive().getY() - 1);
        game.moveCharacter(game.hunters()[3], game.fugitive().getX(), game.fugitive().getY() + 1);
        assertTrue(game.isFugitiveSurrounded());
    }


    @Test
    public void testGameResetsAfterEnd() {
        game.showWinner("Hunter");
        assertEquals(0, game.moveCount);
        assertEquals("F", game.getCells()[game.boardSize() / 2][game.boardSize() / 2].getText());
    }

    @Test
    public void testTurnAlternatesAfterFugitiveMove() {
        int initialX = game.fugitive().getX();
        int initialY = game.fugitive().getY();
        game.handleCellClick(initialX + 1, initialY); // Move down
        assertFalse(game.fugitiveTurn); // Should be hunter's turn
    }

    @Test
    public void testTurnAlternatesAfterHunterMove() {
        int initialX = game.hunters()[0].getX();
        int initialY = game.hunters()[0].getY();
        game.fugitiveTurn = false; // Set turn to hunter
        game.handleCellClick(initialX + 1, initialY); // Move down
        assertTrue(game.fugitiveTurn); // Should be fugitive's turn
    }

    @Test
    public void testFugitiveCannotMoveOutOfBounds() {
    int initialX = game.fugitive().getX();
    int initialY = game.fugitive().getY();
    game.handleCellClick(game.boardSize(), game.boardSize()); // Move out of bounds
    assertEquals("F", game.getCells()[initialX][initialY].getText());
    }

    @Test
    public void testInvalidInputFugitive() {
        int initialX = game.fugitive().getX();
        int initialY = game.fugitive().getY();
        game.handleCellClick(-1, -1); // Invalid input
        assertEquals("F", game.getCells()[initialX][initialY].getText());
    }

    @Test
    public void testInvalidInputHunter() {
        int initialX = game.hunters()[0].getX();
        int initialY = game.hunters()[0].getY();
        game.fugitiveTurn = false; // Set turn to hunter
        game.handleCellClick(-1, -1); // Invalid input
        assertEquals("H", game.getCells()[initialX][initialY].getText());
    }
        
}
