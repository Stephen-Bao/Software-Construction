package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class SegmentTest {

    private Segment s;

    @Test (expected = IllegalArgumentException.class)
    public void testSegment() {
        s = new Segment(0, 0, List.of(Orientation.LEFT, Orientation.UP), 1);
        // System.out.println(s);
        Segment s1 = new Segment(s);
        // System.out.println(s1);
        assertEquals(s, s1);

        Segment s2 = new Segment(null);
    }

    @Test
    public void testGetOrientaionList() {
        s = new Segment(0, 0, List.of(Orientation.CENTER, Orientation.UP), 0);
        System.out.println(s.getOrientationList());
    }

}









