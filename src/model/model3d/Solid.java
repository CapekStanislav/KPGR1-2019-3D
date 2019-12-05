package model.model3d;

import model.Drawable;
import transforms.Point3D;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Instance třídy {@code Solid}
 *
 * @author Stanislav Čapek
 */
public abstract class Solid implements Drawable {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ =========
    // =====================================
    /**
     * Vertex buffer
     */
    final List<Point3D> verticies = new ArrayList<>();

    /**
     * Index buffer
     */
    final List<Integer> indices = new ArrayList<>();
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /**
     * Barva
     */
    Color color;

    /**
     * Střed
     */
    Point3D center;

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================


    @Override
    public List<Point3D> getVerticies() {
        return verticies;
    }

    @Override
    public List<Integer> getIndices() {
        return indices;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================
    protected final void addIndicies(Integer... toAdd) {
        indices.addAll(List.of(toAdd));
    }

    @Override
    public String toString() {
        return this.getClass().toString() + this.verticies.toString();
    }

    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
