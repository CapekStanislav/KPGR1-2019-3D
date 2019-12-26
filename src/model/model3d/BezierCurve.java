package model.model3d;

import transforms.Cubic;
import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code BezierCurve} představují křivku typu BEZIÉR.
 *
 * @author Stanislav Čapek
 */
public class BezierCurve extends Curve {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    private static final String DEFAULT_NAME = "BezierCurve";
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    private static int id = 1;
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final String name = DEFAULT_NAME + "_" + id++;
    private final Color black = Color.BLACK;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================


    /**
     * Vytvoří křivku BEZIÉR pomocí 4 bodů
     *
     * @param a počáteční bod
     * @param b kontrolní bod
     * @param c kontrolní bod
     * @param d koncový bod
     */
    public BezierCurve(Point3D a, Point3D b, Point3D c, Point3D d) {
        this.color = black;
        final Cubic cubic = new Cubic(Cubic.BEZIER, a, b, c, d);

        initCurve(cubic, Curve.getLevelOfDetail());

//        setting up center of curve
        setupCenter(a, d);
    }

    /**
     * Vytvoří křivku BEZIÉR pro více kontrolních bodů
     *
     * @param points kontrolní body
     * @throws IllegalArgumentException počet kontrolních bodů je menší jak 4
     */
    public BezierCurve(Point3D... points) throws IllegalArgumentException {
        final int length = points.length;
        if (length < 4) {
            throw new IllegalArgumentException("Minimální počet kontrolních bodů je 4: " + length);
        }
        this.color = black;

        final int modulOfLenght = length % 4;
        final int celkemBodu = length + (4 - modulOfLenght);
        Point3D[] tempPoints = new Point3D[celkemBodu];

        for (int i = 0; i < length; i++) {
            tempPoints[i] = points[i];
        }
        for (int i = length; i < tempPoints.length; i++) {
            tempPoints[i] = points[length - 1];
        }
        final int left = length / 4;
        for (int i = 0; i <= left; i++) {
            initCurve(new Cubic(Cubic.BEZIER, tempPoints, i * 3), getLevelOfDetail());
        }
        setupCenter(points[0], points[length - 1]);
    }

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
