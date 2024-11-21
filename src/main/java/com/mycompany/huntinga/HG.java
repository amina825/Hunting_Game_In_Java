/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.huntinga;

/**
 * Main class for the Hunting Game.
 * This game involves a fugitive (F) trying to escape from multiple hunters (H)
 * on a grid-based board. Players take turns to move, and the game ends when 
 * the fugitive is surrounded by hunters or a set number of turns is reached.
 *
 * @author Muhammad Saqib - G3FUKC
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class HG {

    private JFrame mainWindow;
    private JPanel gameBoardPanel;
    private JComboBox<String> gridSizeSelector;
    private JButton[][] boardButtons;
    private Player escapee;
    private Player[] pursuers;
    private int boardDimension = 3; // Default grid size
    private int turnCounter = 0;
    private boolean isEscapeeTurn = true;
    private String winner = "";  // Added to store the winner name

    /**
     * Constructor for HG, initializes the game window and starts the game setup.
     */
    public HG() {
        configureMainWindow();
        setupGame();
    }

    /**
     * Configures the main game window with layout and adds the options panel
     * and game board panel.
     */
    private void configureMainWindow() {
        mainWindow = new JFrame("Hunting Game");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new BorderLayout());

        JPanel optionsPanel = createOptionsPanel();
        mainWindow.add(optionsPanel, BorderLayout.NORTH);

        gameBoardPanel = new JPanel();
        mainWindow.add(gameBoardPanel, BorderLayout.CENTER);

        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    /**
     * Creates the options panel containing the grid size selector dropdown.
     * 
     * @return JPanel containing the options for selecting board size.
     */
    private JPanel createOptionsPanel() {
        JPanel optionsPanel = new JPanel(new FlowLayout());
        optionsPanel.add(new JLabel("Choose Board Size:"));

        gridSizeSelector = new JComboBox<>(new String[]{"3x3", "5x5", "7x7"});
        gridSizeSelector.addActionListener(e -> {
            String selectedOption = (String) gridSizeSelector.getSelectedItem();
            if (selectedOption != null) {
                boardDimension = Integer.parseInt(selectedOption.substring(0, 1));
                initializeBoard();
            }
        });
        optionsPanel.add(gridSizeSelector);

        return optionsPanel;
    }

    /**
     * Sets up the game by initializing the game board.
     */
    private void setupGame() {
        initializeBoard();
        mainWindow.setLocationRelativeTo(null);
    }

    /**
     * Initializes the game board based on the selected grid size, creating buttons
     * for each cell and setting up the initial positions of the fugitive and hunters.
     */
    private void initializeBoard() {
        gameBoardPanel.removeAll();
        gameBoardPanel.setLayout(new GridLayout(boardDimension, boardDimension));
        boardButtons = new JButton[boardDimension][boardDimension];

        for (int row = 0; row < boardDimension; row++) {
            for (int col = 0; col < boardDimension; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(80, 80));
                int x = row;
                int y = col;
                button.addActionListener(e -> processButtonClick(x, y));
                boardButtons[row][col] = button;
                gameBoardPanel.add(button);
            }
        }
        positionGameCharacters();
        gameBoardPanel.revalidate();
        gameBoardPanel.repaint();
    }

    /**
     * Positions the fugitive and hunters on the board at their initial locations.
     */
    private void positionGameCharacters() {
        escapee = new Fugitive(boardDimension / 2, boardDimension / 2);
        boardButtons[escapee.getA()][escapee.getB()].setText("F");

        pursuers = new Player[]{
            new Hunter(0, 0),
            new Hunter(0, boardDimension - 1),
            new Hunter(boardDimension - 1, 0),
            new Hunter(boardDimension - 1, boardDimension - 1)
        };
        for (Player hunter : pursuers) {
            boardButtons[hunter.getA()][hunter.getB()].setText("H");
        }
    }

    /**
     * Processes a button click event for moving either the fugitive or hunters
     * based on whose turn it is.
     * 
     * @param x The row position of the clicked cell.
     * @param y The column position of the clicked cell.
     */
    public void processButtonClick(int x, int y) {
        if (isEscapeeTurn) {
            if (attemptMoveEscapee(x, y)) {
                evaluateGameStatus();
                isEscapeeTurn = false;
            }
        } else {
            for (Player hunter : pursuers) {
                if (attemptMovePursuer(hunter, x, y)) {
                    evaluateGameStatus();
                    isEscapeeTurn = true;
                    break;
                }
            }
        }
    }

    /**
     * Attempts to move the fugitive to a new position if it is adjacent and empty.
     * 
     * @param newX The new row position for the fugitive.
     * @param newY The new column position for the fugitive.
     * @return true if the move is successful, false otherwise.
     */
    public boolean attemptMoveEscapee(int newX, int newY) {
        if (isAdjacentMove(escapee, newX, newY) && isCellEmpty(newX, newY)) {
            updatePlayerPosition(escapee, newX, newY);
            turnCounter++;
            return true;
        }
        return false;
    }

    /**
     * Attempts to move a hunter to a new position if it is adjacent and empty.
     * 
     * @param pursuer The hunter attempting to move.
     * @param newX The new row position for the hunter.
     * @param newY The new column position for the hunter.
     * @return true if the move is successful, false otherwise.
     */
    public boolean attemptMovePursuer(Player pursuer, int newX, int newY) {
        if (isAdjacentMove(pursuer, newX, newY) && isCellEmpty(newX, newY)) {
            updatePlayerPosition(pursuer, newX, newY);
            turnCounter++;
            return true;
        }
        return false;
    }

    /**
     * Checks if a move is adjacent to the player's current position.
     * 
     * @param player The player making the move.
     * @param x The row position of the destination.
     * @param y The column position of the destination.
     * @return true if the move is adjacent, false otherwise.
     */
    private boolean isAdjacentMove(Player player, int x, int y) {
        return Math.abs(player.getA() - x) + Math.abs(player.getB() - y) == 1;
    }

    /**
     * Checks if a cell is empty.
     * 
     * @param x The row position of the cell.
     * @param y The column position of the cell.
     * @return true if the cell is empty, false otherwise.
     */
    private boolean isCellEmpty(int x, int y) {
        return boardButtons[x][y].getText().isEmpty();
    }

    /**
     * Updates a player's position on the board.
     * 
     * @param player The player to move.
     * @param x The new row position.
     * @param y The new column position.
     */
    private void updatePlayerPosition(Player player, int x, int y) {
        boardButtons[player.getA()][player.getB()].setText("");
        player.set(x, y);
        boardButtons[x][y].setText(player instanceof Fugitive ? "F" : "H");
    }

    /**
     * Evaluates the game status to check if there is a winner.
     */
    public void evaluateGameStatus() {
        if (isEscapeeSurrounded()) {
            displayWinner("Hunters");
        } else if (turnCounter >= boardDimension * 4) {
            displayWinner(isEscapeeSurrounded() ? "Hunters" : "Fugitive");
        }
    }

    /**
     * Checks if the fugitive is surrounded by hunters.
     * 
     * @return true if the fugitive is surrounded, false otherwise.
     */
    public boolean isEscapeeSurrounded() {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] direction : directions) {
            int adjX = escapee.getA() + direction[0];
            int adjY = escapee.getB() + direction[1];
            if (isWithinBounds(adjX, adjY) && isCellEmpty(adjX, adjY)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a position is within the bounds of the board.
     * 
     * @param x The row position.
     * @param y The column position.
     * @return true if within bounds, false otherwise.
     */
    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < boardDimension && y >= 0 && y < boardDimension;
    }

    /**
     * Displays a winner message and restarts the game.
     * 
     * @param winner The name of the winner.
     */
    private void displayWinner(String winner) {
        JOptionPane.showMessageDialog(mainWindow, winner + " wins after " + turnCounter + " turns!");
        this.winner = winner;  // Store winner
        restartGame();
    }

    /**
     * Restarts the game, resetting the turn counter and board state.
     */
    public void restartGame() {
        turnCounter = 0;
        isEscapeeTurn = true;
        initializeBoard();
    }

    /**
     * Retrieves the current winner.
     * 
     * @return The name of the winner.
     */
    public String getWinner() {
        return winner;
    }
    
    public String getButtonText(int x, int y) {return boardButtons[x][y].getText();}
    public Player[] getPursuers() {return pursuers;}
    public JButton getBoardButtons(int x, int y) {return boardButtons[x][y];}
    public int getBoardDimension() {return boardDimension;}
    
}