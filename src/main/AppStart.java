package main;

import view.PGRFWindow;

import javax.swing.SwingUtilities;

public class AppStart {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PGRFWindow window = new PGRFWindow();
            window.setVisible(true);
        });
    }
}
