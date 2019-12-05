package model.model3d;

import model.Transformable;
import model.Transformer;
import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code Cube}
 *
 * @author Stanislav Čapek
 */
public class Cube extends Solid implements Transformable {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================
    public Cube() {
        this(Color.BLUE);
    }

    public Cube(Color color) {
        this.color = color;
        this.verticies.add(new Point3D(-1, -1, -1));
        this.verticies.add(new Point3D(1, -1, -1));
        this.verticies.add(new Point3D(1, -1, 1));
        this.verticies.add(new Point3D(-1, -1, 1));

        this.verticies.add(new Point3D(-1, 1, -1));
        this.verticies.add(new Point3D(1, 1, -1));
        this.verticies.add(new Point3D(1, 1, 1));
        this.verticies.add(new Point3D(-1, 1, 1));

//        spodní stěna
        addIndicies(0, 1, 1, 2, 2, 3, 3, 0);
//        horní stěna
        addIndicies(4, 5, 5, 6, 6, 7, 7, 4);
//        okolní obal
        addIndicies(0, 4, 1, 5, 2, 6, 3, 7);

        this.center = new Transformer().getCenter(this);

    }


    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================


    @Override
    public Point3D getCenter() {
        return center;
    }

    @Override
    public void setCenter(Point3D point) {
        center = point;
    }

    @Override
    public String toString() {
        int i = 0;
        return "Cube({" + verticies.get(i++) + " " + verticies.get(i++) + " "
                + verticies.get(i++) + " " + verticies.get(i++) + "}\n"
                + "{" + verticies.get(i++) + " " + verticies.get(i++) + " "
                + verticies.get(i++) + " " + verticies.get(i++) + "})";
    }


    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
