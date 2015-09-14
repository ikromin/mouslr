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
