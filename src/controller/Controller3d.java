package controller;

import model.Drawable;
import model.Transformable;
import model.Transformer;
import model.model3d.*;
import renderer.GpuRenderer;
import renderer.Renderer3d;
import transforms.*;
import view.Raster;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Instance třídy {@code Controller3d}
 *
 * @author Stanislav Čapek
 */
public class Controller3d {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    private final GpuRenderer renderer;
    private final List<Drawable> solids;
    private final Solid[] axis;
    private final Transformer transformer;
    private final int width;
    private final int height;
    private final double MOVE_STEP = 1.0;
    private final double ROTATE_STEP = 10.0;
    private final double SCALE_STEP = 0.001;
    private final Camera defaultCamera;
    private final Mat4 defaultProjetions;

    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    private Mat4 model, view, projections;
    private Camera camera;
    private double scale = 1;
    private boolean toggleScale = false;
    private boolean toggleProjection = false;
    private boolean toggleAnimate = false;

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    public Controller3d(Raster raster) {
        this.renderer = new Renderer3d(raster);
        width = raster.getWidth();
        height = raster.getHeight();
        transformer = new Transformer();


        model = new Mat4Identity(); // jednotková matice
        camera = new Camera()
                .withPosition(new Vec3D(9, -7.95, 4.25))
                .withAzimuth(2.45)
                .withZenith(-0.52);
        defaultCamera = new Camera(camera, false);
        projections = new Mat4PerspRH(Math.PI / 3, height / (float) width, 0.1, 100);
        defaultProjetions = projections;

        axis = new Solid[3];
        axis[0] = new Line(
                new Point3D(0, 0, 0),
                new Point3D(1, 0, 0),
                Color.RED
        );
        axis[1] = new Line(
                new Point3D(0, 0, 0),
                new Point3D(0, 1, 0),
                Color.GREEN
        );
        axis[2] = new Line(
                new Point3D(0, 0, 0),
                new Point3D(0, 0, 1),
                Color.BLUE
        );


        solids = new ArrayList<>();

        final Cube c = new Cube(Color.RED);
        transformer.move(c, 0, 0, 1);
        solids.add(c);

        final Axis stred = new Axis(transformer.getCenter(c));
        solids.add(stred);

        final Pyramid e = new Pyramid();
        transformer.move(e, 3, 0, 1);
        transformer.rotate(e, 0, 0, Math.toRadians(45));
        solids.add(e);

        final Cube c2 = new Cube(Color.black);
        transformer.move(c2, -5, 0, 1);
        solids.add(c2);

        final Pyramid pyramid = new Pyramid(Color.white);
        transformer.move(pyramid, 5, 0, 1);
        transformer.scale(pyramid, 0.5, 2, -1);
        solids.add(pyramid);

        final Sphere sphere = new Sphere(Color.MAGENTA, 90);
        transformer.move(sphere, 4, 4, 1);
        solids.add(sphere);

        initListeners(raster);
        display();

    }

    private void initListeners(Raster raster) {
        raster.grabFocus();

        raster.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getExtendedKeyCode()) {
                    case KeyEvent.VK_W:
                        camera = camera.forward(MOVE_STEP);
                        break;
                    case KeyEvent.VK_S:
                        camera = camera.backward(MOVE_STEP);
                        break;
                    case KeyEvent.VK_A:
                        camera = camera.left(MOVE_STEP);
                        break;
                    case KeyEvent.VK_D:
                        camera = camera.right(MOVE_STEP);
                        break;
                    case KeyEvent.VK_LEFT:
                        double az = camera.getAzimuth() + Math.toRadians(ROTATE_STEP);
                        camera = camera.withAzimuth(az);
                        break;
                    case KeyEvent.VK_RIGHT:
                        double az2 = camera.getAzimuth() - Math.toRadians(ROTATE_STEP);
                        camera = camera.withAzimuth(az2);
                        break;
                    case KeyEvent.VK_UP:
                        double zen1 = camera.getZenith() - Math.toRadians(ROTATE_STEP);
                        if (Math.toDegrees(zen1) < -90) {
                            zen1 = Math.toRadians(-90);
                        }
                        camera = camera.withZenith(zen1);
                        break;
                    case KeyEvent.VK_DOWN:
                        double zen2 = camera.getZenith() + Math.toRadians(ROTATE_STEP);
                        if (Math.toDegrees(zen2) > 90) {
                            zen2 = Math.toRadians(90);
                        }
                        camera = camera.withZenith(zen2);
                        break;
                    case KeyEvent.VK_SHIFT:
                        camera = camera.up(MOVE_STEP);
                        break;
                    case KeyEvent.VK_CONTROL:
                        camera = camera.down(MOVE_STEP);
                        break;
                    case KeyEvent.VK_SPACE:
                        camera = defaultCamera;
                        break;
                    case KeyEvent.VK_C:
                        if (!toggleProjection) {
                            projections = new Mat4OrthoRH(10, 10, 0.1, 100);
                            toggleProjection = true;
                        } else {
                            projections = defaultProjetions;
                            toggleProjection = false;
                        }
                        break;
                    case KeyEvent.VK_P:
                        if (!toggleAnimate) {
                            toggleAnimate = true;
                        } else {
                            toggleAnimate = false;
                        }
                        break;

                }
                display();
            }

        });
        raster.addMouseMotionListener(new MouseAdapter() {
            double lastX = -1;
            double lastY = -1;
            final int speed = 8;

            @Override
            public void mouseMoved(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastX < 0 || lastY < 0) {
                    lastX = e.getX();
                    lastY = e.getY();
                }
                double dx = lastX - e.getX();
                double dy = lastY - e.getY();

                double zenith = Math.toDegrees(camera.getZenith());
                zenith += dy/speed;
                if (zenith > 90) {
                    zenith = 90;
                }
                if (zenith < -90) {
                    zenith = -90;
                }

                double azimut = Math.toDegrees(camera.getAzimuth());
                azimut += dx/speed;
                azimut = azimut % 360;

                camera = camera.withZenith(Math.toRadians(zenith)).withAzimuth(Math.toRadians(azimut));
                lastX = e.getX();
                lastY = e.getY();
                display();
            }
        });
        raster.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equalsIgnoreCase("fps")) {
                if (toggleAnimate) {
                    double angelPerFrame = 90d / (int) evt.getNewValue();

                    Transformable solid;

                    solid = (Transformable) solids.get(0);
                    if (scale > 0.96 && !toggleScale) {
                        transformer.scale(solid, scale, scale, scale);
                        scale -= SCALE_STEP;
                    } else {
                        toggleScale = true;
                    }

                    if (scale < 1.040 && toggleScale) {
                        transformer.scale(solid, scale, scale, scale);
                        scale += SCALE_STEP;
                    } else {
                        toggleScale = false;
                    }

                    solid = (Transformable) solids.get(2);
                    transformer.rotate(solid, 0, 0, Math.toRadians(-angelPerFrame / 2));

                    solid = (Transformable) solids.get(3);
                    transformer.rotate(solid, 0, 0, Math.toRadians(angelPerFrame));
                    for (Drawable s : solids) {
                        if (solid instanceof Transformable) {
                            transformer.rotateByCenter((Transformable) s, 0, 0, Math.toRadians(angelPerFrame));
                        }
                    }
                }

                display();
            }
        });

    }

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    private void display() {
        renderer.clear();
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projections);

//        vykreslení gridu
        renderer.draw(new Grid());

//        vykreslení os
        renderer.setModel(new Mat4Identity());
        renderer.draw(axis);


//        kreslení objektů
        renderer.setModel(model);
        renderer.draw(solids.toArray(new Solid[0]));

    }

    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
