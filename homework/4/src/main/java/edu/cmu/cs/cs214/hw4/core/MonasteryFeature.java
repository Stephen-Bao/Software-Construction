package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * This class represents the monastery feature, which is a subclass of Feature.
 */
public class MonasteryFeature extends Feature{

    public static final int MONASTERY_COMPLETE_NUM = 9;

    private int centerX;
    private int centerY;

    /**
     * Constructor which takes a segment list and a position as arguments.
     * (x, y) stands for the monastery position.
     * @param list segment list
     * @param x x position
     * @param y y position
     */
    public MonasteryFeature(List<Segment> list, int x, int y) {
        super(list);
        centerX = x;
        centerY = y;
    }

    public int getCenterX() { return centerX; }
    public int getCenterY() { return centerY; }

    @Override
    public boolean checkComplete() {
        return getSegmentList().size() == MONASTERY_COMPLETE_NUM;
    }

    @Override
    public int computeScore(boolean complete, Board board) {
        return getSegmentList().size();
    }
}













