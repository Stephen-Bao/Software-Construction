package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FeatureTest {

    private Feature myFeature;
    private Board myBoard;

    @Test (expected = NullPointerException.class)
    public void testAddSegment() {
        List<Segment> segments = new ArrayList<>();
        myFeature = new RoadFeature(segments);
        assertEquals(new ArrayList<>(), myFeature.getSegmentList());
        myFeature.addSegment(new Segment(5, 5, List.of(Orientation.CENTER), 0));
        assertEquals(1, myFeature.getSegmentList().size());
        myFeature.addSegment(null);
    }

    @Test
    public void testCheckComplete() {
        myBoard = new Board();
        // Road feature
        List<Segment> segments = new ArrayList<>();
        Segment s1 = new Segment(5, 5,
                List.of(Orientation.UP, Orientation.CENTER, Orientation.DOWN), 0);
        Segment s2 = new Segment(5, 6, List.of(Orientation.DOWN), 0);
        Segment s3 = new Segment(5, 4, List.of(Orientation.UP), 0);
        segments.add(s1);
        segments.add(s2);
        myFeature = new RoadFeature(segments);
        assertFalse(myFeature.checkComplete());
        assertEquals(2, myFeature.computeScore(false, myBoard));
        myFeature.addSegment(s3);
        assertTrue(myFeature.checkComplete());
        assertEquals(3, myFeature.computeScore(true, myBoard));

        // City feature
        segments.clear();
        Segment s4 = new Segment(5, 5,
                List.of(Orientation.RIGHT, Orientation.CENTER, Orientation.DOWN), 0);
        Segment s5 = new Segment(5, 4, List.of(Orientation.UP), 0);
        Segment s6 = new Segment(6, 5, List.of(Orientation.LEFT), 0);
        segments.add(s4);
        segments.add(s5);
        myFeature = new CityFeature(segments);
        assertFalse(myFeature.checkComplete());
        myFeature.addSegment(s6);
        assertTrue(myFeature.checkComplete());

        // Monastery feature
        segments.clear();
        Segment s7 = new Segment(5, 5, List.of(Orientation.CENTER), 1);
        Segment s8 = new Segment(5, 6, List.of(Orientation.CENTER), 0);
        segments.add(s7);
        segments.add(s8);
        myFeature = new MonasteryFeature(segments, 5, 5);
        assertFalse(myFeature.checkComplete());
        assertEquals(2, myFeature.computeScore(false, myBoard));
    }

}













