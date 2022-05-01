package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class GameSystemTest {

    private GameSystem game;

    @Before
    public void setUp() {
        game = new GameSystem(4);
    }

    @Test
    public void testSetUp() {
        assertEquals(4, game.getTotalPlayers());
        assertEquals(1, game.getCurrentPlayer());
        // System.out.println(game.getPlayerList());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testDrawTile() {
        Tile t1 = game.drawTile();
        // System.out.println(t1);
        Tile t2 = game.drawTile();
        // System.out.println(t2);

        // Place a tile so that the tile with only city feature can not be placed anywhere
        Tile t3 = new Tile(FeatureType.CITY, FeatureType.FIELD,
                FeatureType.FIELD, FeatureType.FIELD, List.of(), false);
        game.placeNewTile(t3, 72, 71);
        // A message "a tile cannot be placed and is discarded" should output
        for (int i = 0; i < 69; i++) {
            game.drawTile();
        }

        game.drawTile();
    }

    @Test
    public void testPlaceNewTile() {
        Tile t = new Tile(FeatureType.CITY, FeatureType.CITY,
                FeatureType.CITY, FeatureType.CITY, List.of(FeatureType.CITY), true);
        assertFalse(game.placeNewTile(t, 70, 71));
        assertTrue(game.placeNewTile(t, 72, 71));
    }

    @Test
    public void testPlaceNewTileWithMeeple1() {
        // Place one tile a tile and check the score for each player after each round
        Tile t1 = new Tile(FeatureType.CITY, FeatureType.FIELD,
                FeatureType.CITY, FeatureType.FIELD, List.of(FeatureType.CITY), true);
        assertTrue(game.placeNewTileWithMeeple(t1, 72, 71, Orientation.CENTER));
        game.nextRound();
        // System.out.println(game.getPlayerList());

        Tile t2 = new Tile(FeatureType.CITY, FeatureType.FIELD,
                FeatureType.FIELD, FeatureType.CITY, List.of(), false);
        game.placeNewTileWithMeeple(t2, 73, 71, Orientation.DOWN);
        game.nextRound();
        // System.out.println(game.getPlayerList());

        Tile t3 = new Tile(FeatureType.FIELD, FeatureType.FIELD,
                FeatureType.CITY, FeatureType.CITY, List.of(FeatureType.CITY), true);
        game.placeNewTileWithMeeple(t3, 72, 70, Orientation.CENTER);
        game.nextRound();
        // System.out.println(game.getPlayerList());

        Tile t4 = new Tile(FeatureType.CITY, FeatureType.CITY,
                FeatureType.FIELD, FeatureType.FIELD, List.of(FeatureType.CITY), false);
        game.placeNewTile(t4, 73, 70);
        game.nextRound();;
        // System.out.println(game.getPlayerList());

        Tile t5 = new Tile(FeatureType.ROAD, FeatureType.CITY,
                FeatureType.ROAD, FeatureType.FIELD, List.of(FeatureType.CITY), false);
        game.placeNewTileWithMeeple(t5, 72, 69, Orientation.RIGHT);
        // System.out.println(game.getPlayerList());
    }

    @Test
    public void testPlaceNewTileWithMeeple2() {
        Tile t1 = new Tile(FeatureType.CITY, FeatureType.FIELD,
                FeatureType.CITY, FeatureType.FIELD, List.of(FeatureType.CITY), true);
        game.placeNewTileWithMeeple(t1, 72, 71, Orientation.CENTER);
        game.nextRound();

        Tile t2 = new Tile(FeatureType.CITY, FeatureType.FIELD,
                FeatureType.FIELD, FeatureType.FIELD, List.of(), false);
        game.placeNewTile(t2, 73, 71);
        // System.out.println(game.getPlayerList());
    }

    @Test
    public void testComputeFinalScore() {
        Tile t1 = new Tile(FeatureType.CITY, FeatureType.ROAD,
                FeatureType.ROAD, FeatureType.CITY, List.of(FeatureType.CITY, FeatureType.ROAD), true);
        game.placeNewTileWithMeeple(t1, 72, 71, Orientation.LEFT);
        game.nextRound();

        Tile t2 = new Tile(FeatureType.CITY, FeatureType.ROAD,
                FeatureType.CITY, FeatureType.CITY, List.of(FeatureType.CITY), false);
        game.placeNewTileWithMeeple(t2, 71, 70, Orientation.CENTER);
        game.nextRound();
        // System.out.println(game.getPlayerList());

        Tile t3 = new Tile(FeatureType.FIELD, FeatureType.FIELD,
                FeatureType.FIELD, FeatureType.ROAD, List.of(FeatureType.MONASTERY), false);
        game.placeNewTileWithMeeple(t3, 71, 72, Orientation.CENTER);
        game.nextRound();

        Tile t4 = new Tile(FeatureType.FIELD, FeatureType.FIELD,
                FeatureType.ROAD, FeatureType.ROAD, List.of(FeatureType.ROAD), false);
        game.placeNewTileWithMeeple(t4, 72, 72, Orientation.RIGHT);

        Tile t5 = new Tile(FeatureType.CITY, FeatureType.CITY,
                FeatureType.FIELD, FeatureType.FIELD, List.of(), false);
        game.placeNewTile(t5, 72, 70);
        game.nextRound();
        // System.out.println(game.getPlayerList());

        while (game.getDeckSize() != 0) {
            game.drawTile();
        }
        game.computeFinalScore();
        // System.out.println(game.getPlayerList());
    }

    @Test
    public void testFindWinner() {
        Tile t1 = new Tile(FeatureType.FIELD, FeatureType.FIELD,
                FeatureType.FIELD, FeatureType.ROAD, List.of(FeatureType.MONASTERY), false);
        game.placeNewTileWithMeeple(t1, 70, 71, Orientation.CENTER);
        game.nextRound();

        Tile t2 = new Tile(FeatureType.FIELD, FeatureType.ROAD,
                FeatureType.ROAD, FeatureType.CITY, List.of(FeatureType.ROAD), false);
        game.placeNewTileWithMeeple(t2, 70, 70, Orientation.RIGHT);
        game.nextRound();
        // System.out.println(game.getPlayerList());

        Tile t3 = new Tile(FeatureType.ROAD, FeatureType.ROAD,
                FeatureType.CITY, FeatureType.CITY, List.of(FeatureType.ROAD, FeatureType.CITY), true);
        assertFalse(game.placeNewTileWithMeeple(t3, 71, 70, Orientation.CENTER));
        assertTrue(game.placeNewTileWithMeeple(t3, 71, 70, Orientation.RIGHT));
        game.nextRound();

        Tile t4 = new Tile(FeatureType.CITY, FeatureType.FIELD,
                FeatureType.FIELD, FeatureType.CITY, List.of(), false);
        game.placeNewTileWithMeeple(t4, 72, 71, Orientation.LEFT);
        game.nextRound();
        // System.out.println(game.getPlayerList());

        Tile t5 = new Tile(FeatureType.CITY, FeatureType.CITY,
                FeatureType.FIELD, FeatureType.FIELD, List.of(FeatureType.CITY), false);
        game.placeNewTile(t5, 72, 70);
        game.nextRound();

        while (game.getDeckSize() != 0) {
            game.drawTile();
        }
        game.computeFinalScore();
        System.out.println(game.getPlayerList());

        List<Player> winners = game.findWinner();
        System.out.println("The winners are: ");
        System.out.println(winners);
    }
}












