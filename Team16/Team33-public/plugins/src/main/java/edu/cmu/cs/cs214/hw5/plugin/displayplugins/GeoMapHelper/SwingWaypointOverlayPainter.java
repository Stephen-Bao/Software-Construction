package edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.JButton;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * "Paints" the Swing waypoints. In fact, just takes care of correct positioning of the representing button.
 * Referenced from jxmapviewer documentation https://github.com/msteiger/jxmapviewer2
 * @author Daniel Stahr
 */
public class SwingWaypointOverlayPainter extends WaypointPainter<GeoMapPoint> {

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer jxMapViewer, int width, int height) {
        for (GeoMapPoint geoMapPoint : getWaypoints()) {
            Point2D point = jxMapViewer.getTileFactory().geoToPixel(
                    geoMapPoint.getPosition(), jxMapViewer.getZoom());
            Rectangle rectangle = jxMapViewer.getViewportBounds();
            int buttonX = (int)(point.getX() - rectangle.getX());
            int buttonY = (int)(point.getY() - rectangle.getY());
            JButton button = geoMapPoint.getButton();
            button.setLocation(buttonX - button.getWidth() / 2, buttonY - button.getHeight() / 2);
        }
    }
}
