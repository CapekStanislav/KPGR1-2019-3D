package controller;

import model.Drawable;
import model.Transformable;
import model.Transformer;
import model.model3d.*;
import renderer.GpuRenderer;
import renderer.Renderer3d;
import transforms.*;
import view.InfoPanel;
import view.ListOfDrawablePanel;
import view.Raster;
import view.TransformationPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Instance třídy {@code Controller3d} představuje ovládací jednotku mezi uživatelským
 * rozhraním a data modelem. Tato třída obsahuje vnitřní třídu {@link Animation}, která
 * umužňuje animovat objekty implementující rozhraní {@link Transformable}.
 *
 * @author Stanislav Čapek
 */
public class Controller3d {
    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    private final GpuRenderer renderer;
    private final List<Drawable> solids = new ArrayList<>();
    private final List<Drawable> curves = new ArrayList<>();
    private List<Drawable> toDisplayList = solids;
    private final Solid[] axis;
    private final Transformer transformer;
    private final double moveStep = 1.0;
    private final double rotateStep = 10.0;
    private final Camera defaultCamera;
    private final Mat4 defaultProjetions;
    private final TransformationPanel transformationPanel;
    private final JList<Drawable> drawableJList;
    private final ButtonGroup buttonsOfViewObjects;
    private final List<Animation> animations = new ArrayList<>();
    private final List<Transformable> animatedObjects = new ArrayList<>();
    private final ListOfDrawablePanel listOfDrawablePanel;

    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    private Mat4 model;
    private Mat4 projections;
    private Camera camera;
    private boolean toggleProjection = false;
    private boolean toggleAnimate = false;
    private boolean isDoneAnimation;

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /**
     * Konstruktor
     *
     * @param raster kreslící plátno
     */
    public Controller3d(Raster raster) {
        this.renderer = new Renderer3d(raster);
        int width = raster.getWidth();
        int height = raster.getHeight();
        transformer = new Transformer();

        JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(raster);

        listOfDrawablePanel = new ListOfDrawablePanel();
        buttonsOfViewObjects = listOfDrawablePanel.getButtonsOfViewObjects();
        final ButtonModel buttonModel = buttonsOfViewObjects.getElements().nextElement().getModel();
        buttonsOfViewObjects.setSelected(buttonModel, true);
        drawableJList = listOfDrawablePanel.getView();
        drawableJList.setModel(new DrawableListModel(solids));
        drawableJList.addListSelectionListener(new TransformationListener(drawableJList));
        jFrame.add(listOfDrawablePanel, BorderLayout.EAST);

        transformationPanel = new TransformationPanel();
        jFrame.add(transformationPanel, BorderLayout.SOUTH);

        this.model = new Mat4Identity(); // jednotková matice
        camera = new Camera()
                .withPosition(new Vec3D(9.53, -8.63, 4.76))
                .withAzimuth(2.25)
                .withZenith(-0.51);
        defaultCamera = new Camera(camera, false);
        projections = new Mat4PerspRH(Math.PI / 3, height / (float) width, 0.1, 100);
        defaultProjetions = projections;

//        osy scény
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

//        inicializace bodů a jejich spojení křivkou
        initCurves();
//        inicializace kreslených objektů
        initSolids();
//        inicializace posluchačů
        initListeners(raster);
//        zobrazení
        display();
    }

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================
    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /**
     * Inicializace posluchačů (ovládání klávesnice, ovládání myši, reakce z GUI,
     * pravidelné překreslování - animace)
     *
     * @param raster kreslící plátno
     */
    private void initListeners(Raster raster) {
        raster.grabFocus();
        raster.setTextAnimation("Animation: stopped");

//        ovládání klávesnicí
        raster.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getExtendedKeyCode()) {
                    case KeyEvent.VK_W:
                        camera = camera.forward(moveStep);
                        break;
                    case KeyEvent.VK_S:
                        camera = camera.backward(moveStep);
                        break;
                    case KeyEvent.VK_A:
                        camera = camera.left(moveStep);
                        break;
                    case KeyEvent.VK_D:
                        camera = camera.right(moveStep);
                        break;
                    case KeyEvent.VK_LEFT:
                        double az = camera.getAzimuth() + Math.toRadians(rotateStep);
                        camera = camera.withAzimuth(az);
                        break;
                    case KeyEvent.VK_RIGHT:
                        double az2 = camera.getAzimuth() - Math.toRadians(rotateStep);
                        camera = camera.withAzimuth(az2);
                        break;
                    case KeyEvent.VK_UP:
                        double zen1 = camera.getZenith() - Math.toRadians(rotateStep);
                        if (Math.toDegrees(zen1) < -90) {
                            zen1 = Math.toRadians(-90);
                        }
                        camera = camera.withZenith(zen1);
                        break;
                    case KeyEvent.VK_DOWN:
                        double zen2 = camera.getZenith() + Math.toRadians(rotateStep);
                        if (Math.toDegrees(zen2) > 90) {
                            zen2 = Math.toRadians(90);
                        }
                        camera = camera.withZenith(zen2);
                        break;
                    case KeyEvent.VK_SHIFT:
                        camera = camera.up(moveStep);
                        break;
                    case KeyEvent.VK_CONTROL:
                        camera = camera.down(moveStep);
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
                        if (toggleAnimate) {
                            raster.setTextAnimation("Animation: stopped");
                            toggleAnimate = false;
                        } else {
                            raster.setTextAnimation("Animation: playing");
                            toggleAnimate = true;
                        }
                        break;
                    case KeyEvent.VK_F1:
                        JOptionPane.showMessageDialog(
                                null,
                                new InfoPanel(),
                                "Controls",
                                JOptionPane.PLAIN_MESSAGE
                        );
                        break;

                }
                display();
            }
        });
//        ovládání myší
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
                zenith += dy / speed;
                if (zenith > 90) {
                    zenith = 90;
                }
                if (zenith < -90) {
                    zenith = -90;
                }

                double azimut = Math.toDegrees(camera.getAzimuth());
                azimut += dx / speed;
                azimut = azimut % 360;

                camera = camera.withZenith(Math.toRadians(zenith)).withAzimuth(Math.toRadians(azimut));
                lastX = e.getX();
                lastY = e.getY();
                display();
            }

        });
//        fokus myši
        raster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                raster.grabFocus();
            }
        });

//        animace
        raster.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equalsIgnoreCase("fps")) {
                if (toggleAnimate) {
                    isDoneAnimation = true;
                    final int fps = (int) evt.getNewValue();
                    for (Animation animation : animations) {
                        animation.doAllAnimations(fps);
                        if (isDoneAnimation) {
                            isDoneAnimation = animation.isDone();
                        }
                    }
                    if (isDoneAnimation) {
                        animations.clear();
                        createAnimationForBlackCube(animatedObjects.get(0));
                        createAnimationForRedCube(animatedObjects.get(1));
                        final Transformable bouncer = animatedObjects.get(3);
                        final Vec3D vectorBackToCenter = new Vec3D(bouncer.getCenter()).opposite();
                        transformer.move(bouncer,
                                vectorBackToCenter.getX(),
                                vectorBackToCenter.getY(),
                                vectorBackToCenter.getZ()
                        );
                        createBouncingAnimation(((Curve) animatedObjects.get(2)), bouncer);
                        createAnimationForBluePyramid(animatedObjects.get(4));

                    }
                }
                display();
            }
        });

//        ovládání přesunu objektů
        final Map<String, JSpinner> moveJspinner = transformationPanel.getMoveJspinner();
        moveJspinner.forEach((s, jSpinner) -> jSpinner.addChangeListener(e -> {
            final Drawable drawable = drawableJList.getSelectedValue();
            if (drawable instanceof Transformable) {
                Transformable transformable = (Transformable) drawable;
                final double value = ((double) jSpinner.getValue());
                final Point3D center = transformable.getCenter();

                boolean isChangeValue = false;
                switch (s) {
                    case "X":
                        final double x = value - center.getX();
                        isChangeValue = x != 0;
                        transformer.move(transformable, x, 0, 0);
                        break;
                    case "Y":
                        final double y = value - center.getY();
                        isChangeValue = y != 0;
                        transformer.move(transformable, 0, y, 0);
                        break;
                    case "Z":
                        final double z = value - center.getZ();
                        isChangeValue = z != 0;
                        transformer.move(transformable, 0, 0, z);
                        break;
                }

                if (isChangeValue) {
                    createCurves();
                }
            }
        }));

//        ovládání rotace pomocí JSlider
        final Map<String, JSlider> rotationSliders = transformationPanel.getRotationSliders();
        rotationSliders.forEach((s, jSlider) -> jSlider.addChangeListener(e -> {
            final Drawable drawable = this.drawableJList.getSelectedValue();
            Transformable transformable;
            if (drawable instanceof Transformable) {
                transformable = (Transformable) drawable;
            } else {
                return;
            }
            final double newDegree = Math.toRadians(jSlider.getValue());
            switch (s) {
                case "X":
                    final double alpha = newDegree - transformable.getRotationX();
                    transformer.rotateByCenter(transformable, alpha, 0, 0);
                    break;
                case "Y":
                    final double beta = newDegree - transformable.getRotationY();
                    transformer.rotateByCenter(transformable, 0, beta, 0);
                    break;
                case "Z":
                    final double gamma = newDegree - transformable.getRotationZ();
                    transformer.rotateByCenter(transformable, 0, 0, gamma);
                    break;

            }
        }));

//        ovládání škálování pomocí JSlider
        transformationPanel.getScaleSliders().forEach((s, jSlider) -> {
            jSlider.addChangeListener(e -> {
                final Drawable drawable = this.drawableJList.getSelectedValue();
                Transformable transformable;
                if (drawable instanceof Transformable) {
                    transformable = (Transformable) drawable;

                } else {
                    return;
                }
                switch (s) {
                    case "X":
                        final double valueX = (jSlider.getValue() / 100d) / transformable.getScaleX();
                        transformer.scaleByCenter(transformable, valueX, 1, 1);
                        break;
                    case "Y":
                        final double valueY = (jSlider.getValue() / 100d) / transformable.getScaleY();
                        transformer.scaleByCenter(transformable, 1, valueY, 1);
                        break;
                    case "Z":
                        final double valueZ = (jSlider.getValue() / 100d) / transformable.getScaleZ();
                        transformer.scaleByCenter(transformable, 1, 1, valueZ);
                        break;
                }
            });
        });

//        ovládání přepínacího tlačítka pro zobrazení objektů ve scéně
        final Iterator<AbstractButton> abstractButtonIterator = buttonsOfViewObjects.getElements().asIterator();
        abstractButtonIterator.forEachRemaining(abstractButton -> abstractButton.addActionListener(e -> {
            switch (e.getActionCommand()) {
                case "solids":
                    toDisplayList = solids;
                    this.drawableJList.setModel(new DrawableListModel(solids));
                    break;
                case "curves":
                    toDisplayList = curves;
                    this.drawableJList.setModel(new DrawableListModel(curves));
                    break;
            }
        }));

//        ovládání tlačítka pro přidávání bodů do scény
        final JButton btnAddPoint = listOfDrawablePanel.getBtnAddPoint();
        btnAddPoint.addActionListener(e -> {
            final String actionCommand = buttonsOfViewObjects.getSelection().getActionCommand();
            switch (actionCommand) {
                case "curves":
                    curves.add(createControlPoint(0, 0, 0));
                    createCurves();
                    drawableJList.setModel(new DrawableListModel(curves));
                    break;
                case "solids":
                    JOptionPane.showMessageDialog(
                            null,
                            "Do této scény nelze přidávat" +
                                    " kontrolní bod.",
                            "Omezení přidání bodu",
                            JOptionPane.INFORMATION_MESSAGE
                    );
            }
        });
    }

    /**
     * Inicializace objektů vykreslených ve scéně Solids
     */
    private void initSolids() {
        final Transformable c = new Cube(Color.RED);
        transformer.move(c, 0, 0, 1);
        solids.add(c);

        final Transformable stred = new Axis(transformer.getCenter((Solid) c));
        solids.add(stred);

        final Transformable e = new Pyramid();
        transformer.move(e, 3, 0, 1);
        transformer.rotate(e, 0, 0, Math.toRadians(45));
        solids.add(e);

        final Transformable c2 = new Cube(Color.black);
        transformer.move(c2, -5, 5, 1);
        solids.add(c2);

        final Transformable pyramid = new Pyramid(Color.orange);
        transformer.move(pyramid, 5, 0, 1);
        transformer.scaleByCenter(pyramid, 0.5, 2, 1);
        solids.add(pyramid);

        final Transformable sphere = new Sphere(Color.MAGENTA, 90);
        solids.add(sphere);

        final Curve bouncingCurve = createBouncingCurve();
        solids.add(bouncingCurve);

//        nastavení animací
        this.animatedObjects.add(c2);
        this.animatedObjects.add(c);
        this.animatedObjects.add(bouncingCurve);
        this.animatedObjects.add(sphere);
        this.animatedObjects.add(e);
        createAnimationForBlackCube(c2);
        createAnimationForRedCube(c);
        createBouncingAnimation(bouncingCurve, sphere);
        createAnimationForBluePyramid(e);

        final Animation a = new Animation(stred).setRotionByCenter(0, 0, Math.toRadians(360));
        animations.add(a);

    }

    /**
     * Vytvoření specifické animace pro skákající míč.
     *
     * @param bouncingCurve křivka dráhy
     * @param bouncer       objekt, který následuje křivku
     */
    private void createBouncingAnimation(Curve bouncingCurve, Transformable bouncer) {
        final Point3D firstPoint = bouncingCurve.getVerticies().get(0);
        transformer.move(bouncer, firstPoint.getX(), firstPoint.getY(), firstPoint.getZ());

        final Animation a = new Animation(bouncer);
        a.changeAcceleration(1);
        for (int i = 0; i < bouncingCurve.getVerticies().size() - 1; i++) {
            final Point3D p1 = bouncingCurve.getVerticies().get(i);
            final Point3D p2 = bouncingCurve.getVerticies().get(i + 1);
            final Vec3D v1 = new Vec3D(p1);
            final Vec3D v2 = new Vec3D(p2);
            final Vec3D delta = v2.sub(v1);
            if (delta.eEquals(new Vec3D(), 0.1)) {
                continue;
            }
            a.addAnimation(a.setMove(delta.getX(), delta.getY(), delta.getZ()));
        }
        animations.add(a);

    }

    /**
     * Vytvoření pomocnou křivku pro animaci skákajícího míče
     *
     * @return křivka dráhy skákajícího míče
     */
    private Curve createBouncingCurve() {
        final CompoundCurve compoundCurve = new CompoundCurve();
        final double levelOfDetail = Curve.getLevelOfDetail();
        Curve.setLevelOfDetail(0.5);
        int initHeight = 3;
        PowerParametricCurve pc1 = new PowerParametricCurve(0, initHeight, -1);
        transformer.move(pc1, 0, 0, Math.pow(initHeight, 2));
        compoundCurve.addCurve(pc1);
        for (int i = 0; i < 5; i++) {
            double z = pc1.getCenter().getZ();
            z *= 0.4; // bude nahrazen koeficientem
            final double def = Math.sqrt(z);
            final PowerParametricCurve nextCurve = new PowerParametricCurve(-def, def, -1);
            final Point3D previousLastPoint = pc1.getVerticies().get(pc1.getVerticies().size() - 1);
            final Point3D nextFirstPoint = nextCurve.getVerticies().get(0);
            double dx = previousLastPoint.getX() - nextFirstPoint.getX();
            double dz = Math.abs(nextFirstPoint.getZ());
            transformer.move(nextCurve, dx, 0, dz);
            compoundCurve.addCurve(nextCurve);
            pc1 = nextCurve;
        }
        Curve.setLevelOfDetail(levelOfDetail);
        return compoundCurve;
    }

    /**
     * Vytvoření specifické animace pro červenou kostku
     *
     * @param c červená kostka
     */
    private void createAnimationForRedCube(Transformable c) {
        Animation animation = new Animation(c);
        animation.addAnimation(
                animation.setMove(-5, 5, 0)
        );
        animation.addAnimation(
                animation.setMove(5, -5, 0)
        );
        animation.addAnimation(
                animation.setMove(5, -5, 0)
        );
        animation.addAnimation(
                animation.setMove(-5, 5, 0)
        );
        animations.add(animation);
    }

    /**
     * Vytvoření specifické animace pro černou kostku
     *
     * @param c2 černá kostka
     */
    private void createAnimationForBlackCube(Transformable c2) {
        Animation animation = new Animation(c2);
        final double fullRoll = Math.toRadians(360);
        animation.addAnimation(
                animation.setMove(10, 0, 0).setRotionByCenter(0, fullRoll, 0)
        );
        animation.addAnimation(
                animation.setMove(0, -10, 0).setRotionByCenter(fullRoll, 0, 0)
        );
        animation.addAnimation(
                animation.setMove(-10, 0, 0).setRotionByCenter(0, -fullRoll, 0)
        );
        animation.addAnimation(
                animation.setMove(0, 10, 0).setRotionByCenter(-fullRoll, 0, 0)
        );
        animations.add(animation);
    }

    /**
     * Vytvoření specifické naimace pro modrý jehlan
     *
     * @param pyramid modrý jehlan
     */
    private void createAnimationForBluePyramid(Transformable pyramid) {
        final Animation a = new Animation(pyramid);
        a.addAnimation(a.setScaleByCenter(2, 2, 2));
        a.addAnimation(a.setScaleByCenter(0.2, 0.2, 0.2));
        a.addAnimation(a.setScaleByCenter(1, 0.5, 1));
        animations.add(a);
    }

    /**
     * Inicializace křivek a kontrolních bodů, které jsou vykresleny ve scéně
     * Curves
     */
    private void initCurves() {

        final Transformable cp1 = createControlPoint(-5, 0, 0);
        final Transformable cp2 = createControlPoint(-3, 0, 2);
        final Transformable cp3 = createControlPoint(0, 0, -3);
        final Transformable cp4 = createControlPoint(3, 0, 1);
        final Transformable cp5 = createControlPoint(5, 0, -3);
        final Transformable cp6 = createControlPoint(7, 0, 2);
        final Transformable cp7 = createControlPoint(9, 0, 0);

        curves.add(cp1);
        curves.add(cp2);
        curves.add(cp3);
        curves.add(cp4);
        curves.add(cp5);
        curves.add(cp6);
        curves.add(cp7);

        createCurves();
    }

    /**
     * Pomocná metoda, která vytvoří předdefinovaný kontrolní bod.
     *
     * @param x počáteční umístění na ose X
     * @param y počáteční umístění na ose Y
     * @param z počáteční umístění na ose Z
     * @return nový kontroní bod
     */
    private Transformable createControlPoint(double x, double y, double z) {
        final double scaleFactor = 0.2;
        final int numberOfCircle = 10;
        final Color color = Color.CYAN;
        final Sphere sphere = new Sphere(color, numberOfCircle);
        transformer.move(sphere, x, y, z);
        transformer.scaleByCenter(sphere, scaleFactor, scaleFactor, scaleFactor);
        return sphere;
    }

    /**
     * Vytvoří křivky BEZIÉR, FERGUSON a COONS dle zadaných kontrolních
     * bodů. Kontrolní musí být instance {@link Sphere} a přidán do seznamu
     * {@link Controller3d#curves}. K vytvoření kontrolního bodu slouží
     * metoda {@link Controller3d#createControlPoint(double, double, double)}.
     */
    private void createCurves() {
        final List<Drawable> controlPoints = curves
                .stream()
                .filter(drawable -> drawable instanceof Sphere)
                .collect(Collectors.toList());
        final Point3D[] points = controlPoints
                .stream()
                .filter(drawable -> drawable instanceof Transformable)
                .map(t -> ((Transformable) t).getCenter())
                .toArray(Point3D[]::new);

        curves.clear();
        curves.addAll(controlPoints);
        Curve.setLevelOfDetail(0.05);

        final BezierCurve bezierCurve = new BezierCurve(points);
        curves.add(bezierCurve);
        final FergusonCurve fergusonCurve = new FergusonCurve(points);
        curves.add(fergusonCurve);
        final CoonsCurve coonsCurve = new CoonsCurve(points);
        curves.add(coonsCurve);
        final SinusParametricCurve parametricCurve = new SinusParametricCurve();
        curves.add(parametricCurve);
    }

    /**
     * Vykreslí objekty na plátně
     */
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
        renderer.draw(toDisplayList.toArray(new Solid[0]));

    }

//== INTERNÍ DATOVÉ TYPY =======================================================

    /**
     * List model je využíván v GUI pro zobrazení vykreslovaných objektů ve scéně
     */
    private class DrawableListModel extends AbstractListModel<Drawable> {

        private final List<Drawable> drawables;

        /**
         * Konstruktor
         *
         * @param drawables seznam vykreslovaných objektů
         */
        public DrawableListModel(List<Drawable> drawables) {
            this.drawables = drawables;
        }

        @Override
        public int getSize() {
            return drawables.size();
        }

        @Override
        public Drawable getElementAt(int index) {
            return drawables.get(index);
        }
    }

    /**
     * Třída {@code TransformationListener} posluchače změny výběru v listu.
     * Dle vybraného objektu nastaví hodnoty pro ovladacích prvků transformace
     * (pohyb, rotace a škálování).
     */
    private class TransformationListener implements ListSelectionListener {

        private final JList<Drawable> list;

        /**
         * Konstruktor
         *
         * @param list zobrazovaný seznam
         */
        public TransformationListener(JList<Drawable> list) {
            this.list = list;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            final Drawable drawable = list.getSelectedValue();
            if (drawable instanceof Transformable) {
                Transformable transformable = (Transformable) drawable;
                final String[] axis = transformationPanel.getAxis();

                final Map<String, JSpinner> moveJspinner = transformationPanel.getMoveJspinner();
                final Point3D center = transformable.getCenter();
                moveJspinner.get(axis[0]).setValue(center.getX());
                moveJspinner.get(axis[1]).setValue(center.getY());
                moveJspinner.get(axis[2]).setValue(center.getZ());

                final Map<String, JSlider> rotationSliders = transformationPanel.getRotationSliders();
                rotationSliders.get(axis[0]).setValue((int) Math.toDegrees(transformable.getRotationX()));
                rotationSliders.get(axis[1]).setValue((int) Math.toDegrees(transformable.getRotationY()));
                rotationSliders.get(axis[2]).setValue((int) Math.toDegrees(transformable.getRotationZ()));

                final Map<String, JSlider> scaleSliders = transformationPanel.getScaleSliders();
                scaleSliders.get(axis[0]).setValue((int) (transformable.getScaleX() * 100));
                scaleSliders.get(axis[1]).setValue((int) (transformable.getScaleY() * 100));
                scaleSliders.get(axis[2]).setValue((int) (transformable.getScaleZ() * 100));

            }
        }
    }

    /**
     * Instance třídy {@code Animation} je immutable služebníkem pro objekty rozhraní {@link Transformable}.
     * S těmito objekty, provádí animované transformace (pohyb, rotace podle středu objektu, rotace podle
     * počátku a škálování podle středu objektu).
     * <br><br>
     * Pro jednoduchou animaci se vytvoří nová instance třídy na které se zvolají příslušné transformace. Ty
     * jsou vykonány zároveň. Například tato animace pohne objektem po ose X o 10 jednotek a zrotuje jej podél osy Z
     * v protisměru hodinových ručiček o 180°:
     * <br><br>
     * {@code
     * final Animation a = new Animation(stred).setMove(10, 0, 0).setRotionByCenter(0, 0, Math.toRadians(180)));
     * }
     * <br><br>
     * Pro složitější animaci se využije metoda {@link Animation#addAnimation(Animation)}, která přidá do
     * seznamu další animaci. Animace jsou pak vykonány, tak jak byly přidány (First in, First out). Všechny
     * animace se poté spustí metodou {@link Animation#doAllAnimations(int)} zvolanou na obácle těchto animaci, tzn.
     * na instanci, do které byly animace přidávány. Příklad: <br><br>
     * <code>
     * Animation animation = new Animation(transformable);<br>
     * final double fullRoll = Math.toRadians(360);<br>
     * animation.addAnimation(<br>
     * animation.setMove(10, 0, 0).setRotionByCenter(0, fullRoll, 0)<br>
     * );<br>
     * animation.addAnimation(<br>
     * animation.setMove(0, -10, 0).setRotionByCenter(fullRoll, 0, 0)<br>
     * );<br>
     * animation.addAnimation(<br>
     * animation.setMove(-10, 0, 0).setRotionByCenter(0, -fullRoll, 0)<br>
     * );<br>
     * animation.addAnimation(<br>
     * animation.setMove(0, 10, 0).setRotionByCenter(-fullRoll, 0, 0)<br>
     * );<br>
     * animation.doAllAnimation(fps);<br>
     * </code>
     */
    private class Animation {

        private final Transformer transformer = new Transformer();
        private final Transformable transformable;
        private final Deque<Animation> animationDeque = new ArrayDeque<>();
        private double moveX = 0;
        private double moveY = 0;
        private double moveZ = 0;
        private double alpha = 0;
        private double beta = 0;
        private double gamma = 0;
        private double alphaCenter = 0;
        private double betaCenter = 0;
        private double gammaCenter = 0;
        private double scaleX, scaleY, scaleZ;
        private double scaleXOrigin, scaleYOrigin, scaleZOrigin;
        private double dx, dy, dz;
        private boolean isMove = false;
        private boolean isScale = false;
        private boolean isRotate = false;
        private boolean isDone = false;
        private int countMove = 0;
        private int countScale = 0;
        private double countRotate = 0;
        private double scaleXStep;
        private double scaleYStep;
        private double scaleZStep;
        private double speed = 0;

        /**
         * Privátní konstruktor pro kopírování instancí
         *
         * @param animation kopírovaná instance
         */
        private Animation(Animation animation) {
            this.transformable = animation.transformable;
            this.moveX = animation.moveX;
            this.moveY = animation.moveY;
            this.moveZ = animation.moveZ;

            this.alpha = animation.alpha;
            this.beta = animation.beta;
            this.gamma = animation.gamma;

            this.alphaCenter = animation.alphaCenter;
            this.betaCenter = animation.betaCenter;
            this.gammaCenter = animation.gammaCenter;

            this.scaleX = animation.scaleX;
            this.scaleY = animation.scaleY;
            this.scaleZ = animation.scaleZ;

            this.isMove = animation.isMove;
            this.isRotate = animation.isRotate;
            this.isScale = animation.isScale;

            this.speed = animation.speed;
        }

        /**
         * Vytvoří novou instanci animace
         *
         * @param transformable animovaný objekt
         */
        Animation(Transformable transformable) {
            this.transformable = transformable;

            scaleXOrigin = transformable.getScaleX();
            scaleYOrigin = transformable.getScaleY();
            scaleZOrigin = transformable.getScaleZ();

            scaleX = scaleXOrigin;
            scaleY = scaleYOrigin;
            scaleZ = scaleZOrigin;
        }

        /**
         * Spustí všechny vložené animace včetně obalové
         *
         * @param fps obnovovací frekvence
         */
        void doAllAnimations(int fps) {
            final int newFps = calulateNewFps(fps);
            doAnimation(newFps);
            try {
                final Animation a = animationDeque.getFirst();
                if (a.isScale || a.isRotate || a.isMove) {
                    a.doAnimation(newFps);
                } else {
                    animationDeque.remove();
                }
            } catch (NoSuchElementException e) {
                this.isDone = true;
            }
        }

        /**
         * Přidá animaci do fronty
         *
         * @param animation nová animace
         */
        void addAnimation(Animation animation) {
            animationDeque.add(animation);
        }

        /**
         * Vymaže animaci z fronty, pokud existuje
         *
         * @param animation animace k vymazání
         */
        void removeAnimation(Animation animation) {
            animationDeque.remove(animation);
        }

        /**
         * Pohne objektem ve směru os x,y,z. Jedná se o přírustek, nikoliv o absolutní
         * pozici.
         *
         * @param x přírustek na ose X
         * @param y přírustek na ose Y
         * @param z přírustek na ose Z
         * @return nová animace s nastaveným pohybem
         */
        Animation setMove(double x, double y, double z) {
            final Animation newAnimation = new Animation(this);
            newAnimation.isMove = true;
            newAnimation.dx = 0;
            newAnimation.dy = 0;
            newAnimation.dz = 0;

            newAnimation.moveX = x;
            newAnimation.moveY = y;
            newAnimation.moveZ = z;
            return newAnimation;
        }

        /**
         * Nastaví animaci na otáčení objektu podél středu souřadnic úhlu/sekundu podél os x,y,z.
         *
         * @param alpha podél osy x v radiánech
         * @param beta  podél osy y v radiánech
         * @param gamma podél osy z v radiánech
         * @return nová animace s nastavenou rotací
         */
        Animation setRotationByOrigin(double alpha, double beta, double gamma) {
            final Animation newAnimation = new Animation(this);
            newAnimation.isRotate = true;

            newAnimation.alpha = alpha;
            newAnimation.beta = beta;
            newAnimation.gamma = gamma;
            return newAnimation;
        }

        /**
         * Nastaví animaci na otáčení objetku podél jeho středu úhlu/seknudu podél os x,y,z.
         *
         * @param alpha podél osy x v radiánech
         * @param beta  podél osy y v radiánech
         * @param gamma podél osy z v radiánech
         * @return nová animace s nastavenou rotací
         */
        Animation setRotionByCenter(double alpha, double beta, double gamma) {
            final Animation newAnimation = new Animation(this);
            newAnimation.isRotate = true;

            newAnimation.alphaCenter = alpha;
            newAnimation.betaCenter = beta;
            newAnimation.gammaCenter = gamma;
            return newAnimation;
        }

        /**
         * Nastaví objekt na novu velikost. Aktuální velikost lze zjistit
         * zvoláním na transformovaném objektu {@link Transformable#getScaleX()}, {@link Transformable#getScaleY()}
         * a {@link Transformable#getScaleZ()}.
         *
         * @param x nová velikost podél osy x
         * @param y nová velikost podél osy y
         * @param z nová velikost podél osy z
         * @return nová animace s nastaveným zvětšením/zmenšením
         */
        Animation setScaleByCenter(double x, double y, double z) {
            final Animation newAnimation = new Animation(this);
            newAnimation.isScale = true;
            newAnimation.scaleXOrigin = transformable.getScaleX();
            newAnimation.scaleYOrigin = transformable.getScaleY();
            newAnimation.scaleZOrigin = transformable.getScaleZ();

            newAnimation.scaleX = x;
            newAnimation.scaleY = y;
            newAnimation.scaleZ = z;
            return newAnimation;
        }


        /**
         * Vrátí jestli jsou již všechny animace provedeny.
         *
         * @return {@code true} všechny animace jsou provedeny a není nic dalšího co dělat <br>
         * {@code false} zbývají animace k provedení
         */
        public boolean isDone() {
            return isDone;
        }

        /**
         * Procentuální změna zrychlení animace. V intervalu od {@code 0-1}, kde
         * nula představuje žádné zrychlení a animace je závislá na dodané obnovovací
         * frekvenci snímků předané v parametru metody {@link Animation#doAllAnimations(int)}.
         *
         * @param percent interval rychlosti 0-1
         */
        void changeAcceleration(double percent) {
            this.speed = percent;
        }

        /**
         * Přemapuje procentuální zrychlení na hodnotu fps.
         *
         * @param fps obnovovací frekvence
         * @return nová přemapovaná hodnota
         */
        private int calulateNewFps(int fps) {
            double slope = 1 - fps;
            return ((int) (fps + slope * speed));
        }

        /**
         * Provede animaci škálování, rotaci a pohyb u jedné animace
         *
         * @param fps obnovovací frekvence
         * @throws IllegalArgumentException obnovovací frekvence je menší jak 1
         */
        private void doAnimation(int fps) throws IllegalArgumentException {
            if (fps < 1) {
                throw new IllegalArgumentException("Hodnota fps musí být kladné nenulové číslo: " + fps);
            }
            doScaling(fps);
            doRotation(fps);
            doMoving(fps);
        }

        private void doMoving(int fps) {
            if (moveX != 0 || moveY != 0 || moveZ != 0) {
                if (countMove == 0) {
                    dx = moveX / fps;
                    dy = moveY / fps;
                    dz = moveZ / fps;
                }
                if (fps - countMove > 0) {
                    transformer.move(transformable, dx, dy, dz);
                    countMove++;
                } else {
                    moveX = 0;
                    moveY = 0;
                    moveZ = 0;
                }
            } else {
                isMove = false;
            }
        }

        private void doRotation(int fps) {
            if (fps - countRotate > 0) {
                countRotate++;
                if (alpha != 0 || beta != 0 || gamma != 0) {
                    transformer.rotate(transformable, alpha / fps, beta / fps, gamma / fps);
                } else if (alphaCenter != 0 || betaCenter != 0 || gammaCenter != 0) {
                    transformer.rotateByCenter(transformable, alphaCenter / fps, betaCenter / fps, gammaCenter / fps);
                }
            } else {
                isRotate = false;
            }

        }

        private void doScaling(int fps) {
            if (fps - countScale > 0 && isScale) {
                isScale = true;

                double tempScaleX;
                double tempScaleY;
                double tempScaleZ;

                if (countScale == 0) {
                    scaleXStep = (scaleX - transformable.getScaleX()) / fps;
                    scaleYStep = (scaleY - transformable.getScaleY()) / fps;
                    scaleZStep = (scaleZ - transformable.getScaleZ()) / fps;
                }

                final double actualScaleX = transformable.getScaleX();
                if (this.scaleX / actualScaleX != 1) {
                    tempScaleX = (actualScaleX + scaleXStep) / actualScaleX;
                } else {
                    tempScaleX = 1;
                }

                final double actualScaleY = transformable.getScaleY();
                if (this.scaleY / actualScaleY != 1) {
                    tempScaleY = (actualScaleY + scaleYStep) / actualScaleY;
                } else {
                    tempScaleY = 1;
                }

                final double actualScaleZ = transformable.getScaleZ();
                if (this.scaleZ / actualScaleZ != 1) {
                    tempScaleZ = (actualScaleZ + scaleZStep) / actualScaleZ;
                } else {
                    tempScaleZ = 1;
                }

                transformer.scaleByCenter(transformable, tempScaleX, tempScaleY, tempScaleZ);
                countScale++;
            } else {
                isScale = false;
            }

        }
    }


//== TESTY A METODA MAIN =======================================================

}
