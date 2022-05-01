package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * This class represents the city feature, which is a subclass of Feature.
 */
public class CityFeature extends Feature{

    /**
     * Constructor which takes a segment list as the argument.
     * @param list segment list
     */
    public CityFeature(List<Segment> list) {
        super(list);
    }

    @Override
    public int computeScore(boolean complete, Board board) {
        int basePoint = (complete == true ? 2 : 1);
        List<Segment> list = getSegmentList();
        int score = basePoint * list.size();
        int shieldCount = 0;
        for (Segment s : list) {
            if (board.getTile(s.getX(), s.getY()).getShield()) {
                shieldCount++;
            }
        }
        score += basePoint * shieldCount;
        return score;
    }
}












