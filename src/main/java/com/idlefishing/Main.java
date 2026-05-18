package com.idlefishing;

import com.idlefishing.ui.MainFrame;
import javax.swing.SwingUtilities;

/**
 * Application entry point.
 * Bootstraps the Swing UI on the Event Dispatch Thread.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
