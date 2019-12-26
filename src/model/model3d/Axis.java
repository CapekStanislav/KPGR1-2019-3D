package model.model3d;

import model.Transformable;
import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code Axis} představuje pravoúhlou osu ve třech směrech.
 *
 * @author Stanislav Čapek
 */
public class Axis extends Solid implements Transformable {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    private static final String DEFAULT_NAME = "Axis";
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    private static int id = 1;
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final String name = DEFAULT_NAME + "_" + id++;
    private final Line X;
    private final Line Y;
    private final Line Z;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
    private double scaleX;
    private double rotationX;
    private double scaleY;
    private double rotationY;
    private double scaleZ;
    private double rotationZ;
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    public Axis(Point3D center) {
        final double x = center.getX();
        final double y = center.getY();
        final double z = center.getZ();
        X = new Line(
                new Point3D(x, y, z),
                new Point3D(x + 1, y, z)
        );
        Y = new Line(
                new Point3D(x, y, z),
                new Point3D(x, y + 1, z)
        );
        Z = new Line(
                new Point3D(x, y, z),
                new Point3D(x, y, z + 1)
        );
        this.color = Color.CYAN;

        verticies.clear();
        indices.clear();
        verticies.addAll(X.getVerticies());
        verticies.addAll(Y.getVerticies());
        verticies.addAll(Z.getVerticies());
        addIndicies(0, 1, 2, 3
                , 4, 5);

        this.center = center;
        this.scaleX = 1;
        this.scaleY = 1;
        this.scaleZ = 1;

        this.rotationX = 0;
        this.rotationY = 0;
        this.rotationZ = 0;
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
