package com.idlefishing;
/*
This gem written by me on 2 hours of sleep and a can of redbull is something between a
cookie clicker game and fisherman dream (i had this game in my sleeve for a while so it was not done from a scratch, i had some existing parts of it before)

All by me (CupOfTea12 on Github)
I hope that anyone who opens up this code will like it and understand it like i do
:)
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
