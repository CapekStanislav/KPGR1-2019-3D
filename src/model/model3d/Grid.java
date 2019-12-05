package model.model3d;

import model.Transformable;
import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code Grid}
 *
 * @author Stanislav Čapek
 */
public class Grid extends Solid implements Transformable {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final int width;
    private final int height;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================


    public Grid() {
        this(Color.GRAY);
    }

    public Grid(Color color) {
        this(color, 10, 10);

    }

    public Grid(Color color, int width, int height) {
        this.width = width;
        this.height = height;
        this.color = color;

        this.center = new Point3D(0, 0, 0);

        int xMax = width / 2;
        int xMin = -xMax;
        int yMax = height / 2;
        int yMin = -yMax;
        int index = 0;

        for (int i = xMin; i <= xMax; i++) {
            verticies.add(new Point3D(i, yMin, 0));
            verticies.add(new Point3D(i, yMax, 0));
            addIndicies(index++, index++);
        }

        for (int i = yMin; i <= yMax; i++) {
            verticies.add(new Point3D(xMin, i, 0));
            verticies.add(new Point3D(xMax, i, 0));
            addIndicies(index++, index++);
        }

    }


    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    @Override
    public Point3D getCenter() {
        return center;
    }

    @Override
    public void setCenter(Point3D point) {
        this.center = point;
    }


    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
