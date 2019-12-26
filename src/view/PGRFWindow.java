package view;

import controller.Controller3d;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;

public class PGRFWindow extends JFrame {

    private final Raster raster;

    public PGRFWindow() {
        // bez tohoto nastavení se okno zavře, ale aplikace stále běží na pozadí
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("PGRF1 úloha č. 3"); // titulek okna

        raster = new Raster();
        raster.setFocusable(true);
        raster.grabFocus(); // důležité pro pozdější ovládání z klávesnice

        BorderLayout layout = new BorderLayout(5, 5);

        setLayout(layout);
        add(raster,BorderLayout.CENTER); // vložit plátno do okna
        pack();
        new Controller3d(raster);
        setLocationRelativeTo(null); // vycentrovat okno
    }

    public Raster getRaster() {
        return raster;
    }

}
