package edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * GeoMapPoint that extends DefaultWaypoint
 * and displays a data point (GeoMapPoint) on the geomap
 */
public class GeoMapPoint extends DefaultWaypoint {
    private final JButton button;
    private final String text;
    private final ImageIcon icon;

    /**
     * Constructor for GeoMapPoint
     * @param region region code
     * @param value feature value
     * @param coord coordinates of region
     * @param geoIcon shade of the red dot icon
     */
    public GeoMapPoint(String region, long value, GeoPosition coord, GeoIcon geoIcon) {
        super(coord);
        this.text = region+" count: "+value;
        button = new JButton();
        BufferedImage bufferedImage = null;
        try{
            bufferedImage = resizeBufferedImage(ImageIO.read(new File(geoIcon.getIconPath())), geoIcon.getIconSize());

        } catch (IOException e){
            e.printStackTrace();
        }
        icon = new ImageIcon(bufferedImage);
        button.setIcon(icon);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.addMouseListener(new SwingWaypointMouseListener());
        button.setVisible(true);

    }

    /**
     * this method gets the button for geomap point
     * @return button
     */
    public JButton getButton() {
        return button;
    }

    /**
     * This method resizes a BufferedImage according to a given size
     * referenced from https://stackoverflow.com/questions/9417356/bufferedimage-resize
     * @param image original BufferedImage
     * @param newSize new size
     * @return new BufferedImage
     */
    private BufferedImage resizeBufferedImage(BufferedImage image, int newSize){
        Image tmp = image.getScaledInstance( newSize, newSize,  java.awt.Image.SCALE_SMOOTH ) ;
        BufferedImage bufferedImage = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return bufferedImage;
    }

    /**
     * SwingWaypointMouseListener class implements MouseListener
     * to handle mouse interaction
     */
    private class SwingWaypointMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            button.setIcon(null);
            button.setText(button.getText()+text);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setIcon(icon);
            button.setText("");
        }
    }
}