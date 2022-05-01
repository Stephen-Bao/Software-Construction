package edu.cmu.cs.cs214.hw6;

import org.junit.*;

import java.awt.image.BufferedImage;
import java.awt.Component;

import static org.junit.Assert.*;

public class MandelbrotMainTest {

    private BufferedImage colorImage =
            new BufferedImage(1366, 768, BufferedImage.TYPE_INT_ARGB_PRE);
    private BufferedImage grayImage =
            new BufferedImage(1366, 768, BufferedImage.TYPE_INT_ARGB_PRE);
    private BufferedImage colorImageTest =
            new BufferedImage(1366, 768, BufferedImage.TYPE_INT_ARGB_PRE);
    private BufferedImage grayImageTest =
            new BufferedImage(1366, 768, BufferedImage.TYPE_INT_ARGB_PRE);

    @Before
    public void setUp() {
        MandelbrotMain.SEQUENTIAL_RENDERER.render(-2.5, 1, -1, 1,
                colorImage, MandelbrotMain::colorForEscapeCount);
        MandelbrotMain.SEQUENTIAL_RENDERER.render(-2.5, 1, -1, 1,
                grayImage, MandelbrotMain::grayLevelForEscapeCount);
    }

    @Test
    public void testExecutorRenderer() {
        MandelbrotMain.EXECUTORS_RENDERER.render(-2.5, 1, -1, 1,
                colorImageTest, MandelbrotMain::colorForEscapeCount);
        MandelbrotMain.EXECUTORS_RENDERER.render(-2.5, 1, -1, 1,
                grayImageTest, MandelbrotMain::grayLevelForEscapeCount);

        for (int width = 0; width < colorImage.getWidth(); width++) {
            for (int height = 0; height < colorImage.getHeight(); height++) {
                assertEquals(colorImage.getRGB(width, height), colorImageTest.getRGB(width, height));
                assertEquals(grayImage.getRGB(width, height), grayImageTest.getRGB(width, height));
            }
        }
    }

    @Test
    public void testStreamRenderer() {
        MandelbrotMain.STREAM_RENDERER.render(-2.5, 1, -1, 1,
                colorImageTest, MandelbrotMain::colorForEscapeCount);
        MandelbrotMain.STREAM_RENDERER.render(-2.5, 1, -1, 1,
                grayImageTest, MandelbrotMain::grayLevelForEscapeCount);

        for (int width = 0; width < colorImage.getWidth(); width++) {
            for (int height = 0; height < colorImage.getHeight(); height++) {
                assertEquals(colorImage.getRGB(width, height), colorImageTest.getRGB(width, height));
                assertEquals(grayImage.getRGB(width, height), grayImageTest.getRGB(width, height));
            }
        }
    }

}






