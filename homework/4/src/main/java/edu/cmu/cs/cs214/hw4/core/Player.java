package edu.cmu.cs.cs214.hw4.core;

/**
 * This class records a player's information during the game.
 */
public class Player {

    public static final int DEFAULT_MEEPLE_NUM = 7;

    private final int id;
    private int score;
    private int meepleNumber;

    /**
     * Constructs a new player with id.
     * @param newId player id
     */
    public Player(int newId) {
        id = newId;
        score = 0;
        meepleNumber = DEFAULT_MEEPLE_NUM;
    }

    public int getId() { return id; }
    public int getScore() { return score; }
    public int getMeepleNumber() { return meepleNumber; }

    public void addScore(int score) { this.score += score; }

    /**
     * Aftering placing a meeple, reduce the player's meeple number by 1.
     */
    public void placeMeeple() {
        if (meepleNumber == 0) {
            throw new UnsupportedOperationException("Meeple number is 0!");
        }
        meepleNumber--;
    }

    /**
     * Returns a given number of meeples back to the player.
     * @param returnNum number of returned meeples
     */
    public void returnMeeple(int returnNum) {
        if (meepleNumber + returnNum > 7) {
            throw new IllegalArgumentException("Meeple number should not be larger than 7!");
        }
        meepleNumber += returnNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[id: ").append(id).append(", score: ").append(score)
                .append(", meeples: ").append(meepleNumber).append("]");

        return sb.toString();
    }

}













