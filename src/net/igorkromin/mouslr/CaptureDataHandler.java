package net.igorkromin.mouslr;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by ikromin on 6/07/2015.
 */
public class CaptureDataHandler implements NativeMouseInputListener, NativeMouseWheelListener {

    // number of recorded events until a write to disk is triggered
    private static final int THRESHOLD = 100;

    private File mOutFile;
    private FileWriter mFileWriter;
    private long mStartTime;
    private boolean mRecording = false;
    private boolean mCloseFile = false;
    private ConcurrentLinkedDeque<String> mMouseData = new ConcurrentLinkedDeque<>();
    private int mScreenHeight;
    private int mScreenWidth;

    public File getOutFile() {
        return mOutFile;
    }

    public void setOutFile(File outFile) {
        mOutFile = outFile;
    }


    public boolean isRecording() {
        return mRecording;
    }

    public void setRecording(boolean mRecording) throws IOException {
        mStartTime = System.currentTimeMillis();
        this.mRecording = mRecording;

        if (mRecording) {
            mCloseFile = false;
            mFileWriter = new FileWriter(mOutFile, false);
            mFileWriter.write("MOUSLR " + mScreenWidth + " " + mScreenHeight + "\n");
        }
        else {
            mCloseFile = true;
            flush();
        }
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
        mark("C," + nativeMouseEvent.getX() + "," + nativeMouseEvent.getY() + "," + nativeMouseEvent.getButton());
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        mark("P," + nativeMouseEvent.getX() + "," + nativeMouseEvent.getY() + "," + nativeMouseEvent.getButton());
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        mark("R," + nativeMouseEvent.getX() + "," + nativeMouseEvent.getY() + "," + nativeMouseEvent.getButton());
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        mark("M," + nativeMouseEvent.getX() + "," + nativeMouseEvent.getY());
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
        mark("D," + nativeMouseEvent.getX() + "," + nativeMouseEvent.getY());
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeMouseWheelEvent) {
        mark("W," + nativeMouseWheelEvent.getX() + "," + nativeMouseWheelEvent.getY() + "," + nativeMouseWheelEvent.getWheelRotation());
    }

    /**
     * Marks an event for recording in the event queue. If the threshold is reached, the event queue will be flushed.
     * @param event
     */
    private void mark(String event) {
        mMouseData.addLast((System.currentTimeMillis() - mStartTime) + "," + event);

        if (mMouseData.size() > THRESHOLD) {
            flush();
        }
    }

    /**
     * Flushes the current event queue to file. The number of events written will be at most as defined
     * by THRESHOLD.
     */
    private void flush() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int events = 0;

                while (events < THRESHOLD && mMouseData.size() > 0) {
                    String event = mMouseData.removeFirst();

                    try {
                        mFileWriter.write(event);
                        mFileWriter.write('\n');
                    } catch (IOException e) {
                        // ignore
                    }

                    events++;
                }

                try {
                    mFileWriter.flush();

                    if (mCloseFile) {
                        mFileWriter.close();
                        mFileWriter = null;
                    }
                } catch (IOException e) {
                    // ignore
                }
            }
        });
    }

    public void setHeight(int height) {
        this.mScreenHeight = height;
    }

    public void setWidth(int width) {
        this.mScreenWidth = width;
    }
}
