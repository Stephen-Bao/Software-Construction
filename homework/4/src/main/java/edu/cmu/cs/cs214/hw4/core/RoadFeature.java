package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * This class represents the road feature, which is a subclass of Feature.
 */
public class RoadFeature extends Feature{

    /**
     * Constructor which takes a segment list as the argument.
     * @param list segment list
     */
    RoadFeature(List<Segment> list) {
        super(list);
    }

    @Override
    public int computeScore(boolean complete, Board board) {
        return getSegmentList().size();
    }

}







