/**
 * Mouslr - Mouse Tracking Pixel Art!
 * http://mouslr.kr0m.in
 * Copyright (C) 2015  Igor Kromin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * You can find this and my other open source projects here - http://github.com/ikromin
 */

package net.igorkromin.mouslr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by ikromin on 8/07/2015.
 */
public class ImageGeneratorLines implements ImageGenerator {

    private static final Color COLOUR_C = new Color(255,    0,      0,      127);
    private static final Color COLOUR_P = new Color(255,    0,      255,    127);
    private static final Color COLOUR_R = new Color(0,      255,    255,    127);
    private static final Color COLOUR_M = new Color(255,    255,    255,    127);
    private static final Color COLOUR_D = new Color(0,      255,    0,      127);
    private static final Color COLOUR_W = new Color(255,    25,     0,      127);

    private static final Stroke STROKE_SINGLE = new BasicStroke(1);
    private static final Stroke STROKE_BOLD = new BasicStroke(2);

    public void generate(File inputData, File outputFile) throws IOException {
        BufferedImage image = generateImage(inputData);
        ImageIO.write(image, "png", outputFile);
    }

    public BufferedImage generateImage(File inputData) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputData))) {

            boolean fileOk = false;
            int imageWidth = 0;
            int imageHeight = 0;

            String headerLine = reader.readLine();
            if (headerLine != null && headerLine.startsWith(MOUSLR_HEADER)) {
                String[] params = headerLine.split(" ");

                if (params.length == 3) {
                    try {
                        imageWidth = Integer.parseInt(params[1]);
                        imageHeight = Integer.parseInt(params[2]);

                        fileOk = true;
                    } catch (NumberFormatException ex) {
                        // ignore number format exceptions to allow the general runtime exception to be thrown below
                    }
                }
            }

            if (!fileOk) {
                throw new RuntimeException("Not a valid data file.");
            }

            BufferedImage image = makeImage(reader, imageWidth, imageHeight);
            return image;
        }
    }

    private BufferedImage makeImage(BufferedReader reader, int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(width, height,  BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setBackground(Color.black);
        g2.clearRect(0, 0, width, height);

        int sx = -1;
        int sy = -1;
        String sc = null;
        int boxSize = 4;
        int repeatCommandCounter = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            String[] params = line.split(",");

            if (params.length >= 4) {
                try {
                    int time = Integer.parseInt(params[0]);
                    String command = params[1];
                    int x = Integer.parseInt(params[2]);
                    int y = Integer.parseInt(params[3]);

                    if (sx == -1 && sy == -1) {
                        sx = x;
                        sy = y;
                        sc = command;
                        continue;
                    }

                    Color c = getCommandColour(command);
                    g2.setColor(c);

                    if ("C".equals(command)) {
                        g2.setStroke(STROKE_SINGLE);
                        g2.fillOval(x + 1, y + 1, 8, 8);
                    }
                    else if ("P".equals(command) || "R".equals(command)) {
                        g2.setStroke(STROKE_BOLD);
                        g2.drawOval(x, y, 10, 10);
                    }
                    else if ("W".equals(command)) {
                        if ("W".equals(sc)) {
                            repeatCommandCounter++;
                            boxSize += 2;

                            int alpha = c.getAlpha() - (repeatCommandCounter * 20);
                            if (alpha < 1) {
                                alpha = 1;
                            }
                            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
                        }
                        else {
                            repeatCommandCounter = 0;
                            boxSize = 4;
                        }

                        g2.setStroke(STROKE_SINGLE);
                        g2.fillRect(x - (boxSize/2), y - (boxSize/2), boxSize, boxSize);
                    }
                    else if ("D".equals(command)) {
                        if ("D".equals(sc)) {
                            repeatCommandCounter++;

                            int alpha = c.getAlpha() - (repeatCommandCounter * 1);
                            if (alpha < 1) {
                                alpha = 1;
                            }
                            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
                            g2.setStroke(new BasicStroke(((BasicStroke) g2.getStroke()).getLineWidth() + 0.1f));
                        }
                        else {
                            repeatCommandCounter = 0;
                            g2.setStroke(STROKE_SINGLE);
                        }

                        g2.drawLine(sx, sy, x, y);
                    }
                    else if ("M".equals(command)) {
                        if ("M".equals(sc)) {
                            repeatCommandCounter++;

                            int alpha = c.getAlpha() - (repeatCommandCounter * 1);
                            if (alpha < 1) {
                                alpha = 1;
                            }
                            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
                            g2.setStroke(new BasicStroke(((BasicStroke) g2.getStroke()).getLineWidth() + 0.05f));
                        }
                        else {
                            repeatCommandCounter = 0;
                            g2.setStroke(STROKE_SINGLE);
                        }

                        g2.drawLine(sx, sy, x, y);
                    }

                    sx = x;
                    sy = y;
                    sc = command;
                }
                catch (RuntimeException  ex) {
                    //    // ignore exceptions due to bad data
                    ex.printStackTrace();
                }
            }
        }

        return image;
    }

    private Color getCommandColour(String command) {
        if ("C".equals(command)) {
            return COLOUR_C;
        }
        else if ("P".equals(command)) {
            return COLOUR_P;
        }
        else if ("R".equals(command)) {
            return COLOUR_R;
        }
        else if ("M".equals(command)) {
            return COLOUR_M;
        }
        else if ("D".equals(command)) {
            return COLOUR_D;
        }
        else if ("W".equals(command)) {
            return COLOUR_W;
        }

        return null;
    }

}
