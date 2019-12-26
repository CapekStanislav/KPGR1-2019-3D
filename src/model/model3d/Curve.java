package model.model3d;

import model.Transformable;
import transforms.Cubic;
import transforms.Point3D;

/**
 * Abstraktní třída {@code Curve} představuje vzor pro všechny křivky.
 *
 * @author Stanislav Čapek
 */
public abstract class Curve extends Solid implements Transformable {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    private static final double DEFAULT_LEVEL_OF_DETAIL = 0.1;
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    private static double levelOfDetail = DEFAULT_LEVEL_OF_DETAIL;
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
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

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /**
     * Inicializuje křivku s využitím kubiky {@link Cubic}.
     *
     * @param cubic         kubika
     * @param levelOfDetail detail křivky určuje kolik bodů se vytvoří, rozmezí 0 - 1
     */
    protected final void initCurve(Cubic cubic, double levelOfDetail) {
        for (double i = 0; i <= 1; i += levelOfDetail) {
            final Point3D point3D = cubic.compute(i);
            verticies.add(point3D);
        }
        for (int i = verticies.size() - 2; i >= 0; i--) {
            addIndicies(i, i + 1);
        }
    }

    /**
     * Nastaví střed dle počátečního a konečného bodu křivky.
     *
     * @param a počáteční bod
     * @param b konečný bod
     */
    protected final void setupCenter(Point3D a, Point3D b) {
        double x = (b.getX() - Math.abs(a.getX())) / 2;
        double y = (b.getY() - Math.abs(a.getY())) / 2;
        double z = (b.getZ() - Math.abs(a.getZ())) / 2;
        setCenter(new Point3D(x, y, z));
    }

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /**
     * Vrátí detail křivky, rozmezí je {@code (0,1>}, přičemž čím blíže k
     * nule jsme tím detailnější je křivka.
     *
     * @return detail křivky
     */
    public static double getLevelOfDetail() {
        return levelOfDetail;
    }


    /**
     * Nastaví nový detail křivky. Rozmezí musí být {@code (0,1>}, jinak
     * vyjímka. Čím blíže k nule, tím detalnější bude křivka.
     *
     * @param levelOfDetail detail křivky v rozmezí (0,1>
     * @throws IllegalArgumentException hodnota je mimo rozmezí
     */
    public static void setLevelOfDetail(double levelOfDetail) throws IllegalArgumentException {
        if (levelOfDetail <= 0 || levelOfDetail > 1) {
            throw new IllegalArgumentException(
                    "Rozmezí musí být větší jak nula a zároveň menší nebo rovno 1: " + levelOfDetail
            );
        }
        Curve.levelOfDetail = levelOfDetail;
    }

    @Override
    public Point3D getCenter() {
        return this.center;
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
    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
