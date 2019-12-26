package model.model3d;

import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code ParametricCurve} představují parametrické křivky
 * funkce sinus.
 *
 * @author Stanislav Čapek
 */
public class SinusParametricCurve extends Curve {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    private static final String DEFAULT_NAME = "ParametricCurve";
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    private static int id = 1;
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final Color green = Color.GREEN;
    private final int levelOfDetail = 100;
    private final String name = DEFAULT_NAME + "_" + id++;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /**
     * Vytvoří křivku funkce sinus s zafixovanou osou X
     */
    public SinusParametricCurve() {
        this.color = green;
        final double x = 0;
        int multiple = 3;
        double min = -Math.PI * multiple;
        double max = Math.PI * multiple;
        final Point3D first = new Point3D(x, min, Math.sin(min));
        verticies.add(first);
        for (double y = min; y <= max; y += max / levelOfDetail) {
            final Point3D next = new Point3D(x, y, Math.sin(y));
            final int size = verticies.size();
            addIndicies(size - 1, size);
            verticies.add(next);
        }

        this.center = new Point3D();

    }

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    @Override
    public String toString() {
        return name;
    }

    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
