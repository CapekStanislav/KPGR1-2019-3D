package model.model3d;

import model.Transformable;
import model.Transformer;
import transforms.Point3D;

import java.awt.Color;

/**
 * Instance třídy {@code Axis}
 *
 * @author Stanislav Čapek
 */
public class Axis extends Solid implements Transformable {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final Line X;
    private final Line Y;
    private final Line Z;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    public Axis(Point3D center) {
        X = new Line(
                new Point3D(0, 0, 0),
                new Point3D(1, 0, 0)
        );
        Y = new Line(
                new Point3D(0, 0, 0),
                new Point3D(0, 1, 0)
        );
        Z = new Line(
                 new Point3D(0, 0, 0),
                new Point3D(0, 0, 1)
        );
        this.color = Color.CYAN;

        verticies.clear();
        indices.clear();
        verticies.addAll(X.getVerticies());
        verticies.addAll(Y.getVerticies());
        verticies.addAll(Z.getVerticies());
        addIndicies(0,1,2,3,4,5);

        this.center = center;
        new Transformer().move(this,center.getX(),center.getY(),center.getZ());
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
