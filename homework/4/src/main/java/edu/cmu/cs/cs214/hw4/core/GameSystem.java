package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class acts as a system for the Carcassonne game, which integrates and coordinates
 * different components in the gameplay.
 */
public class GameSystem {

    private final int totalPlayers;
    private int currentPlayerId;
    private final List<Player> playerList;
    private final Deck deck;
    private final Board board;
    private final Random random;
    private final List<GameChangeListener> gameChangeListeners = new ArrayList<>();

    /**
     * Initials the game with a given number of players.
     * @param number number of players
     */
    public GameSystem(int number) {
        this.totalPlayers = number;
        currentPlayerId = 1;
        playerList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            playerList.add(new Player(i + 1));  // ID starts from 1
        }
        deck = new Deck();
        board = new Board();
        random = new Random();
    }

    /**
     * Register an observer to the listener list.
     * @param listener the observer listener
     */
    public void addGameChangeListener(GameChangeListener listener) {
        gameChangeListeners.add(listener);
    }

    public int getTotalPlayers() { return totalPlayers; }
    public int getCurrentPlayer() { return currentPlayerId; }
    public List<Player> getPlayerList() { return new ArrayList<>(playerList); }
    public Tile getTileFromDeck(int index) { return deck.getTileByIndex(index); }
    public Tile getTileFromBoard(int x, int y) { return board.getTile(x, y); }
    public int getDeckSize() { return deck.getSize(); }
    public List<Feature> getFeatureList() { return board.getFeatureList(); }

    /**
     * Draw a tile from the deck. If there's no place legal for the tile, the tile is discarded
     * and a new one is drawn. Repeat until there's a legal one or non tile left in deck.
     * @return drawn tile
     */
    public Tile drawTile() {
        if (deck.getSize() == 0) {
            throw new UnsupportedOperationException("No tile left in stack!");
        }

        int randomIndex = random.nextInt(deck.getSize());
        Tile tile = deck.getTileByIndex(randomIndex);
        deck.removeTileByIndex(randomIndex);

        return tile;
    }

    /**
     * Rotates a tile by a given degree. Clockwise.
     * @param tile tile
     * @param degree degree (ca be 90, 180 or 270)
     * @return rotated tile
     */
    public Tile rotateTile(Tile tile, int degree) {
        return tile.rotate(degree);
    }

    /**
     * Places a tile in position (x, y), then update score and return meeples if needed.
     * @param tile tile
     * @param x x position
     * @param y y position
     * @return success or not
     */
    public boolean placeNewTile(Tile tile, int x, int y) {
        boolean success = board.placeTile(tile, x, y);
        if (!success) {
            return false;
        }
        updateScoreAndMeeple();

        notifyGameBoardChanged(x, y);
        notifyPlayerInfoChanged();
        return true;
    }

    /**
     * Places a tile in position (x, y) with a meeple in a given orientation,
     * then update score and return meeples if needed.
     * @param tile tile
     * @param x x position
     * @param y y position
     * @param orient orientation for meeple placement
     * @return success or not
     */
    public boolean placeNewTileWithMeeple(Tile tile, int x, int y, Orientation orient) {
        boolean success = board.placeTileWithMeeple(tile, x, y, orient, currentPlayerId);
        if (!success) {
            return false;
        }
        reduceMeepleNumber(currentPlayerId);
        updateScoreAndMeeple();

        notifyGameBoardChanged(x, y);
        notifyPlayerInfoChanged();
        return true;
    }

    /**
     * When a player's round is over, continue to next round and update the current player's id.
     */
    public void nextRound() {
        currentPlayerId = currentPlayerId % totalPlayers + 1;

        notifyRoundChanged();
    }

    /**
     * When the game is over, compute all uncompleted feature's score and add them to corresponding players.
     */
    public void computeFinalScore() {
        /*if (deck.getSize() != 0) {
            throw new UnsupportedOperationException("Game is not over. Can not compute final score!");
        }*/
        for (Feature f : board.getFeatureList()) {
            // Count each player's meeple
            boolean hasMeeple = false;
            int[] meepleArray = new int[totalPlayers];
            for (int num : meepleArray) {
                num = 0;
            }
            for (Segment s : f.getSegmentList()) {
                if (s.getMeeple() != 0) {
                    meepleArray[s.getMeeple() - 1]++;
                    hasMeeple = true;
                }
            }
            if (hasMeeple) {
                // Find players with the most meeples
                int score = f.computeScore(false, board);
                int maxMeepleNum = findMax(meepleArray);
                List<Integer> playerIdList = new ArrayList<>();
                for (int i = 0; i < meepleArray.length; i++) {
                    if (meepleArray[i] == maxMeepleNum) {
                        playerIdList.add(i + 1);
                    }
                }
                // Add score to players with most meeples
                for (int id : playerIdList) {
                    addPlayerScore(id, score);
                }
            }
        }
        notifyPlayerInfoChanged();
        notifyGameEnded();
    }

    /**
     * Find the players with the most score when the game ends.
     * @return a list of players with the highest score
     */
    public List<Player> findWinner() {
        int maxScore = 0;
        for (Player p : playerList) {
            if (p.getScore() > maxScore) {
                maxScore = p.getScore();
            }
        }
        List<Player> winners = new ArrayList<>();
        for (Player p : playerList) {
            if (p.getScore() == maxScore) {
                winners.add(p);
            }
        }

        return winners;
    }

    /**
     * A method to check whether there is a possible place for the tile.
     * @param tile tile
     * @return true or false
     */
    public boolean checkPossiblePosition(Tile tile) {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(tile);
        tiles.add(tile.rotate(Tile.DEGREE_90));
        tiles.add(tile.rotate(Tile.DEGREE_180));
        tiles.add(tile.rotate(Tile.DEGREE_270));
        // For every tile on board
        for (int x = 0; x < Board.NUM_X; x++) {
            for (int y = 0; y < Board.NUM_Y; y++) {
                if (board.getTile(x, y) != null) {
                    // Check around positions
                    for (Tile t : tiles) {
                        if (board.checkTileLegal(t, x - 1, y)) {
                            return true;
                        }
                        if (board.checkTileLegal(t, x, y + 1)) {
                            return true;
                        }
                        if (board.checkTileLegal(t, x + 1, y)) {
                            return true;
                        }
                        if (board.checkTileLegal(t, x, y - 1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void updateScoreAndMeeple() {
        List<Feature> fList = board.getFeatureList();
        for (Feature f : fList) {
            if (f.checkComplete()) {
                // Count each player's meeple
                boolean hasMeeple = false;
                int[] meepleArray = new int[totalPlayers];
                for (int num : meepleArray) {
                    num = 0;
                }
                for (Segment s: f.getSegmentList()) {
                    if (s.getMeeple() != 0) {
                        meepleArray[s.getMeeple() - 1]++;
                        hasMeeple = true;
                    }
                }
                if (hasMeeple) {
                    // Find players with the most meeples
                    int score = f.computeScore(true, board);
                    int maxMeepleNum = findMax(meepleArray);
                    List<Integer> playerIdList = new ArrayList<>();
                    for (int i = 0; i < meepleArray.length; i++) {
                        if (meepleArray[i] == maxMeepleNum) {
                            playerIdList.add(i + 1);
                        }
                    }
                    // Add score to players with most meeples
                    for (int id : playerIdList) {
                        addPlayerScore(id, score);
                    }
                    // Return each meeple to player
                    for (int i = 0; i < meepleArray.length; i++) {
                        playerList.get(i).returnMeeple(meepleArray[i]);
                    }
                }
                // Delete feature from feature list
                board.deleteFeatureFromList(f);

                notifyFeatureCompleted(f);
            }
        }
    }

    private void reduceMeepleNumber(int id) {
        playerList.get(id - 1).placeMeeple();
    }

    private void addPlayerScore(int playerId, int score) {
        playerList.get(playerId - 1).addScore(score);
    }

    private int findMax(int[] arr) {
        int max = arr[0];
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    private void notifyPlayerInfoChanged() {
        for (GameChangeListener listener : gameChangeListeners) {
            listener.playerInfoChanged();
        }
    }

    private void notifyGameBoardChanged(int x, int y) {
        for (GameChangeListener listener : gameChangeListeners) {
            listener.gameBoardChanged(x, y);
        }
    }

    private void notifyRoundChanged() {
        for (GameChangeListener listener : gameChangeListeners) {
            listener.roundChanged();
        }
    }

    private void notifyFeatureCompleted(Feature f) {
        for (GameChangeListener listener : gameChangeListeners) {
            listener.featureCompleted(f);
        }
    }

    private void notifyGameEnded() {
        for (GameChangeListener listener : gameChangeListeners) {
            listener.gameEnded();
        }
    }

}
















