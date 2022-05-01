package edu.cmu.cs.cs214.rec08.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.rec08.core.GameChangeListener;
import static edu.cmu.cs.cs214.rec08.core.TicTacToe.Player;
import edu.cmu.cs.cs214.rec08.core.TicTacToe;

public class GameBoardPanel extends JPanel implements GameChangeListener {
  private final TicTacToe game;
  private final JButton[][] squares;
  private final JLabel currentPlayerLabel;

  public GameBoardPanel(TicTacToe g) {
    game = g;
    game.addGameChangeListener(this);
    currentPlayerLabel = new JLabel();
    squares = new JButton[TicTacToe.GRID_SIZE][TicTacToe.GRID_SIZE];

    setLayout(new BorderLayout());
    add(currentPlayerLabel, BorderLayout.NORTH);
    add(createBoardPanel(), BorderLayout.CENTER);
  }

  private JPanel createBoardPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(TicTacToe.GRID_SIZE, TicTacToe.GRID_SIZE));

    // Create all of the squares and display them.
    for (int row = 0; row < TicTacToe.GRID_SIZE; row++) {
      for (int col = 0; col < TicTacToe.GRID_SIZE; col++) {
        squares[row][col] = new JButton();
        squares[row][col].setText(col + "," + row);

        // We want to access row and col from the lambda.
        // row and col are both non-final variables: the values stored
        // at these locations change as the for-loop executes.
        // Java allows only final variables to be accessed from within
        // a lambda, so we must create new variables r and c.
        int r = row;
        int c = col;
        squares[row][col].addActionListener(e -> {
          game.playMove(r, c);
        });
        panel.add(squares[row][col]);
      }
    }
    return panel;
  }

  @Override
  public void squareChanged(int row, int col) {
    JButton button = squares[row][col];
    Player player = game.getSquare(row, col);
    if (player != null) {
      button.setText(player.toString());
    } else {
      button.setText("   ");
    }
  }

  @Override
  public void currentPlayerChanged(Player player) {
    currentPlayerLabel.setText("Current player: " + player);
  }

  @Override
  public void gameEnded(Player winner) {
    JFrame frame = (JFrame) SwingUtilities.getRoot(this);

    if (winner != null) {
      showDialog(frame, "Winner!", winner + " just won the game!");
    } else {
      showDialog(frame, "Stalemate", "The game has ended in a stalemate.");
    }

    // Append the 'start new game' command to the end of the
    // EventQueue. This is necessary because we need to wait
    // for all of the buttons to finish dispatching before
    // we reset the game's state. (If you are confused about
    // this, try calling 'game.startNewGame()' without the
    // 'invokeLater' and see what happens).
    SwingUtilities.invokeLater(game::startNewGame);
  }

  private static void showDialog(Component component, String title, String message) {
    JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE);
  }
}
