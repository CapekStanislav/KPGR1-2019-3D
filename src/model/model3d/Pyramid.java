package model.model3d;

import model.Transformable;
import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code Pyramid}
 *
 * @author Stanislav Čapek
 */
public class Pyramid extends Solid implements Transformable {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================


    public Pyramid() {
        this(Color.BLUE);
    }

    public Pyramid(Color color) {
        this.color = color;
        this.verticies.add(new Point3D(-1, -1, -1));
        this.verticies.add(new Point3D(1, -1, -1));
        this.verticies.add(new Point3D(1, 1, -1));
        this.verticies.add(new Point3D(-1, 1, -1));
        this.verticies.add(new Point3D(0, 0, 1));

//        podstava
        addIndicies(0, 1, 1, 2, 2, 3, 3, 0);

//        stěny
        addIndicies(0, 4, 1, 4, 2, 4, 3, 4);

        this.center = new Point3D(0, 0, 0);

    }


    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    @Override
    public Point3D getCenter() {
        return center;
    }

    @Override
    public void setCenter(Point3D point) {
        center = point;
    }

    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
