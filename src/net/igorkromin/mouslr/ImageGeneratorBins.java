package net.igorkromin.mouslr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by ikromin on 6/07/2015.
 */
public class ImageGeneratorBins implements ImageGenerator {

    private static final int BIN_SIZE = 30;

    @Override
    public void generate(File inputData, File outputFile) throws IOException {
        ImageGeneratorLines lineGen = new ImageGeneratorLines();
        BufferedImage image = lineGen.generateImage(inputData);

        image = filterImage(image);

        ImageIO.write(image, "png", outputFile);
    }

    private BufferedImage filterImage(BufferedImage image) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();

        int width = image.getWidth() / BIN_SIZE;
        int height = image.getHeight() / BIN_SIZE;

        for (int x = 0;  x < width; x++) {
            for (int y = 0; y < height; y++) {

                int px = x * BIN_SIZE;
                int py = y * BIN_SIZE;

                int r = 0;
                int g = 0;
                int b = 0;
                int c = 0;

                for (int ix = px; ix < px + BIN_SIZE; ix++) {
                    for (int iy = py; iy < py + BIN_SIZE; iy++) {
                        int pColour = image.getRGB(ix, iy);
                        Color colour = new Color(pColour, true);

                        if (!colour.equals(Color.black)) {
                            r += colour.getRed();
                            g += colour.getGreen();
                            b += colour.getBlue();
                            c++;
                        }
                    }
                }

                if (c != 0) {
                    Color colour = new Color(r / c, g / c, b / c, 200);

                    g2.setColor(colour);
                    g2.fillRect(px, py, BIN_SIZE, BIN_SIZE);

                    // magnify bins
                    //Color colour2 = new Color(r / c, g / c, b / c, 50);
                    //g2.setColor(colour2);
                    //g2.fillRect(px - BIN_SIZE, py - BIN_SIZE, BIN_SIZE * 2, BIN_SIZE * 2);
                }
            }
        }

        return image;
    }

}
