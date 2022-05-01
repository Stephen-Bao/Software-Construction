package edu.cmu.cs.cs214.rec08;

import javax.swing.*;

import edu.cmu.cs.cs214.rec08.core.TicTacToe;
import edu.cmu.cs.cs214.rec08.core.TicTacToeImpl;
import edu.cmu.cs.cs214.rec08.gui.GameBoardPanel;

public class Main {

    private static final String RECITATION_NAME = "Recitation 8";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGameBoard();
        });
    }

    private static void createAndShowGameBoard() {
        // Create and set-up the window.
        JFrame frame = new JFrame(RECITATION_NAME);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        TicTacToe game = new TicTacToeImpl();

        // Create and set up the content pane.
        GameBoardPanel gamePanel = new GameBoardPanel(game);
        gamePanel.setOpaque(true);
        frame.setContentPane(gamePanel);

        game.startNewGame();

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

}
