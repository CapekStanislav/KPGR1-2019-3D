package renderer;

import model.model3d.Solid;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec3D;
import view.Raster;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

/**
 * Instance třídy {@code Renderer3d} dokáže vykreslovat jemu zadané objekty.
 *
 * @author Stanislav Čapek
 */
public class Renderer3d implements GpuRenderer {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    private final Raster raster;
    private Mat4 model;
    private Mat4 view;
    private Mat4 projection;

    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    public Renderer3d(Raster raster) {
        this.raster = raster;
        this.model = new Mat4Identity();
        this.view = new Mat4Identity();
        this.projection = new Mat4Identity();
    }


    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    @Override
    public void setModel(Mat4 model) {

        this.model = model;
    }

    @Override
    public void setView(Mat4 view) {

        this.view = view;
    }

    @Override
    public void setProjection(Mat4 projection) {

        this.projection = projection;
    }

    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    @Override
    public void clear() {
        raster.clear();
    }

    @Override
    public void draw(Solid... solids) {
        for (Solid solid : solids) {
            final List<Point3D> verticies = solid.getVerticies();
            final List<Integer> indices = solid.getIndices();

            for (int i = 0; i < indices.size(); i += 2) {

                final Point3D a = verticies.get(indices.get(i));
                final Point3D b = verticies.get(indices.get(i + 1));
                transformLine(a, b, solid.getColor());

            }
        }
    }


    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    private void transformLine(Point3D a, Point3D b, Color color) {
        a = a.mul(model).mul(view).mul(projection);
        b = b.mul(model).mul(view).mul(projection);

//        implementováno pokročilejší ořezávání
        if (clip(a)) {
            final Optional<Point3D> p = clipEdge(a, b);
            if (p.isPresent()) {
                a = p.get();
            }
        }
        if (clip(b)) {
            final Optional<Point3D> p = clipEdge(b, a);
            if (p.isPresent()) {
                b = p.get();
            }
        }

//        demohogenizace
        final Optional<Vec3D> dehomoA = a.dehomog();
        final Optional<Vec3D> dehomoB = b.dehomog();

//        kontrola
        if (dehomoA.isEmpty() || dehomoB.isEmpty()) {
            return;
        }

        Vec3D v1 = dehomoA.get();
        Vec3D v2 = dehomoB.get();

        v1 = transformToWindow(v1);
        v2 = transformToWindow(v2);

        raster.drawLine(
                (int) Math.round((v1.getX())),
                (int) Math.round((v1.getY())),
                (int) Math.round((v2.getX())),
                (int) Math.round((v2.getY())),
                color
        );

    }

    private Vec3D transformToWindow(Vec3D p) {
        return p
//                oprava y, které roste nahoru
                .mul(new Vec3D(1, -1, 1))
//        posun počátku do levého horního rohu
                .add(new Vec3D(1, 1, 0))
//                        vynásobíme polovinou velikosti
                .mul(new Vec3D(800 / 2f, 600 / 2f, 1)
                );
    }

    /**
     * Kontorola zda se bod nenachází mimo zobrazovací objem
     *
     * @param p bod
     * @return {@code true} bod se nachází mimo,
     * {@code false} je v zobrazovaném objemu
     */
    private boolean clip(Point3D p) {
        if (p.getW() < p.getX() || p.getX() < -p.getW()) {
            return true;
        }
        if (p.getW() < p.getY() || p.getY() < -p.getW()) {
            return true;
        }
        if (p.getW() < p.getZ() || p.getZ() < 0) {
            return true;
        }
        return false;
    }

    /**
     * Metoda ořeže úsečku dle zobrazovacího objemu
     *
     * @param a počáteční bod
     * @param b konečný bod
     * @return nový průsečík se zobrazovacím objemem
     */
    private Optional<Point3D> clipEdge(Point3D a, Point3D b) {
        Point3D p = null;

        if (a.getW() < a.getX() && b.getW() > b.getX()) {
            double k = getKx(a, b);
            p = getIntersectionPoint(a, b, k);

        } else if (a.getX() < -a.getW() && -b.getW() < b.getX()) {
            double k = getKx(a, b);
            p = getIntersectionPoint(a, b, k);
        }

        if (a.getW() < a.getY() && b.getW() > b.getY()) {
            double k = getKy(a, b);
            p = getIntersectionPoint(a, b, k);

        } else if (a.getY() < -a.getW()) {
            double k = getKy(a, b);
            p = getIntersectionPoint(a, b, k);
        }

        if (a.getW() < a.getZ()) {
//            double k = getKz(a, b);
//            p = getIntersectionPoint(a, b, k);

//            zadní stranu zobrazovacího objemu není ořezána
            return Optional.empty();
        } else if (a.getZ() < 0) {
            double k = getKz(a, b);
            p = getIntersectionPoint(a, b, k);
        }

        return Optional.ofNullable(p);
    }

    private Point3D getIntersectionPoint(Point3D a, Point3D b, double k) {
        return a.mul(1 - k).add(b.mul(k));
    }

    private double getKz(Point3D a, Point3D b) {
        if (a.getZ() > a.getW()) {
            return (a.getW() - a.getZ()) /
                    ((a.getW() - a.getZ()) - (b.getW() - b.getZ()));
        } else {
            return (a.getW() + a.getZ()) /
                    ((a.getW() + a.getZ()) - (b.getW() + b.getZ()));
        }
    }

    private double getKy(Point3D a, Point3D b) {
        if (a.getY() > a.getW()) {
            return (a.getW() - a.getY()) /
                    ((a.getW() - a.getY()) - (b.getW() - b.getY()));
        } else {
            return (a.getW() + a.getY()) /
                    ((a.getW() + a.getY()) - (b.getW() + b.getY()));
        }
    }

    private double getKx(Point3D a, Point3D b) {
        if (a.getX() > a.getW()) {
            return (a.getW() - a.getX()) /
                    ((a.getW() - a.getX()) - (b.getW() - b.getX()));
        } else {
            return (a.getW() + a.getX()) /
                    ((a.getW() + a.getX()) - (b.getW() + b.getX()));
        }
    }

    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
