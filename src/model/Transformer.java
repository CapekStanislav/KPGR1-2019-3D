package model;

import model.model3d.Solid;
import transforms.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Instance třídy {@code Transformer}
 *
 * @author Stanislav Čapek
 */
public class Transformer {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /**
     * Pohne s objektem podel os {@code X},{@code Y} a {@code Z}
     *
     * @param transformable transformovaný objekt
     * @param x             posun po ose x
     * @param y             posun po ose y
     * @param z             posun po ose z
     */
    public void move(Transformable transformable, double x, double y, double z) {
        final List<Point3D> verticies = transformable.getVerticies();
        Mat4Transl tran = new Mat4Transl(x, y, z);
        final List<Point3D> newVerticies = verticies.stream()
                .map(point3D -> point3D.mul(tran))
                .collect(Collectors.toList());
        verticies.clear();
        verticies.addAll(newVerticies);
        transformable.setCenter(transformable.getCenter().mul(tran));
    }

    /**
     * Rotace podél počátku os {@code X}, {@code Y} a {@code Z}.
     *
     * @param transformable transformovaný objekt
     * @param alpha         rotace podél X, v radiánech
     * @param beta          rotace podél Y, v radiánech
     * @param gamma         rotace podél Z, v radiánech
     */
    public void rotate(Transformable transformable, double alpha, double beta, double gamma) {
        final List<Point3D> verticies = transformable.getVerticies();
        Mat4RotXYZ rot = new Mat4RotXYZ(alpha, beta, gamma);
        final List<Point3D> newVerticies = verticies.stream()
                .map(point3D -> point3D.mul(rot))
                .collect(Collectors.toList());
        verticies.clear();
        verticies.addAll(newVerticies);
        transformable.setCenter(transformable.getCenter().mul(rot));
    }

    /**
     * Rotace podél středu objektu
     *
     * @param transformable transformovaný objekt
     * @param alpha         rotace podél X, v radiánech
     * @param beta          rotace podél Y, v radiánech
     * @param gamma         rotace podél Z, v radiánech
     */
    public void rotateByCenter(Transformable transformable, double alpha, double beta, double gamma) {
        final Vec3D center = new Vec3D(transformable.getCenter());
        Mat4 rot = new Mat4Identity()
                .mul(new Mat4Transl(center.opposite()))
                .mul(new Mat4RotXYZ(alpha, beta, gamma))
                .mul(new Mat4Transl(center));
        final List<Point3D> verticies = transformable.getVerticies();
        final List<Point3D> collect = verticies.stream()
                .map(point3D -> point3D.mul(rot))
                .collect(Collectors.toList());
        verticies.clear();
        verticies.addAll(collect);
    }


    public void scale(Transformable transformable, double x, double y, double z) {
        final List<Point3D> verticies = transformable.getVerticies();
        Mat4Scale scale = new Mat4Scale(x, y, z);
        final List<Point3D> collect = verticies
                .stream()
                .map(point3D -> point3D.mul(scale))
                .collect(Collectors.toList());

        verticies.clear();
        verticies.addAll(collect);
    }

    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /**
     * Najde střed objektu
     *
     * @param solid objekt
     * @return střed
     */
    public Point3D getCenter(Solid solid) {
        final Point3D first = solid.getVerticies().get(0);
        double xMax, xMin, yMax, yMin, zMax, zMin;
        xMax = first.getX();
        xMin = first.getX();
        yMax = first.getY();
        yMin = first.getY();
        zMax = first.getZ();
        zMin = first.getZ();

        for (Point3D point3D : solid.getVerticies()) {
            xMax = Math.max(xMax, point3D.getX());
            xMin = Math.min(xMin, point3D.getX());

            yMax = Math.max(yMax, point3D.getY());
            yMin = Math.min(yMin, point3D.getY());

            zMax = Math.max(zMax, point3D.getZ());
            zMin = Math.min(zMin, point3D.getZ());
        }

        Point3D center = new Point3D(
                (xMax - Math.abs(xMin)) / 2d,
                (yMax - Math.abs(yMin)) / 2d,
                (zMax - Math.abs(zMin)) / 2d
        );

        return center;
    }
    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
