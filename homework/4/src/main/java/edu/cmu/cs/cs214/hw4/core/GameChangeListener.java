package edu.cmu.cs.cs214.hw4.core;

/**
 * The observer to be registered to game system.
 */
public interface GameChangeListener {

    /**
     * Invoked when player's info has changed.
     */
    void playerInfoChanged();

    /**
     * Invoked when the game board has changed.
     * @param x x position
     * @param y y position
     */
    void gameBoardChanged(int x, int y);

    /**
     * Invoked when the game goes into next round.
     */
    void roundChanged();

    /**
     * Invoked when a feature is completed.
     * @param f feature
     */
    void featureCompleted(Feature f);

    /**
     * Invoked when the game is ended.
     */
    void gameEnded();

}











