package model.model3d;

import model.Transformable;
import model.Transformer;
import transforms.Mat4RotY;
import transforms.Point3D;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Instance třídy {@code Sphere}
 *
 * @author Stanislav Čapek
 */
public class Sphere extends Solid implements Transformable {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    public Sphere(Color color, int numberOfCircle) {
        this.color = color;
        this.center = new Point3D(0, 0, 0);

        final int numberOfsides = numberOfCircle;
        final double alpha = 360d / numberOfsides;

        final int radius = 1;
        final Point3D start = new Point3D(0, 0, radius);
        final Point3D end = new Point3D(0, 0, -radius);

        final HalfCircle halfCircle = new HalfCircle(color, numberOfsides, start);
        final List<Point3D> verticies = halfCircle.getVerticies();
        this.verticies.addAll(verticies);
        this.addIndicies(halfCircle.indices.toArray(new Integer[0]));

        for (int i = 1; i < numberOfsides; i++) {
            final int size = this.getVerticies().size();
            final HalfCircle next = new HalfCircle(color, numberOfsides, start);
            new Transformer().rotate(next, 0, 0, Math.toRadians(alpha * i));
            this.verticies.addAll(next.getVerticies());

            this.addIndicies(next.getIndices()
                    .stream()
                    .map(integer -> integer + size).toArray(Integer[]::new));
        }

        final int sizeOfIndices = halfCircle.indices.size();
        final int sizeOfVerticies = halfCircle.getVerticies().size();
        final int sizeAllIndicies = this.getIndices().size();
        final ArrayList<Integer> firstCircle = new ArrayList<>();
        final ArrayList<Integer> lastCircle = new ArrayList<>();

        for (int i = 0; i < sizeAllIndicies - sizeOfIndices; i += sizeOfIndices) {
            for (int j = 0; j < sizeOfIndices; j += 2) {
                final Integer index1 = this.getIndices().get(i + j);
                final Integer index2 = index1 + sizeOfVerticies;
                if (j == 0) {
                    firstCircle.add(index1);
                    firstCircle.add(index2);
                }
                addIndicies(index1, index2);
                if (i / sizeOfIndices == numberOfsides-2) {
                    final Integer index0 = halfCircle.getIndices().get(j);
                    addIndicies(index2, index0);
                    if (j == 0) {
                        firstCircle.add(index2);
                        firstCircle.add(index0);
                    }
                }
            }
            final Integer last1 = this.getIndices().get(i + (sizeOfIndices - 1));
            final Integer last2 = last1 + sizeOfVerticies;
            addIndicies(last1, last2);
            lastCircle.add(last1);
            lastCircle.add(last2);
            if (i / sizeOfIndices == numberOfsides - 2) {
                final Integer last0 = halfCircle.getIndices().get(sizeOfIndices - 1);
                addIndicies(last2, last0);
                lastCircle.add(last2);
                lastCircle.add(last0);
            }

        }
        this.verticies.add(start);
        int lastVertex = this.getVerticies().size() - 1;
        for (int i = 0; i < firstCircle.size(); i += 2) {
            final Integer index = firstCircle.get(i);
            addIndicies(index,lastVertex);
        }

        this.verticies.add(end);
        lastVertex = this.getVerticies().size()-1;
        for (int i = 0; i < lastCircle.size(); i += 2) {
            final Integer index = lastCircle.get(i);
            addIndicies(index,lastVertex);
        }
    }

    public Sphere(Color color) {
        this(color, 12);
    }

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    @Override
    public Point3D getCenter() {
        return this.center;
    }

    @Override
    public void setCenter(Point3D point) {
        this.center = point;
    }

    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    //== INTERNÍ DATOVÉ TYPY =======================================================
    private class HalfCircle extends Solid implements Transformable {

        public HalfCircle(Color color, int numberOfsides, Point3D start) {
            this.color = color;
            this.center = Sphere.this.center;

            final double alpha = 360d / numberOfsides;

            final int steps = numberOfsides / 2;
            for (int i = 1; i < steps; i++) {
                final Point3D nextPoint = start.mul(new Mat4RotY(Math.toRadians(alpha * i)));
                verticies.add(nextPoint);
            }
            for (int i = 0; i < this.getVerticies().size() - 1; i++) {
                addIndicies(i, i + 1);
            }
        }

        @Override
        public Point3D getCenter() {
            return this.center;
        }

        @Override
        public void setCenter(Point3D point) {
            this.center = point;
        }
    }
    //== TESTY A METODA MAIN =======================================================

}
