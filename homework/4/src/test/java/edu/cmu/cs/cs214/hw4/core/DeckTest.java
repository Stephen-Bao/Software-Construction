package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DeckTest {

    private Deck myDeck;

    @Before
    public void setUp() {
        myDeck = new Deck();
    }

    @Test
    public void testSize() {
        assertTrue(myDeck.getSize() == 71);
    }

    @Test
    public void testGetTileByIndex() {
        Tile t1 = myDeck.getTileByIndex(0);
        assertTrue(t1.equals(new Tile(FeatureType.FIELD, FeatureType.FIELD, FeatureType.FIELD,
                FeatureType.ROAD, List.of(FeatureType.MONASTERY), false)));

        Tile t2 = myDeck.getTileByIndex(20);
        assertTrue(t2.equals(new Tile(FeatureType.CITY, FeatureType.FIELD, FeatureType.CITY,
                FeatureType.FIELD, List.of(), false)));

        Tile t3 = myDeck.getTileByIndex(70);
        assertTrue(t3.equals(new Tile(FeatureType.ROAD, FeatureType.ROAD, FeatureType.ROAD,
                FeatureType.ROAD, List.of(FeatureType.CROSSING), false)));
    }

    @Test
    public void testDraw() {
        Tile t = myDeck.draw();
        System.out.println(t);
        assertTrue(myDeck.getSize() == 70);

        for (int i = 0; i < 70; i++) {
            myDeck.draw();
        }
        assertEquals(myDeck.getSize(), 0);
        assertTrue(myDeck.getTiles().equals(List.of()));

        assertNull(myDeck.draw());
    }

}














