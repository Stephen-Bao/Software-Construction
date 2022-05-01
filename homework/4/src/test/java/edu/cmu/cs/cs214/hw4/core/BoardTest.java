package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {

    private Board myBoard;

    @Before
    public void setUp() {
        myBoard = new Board();
    }

    @Test
    public void testInitialBoard() {
        for (int i = 0; i < 143; i++) {
            for (int j = 0; j < 143; j++) {
                if (i == 71 && j == 71) {
                    assertNotNull(myBoard.getTile(i, j));
                    continue;
                }
                assertNull(myBoard.getTile(i, j));
            }
        }

//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }
    }

    @Test
    public void testSplitTile() {
        Tile t1 = new Tile(FeatureType.CITY, FeatureType.FIELD,
                FeatureType.CITY, FeatureType.FIELD, List.of(), false);
        List<Segment> segments = Board.splitTile(t1, 5, 5);
//        for (Segment s : segments) {
//            System.out.println(s);
//        }

        Tile t2 = new Tile(FeatureType.CITY, FeatureType.CITY,
                FeatureType.CITY, FeatureType.ROAD, List.of(FeatureType.CITY), true);
        segments = Board.splitTile(t2, 5, 5);
//        for (Segment s : segments) {
//            System.out.println(s);
//        }
    }

    @Test
    public void testPlaceTile() {
        Tile t = new Tile(FeatureType.CITY, FeatureType.FIELD,
                FeatureType.CITY, FeatureType.FIELD, List.of(), false);
        assertFalse(myBoard.placeTile(t, 71, 71));
        assertFalse(myBoard.placeTile(t, 73, 73));
        assertFalse(myBoard.placeTile(t, 71, 70));
        assertFalse(myBoard.placeTile(t, 71, 73));

        assertTrue(myBoard.placeTile(t, 72, 71));
        assertNotNull(myBoard.getTile(72, 71));
    }

    @Test
    public void testFeatureUpdate1() {
        Tile t1 = new Tile(FeatureType.FIELD, FeatureType.FIELD,
                FeatureType.FIELD, FeatureType.ROAD, List.of(FeatureType.MONASTERY), false);
        myBoard.placeTile(t1, 70, 71);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t2 = new Tile(FeatureType.CITY, FeatureType.FIELD,
                FeatureType.CITY, FeatureType.FIELD, List.of(), false);
        myBoard.placeTile(t2, 72, 71);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t3 = new Tile(FeatureType.CITY, FeatureType.ROAD,
                FeatureType.ROAD, FeatureType.FIELD, List.of(FeatureType.ROAD), false);
        myBoard.placeTile(t3, 70, 70);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t4 = new Tile(FeatureType.ROAD, FeatureType.ROAD,
                FeatureType.FIELD, FeatureType.FIELD, List.of(FeatureType.ROAD), false);
        myBoard.placeTile(t4, 71, 70);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t5 = new Tile(FeatureType.ROAD, FeatureType.FIELD,
                FeatureType.ROAD, FeatureType.ROAD, List.of(FeatureType.CROSSING), false);
        myBoard.placeTile(t5, 71, 72);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t6 = new Tile(FeatureType.ROAD, FeatureType.FIELD,
                FeatureType.ROAD, FeatureType.FIELD, List.of(FeatureType.ROAD), false);
        myBoard.placeTile(t6, 72, 72);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }
    }

    @Test
    public void testFeatureUpdate2() {
        Tile t1 = new Tile(FeatureType.FIELD, FeatureType.CITY,
                FeatureType.FIELD, FeatureType.CITY, List.of(FeatureType.CITY), false);
        myBoard.placeTile(t1, 70, 71);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t2 = new Tile(FeatureType.FIELD, FeatureType.FIELD,
                FeatureType.FIELD, FeatureType.CITY, List.of(), false);
        myBoard.placeTile(t2, 70, 72);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t3 = new Tile(FeatureType.CITY, FeatureType.ROAD,
                FeatureType.CITY, FeatureType.CITY, List.of(FeatureType.CITY), true);
        myBoard.placeTile(t3, 71, 70);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t4 = new Tile(FeatureType.FIELD, FeatureType.CITY,
                FeatureType.CITY, FeatureType.FIELD, List.of(FeatureType.CITY), false);
        myBoard.placeTile(t4, 70, 70);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }
    }

    @Test
    public void testPlaceTileWithMeeple() {
        Tile t1 = new Tile(FeatureType.ROAD, FeatureType.ROAD,
                FeatureType.CITY, FeatureType.FIELD, List.of(FeatureType.ROAD), false);
        myBoard.placeTileWithMeeple(t1, 71, 70, Orientation.RIGHT, 1);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t2 = new Tile(FeatureType.CITY, FeatureType.CITY,
                FeatureType.FIELD, FeatureType.FIELD, List.of(FeatureType.CITY), false);
        assertFalse(myBoard.placeTileWithMeeple(t2, 72, 70, Orientation.CENTER, 1));
        myBoard.placeTile(t2, 72, 70);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t3 = new Tile(FeatureType.CITY, FeatureType.FIELD,
                FeatureType.FIELD, FeatureType.CITY, List.of(FeatureType.CITY), false);
        assertFalse(myBoard.placeTileWithMeeple(t3, 72, 71, Orientation.LEFT, 1));
        myBoard.placeTile(t3, 72, 71);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }

        Tile t4 = new Tile(FeatureType.ROAD, FeatureType.ROAD,
                FeatureType.ROAD, FeatureType.FIELD, List.of(FeatureType.CROSSING), false);
        myBoard.placeTileWithMeeple(t4, 70,70, Orientation.RIGHT, 1);
//        for (Feature f : myBoard.getFeatureList()) {
//            System.out.println(f);
//        }
    }

}















