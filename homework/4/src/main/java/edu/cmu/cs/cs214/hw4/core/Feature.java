package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This abstract represents a general feature and defines some necessary attributes and operations.
 */
public abstract class Feature {

    private List<Segment> segmentList;

    /**
     * Constructor.
     * @param list a list of segments
     */
    public Feature(List<Segment> list) {
        segmentList = new ArrayList<>(list);
    }

    /**
     * Getter for the segment list with defensive copy.
     * @return segment list
     */
    public List<Segment> getSegmentList() {
        return new ArrayList<>(segmentList);
    }

    /**
     * Add a new segment into feature.
     * @param seg the new segment
     */
    public void addSegment(Segment seg) {
        segmentList.add(new Segment(Objects.requireNonNull(seg)));
    }

    /**
     * Check whether the feature is complete.
     * @return true or false
     */
    public boolean checkComplete() {
        // A feature is complete if non of its segment can be extended.
        for (Segment seg : segmentList) {
            List<Orientation> orientationList = seg.getOrientationList();
            for (Orientation orient : orientationList) {
                switch (orient) {
                    case LEFT:
                        if (!containsTile(seg.getX() - 1, seg.getY())) {
                            return false;
                        }
                        break;
                    case UP:
                        if (!containsTile(seg.getX(), seg.getY() + 1)) {
                            return false;
                        }
                        break;
                    case RIGHT:
                        if (!containsTile(seg.getX() + 1, seg.getY())) {
                            return false;
                        }
                        break;
                    case DOWN:
                        if (!containsTile(seg.getX(), seg.getY() - 1)) {
                            return false;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }

    /**
     * An abstract method to compute score of the feature. The score is dependent on whether the feature
     * is completed or not.
     * @param complete indicates whether the feature is completed
     * @param board game board
     * @return score
     */
    public abstract int computeScore(boolean complete, Board board);

    /**
     * Add a meeple into a specific segment in the feature.
     * @param seg segment
     * @param id meeple id
     */
    public void addMeeple(Segment seg, int id) {
        for (Segment s : segmentList) {
            if (s.equals(seg)) {
                s.setMeeple(id);
            }
        }
    }

    /**
     * Check whether the feature contains a meeple (in order to decide whether to compute score).
     * @return true or false
     */
    public boolean containsMeeple() {
        boolean hasMeeple = false;
        for (Segment s : segmentList) {
            if (s.getMeeple() != 0) {
                hasMeeple = true;
                break;
            }
        }
        return hasMeeple;
    }

    private boolean containsTile(int x, int y) {
        for (Segment seg : segmentList) {
            if (seg.getX() == x && seg.getY() == y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segmentList.size() - 1; i++) {
            sb.append(segmentList.get(i)).append(", ");
        }
        sb.append(segmentList.get(segmentList.size() - 1));

        return sb.toString();
    }

}










