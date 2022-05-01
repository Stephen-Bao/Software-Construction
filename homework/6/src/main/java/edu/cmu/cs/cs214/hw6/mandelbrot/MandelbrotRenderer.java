package edu.cmu.cs.cs214.hw6.mandelbrot;

import java.awt.image.BufferedImage;
import java.util.function.DoubleToIntFunction;

/**
 * A renderer for Mandelbrot images.
 */
public interface MandelbrotRenderer {
    /**
     * Renders the indicated region of the Mandelbrot set into the given image with the given
     * color scheme. The number of horizontal pixels in the image is image.width(), and the number
     * of vertical pixels in the image is image.height(). If the aspect ratios of the region and
     * the image do not match, the resulting image will appear stretched.
     *
     * @param xMin The minimum x (real) value
     * @param xMax The maximum x (real) value
     * @param yMin The minimum y (complex) value
     * @param yMax The maximum y (complex) value
     * @param image The image
     * @param colorScheme The color scheme (as described in {@link MandelbrotViewer#startViewer}).
     */
    void render(double xMin, double xMax, double yMin, double yMax,
                BufferedImage image, DoubleToIntFunction colorScheme);
}
