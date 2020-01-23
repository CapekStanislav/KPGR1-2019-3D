package model.model3d;

import transforms.Point3D;

import java.awt.Color;
import java.util.function.Function;

/**
 * Instance třídy {@code PowerParametricCurve} představují parametrické křivky
 * druhé mocniny.
 *
 * @author Stanislav Čapek
 */
public class PowerParametricCurve extends Curve {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    private static final String DEAFULT_NAME = "PowerParametricCurve";
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    private static int id = 1;
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final Color magenta = Color.MAGENTA;
    private final String name = DEAFULT_NAME + "_" + id++;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /**
     * Zafixovaná osa y
     *
     * @param min      minimum definičnoho oboru
     * @param max      maximum definičního oboru
     * @param parametr násobek křivky
     */
    public PowerParametricCurve(double min, double max, double parametr) {
        this.color = magenta;
        final int y = 0;
        double z = getZ(min, parametr);

        verticies.add(new Point3D(min, y, z));
        for (double x = min; x < max; x += getLevelOfDetail()) {
            final Point3D p = new Point3D(x, y, getZ(x, parametr));
            final int size = verticies.size();
            addIndicies(size - 1, size);
            verticies.add(p);
        }
        final int size = verticies.size();
        addIndicies(size - 1, size);
        verticies.add(new Point3D(max, y, getZ(max, parametr)));

        this.center = new Point3D();
    }

    private static void testFunctionInterface(double min, double max, Function<Double, Double> function) {
        for (double i = min; i <= max; i++) {
            System.out.println("function.apply(i) = " + function.apply(i));
        }

    }


    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    @Override
    public String toString() {
        return name;
    }

    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================

    private double getZ(double x, double parametr) {
        return parametr * Math.pow(x, 2);
    }

    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================


    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================
    public static void main(String[] args) {
        PowerParametricCurve.testFunctionInterface(0d, 10d, d -> Math.pow(d, 2));
    }
}
