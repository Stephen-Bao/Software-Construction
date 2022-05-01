package edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper;

import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.GLOBAL_ZOOM;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.OTHER_ZOOM;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.US_ZOOM;

/**
 * MapZoom enum, which stores zoom value for geomap
 */
public enum GeoScope {
    GLOBAL(GLOBAL_ZOOM),
    US(US_ZOOM),
    OTHER(OTHER_ZOOM);

    private final int zoom;

    GeoScope(int zoom){
        this.zoom = zoom;
    }

    /**
     * return the zoom value for a map scale
     * @return zoom value
     */
    public int getZoom() {
        return zoom;
    }

}
