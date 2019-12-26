package model.model3d;

import model.Transformable;
import model.Transformer;
import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code Cube} představuje krychli.
 *
 * @author Stanislav Čapek
 */
public class Cube extends Solid implements Transformable {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    private static final String DEFAULT_NAME = "Cube";
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    private static int id = 1;
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final String name = DEFAULT_NAME + "_" + id++;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
    private double scaleX = 1;
    private double scaleY = 1;
    private double scaleZ = 1;
    private double rotationX = 0;
    private double rotationY = 0;
    private double rotationZ = 0;
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

    @Override
    public Point3D getCenter() {
        return center;
    }

    @Override
    public void setCenter(Point3D point) {
        center = point;
    }

    @Override
    public double getScaleX() {
        return scaleX;
    }

    @Override
    public void setScaleX(double ration) {
        this.scaleX = ration;
    }

    @Override
    public double getRotationX() {
        return rotationX;
    }

    @Override
    public void setRotationX(double radians) {
        this.rotationX = radians;
    }

    @Override
    public double getScaleY() {
        return scaleY;
    }

    @Override
    public void setScaleY(double ration) {
        this.scaleY = ration;
    }

    @Override
    public double getRotationY() {
        return rotationY;
    }

    @Override
    public void setRotationY(double radians) {
        this.rotationY = radians;
    }

    @Override
    public double getScaleZ() {
        return scaleZ;
    }

    @Override
    public void setScaleZ(double ration) {
        this.scaleZ = ration;
    }

    @Override
    public double getRotationZ() {
        return rotationZ;
    }

    @Override
    public void setRotationZ(double radians) {
        this.rotationZ = radians;
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
