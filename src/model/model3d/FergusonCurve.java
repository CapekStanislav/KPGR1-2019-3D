package model.model3d;

import transforms.Cubic;
import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code FergusonCurve} představují křivky typu FERGUSON.
 *
 * @author Stanislav Čapek
 */
public class FergusonCurve extends Curve {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    private static final String DEFAULT_NAME = "FergusonCurve";
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    private static int id = 1;
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final String name = DEFAULT_NAME + "_" + id++;
    private final Color red = Color.RED;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /**
     * Vytvoří křivku FERGUSON pomocí 4 bodů
     *
     * @param a počáteční bod
     * @param b koncový bod
     * @param c kontrolní bod
     * @param d kontrolní bod
     */
    public FergusonCurve(Point3D a, Point3D b, Point3D c, Point3D d) {
        this.color = red;
        initCurve(new Cubic(Cubic.FERGUSON, a, b, c, d), getLevelOfDetail());
        setupCenter(a, d);
    }

    /**
     * Vytvoří křivku FERGUSON pro více kontrolních bodů
     *
     * @param points kontrolní body
     * @throws IllegalArgumentException počet kontrolních bodů je menší jak 4
     */
    public FergusonCurve(Point3D... points) throws IllegalArgumentException {
        final int length = points.length;
        if (length < 4) {
            throw new IllegalArgumentException(
                    "Minimální počet řídících bodů je 4: " + length);
        }
        this.color = red;

        for (int i = 0; i <= length - 4; i++) {
            initCurve(new Cubic(Cubic.FERGUSON, points, i), getLevelOfDetail());
        }

        setupCenter(points[0], points[points.length - 1]);
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
