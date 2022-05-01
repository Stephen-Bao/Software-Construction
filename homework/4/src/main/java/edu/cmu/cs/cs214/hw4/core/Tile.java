package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class that represents a tile in Carcassonne game.
 */
public final class Tile {

    private final FeatureType left;
    private final FeatureType up;
    private final FeatureType right;
    private final FeatureType down;
    private final List<FeatureType> center;
    private final boolean shield;

    // Clockwise
    public static final int DEGREE_90 = 1;
    public static final int DEGREE_180 = 2;
    public static final int DEGREE_270 = 3;

    /**
     * Constructor to create a new tile.
     * @param newLeft left feature type
     * @param newUp up feature type
     * @param newRight right feature type
     * @param newDown down feature type
     * @param newCenter center feature type list
     * @param newShield coat-of-arms
     */
    public Tile(FeatureType newLeft, FeatureType newUp, FeatureType newRight,
                FeatureType newDown, List<FeatureType> newCenter, boolean newShield) {
        left = newLeft;
        up = newUp;
        right = newRight;
        down = newDown;
        center = new ArrayList<FeatureType>(Objects.requireNonNull(newCenter));
        shield = newShield;
    }

    public FeatureType getLeft() { return left; }
    public FeatureType getUp() { return up; }
    public FeatureType getRight() { return right; }
    public FeatureType getDown() { return down; }
    public List<FeatureType> getCenter() { return new ArrayList<FeatureType>(center); }
    public boolean getShield() { return shield; }
    protected FeatureType getFeatureTypeByOrientation(Orientation o) {
        switch (o) {
            case LEFT:
                return left;
            case UP:
                return up;
            case RIGHT:
                return right;
            case DOWN:
                return down;
            default:
                return null;
        }
    }

    /**
     * Rotate and return a new tile based on an old one.
     * @param degree rotation degree (can be 90, 180 and 270)
     * @return a new rotated tile
     */
    public Tile rotate(int degree) {
        if (degree != DEGREE_90 && degree != DEGREE_180 && degree != DEGREE_270) {
            System.out.println("Invalid degree number!");
            return null;
        }
        switch (degree) {
            case 1:
                return new Tile(down, left, up, right, center, shield);
            case 2:
                return new Tile(right, down, left, up, center, shield);
            case 3:
                return new Tile(up, right, down, left, center, shield);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append("left:").append(left).append(" up:").append(up).append(" right:")
                .append(right).append(" down:").append(down).append(" center:").append(center)
                .append(" shield:").append(shield).append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tile)) {
            return false;
        }
        Tile t = (Tile)o;
        return left == t.getLeft() && up == t.getUp() && right == t.getRight() && down == t.getDown()
                && center.equals(t.getCenter()) && shield == t.getShield();
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, up, right, down, center, shield);
    }
}









