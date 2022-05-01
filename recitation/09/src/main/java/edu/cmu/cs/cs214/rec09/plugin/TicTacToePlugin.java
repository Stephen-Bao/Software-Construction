package edu.cmu.cs.cs214.rec09.plugin;

import edu.cmu.cs.cs214.rec09.framework.core.GameFramework;
import edu.cmu.cs.cs214.rec09.framework.core.GamePlugin;

public class TicTacToePlugin implements GamePlugin {

    public static final String GAME_NAME = "Tic Tac Toe";
    public static final int GRID_WIDTH = 3;
    public static final int GRID_HEIGHT = 3;

    private GameFramework framework;
    private final String[][] gameGrid = new String[GRID_WIDTH][GRID_HEIGHT];

    @Override
    public String getGameName() {
        return GAME_NAME;
    }

    @Override
    public int getGridWidth() {
        return GRID_WIDTH;
    }

    @Override
    public int getGridHeight() {
        return GRID_HEIGHT;
    }

    @Override
    public void onRegister(GameFramework framework) {
        this.framework = framework;
    }

    @Override
    public void onNewGame() {
        for (int row = 0; row < GRID_WIDTH; row++) {
            for (int col = 0; col < GRID_HEIGHT; col++) {
                gameGrid[row][col] = null;
            }
        }
    }

    @Override
    public void onNewMove() {

    }

    @Override
    public boolean isMoveValid(int x, int y) {
        return framework.getSquare(x, y) == null;
    }

    @Override
    public boolean isMoveOver() {
        return true;
    }

    @Override
    public void onMovePlayed(int x, int y) {
        gameGrid[x][y] = framework.getCurrentPlayer().getSymbol();
        framework.setSquare(x, y, framework.getCurrentPlayer().getSymbol());
    }

    @Override
    public boolean isGameOver() {

        return hasWon(framework.getCurrentPlayer().getSymbol()) || isFull();

    }

    @Override
    public String getGameOverMessage() {
        return hasWon(framework.getCurrentPlayer().getSymbol()) ?
                framework.getCurrentPlayer().getName() + " won the game!" : "A tie game!";
    }

    @Override
    public void onGameClosed() {

    }

    private boolean hasWon(String player) {
        // Check for a horizontal win.
        for (int row = 0; row < GRID_WIDTH; row++) {
            if (isWin(player, row, 0, 0, 1))
                return true;
        }

        // Check for a vertical win.
        for (int col = 0; col < GRID_HEIGHT; col++) {
            if (isWin(player, 0, col, 1, 0))
                return true;
        }

        // Check for a diagonal win.
        return isWin(player, 0, 0, 1, 1)
                || isWin(player, 0, GRID_WIDTH - 1, 1, -1);
    }

    private boolean isWin(String player, int startRow, int startCol, int rowInc, int colInc) {
        for(int i = 0; i < GRID_WIDTH; i++) {
            if (gameGrid[startRow][startCol] == null || !gameGrid[startRow][startCol].equals(player))
                return false;
            startRow += rowInc;
            startCol += colInc;
        }
        return true;
    }

    private boolean isFull() {
        for (int row = 0; row < GRID_WIDTH; row++) {
            for (int col = 0; col < GRID_HEIGHT; col++) {
                if (gameGrid[row][col] == null) {
                    return false;
                }
            }
        }
        return true;
    }

}










