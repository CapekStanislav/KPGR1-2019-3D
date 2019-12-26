package view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * Instance třídy {@code InfoPanel}
 *
 * @author Stanislav Čapek
 */
public class InfoPanel extends JPanel {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    public InfoPanel() {
        final FlowLayout mgr = new FlowLayout(FlowLayout.CENTER, 15, 10);
        this.setLayout(mgr);
        final String htmlOpen = "<html>";

        String wsad = htmlOpen +
                "W - forward <br>" +
                "S - backward <br>" +
                "A - left <br>" +
                "D - right";
        this.add(new JLabel(wsad));

        String arrows = htmlOpen +
                "UP - turn up <br>" +
                "DOWN - turn down <br>" +
                "LEFT - turn left <br>" +
                "RIGHT - turn right <br>";
        this.add(new JLabel(arrows));

        String other = htmlOpen +
                "SHIFT - move up <br>" +
                "CTRL - move down <br>" +
                "C - change view <br>" +
                "SPACE - reset camera <br>" +
                "P - start/stop animation";
        this.add(new JLabel(other));

        String mouse = htmlOpen +
                "CLICK & DRAGG - look around";
        this.add(new JLabel(mouse));
    }

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
