package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TileTest {

    private Tile myTile;

    @Test
    public void testTile() {
        myTile = new Tile(FeatureType.FIELD, FeatureType.FIELD, FeatureType.CITY,
                FeatureType.CITY, new ArrayList<FeatureType>(), false);
        System.out.println(myTile);

        Tile newTile = new Tile(FeatureType.FIELD, FeatureType.FIELD, FeatureType.CITY,
                FeatureType.CITY, List.of(FeatureType.CITY), false);
        System.out.println(newTile);

        assertFalse(myTile.equals(newTile));
    }

    @Test (expected = NullPointerException.class)
    public void testIllegalInput() {
        myTile = new Tile(FeatureType.FIELD, FeatureType.FIELD, FeatureType.CITY,
                FeatureType.CITY, null, false);
    }

    @Test
    public void testRotate() {
        myTile = new Tile(FeatureType.FIELD, FeatureType.FIELD, FeatureType.CITY,
                FeatureType.CITY, new ArrayList<FeatureType>(), false);
        Tile rotateTile1 = myTile.rotate(Tile.DEGREE_90);
        Tile expectedTile1 = new Tile(FeatureType.CITY, FeatureType.FIELD, FeatureType.FIELD,
                FeatureType.CITY, new ArrayList<FeatureType>(), false);
        assertEquals(rotateTile1, expectedTile1);  // assertEquals() invoke equals() method

        Tile rotateTile2 = myTile.rotate(Tile.DEGREE_180);
        Tile expectedTile2 = new Tile(FeatureType.CITY, FeatureType.CITY, FeatureType.FIELD,
                FeatureType.FIELD, new ArrayList<FeatureType>(), false);
        assertEquals(rotateTile2, expectedTile2);
    }

}












