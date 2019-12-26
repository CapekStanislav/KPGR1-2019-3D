package model.model3d;

import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code CompandCurve} představují obálku pro instance třídy
 * {@link Curve}. Vložené křivy se propojí a stanou se jedním objektem.
 * <br><br>
 * Střed je vypočítán v závislosti na vložených křivkách.
 *
 * @author Stanislav Čapek
 */
public class CompoundCurve extends Curve {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    private static final String DEFAULT_NAME = "CompoundCurve";
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    private static int id = 1;
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final String name = DEFAULT_NAME + "_" + id++;
    private final Color pink = Color.PINK;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================


    /**
     * Vytvoří prázdnou obálku
     */
    public CompoundCurve() {
        this(new Curve[0]);
    }

    /**
     * Vytvoří obálku pro zadané křivky v parametru.
     *
     * @param curves obalované křivky
     */
    public CompoundCurve(Curve... curves) {
        this.color = pink;

        for (int i = 0; i < curves.length; i++) {
            final Curve curve = curves[i];
            addCurve(curve);
        }
    }

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /**
     * Přidá další křivku do obálky
     *
     * @param curve další křivka
     */
    public void addCurve(Curve curve) {
        final int size = this.verticies.size();
        final Integer[] indices = curve.getIndices().stream().map(integer -> integer + size).toArray(Integer[]::new);
        addIndicies(indices);
        verticies.addAll(curve.getVerticies());
        if (size > 0) {
            setupCenter(verticies.get(0), verticies.get(size - 1));
        } else {
            this.center = new Point3D();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
