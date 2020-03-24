package controller;

import model.Transformable;
import model.Transformer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

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
public class Animation {

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
