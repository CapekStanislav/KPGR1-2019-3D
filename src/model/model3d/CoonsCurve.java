package model.model3d;

import transforms.Cubic;
import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code CoonsCurve} představují křivku typu COONS.
 *
 * @author Stanislav Čapek
 */
public class CoonsCurve extends Curve {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    private static final String DEFAULT_NAME = "CoonsCurve";
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    private static int id = 1;
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final String name = DEFAULT_NAME + "_" + id++;
    private final Color blue = Color.BLUE;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /**
     * Vytvoří křivku COONS pomocí 4 bodů
     *
     * @param a počáteční bod
     * @param b kontrolní bod
     * @param c kontrolní bod
     * @param d koncový bod
     */
    public CoonsCurve(Point3D a, Point3D b, Point3D c, Point3D d) {
        this.color = blue;
        initCurve(new Cubic(Cubic.COONS, a, b, c, d), getLevelOfDetail());
        setupCenter(a, d);
    }

    /**
     * Vytvoří křivku COOS pro více kontrolních bodů
     *
     * @param points kontrolní body
     * @throws IllegalArgumentException počet kontrolních bodů je menší jak 4
     */
    public CoonsCurve(Point3D... points) {
        final int length = points.length;
        if (length < 4) {
            throw new IllegalArgumentException("Minimální počet řídících bodů je 4: " + length);
        }
        this.color = blue;

        for (int i = 0; i <= length - 4; i++) {
            initCurve(new Cubic(Cubic.COONS, points, i), getLevelOfDetail());
        }


        setupCenter(points[0], points[length - 1]);
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
