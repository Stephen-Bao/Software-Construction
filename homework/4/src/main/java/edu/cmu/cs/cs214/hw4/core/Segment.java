package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a segment of a tile on a specific position (x, y) of the game board.
 */
public final class Segment {

    private final int x;
    private final int y;
    private final List<Orientation> orientationList;
    private int meeple;

    /**
     * Constructor for a new segment.
     * @param newX x position
     * @param newY y position
     * @param newOrientationList orientations the segment contains
     * @param newMeeple meeple id of the segment (0 if no meeple exists)
     */
    public Segment(int newX, int newY, List<Orientation> newOrientationList, int newMeeple) {
        x = newX;
        y = newY;
        orientationList = new ArrayList<>(newOrientationList);
        meeple = newMeeple;
    }

    /**
     * Copy constructor.
     * @param s an old segment
     */
    public Segment(Segment s) {
        if (s == null) {
            throw new IllegalArgumentException("Old segment doesn't exist!");
        }
        x = s.getX();
        y = s.getY();
        orientationList = new ArrayList<>(s.getOrientationList());
        meeple = s.getMeeple();
    }

    /**
     * A getter for orientation list with defensive copy.
     * @return orientation list
     */
    public List<Orientation> getOrientationList() {
        return new ArrayList<>(orientationList);
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getMeeple() { return meeple; }
    public void setMeeple(int id) { meeple = id; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("(").append(x).append(",").append(y).append("), ");
        sb.append("(");
        for (int i = 0; i < orientationList.size() - 1; i++) {
            sb.append(orientationList.get(i)).append(",");
        }
        sb.append(orientationList.get(orientationList.size() - 1)).append("), ");
        sb.append(meeple).append("}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Segment)) {
            return false;
        }
        Segment s = (Segment)o;

        if (x == s.getX() && y == s.getY()) {
            for (Orientation orient : orientationList) {
                if (!s.getOrientationList().contains(orient)) {
                    return false;
                }
            }
            for (Orientation orient : s.getOrientationList()) {
                if (!orientationList.contains(orient)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, orientationList, meeple);
    }

}















