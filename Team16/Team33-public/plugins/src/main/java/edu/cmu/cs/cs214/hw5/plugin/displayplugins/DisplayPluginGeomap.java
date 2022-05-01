package edu.cmu.cs.cs214.hw5.plugin.displayplugins;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.cmu.cs.cs214.hw5.core.Dimension;
import edu.cmu.cs.cs214.hw5.core.DisplayConfig;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.ProcessedData;
import edu.cmu.cs.cs214.hw5.core.ProcessedFeature;
import edu.cmu.cs.cs214.hw5.core.RegionalProcessedData;
import edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper.CountryCoords;
import edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper.GeoMapPoint;
import edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper.GeoScope;
import edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper.GeoIcon;
import edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper.StateCoords;
import edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper.SwingWaypointOverlayPainter;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.PLOT_HEIGHT;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.PLOT_WIDTH;

/**
 * DisplayPluginGeomap class implements DisplayPlugin
 * and provide geomap visualization for data processed by framework
 */
public class DisplayPluginGeomap implements DisplayPlugin {
    private final String name = "DisplayPluginGeomap";
    private final Dimension dimension = Dimension.ONE_DIMENSIONAL;
    
    private final StateCoords stateCoords = new StateCoords();
    private final CountryCoords countryCoords = new CountryCoords();
    private final GeoPosition centerUS = new GeoPosition(40,  7, 0, -99, 41, 0);
    private final GeoPosition centerGLOBAL = new GeoPosition(0,  0, 0, 0, 0, 0);

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * This method returns a JXMapViewer of the appropriate zoom
     * Referenced from jxmapviewer documentation https://github.com/msteiger/jxmapviewer2
     * @param scope scope of the geomap
     * @return JXMapViewer object
     */
    private JXMapViewer getMapViewer(GeoScope scope){

        // create a TileFactoryInfo for OSM
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8);

        // setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        // setup JXMapViewer
        JXMapViewer mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);
        mapViewer.setZoom(scope.getZoom());
        mapViewer.setAddressLocation((scope == GeoScope.US) ? centerUS : centerGLOBAL);
        addMapInteractions(mapViewer);

        return mapViewer;
    }

    /**
     * Add interaction listeners to map
     * @param mapViewer JXMapViewer object
     */
    private void addMapInteractions(JXMapViewer mapViewer) {
        // Add interactions
        MouseInputListener listener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(listener);
        mapViewer.addMouseMotionListener(listener);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));
    }

    /**
     * This method retrieves geo icon enum for plotting dots no geomap
     * @param max max value
     * @param min min value
     * @param count current value
     * @return geo point enum
     */
    private GeoIcon getIcon(long max, long min, long count){
        long mid = (max+min)/2;
        if(count > (max+mid)/2){
            return GeoIcon.LEVEL4;
        } else if(count > mid){
            return GeoIcon.LEVEL3;
        } else if(count > (mid+min)/2){
            return GeoIcon.LEVEL2;
        } else {
            return GeoIcon.LEVEL1;
        }
    }

    @Override
    public boolean display(ProcessedData processedData, DisplayConfig displayConfig) {
        GeoScope scope = GeoScope.valueOf(processedData.getGeoScope().name());
        String featureName = displayConfig.getFeatureSelections().get(0).getSelectedFeatureNames().get(0);
        List<String> regions = displayConfig.getFeatureSelections().get(1).getSelectedFeatureNames();
        try{
            // new map viewer
            JXMapViewer mapViewer = getMapViewer(scope);

            // get geomap points
            List<GeoMapPoint> geoMapPoints = getGeoMapPoints(processedData, scope, featureName, regions);

            // create waypoints from the geo-positions
            Set<GeoMapPoint> waypoints = new HashSet<>(geoMapPoints);
            WaypointPainter<GeoMapPoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
            swingWaypointPainter.setWaypoints(waypoints);
            mapViewer.setOverlayPainter(swingWaypointPainter);

            // add the JButtons to the map
            for (GeoMapPoint w : waypoints) {
                mapViewer.add(w.getButton());
            }

            // display JFrame
            JFrame frame = new JFrame("GeoMap");
            frame.getContentPane().add(mapViewer);
            frame.setSize(PLOT_WIDTH, PLOT_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.setVisible(true);

        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
        return true;
    }

    /**
     * create and add geomap points to a GeoMapPoint list
     * @param processedData processed data from the framework
     * @param scope scope of the geomap
     * @param featureName name of feature
     * @param regions list of all region names to plot
     * @return list of GeoMapPoint
     */
    private List<GeoMapPoint> getGeoMapPoints(ProcessedData processedData, GeoScope scope, String featureName, List<String> regions) {
        // find min max of data in all regions
        long maxCount = 0;
        long minCount = 0;
        for (RegionalProcessedData data : processedData.getRegionalProcessedDataMap().values()){
            long regionSum = data.getFeature(featureName).getValue().stream().mapToLong(Long::intValue).sum();
            maxCount = Math.max(maxCount, regionSum);
            minCount = Math.min(minCount, regionSum);
        }

        // create and add points
        List<GeoMapPoint> geoMapPoints = new ArrayList<>();
        for(String region : regions){
            RegionalProcessedData data = processedData.getRegionalProcessedDataMap().get(region);
            // continue if data is null or region is not in our coordinate resources
            if((data==null) || (scope == GeoScope.GLOBAL && !countryCoords.hasRegion(region)) || (scope == GeoScope.US && !stateCoords.hasRegion(region)))
                continue;

            ProcessedFeature feature = data.getFeature(featureName);
            long sum = feature.getValue().stream().mapToLong(Long::intValue).sum();
            GeoIcon icon = getIcon(maxCount, minCount, sum);

            // add point to geomap point list
            try{
                GeoPosition position;
                switch(scope) {
                    case US:
                        position =  new GeoPosition(stateCoords.getLatDegrees(region), 0, 0, stateCoords.getLonDegrees(region), 0, 0);
                        break;
                    case GLOBAL:
                        default:
                            position =  new GeoPosition(countryCoords.getLatDegrees(region), 0, 0, countryCoords.getLonDegrees(region), 0, 0);
                }
                GeoMapPoint point = new GeoMapPoint(region, sum, position, icon);
                geoMapPoints.add(point);
            } catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Cannot retrieve location for region code: "+region);
            }
        }
        return geoMapPoints;
    }
}
