package com.idlefishing;
/*
This gem written by me on 2 hours of sleep and a can of redbull is something between a
cookie clicker game and fisherman dream

All by me (CupOfTea12 on Github)
 */
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
