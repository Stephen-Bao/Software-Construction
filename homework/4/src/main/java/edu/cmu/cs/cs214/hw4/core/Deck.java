package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class representing a tile stack in Carcassonne game.
 */
public class Deck {

    private List<Tile> tiles;
    private Random random;

    /**
     * Constructor that creates a tile stack containing the initial tiles when a game starts.
     */
    public Deck() {
        tiles = new ArrayList<>();
        random = new Random();

        // A
        addTiles(2, FeatureType.FIELD, FeatureType.FIELD, FeatureType.FIELD, FeatureType.ROAD,
                List.of(FeatureType.MONASTERY), false);
        // B
        addTiles(4, FeatureType.FIELD, FeatureType.FIELD, FeatureType.FIELD, FeatureType.FIELD,
                List.of(FeatureType.MONASTERY), false);
        // C
        addTiles(1, FeatureType.CITY, FeatureType.CITY, FeatureType.CITY, FeatureType.CITY,
                List.of(FeatureType.CITY), true);
        // D
        addTiles(3, FeatureType.FIELD, FeatureType.ROAD, FeatureType.CITY, FeatureType.ROAD,
                List.of(FeatureType.ROAD), false);
        // E
        addTiles(5, FeatureType.FIELD, FeatureType.CITY, FeatureType.FIELD, FeatureType.FIELD,
                List.of(), false);
        // F
        addTiles(2, FeatureType.CITY, FeatureType.FIELD, FeatureType.CITY, FeatureType.FIELD,
                List.of(FeatureType.CITY), true);
        // G
        addTiles(1, FeatureType.FIELD, FeatureType.CITY, FeatureType.FIELD, FeatureType.CITY,
                List.of(FeatureType.CITY), false);
        // H
        addTiles(3, FeatureType.CITY, FeatureType.FIELD, FeatureType.CITY, FeatureType.FIELD,
                List.of(), false);
        // I
        addTiles(2, FeatureType.FIELD, FeatureType.FIELD, FeatureType.CITY, FeatureType.CITY,
                List.of(), false);
        // J
        addTiles(3, FeatureType.FIELD, FeatureType.CITY, FeatureType.ROAD, FeatureType.ROAD,
                List.of(FeatureType.ROAD), false);
        // K
        addTiles(3, FeatureType.ROAD, FeatureType.ROAD, FeatureType.CITY, FeatureType.FIELD,
                List.of(FeatureType.ROAD), false);
        // L
        addTiles(3, FeatureType.ROAD, FeatureType.ROAD, FeatureType.CITY, FeatureType.ROAD,
                List.of(FeatureType.CROSSING), false);
        // M
        addTiles(2, FeatureType.CITY, FeatureType.CITY, FeatureType.FIELD, FeatureType.FIELD,
                List.of(FeatureType.CITY), true);
        // N
        addTiles(3, FeatureType.CITY, FeatureType.CITY, FeatureType.FIELD, FeatureType.FIELD,
                List.of(FeatureType.CITY), false);
        // O
        addTiles(2, FeatureType.CITY, FeatureType.CITY, FeatureType.ROAD, FeatureType.ROAD,
                List.of(FeatureType.CITY, FeatureType.ROAD), true);
        // P
        addTiles(3, FeatureType.CITY, FeatureType.CITY, FeatureType.ROAD, FeatureType.ROAD,
                List.of(FeatureType.CITY, FeatureType.ROAD), false);
        // Q
        addTiles(1, FeatureType.CITY, FeatureType.CITY, FeatureType.CITY, FeatureType.FIELD,
                List.of(FeatureType.CITY), true);
        // R
        addTiles(3, FeatureType.CITY, FeatureType.CITY, FeatureType.CITY, FeatureType.FIELD,
                List.of(FeatureType.CITY), false);
        // S
        addTiles(2, FeatureType.CITY, FeatureType.CITY, FeatureType.CITY, FeatureType.ROAD,
                List.of(FeatureType.CITY), true);
        // T
        addTiles(1, FeatureType.CITY, FeatureType.CITY, FeatureType.CITY, FeatureType.ROAD,
                List.of(FeatureType.CITY), false);
        // U
        addTiles(8, FeatureType.FIELD, FeatureType.ROAD, FeatureType.FIELD, FeatureType.ROAD,
                List.of(FeatureType.ROAD), false);
        // V
        addTiles(9, FeatureType.ROAD, FeatureType.FIELD, FeatureType.FIELD, FeatureType.ROAD,
                List.of(FeatureType.ROAD), false);
        // W
        addTiles(4, FeatureType.ROAD, FeatureType.FIELD, FeatureType.ROAD, FeatureType.ROAD,
                List.of(FeatureType.CROSSING), false);
        // X
        addTiles(1, FeatureType.ROAD, FeatureType.ROAD, FeatureType.ROAD, FeatureType.ROAD,
                List.of(FeatureType.CROSSING), false);

    }

    public List<Tile> getTiles() { return new ArrayList<>(tiles); }
    public int getSize() { return tiles.size(); }
    protected Tile getTileByIndex(int index) {
        Tile t = tiles.get(index);
        return new Tile(t.getLeft(), t.getUp(), t.getRight(), t.getDown(),
                t.getCenter(), t.getShield());
    }
    protected void removeTileByIndex(int index) {
        tiles.remove(index);
    }

    /**
     * Randomly draw a tile from deck.
     * @return a random tile from deck
     */
    public Tile draw() {
        if (tiles.size() == 0) {
            System.out.println("No tile left in stack!");
            return null;
        }
        int randomIndex = random.nextInt(tiles.size());
        Tile ret = tiles.get(randomIndex);
        tiles.remove(randomIndex);
        return ret;
    }

    private void addTiles(int number, FeatureType left, FeatureType up, FeatureType right,
                          FeatureType down, List<FeatureType> center, boolean shield) {
        for (int i = 0; i < number; i++) {
            tiles.add(new Tile(left, up, right, down, center, shield));
        }
    }

}















