package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player myPlayer;

    @Before
    public void setUp() {
        myPlayer = new Player(1);
    }

    @Test
    public void testAddScore() {
        assertEquals(0, myPlayer.getScore());
        myPlayer.addScore(12);
        assertEquals(12, myPlayer.getScore());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testplaceMeeple() {
        assertEquals(7, myPlayer.getMeepleNumber());
        myPlayer.placeMeeple();
        assertEquals(6, myPlayer.getMeepleNumber());
        for (int i = 0; i < 7; i++) {
            myPlayer.placeMeeple();
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testReturnMeeple() {
        myPlayer.returnMeeple(1);
        myPlayer.placeMeeple();
        myPlayer.placeMeeple();
        myPlayer.returnMeeple(1);
        assertEquals(6, myPlayer.getMeepleNumber());
    }

}













