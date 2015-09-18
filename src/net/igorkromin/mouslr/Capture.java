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

import net.igorkromin.mouslr.ui.CaptureUI;
import org.jnativehook.GlobalScreen;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Capture {

    private static final Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // ignore if Nimbus is not available
        }

        Capture main = new Capture();
        main.start();
    }

    public void start() {

        logger.setLevel(Level.OFF);
        //System.setOut(null);

        CaptureDataHandler captureData = new CaptureDataHandler();

        GlobalScreen.addNativeMouseListener(captureData);
        GlobalScreen.addNativeMouseMotionListener(captureData);
        GlobalScreen.addNativeMouseWheelListener(captureData);

        final CaptureUI ui = new CaptureUI(captureData);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ui.addWindowStateListener(ui);
                ui.setVisible(true);
            }
        });
    }

}
