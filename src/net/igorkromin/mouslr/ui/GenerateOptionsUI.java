package net.igorkromin.mouslr.ui;

import javax.swing.*;

/**
 * Created by ikromin on 9/07/2015.
 */
public class GenerateOptionsUI extends JDialog {
    public GenerateOptionsUI(JFrame owner) {
        super(owner, true);

        setTitle("Generate Options");
        setSize(250, 200);
        setResizable(false);
        setLocationRelativeTo(owner);

        // Colour (button click)
        // Colour (button press)
        // Colour (button release)
        // Colour (movement line)
        // Colour (drag line)
        // Colour (wheel scroll)
        // Initial line stroke width: 1
        // Button events stroke width: 2
        // Wheel events initial box size: 4
        // Click events circle size: 8
        // Press/Release events circle size: 10
        // Repeated wheel events box size modifier: 2
        // Repeated wheel events blend out factor: 20
        // Repeated drag events stroke modifier: 0.1
        // Repeated drag events blend out factor: 1
        // Repeated move events stroke modifier: 0.05
        // Repeated move events blend out factor: 1

        // Pixel bin size: 30
        // Pixel bins blend factor: 200
        // Magnify bins: false
        // Bin Magnification multiplier: 2
        // Ignore empty space: true
    }
}
