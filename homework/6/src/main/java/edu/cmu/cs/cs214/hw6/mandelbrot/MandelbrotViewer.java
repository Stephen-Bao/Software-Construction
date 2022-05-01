package edu.cmu.cs.cs214.hw6.mandelbrot;

// CHECKSTYLE:OFF
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.DoubleToIntFunction;
import javax.swing.*;

/**
 * A flexible Mandelbrot viewer whose performance and appearance can be adjusted by providing
 * a renderer and one or more color schemes to the startViewer method. The viewer runs in fullscreen
 *  mode. It zooms in and out in response to the plus and minus keys, and moves around in response
 * to the arrow keys and the mouse. Clicking on a point in the image centers the image on hat point.
 * The period key returns to the initial state, which is the entire "Mandelbrot region."
 * The c key allows the user to cycle through color schemes (if the viewer was created with more
 * than one. The t key toggles the display of the time that it took to render the last image.
 * The escape key terminates the program.
 */
public class MandelbrotViewer extends JFrame {
    /** The renderer. */
    private final MandelbrotRenderer renderer;

    /** All supported color schemes. */
    private final List<DoubleToIntFunction> colorSchemes;

    /** The index of the color scheme currently in use (from 0 to colorSchemes.size()) */
    int colorSchemeIndex;

    /** The last task submitted for rendering (which may or may not have been rendered yet). */
    private Region currentRegion;

    /** The image into which  currentRegion was (or will be) rendered. */
    private final BufferedImage image;

    enum ImageState { RENDERING, RENDERED, PAINTED }

    /** The current state of image. */
    private volatile ImageState imageState = ImageState.PAINTED;

    /** The time to render the last frame. */
    private volatile Duration renderingTime = Duration.ZERO;

    /** Whether to show the rendering time on the screen. */
    private boolean showRenderingTime = false;

    /** An executor that sequentially renders the images that are submitted to it. */
    private final ExecutorService renderService = Executors.newSingleThreadExecutor();

    /**
     * Starts a Mandelbrot viewer with the given renderer that supports the given color scheme(s).
     * The caller must pass at least one color scheme, and the viewer will initially render with
     * that color scheme. Pressing the c key repeatedly cycles through the color schemes.
     *
     * <p>Color schemes are represented by double-to-int functions. They translate escape counts
     * to colors. Escape counts represent the number of iterations required to confirm that a
     * point is not in the Mandelbrot set, or the maximum number iterations to indicate that the
     * point is presumed to be in the set. Doubles are used in preference to ints to allow form
     * "smoothing." The return values are colors in the ARGB color space: the high order byte
     * is the transparency (typically 0xff) and the remaining three bytes red, green, and blue.</p>
     *
     * @param renderer the renderer to be used for the new Mandelbrot viewer
     * @param colorScheme the color scheme(s) support by the viewer
     * @throws IllegalArgumentException if no color scheme is provided
     * @throws NullPointerException if any parameters are null
     */
    public static void startViewer(MandelbrotRenderer renderer, DoubleToIntFunction... colorScheme) {
        Objects.requireNonNull(renderer);
        if (colorScheme.length == 0)
            throw new IllegalArgumentException("You must provide at least one color scheme.");
        SwingUtilities.invokeLater(() -> new MandelbrotViewer(renderer, List.of(colorScheme)));
    }

    private MandelbrotViewer(MandelbrotRenderer renderer, List<DoubleToIntFunction> colorSchemes) {
        this.renderer = renderer;
        this.colorSchemes = colorSchemes;

        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_UP):
                        enqueueRenderTask(currentRegion.up());
                        break;
                    case (KeyEvent.VK_DOWN):
                        enqueueRenderTask(currentRegion.down());
                        break;
                    case (KeyEvent.VK_LEFT):
                        enqueueRenderTask(currentRegion.left());
                        break;
                    case (KeyEvent.VK_RIGHT):
                        enqueueRenderTask(currentRegion.right());
                }
            }

            @Override public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case ('+'):
                        enqueueRenderTask(currentRegion.zoomIn());
                        break;
                    case ('-'):
                        enqueueRenderTask(currentRegion.zoomOut());
                        break;
                    case ('.'):
                        enqueueRenderTask(Region.MANDELBROT_REGION);
                        break;
                    case ('c'):
                        colorSchemeIndex = (colorSchemeIndex + 1) % colorSchemes.size();
                        if (imageState == ImageState.PAINTED)
                            enqueueRenderTask(currentRegion); //  Re-render with new color scheme
                        break;
                    case ('t'):
                        showRenderingTime = !showRenderingTime;
                        repaint();  // Add or remove rendering time from current image
                        break;
                    case (0x1b): // The escape character
                        renderService.shutdownNow();
                        MandelbrotViewer.this.dispose();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                double newX = currentRegion.xMin + currentRegion.width()  * e.getX()/(image.getWidth() - 1);
                double newY = currentRegion.yMin + currentRegion.height() * e.getY()/(image.getHeight() - 1);
                enqueueRenderTask(currentRegion.moveTo(newX, newY));  // Center image at clicked point
            }
        });
        
        setContentPane(new JPanel() {
            @Override public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imageState != ImageState.RENDERING) {
                    g.drawImage(image, 0, 0, this); // Draw image to our graphics context
                    imageState = ImageState.PAINTED;

                    if (showRenderingTime) {
                        g.setColor(Color.RED);
                        g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
                        g.drawString(renderingTime.toMillis() + " ms", 50, 50);
                    }
                }
            }
        });

        setUndecorated(true);
        setResizable(false);
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        enqueueRenderTask(Region.MANDELBROT_REGION);
        setVisible(true);
    }

    /** Submits an image for rendering. */
    private void enqueueRenderTask(Region task) {
        currentRegion = task;
        renderService.execute(() -> {
            // In rare cases, we may have to wait for last frame to get painted
            while (imageState == ImageState.RENDERED) Thread.yield();
            imageState = ImageState.RENDERING;
            Instant startTime = Instant.now();
            renderer.render(task.xMin, task.xMax, task.yMin, task.yMax,
                    image, colorSchemes.get(colorSchemeIndex));
            renderingTime = Duration.between(startTime, Instant.now());
            imageState = ImageState.RENDERED;
            repaint(); // Causes EDT to paint rendered image when paintComponent is called
        });
    }

    /**
     * An an immutable rectangular region on the (complex) plane.
     */
    private static class Region {
        /** The "Mandelbrot region" (which contains substantially all of the Mandelbrot set. */
        static final Region MANDELBROT_REGION = new Region(-2.5, 1, -1, 1);

        /** The x (real) extent of this region. */
        final double xMin, xMax;

        /** The y (imaginary) extent of this region. */
        final double yMin, yMax;

        Region(double xMin, double xMax, double yMin, double yMax) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
        }

        /** Returns a region with the same center as this one, but half the width and height. */
        Region zoomIn() {
            return new Region(xMin + width() / 4, xMax - width() / 4,
                    yMin + height() / 4, yMax - height() / 4);
        }

        /** Returns a region with the same center as this one, but twice the width and height. */
        Region zoomOut() {
            return new Region(xMin - width() / 2, xMax + width() / 2,
                    yMin - height() / 2, yMax + height() / 2);
        }

        /** Returns a region of the same size as this one above it with a 1/4 frame overlap. */
        Region up() {
            return move(0, .75 * (yMin - yMax));
        }

        /** Returns a region of the same size as this one beneath it with 1/4 frame overlap. */
        Region down() {
            return move(0, .75 * (yMax - yMin));
        }

        /** Returns a region of the same size as this one to its left with a 1/4 frame overlap. */
        Region left() {
            return move(.75 * (xMin - xMax), 0);
        }

        /** Returns a region of the same size as this one to its right with a 1/4 frame overlap. */
        Region right() {
            return move(.75 * (xMax - xMin), 0);
        }

        /** Returns a region of the same size as this one, moved right by deltaX & down by deltaY */
        private Region move(double deltaX, double deltaY) {
            return new Region(xMin + deltaX, xMax + deltaX, yMin + deltaY, yMax + deltaY);
        }

        /** Returns a region of the same size as this one centered at the given point. */
        Region moveTo(double xCenter, double yCenter) {
            return new Region(xCenter - width() / 2, xCenter + width() / 2,
                    yCenter - height() / 2, yCenter + height() / 2);
        }

        double width() {
            return xMax - xMin;
        }

        double height() {
            return yMax - yMin;
        }
    }
}
