package net.igorkromin.mouslr.ui;

import net.igorkromin.mouslr.CaptureDataHandler;
import net.igorkromin.mouslr.ImageGenerator;
import net.igorkromin.mouslr.ImageGeneratorBins;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by ikromin on 5/07/2015.
 */
public class CaptureUI extends JFrame implements ActionListener, WindowStateListener {

    private static final String CMD_SETFILE = "setFile";
    private static final String CMD_STARTSTOP = "startStop";
    private static final String CMD_GENERATE = "generateImage";

    private CaptureDataHandler mCaptureDataHandler;

    private JFileChooser mFileChooser;
    private JLabel mStatusLabel;
    private JLabel mFileLabel;
    private JPanel mMainPanel;
    private JButton mStartButton;
    private JButton mGenerateButton;
    private JButton mFileButton;
    private Icon mStartIcon;
    private Icon mStopIcon;
    private Icon mGenerateIcon;
    private Icon mFileIcon;
    private Icon mMouslrIcon;

    public CaptureUI(CaptureDataHandler captureData) {
        this.mCaptureDataHandler = captureData;

        setTitle("Mouslr");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(Color.white);
        setSize(160, 225);
        setResizable(false);
        setLocation(100, 100);

        Dimension buttonSize = new Dimension(145, 28);

        // set the screen dimensions
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        mCaptureDataHandler.setWidth((int) screenSize.getWidth());
        mCaptureDataHandler.setHeight((int) screenSize.getHeight());

        // File Chooser
        mFileChooser = new JFileChooser();
        mFileChooser.setMultiSelectionEnabled(false);
        mFileChooser.setDialogTitle("Set Data File");

        // Status label
        mStatusLabel = new JLabel();
        mStatusLabel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        mStatusLabel.setOpaque(true);
        mStatusLabel.setBackground(Color.white);
        mStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setStatus("Idle", Color.gray);
        this.add(mStatusLabel, BorderLayout.SOUTH);

        // Main panel
        mMainPanel = new MyJPanel();
        mMainPanel.setOpaque(true);
        mMainPanel.setBackground(Color.white);
        mMainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 1));
        mMainPanel.setBorder(BorderFactory.createEmptyBorder(75, 6, 2, 6));
        this.add(mMainPanel, BorderLayout.CENTER);

        // Icons
        mStartIcon = loadIcon("play_2b1207_16");
        mStopIcon = loadIcon("stop_2b1207_16");
        mGenerateIcon = loadIcon("picture-o_2b1207_16");
        mFileIcon = loadIcon("file-text_2b1207_16");
        mMouslrIcon = loadIcon("mouslr");

        // File label
        mFileLabel = new JLabel("<no file>");
        mMainPanel.add(mFileLabel);

        // File button
        mFileButton = makeButton("Set Data File ...", buttonSize, mFileIcon, CMD_SETFILE);
        mFileButton.setEnabled(true);
        mMainPanel.add(mFileButton);

        // Start button
        mStartButton = makeButton("Start Recording", buttonSize, mStartIcon, CMD_STARTSTOP);
        mMainPanel.add(mStartButton);

        // Generate button
        mGenerateButton = makeButton("Generate Image", buttonSize, mGenerateIcon, CMD_GENERATE);
        mMainPanel.add(mGenerateButton);

        // Set JNativeHook to use the Swing dispatcher
        //GlobalScreen.setEventDispatcher(new SwingDispatchService());
    }

    /**
     * Makes a button with the specified text, dimension and icon.
     * @param buttonText
     * @param buttonSize
     * @param buttonIcon
     * @return
     */
    private JButton makeButton(String buttonText, Dimension buttonSize, Icon buttonIcon, String commandName) {
        JButton button = new JButton();
        button.setIcon(buttonIcon);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setPreferredSize(buttonSize);
        button.setHorizontalAlignment(JButton.LEFT);
        button.setText(buttonText);
        button.setEnabled(false);
        button.setActionCommand(commandName);

        button.addActionListener(this);

        return button;
    }

    /**
     * Loads the icon specified by name from the ui/icons package. Each icon is assume to be a PNG file.
     * @param iconName
     * @return
     */
    private Icon loadIcon(String iconName) {
        URL startIconUrl = getClass().getResource("icons/" + iconName + ".png");

        return new ImageIcon(startIconUrl);
    }

    /**
     * Responds to button click events.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (CMD_SETFILE.equals(command)) {
            setOutFileFromDialog();
        }
        else if (CMD_STARTSTOP.equals(command)) {
            try {
                if (mCaptureDataHandler.isRecording()) {
                    stopRecording();
                } else {
                    startRecording();
                }
            }
            catch (NativeHookException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (CMD_GENERATE.equals(command)) {
            generateImage();
        }
    }

    private void generateImage() {

        /*GenerateOptionsUI optsUI = new GenerateOptionsUI(this);
        optsUI.setVisible(true);

        if (1 == 1) {
            return;
        }*/

        final File dataFile = mCaptureDataHandler.getOutFile();
        final File imageFile = new File(dataFile.getAbsolutePath() + ".png");

        if (imageFile.exists()) {
            int response = JOptionPane.showConfirmDialog(this, "Output file already exists, do you want to overwrite?",
                    "Confirm Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.NO_OPTION) {
                return;
            }
        }

        try {
            setStatus("Generating image", Color.magenta);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    //ImageGeneratorBins.generate(dataFile, imageFile);
                    ImageGenerator generator = new ImageGeneratorBins();
                    try {
                        generator.generate(dataFile, imageFile);
                        setStatus("Image generated", Color.blue);
                    }
                    catch (Exception ex) {
                        setStatus("Error", Color.red);
                        throw new RuntimeException(ex);
                    }
                }
            });
        }
        catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startRecording() throws NativeHookException {

        // if the file exists already, check if it should be overwritten first
        if (mCaptureDataHandler.getOutFile().exists()) {
            int response = JOptionPane.showConfirmDialog(this, "Output file already exists, do you want to overwrite?",
                    "Confirm Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.NO_OPTION) {
                return;
            }
        }

        GlobalScreen.registerNativeHook();

        try {
            mCaptureDataHandler.setRecording(true);

            setStatus("Recording", Color.red);

            // change the start button to a stop button
            mStartButton.setIcon(mStopIcon);
            mStartButton.setText("Stop Recording");

            // disable generate image button while recording
            mGenerateButton.setEnabled(false);
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            GlobalScreen.unregisterNativeHook();
        }
    }

    private void stopRecording() throws NativeHookException {
        GlobalScreen.unregisterNativeHook();

        try {
            mCaptureDataHandler.setRecording(false);
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        setStatus("Idle", Color.gray);

        // change the start button to a stop button
        mStartButton.setIcon(mStartIcon);
        mStartButton.setText("Start Recording");

        // enable generate image button after recording
        mGenerateButton.setEnabled(true);
    }

    private void setStatus(String status, Color colour) {
        mStatusLabel.setText(status);
        mStatusLabel.setForeground(colour);
    }

    /**
     * Shows the open file dialog box and sets the CaptureData output file accordingly
     */
    private void setOutFileFromDialog() {
        if (mFileChooser.showDialog(this, "Set File") == JFileChooser.APPROVE_OPTION) {
            File selectedFile = mFileChooser.getSelectedFile();

            mCaptureDataHandler.setOutFile(selectedFile);
            mFileLabel.setText(selectedFile.getName());

            // enable start button
            mStartButton.setEnabled(true);

            // enable generate button if file already exists
            if (selectedFile.exists()) {
                mGenerateButton.setEnabled(true);
            }
            else {
                mGenerateButton.setEnabled(false);
            }
        }
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        if (WindowEvent.WINDOW_CLOSED == e.getNewState()) {
            if (mCaptureDataHandler.isRecording()) {
                try {
                    stopRecording();
                }
                catch (Exception ex) {
                    // ignore
                }
            }
        }
    }

    /**
     * Customised JPanel that draws the 'Mouslr' icon as the background image.
     */
    private class MyJPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            mMouslrIcon.paintIcon(mMainPanel, g, 5, -8);
        }
    }
}
