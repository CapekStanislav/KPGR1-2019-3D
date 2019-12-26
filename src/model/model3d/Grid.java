package model.model3d;

import model.Transformable;
import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code Grid} představuje mřížku.
 *
 * @author Stanislav Čapek
 */
public class Grid extends Solid implements Transformable {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    private static final String DEFAULT_NAME = "Grid";
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    private static int id = 1;
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final String name = DEFAULT_NAME + "_" + id++;
    private final int width;
    private final int height;
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
