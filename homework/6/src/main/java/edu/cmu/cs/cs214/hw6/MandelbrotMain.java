package edu.cmu.cs.cs214.hw6;

import edu.cmu.cs.cs214.hw6.mandelbrot.MandelbrotRenderer;
import edu.cmu.cs.cs214.hw6.mandelbrot.MandelbrotViewer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleToIntFunction;

/**
 * Mandelbrot browser implemented atop the framework in edu.cmu.cs.cs214.hw6.mandelbrot.
 * As distributed this includes only a sequential renderer.
 */
public class MandelbrotMain {
    /**
     * The maximum number of iterations to consider in determining whether a point is a member
     * of the Mandelbrot set. This controls a tradeoff between speed and image detail. Do not
     * change this constant unless your machine really can't render images with this value, in
     * which case, please talk to course staff before changing it.
     */
    private static final int MAX_ITERATIONS = 1000;

    private static final int STEP_SIZE = 10;

    /**
     * A purely sequential Mandelbrot renderer. The performance of this renderer provides a
     * baseline for you to improve on with parallelism. Ideally you'll be able to achieve
     * linear speedup in the number of cores on your computer.
     */
    static final MandelbrotRenderer SEQUENTIAL_RENDERER = new MandelbrotRenderer() {
        public void render(double xMin, double xMax, double yMin, double yMax,
                           BufferedImage image, DoubleToIntFunction colorScheme) {
            int widthInPixels = image.getWidth();
            int heightInPixels = image.getHeight();
            double deltaX = (xMax - xMin) / (widthInPixels - 1);
            double deltaY = (yMax - yMin) / (heightInPixels - 1);

            // Iterate over each pixel, compute escape count for corresponding point, & set pixel color
            for (int yPixel = 0; yPixel < heightInPixels; yPixel++) {
                double y = yMin + yPixel * deltaY;
                for (int xPixel = 0; xPixel < widthInPixels; xPixel++) {
                    double x = xMin + xPixel * deltaX;
                    image.setRGB(xPixel, yPixel, colorScheme.applyAsInt(escapeCount(x, y)));
                }
            }
        }
    };

    /**
     * A parallel Mandelbrot renderer using executors. The adopted strategy is to wrap the
     * computation of each row (yPixel) as a task and submit them to the thread pool.
     * There is also another version which enables to adjust the number of rows to be wrapped.
     */
    static final MandelbrotRenderer EXECUTORS_RENDERER = new MandelbrotRenderer() {
        @Override
        public void render(double xMin, double xMax, double yMin, double yMax,
                           BufferedImage image, DoubleToIntFunction colorScheme) {

            ExecutorService threadPool = Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors());

            int widthInPixels = image.getWidth();
            int heightInPixels = image.getHeight();
            double deltaX = (xMax - xMin) / (widthInPixels - 1);
            double deltaY = (yMax - yMin) / (heightInPixels - 1);

            // This strategy wraps the computation of each row pixels as a task
            for (int yPixel = 0; yPixel < heightInPixels; yPixel++) {
                double y = yMin + yPixel * deltaY;
                int yPixelCurrent = yPixel;

                threadPool.submit(() -> {
                    for (int xPixel = 0; xPixel < widthInPixels; xPixel++) {
                        double x = xMin + xPixel * deltaX;
                        image.setRGB(xPixel, yPixelCurrent, colorScheme.applyAsInt(escapeCount(x, y)));
                    }
                });
            }

            // This strategy wraps the comptation of STEP_SIZE rows as a task
            // Not as fast as the first one, which is equivalent to STEP_SIZE = 1 in this case
            /*int yPixel;
            for (yPixel = 0; yPixel < heightInPixels; yPixel += STEP_SIZE) {
                int yPixelStart = yPixel;
                int yPixelEnd = yPixel + STEP_SIZE;
                threadPool.submit(() -> {
                    for (int yPixelCurrent = yPixelStart; yPixelCurrent < yPixelEnd; yPixelCurrent++) {
                        double y = yMin + yPixelCurrent * deltaY;
                        for (int xPixel = 0; xPixel < widthInPixels; xPixel++) {
                            double x = xMin + xPixel * deltaX;
                            image.setRGB(xPixel, yPixelCurrent, colorScheme.applyAsInt(escapeCount(x, y)));
                        }
                    }
                });
            }

            int yPixelValue = yPixel - STEP_SIZE;
            threadPool.submit(() -> {
                for (int yPixelCurrent = yPixelValue; yPixelCurrent < heightInPixels; yPixelCurrent++) {
                    double y = yMin + yPixelCurrent * deltaY;
                    for (int xPixel = 0; xPixel < widthInPixels; xPixel++) {
                        double x = xMin + xPixel * deltaX;
                        image.setRGB(xPixel, yPixelCurrent, colorScheme.applyAsInt(escapeCount(x, y)));
                    }
                }
            });*/

            threadPool.shutdown();
            try {
                threadPool.awaitTermination(1, TimeUnit.MINUTES);
            }
            catch (InterruptedException e) {
                // Do nothing
            }
        }
    };

    /**
     * A parallel Mandelbrot renderer using streams. The adopted one is to apply parallel
     * stream on a list of y pixels. There's also a version of applying parallel stream on
     * all pixels, but it is slower than the first one.
     */
    static final MandelbrotRenderer STREAM_RENDERER = new MandelbrotRenderer() {
        @Override
        public void render(double xMin, double xMax, double yMin, double yMax,
                           BufferedImage image, DoubleToIntFunction colorScheme) {
            int widthInPixels = image.getWidth();
            int heightInPixels = image.getHeight();
            double deltaX = (xMax - xMin) / (widthInPixels - 1);
            double deltaY = (yMax - yMin) / (heightInPixels - 1);

            // This strategy uses parallel stream on a list of y pixels
            List<Integer> yPixelList = new ArrayList<>();
            for (int yPixel = 0; yPixel < heightInPixels; yPixel++) {
                yPixelList.add(yPixel);
            }

            yPixelList.parallelStream().forEach((yPixel) -> {
                double y = yMin + yPixel * deltaY;
                for (int xPixel = 0; xPixel < widthInPixels; xPixel++) {
                    double x = xMin + xPixel * deltaX;
                    image.setRGB(xPixel, yPixel, colorScheme.applyAsInt(escapeCount(x, y)));
                }
            });

            // This strategy uses parallel stream on a list of all possible pixels
            // Not as fast as the first one on my machine
            /*List<Pair> pixelList = new ArrayList<>(widthInPixels * heightInPixels);
            for (int yPixel = 0; yPixel < heightInPixels; yPixel++) {
                for (int xPixel = 0; xPixel < widthInPixels; xPixel++) {
                    pixelList.add(new Pair(xPixel, yPixel));
                }
            }
            pixelList.parallelStream().forEach((pixelPair) -> {
                double x = xMin + pixelPair.getXPixel() * deltaX;
                double y = yMin + pixelPair.getYPixel() * deltaY;
                image.setRGB(pixelPair.getXPixel(), pixelPair.getYPixel(), colorScheme.applyAsInt(escapeCount(x, y)));
            });*/
        }
    };

    // Bailout radius of 2^8 (rather than 2) is necessary for smoothing to work
    private static final int BAILOUT_RADIUS = 2 ^ 8;

    /**
     * Returns the number of iterations required to determine that the point (x0, y0) is not
     * in the Mandelbrot set, or MAX_ITERATIONS if no escape is observed (in which case the point
     * is assumed to be in the Mandelbrot set).
     *
     * The returned value is not an integer because it is "smoothed" by the standard algorithm
     * ("normalized iteration count") to reduce banding in the final image. See:
     * wikipedia.org/wiki/Plotting_algorithms_for_the_Mandelbrot_set#Continuous_(smooth)_coloring
     *
     * @param x0 the x value (i.e., real part) of the point to investigate
     * @param y0 the y value (i.e., imaginary part) of the point to investigate
     * @return the escape count for the point, or MAX_ITERATIONS if no escape observed
     */
    static double escapeCount(double x0, double y0) {
        int iteration = 0;
        double x = 0, y = 0;

        // Optimization: check if we're in cardioid or first bulb. If so, we're in Mandelbrot set
        double q = (x0 - 0.25) * (x0 - 0.25) + y0 * y0;
        if (q * (q + (x0 - 0.25)) <= 0.25 * y0 * y0 || (x0 + 1) * (x0 + 1) + y0 * y0 <= 0.0625) {
            return  MAX_ITERATIONS;
        }

        while (x * x + y * y <= BAILOUT_RADIUS * BAILOUT_RADIUS && iteration < MAX_ITERATIONS) {
            double nextX = x * x - y * y + x0;
            y = 2 * x * y + y0;
            x = nextX;
            iteration++;
        }

        return iteration == MAX_ITERATIONS ? iteration :
                iteration + 1 - Math.log(Math.log(Math.hypot(x, y))) / LOG2;
    }

    private static final double LOG2 = Math.log(2);

    /** Coloring function: www.khanacademy.org/computer-programming/mandelbrot-set/1274517860 */
    static int colorForEscapeCount(double escapeCount) {
        if (escapeCount == MAX_ITERATIONS) return 0xff000000;
        double greenFactor  = escapeCount / 80.0;
        double purpleFactor = escapeCount / 24.0;
        double yellowHighlight1 = (escapeCount - 25.0) / 10;
        double yellowHighlight2 = 45 / (yellowHighlight1 * yellowHighlight1);
        int redLevel   = (int) (0xff * purpleFactor + yellowHighlight2);
        int greenLevel = (int) (0xff * greenFactor  + yellowHighlight2);
        int blueLevel  = (int) (0xff * purpleFactor + 45 - yellowHighlight2);
        return 0xff << 24 | redLevel << 16 | greenLevel << 8 | blueLevel;
    }

    /** Gray-level coloring function: sqrt(escape fraction). */
    static int grayLevelForEscapeCount(double escapeCount) {
        int grayLevel = (int) Math.round(Math.sqrt(escapeCount / MAX_ITERATIONS) * 0xFF);
        return 0xFF000000 | (grayLevel << 16) | (grayLevel << 8) | grayLevel;
    }

    /**
     * The helper class to represent a pixel in an image.
     */
    private static class Pair {
        private final int xPixel;
        private final int yPixel;

        Pair(int newXPixel, int newYPixel) {
            xPixel = newXPixel;
            yPixel = newYPixel;
        }

        int getXPixel() { return xPixel; }
        int getYPixel() { return yPixel; }
    }

    /**
     * The main method to start rendering.
     * @param args cmd args
     */
    public static void main(String[] args) {
        MandelbrotViewer.startViewer(EXECUTORS_RENDERER,
                MandelbrotMain::colorForEscapeCount, MandelbrotMain::grayLevelForEscapeCount);
    }
}
