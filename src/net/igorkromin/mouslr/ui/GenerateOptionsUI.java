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
