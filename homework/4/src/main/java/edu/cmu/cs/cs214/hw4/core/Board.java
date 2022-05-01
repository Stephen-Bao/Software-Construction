package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a game board.
 */
public class Board {

    public static final int NUM_X = 143;
    public static final int NUM_Y = 143;
    public static final int START_X = 71;
    public static final int START_Y = 71;

    private Tile[][] landscape;
    private List<Feature> featureList;

    /**
     * Initiates the board with a starter tile and an initial feature list
     * resulted from the starter tile.
     */
    public Board() {
        featureList = new ArrayList<>();
        landscape = new Tile[NUM_X][NUM_Y];
        Tile startTile = new Tile(FeatureType.FIELD, FeatureType.ROAD, FeatureType.CITY,
                FeatureType.ROAD, List.of(FeatureType.ROAD), false);
        landscape[START_X][START_Y] = startTile;

        Segment s1 = new Segment(START_X, START_Y, List.of(Orientation.RIGHT), 0);
        Segment s2 = new Segment(START_X, START_Y,
                List.of(Orientation.UP, Orientation.DOWN, Orientation.CENTER), 0);
        Feature f1 = new CityFeature(List.of(s1));
        Feature f2 = new RoadFeature(List.of(s2));
        featureList.add(f1);
        featureList.add(f2);
    }

    /**
     * Getter for the feature list with defensive copy.
     * @return feature list
     */
    public List<Feature> getFeatureList() { return new ArrayList<>(featureList); }
    public Tile getTile(int x, int y) { return landscape[x][y]; }

    /**
     * Checks whether a position (x, y) is legal for placing a tile.
     * @param tile tile
     * @param x x position
     * @param y y position
     * @return true or false
     */
    public boolean checkTileLegal(Tile tile, int x, int y) {
        // No existing tile in position
        if (landscape[x][y] != null) {
            return false;
        }
        // Adjacent tiles exist
        if (landscape[x + 1][y] == null && landscape[x - 1][y] == null &&
                landscape[x][y + 1] == null && landscape[x][y - 1] == null) {
            return false;
        }
        // Abutting segments must match
        if (landscape[x + 1][y] != null) {
            if (tile.getRight() != landscape[x + 1][y].getLeft()) {
                return false;
            }
        }
        if (landscape[x - 1][y] != null) {
            if (tile.getLeft() != landscape[x - 1][y].getRight()) {
                return false;
            }
        }
        if (landscape[x][y + 1] != null) {
            if (tile.getUp() != landscape[x][y + 1].getDown()) {
                return false;
            }
        }
        if (landscape[x][y - 1] != null) {
            if (tile.getDown() != landscape[x][y - 1].getUp()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether a position (x, y, orientation) is legal for placing a meeple.
     * @param tile tile
     * @param x x position
     * @param y t position
     * @param position orientation
     * @return true or false
     */
    public boolean checkMeepleLegal(Tile tile, int x, int y, Orientation position) {
        if (position == Orientation.CENTER) {
            if (tile.getCenter().size() > 1 || tile.getCenter().size() == 0 ||
                    tile.getCenter().get(0) == FeatureType.CROSSING) {
                return false;
            }
            // If meeple is placed on a monastery, no need to check
            if (tile.getCenter().get(0) == FeatureType.MONASTERY) {
                return true;
            }
        }
        // Find meeple's segment
        List<Segment> segments = splitTile(tile, x, y);
        Segment segment = null;
        for (Segment s : segments) {
            if (s.getOrientationList().contains(position)) {
                segment = s;
            }
        }
        // If meeple is not on a segment, return false
        if (segment == null) {
            return false;
        }
        // Go over feature list to check meeples
        List<Orientation> orientations = segment.getOrientationList();
        if (orientations.contains(Orientation.LEFT)) {
            if (landscape[x - 1][y] != null) {
                Segment targetSeg = null;
                List<Segment> list = splitTile(landscape[x - 1][y], x - 1, y);
                for (Segment s : list) {
                    if (s.getOrientationList().contains(Orientation.RIGHT)) {
                        targetSeg = s;
                    }
                }
                for (Feature f : featureList) {
                    if (f.getSegmentList().contains(targetSeg) && f.containsMeeple()) {
                        return false;
                    }
                }
            }
        }

        if (orientations.contains(Orientation.UP)) {
            if (landscape[x][y + 1] != null) {
                Segment targetSeg = null;
                List<Segment> list = splitTile(landscape[x][y + 1], x, y + 1);
                for (Segment s : list) {
                    if (s.getOrientationList().contains(Orientation.DOWN)) {
                        targetSeg = s;
                    }
                }
                for (Feature f : featureList) {
                    if (f.getSegmentList().contains(targetSeg) && f.containsMeeple()) {
                        return false;
                    }
                }
            }
        }

        if (orientations.contains(Orientation.RIGHT)) {
            if (landscape[x + 1][y] != null) {
                Segment targetSeg = null;
                List<Segment> list = splitTile(landscape[x + 1][y], x + 1, y);
                for (Segment s : list) {
                    if (s.getOrientationList().contains(Orientation.LEFT)) {
                        targetSeg = s;
                    }
                }
                for (Feature f : featureList) {
                    if (f.getSegmentList().contains(targetSeg) && f.containsMeeple()) {
                        return false;
                    }
                }
            }
        }

        if (orientations.contains(Orientation.DOWN)) {
            if (landscape[x][y - 1] != null) {
                Segment targetSeg = null;
                List<Segment> list = splitTile(landscape[x][y - 1], x, y - 1);
                for (Segment s : list) {
                    if (s.getOrientationList().contains(Orientation.UP)) {
                        targetSeg = s;
                    }
                }
                for (Feature f : featureList) {
                    if (f.getSegmentList().contains(targetSeg) && f.containsMeeple()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Update feature list given a tile is placed in (x, y) position.
     * @param tile tile
     * @param x x position
     * @param y y position
     */
    public void updateFeatures(Tile tile, int x, int y) {
        // New tile contains a monastery
        if (tile.getCenter().contains(FeatureType.MONASTERY)) {
            MonasteryFeature f = new MonasteryFeature(List.of(), x, y);
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (landscape[x + i][y + j] != null) {
                        Segment s = new Segment(x + i, y + j,
                                List.of(Orientation.CENTER), 0);
                        f.addSegment(s);
                    }
                }
            }
            featureList.add(f);

        }
        // Check surroundings for monasteries
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (landscape[x + i][y + j] != null &&
                        landscape[x + i][y + j].getCenter().contains(FeatureType.MONASTERY)) {
                    Segment s = new Segment(x, y, List.of(Orientation.CENTER), 0);
                    for (Feature feature : featureList) {
                        if (feature.getClass() == MonasteryFeature.class) {
                            MonasteryFeature f = (MonasteryFeature)feature;
                            if (f.getCenterX() == x + i && f.getCenterY() == y + j) {
                                feature.addSegment(s);
                            }
                        }
                    }
                }
            }
        }
        // Split tile into independent segments
        List<Segment> segments = splitTile(tile, x, y);
        // Update featureList wrt each segment
        for (Segment seg : segments) {
            List<Orientation> orientations = seg.getOrientationList();
            Set<Feature> mergeSet = new HashSet<>();

            // Update featureList wrt each orientation
            if (orientations.contains(Orientation.LEFT)) {
                if (landscape[x - 1][y] != null) {
                    Segment targetSeg = null;
                    List<Segment> list = splitTile(landscape[x - 1][y], x - 1, y);
                    for (Segment s : list) {
                        if (s.getOrientationList().contains(Orientation.RIGHT)) {
                            targetSeg = s;
                        }
                    }
                    for (Feature f : featureList) {
                        if (f.getSegmentList().contains(targetSeg) &&
                                !f.getSegmentList().contains(seg)) {
                            f.addSegment(seg);
                            mergeSet.add(f);
                        }
                    }
                }
            }

            if (orientations.contains(Orientation.UP)) {
                if (landscape[x][y + 1] != null) {
                    Segment targetSeg = null;
                    List<Segment> list = splitTile(landscape[x][y + 1], x, y + 1);
                    for (Segment s : list) {
                        if (s.getOrientationList().contains(Orientation.DOWN)) {
                            targetSeg = s;
                        }
                    }
                    for (Feature f : featureList) {
                        if (f.getSegmentList().contains(targetSeg)
                                && !f.getSegmentList().contains(seg)) {
                            f.addSegment(seg);
                            mergeSet.add(f);
                        }
                    }
                }
            }

            if (orientations.contains(Orientation.RIGHT)) {
                if (landscape[x + 1][y] != null) {
                    Segment targetSeg = null;
                    List<Segment> list = splitTile(landscape[x + 1][y], x + 1, y);
                    for (Segment s : list) {
                        if (s.getOrientationList().contains(Orientation.LEFT)) {
                            targetSeg = s;
                        }
                    }
                    for (Feature f : featureList) {
                        if (f.getSegmentList().contains(targetSeg)
                                && !f.getSegmentList().contains(seg)) {
                            f.addSegment(seg);
                            mergeSet.add(f);
                        }
                    }
                }
            }

            if (orientations.contains(Orientation.DOWN)) {
                if (landscape[x][y - 1] != null) {
                    Segment targetSeg = null;
                    List<Segment> list = splitTile(landscape[x][y - 1], x, y - 1);
                    for (Segment s : list) {
                        if (s.getOrientationList().contains(Orientation.UP)) {
                            targetSeg = s;
                        }
                    }
                    for (Feature f : featureList) {
                        if (f.getSegmentList().contains(targetSeg)
                                && !f.getSegmentList().contains(seg)) {
                            f.addSegment(seg);
                            mergeSet.add(f);
                        }
                    }
                }
            }
            // Create a new feature if the new segment is not added to feature list
            if (mergeSet.size() == 0) {
                Feature newFeature;
                if (tile.getFeatureTypeByOrientation(orientations.get(0)) == FeatureType.ROAD) {
                    newFeature = new RoadFeature(List.of(seg));
                }
                else if (tile.getFeatureTypeByOrientation(orientations.get(0)) == FeatureType.CITY){
                    newFeature = new CityFeature(List.of(seg));
                }
                else {
                    if (tile.getFeatureTypeByOrientation(orientations.get(1)) == FeatureType.ROAD) {
                        newFeature = new RoadFeature(List.of(seg));
                    }
                    else {
                        newFeature = new CityFeature(List.of(seg));
                    }
                }
                featureList.add(newFeature);
            }
            // Merge features if a segment is added to multiple features
            if (mergeSet.size() > 1) {
                Set<Segment> set = new HashSet<>();
                for (Feature f : mergeSet) {
                    set.addAll(f.getSegmentList());
                    featureList.remove(f);
                }

                Feature newFeature;
                if (tile.getFeatureTypeByOrientation(orientations.get(0)) == FeatureType.ROAD) {
                    newFeature = new RoadFeature(new ArrayList<>(set));
                }
                else if (tile.getFeatureTypeByOrientation(orientations.get(0)) == FeatureType.CITY){
                    newFeature = new CityFeature(new ArrayList<>(set));
                }
                // For the center type has both city and road
                else {
                    if (tile.getFeatureTypeByOrientation(orientations.get(1)) == FeatureType.ROAD) {
                        newFeature = new RoadFeature(new ArrayList<>(set));
                    }
                    else {
                        newFeature = new CityFeature(new ArrayList<>(set));
                    }
                }
                featureList.add(newFeature);
            }
        }

    }

    /**
     * The movement of placing a tile, including checking whether a placement is legal,
     * updating the board and updating the feature list.
     * @param tile tile
     * @param x x position
     * @param y y position
     * @return movement success or not
     */
    public boolean placeTile(Tile tile, int x, int y) {
        if (!checkTileLegal(tile, x, y)) {
            return false;
        }
        // Add tile to landscape
        landscape[x][y] = tile;
        // Update featureSet
        updateFeatures(tile, x, y);

        return true;
    }

    /**
     * The movement of placing a tile with a meeple, including checking whether a placement is legal,
     * checking whether the meeple placement is legal, updating the board and the feature list, and adding
     * the meeple to a feature.
     * @param tile tile
     * @param x x position
     * @param y y position
     * @param meeplePosition orientation for the meeple
     * @param playerId meeple's player id
     * @return success or not
     */
    public boolean placeTileWithMeeple(Tile tile, int x, int y,
                                       Orientation meeplePosition, int playerId) {
        if (!checkTileLegal(tile, x, y)) {
            return false;
        }
        if (!checkMeepleLegal(tile, x, y, meeplePosition)) {
            return false;
        }
        // Add tile to landscape
        landscape[x][y] = tile;
        // Update feature list
        updateFeatures(tile, x, y);
        // If meeple is placed on a monastery
        if (tile.getCenter().contains(FeatureType.MONASTERY)
                && meeplePosition == Orientation.CENTER) {
            for (Feature f : featureList) {
                if (f.getClass() == MonasteryFeature.class) {
                    MonasteryFeature temp = (MonasteryFeature)f;
                    if (temp.getCenterX() == x && temp.getCenterY() == y) {
                        Segment s = new Segment(x, y, List.of(Orientation.CENTER), 0);
                        f.addMeeple(s, playerId);
                    }
                }
            }
            return true;
        }
        // Find meeple's segment
        List<Segment> segments = splitTile(tile, x, y);
        Segment segment = null;
        for (Segment s : segments) {
            if (s.getOrientationList().contains(meeplePosition)) {
                segment = s;
            }
        }
        // Update feature list with a new meeple
        for (Feature f: featureList) {
            if (f.getSegmentList().contains(segment)) {
                f.addMeeple(segment, playerId);
            }
        }

        return true;
    }

    /**
     * A helper method to split a tile into different segments.
     * @param tile tile
     * @param x tile's x position
     * @param y tile's y position
     * @return tile's segment list
     */
    public static List<Segment> splitTile(Tile tile, int x, int y) {
        List<Segment> retList = new ArrayList<>();
        List<Orientation> allOrientations = new ArrayList<>(
                List.of(Orientation.LEFT, Orientation.UP, Orientation.RIGHT, Orientation.DOWN)
        );
        List<FeatureType> centerList = tile.getCenter();

        if (centerList.contains(FeatureType.ROAD)) {
            List<Orientation> roadOrientation = new ArrayList<>();
            roadOrientation.add(Orientation.CENTER);
            if (tile.getLeft() == FeatureType.ROAD) {
                roadOrientation.add(Orientation.LEFT);
                allOrientations.remove(Orientation.LEFT);
            }
            if (tile.getUp() == FeatureType.ROAD) {
                roadOrientation.add(Orientation.UP);
                allOrientations.remove(Orientation.UP);
            }
            if (tile.getRight() == FeatureType.ROAD) {
                roadOrientation.add(Orientation.RIGHT);
                allOrientations.remove(Orientation.RIGHT);
            }
            if (tile.getDown() == FeatureType.ROAD) {
                roadOrientation.add(Orientation.DOWN);
                allOrientations.remove(Orientation.DOWN);
            }
            Segment s = new Segment(x, y, roadOrientation, 0);
            retList.add(s);
        }

        if (centerList.contains(FeatureType.CITY)) {
            List<Orientation> cityOrientation = new ArrayList<>();
            cityOrientation.add(Orientation.CENTER);
            if (tile.getLeft() == FeatureType.CITY) {
                cityOrientation.add(Orientation.LEFT);
                allOrientations.remove(Orientation.LEFT);
            }
            if (tile.getUp() == FeatureType.CITY) {
                cityOrientation.add(Orientation.UP);
                allOrientations.remove(Orientation.UP);
            }
            if (tile.getRight() == FeatureType.CITY) {
                cityOrientation.add(Orientation.RIGHT);
                allOrientations.remove(Orientation.RIGHT);
            }
            if (tile.getDown() == FeatureType.CITY) {
                cityOrientation.add(Orientation.DOWN);
                allOrientations.remove(Orientation.DOWN);
            }
            Segment s = new Segment(x, y, cityOrientation, 0);
            retList.add(s);
        }

        for (Orientation o : allOrientations) {
            if (tile.getFeatureTypeByOrientation(o) != FeatureType.FIELD) {
                Segment s = new Segment(x, y, List.of(o), 0);
                retList.add(s);
            }
        }

        return retList;
    }

    /**
     * Deletes a feature from feature list. Used when a feature is completed.
     * @param f feature that needs to be deleted
     */
    public void deleteFeatureFromList(Feature f) {
        featureList.remove(f);
    }

}












