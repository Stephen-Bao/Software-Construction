package edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper;

import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.ICON_LEVEL1_PATH;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.ICON_LEVEL1_SIZE;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.ICON_LEVEL2_PATH;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.ICON_LEVEL2_SIZE;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.ICON_LEVEL3_PATH;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.ICON_LEVEL3_SIZE;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.ICON_LEVEL4_PATH;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.ICON_LEVEL4_SIZE;

/**
 * RedLevel enum for different shades for the geomap icon
 */
public enum GeoIcon {
    LEVEL1(ICON_LEVEL1_PATH, ICON_LEVEL1_SIZE),
    LEVEL2(ICON_LEVEL2_PATH, ICON_LEVEL2_SIZE),
    LEVEL3(ICON_LEVEL3_PATH, ICON_LEVEL3_SIZE),
    LEVEL4(ICON_LEVEL4_PATH, ICON_LEVEL4_SIZE);

    private String iconPath;
    private int iconSize;

    GeoIcon(String path, int size){
        this.iconPath = path;
        this.iconSize = size;
    }

    /**
     * retrieves the path for the icon
     * @return icon path
     */
    public String getIconPath() {
        return iconPath;
    }

    /**
     * retrieves the size for the icon
     * @return icon size
     */
    public int getIconSize() {
        return iconSize;
    }
}
