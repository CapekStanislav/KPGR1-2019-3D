package renderer;

import model.model3d.Solid;
import transforms.Mat4;

/**
 * Instance rozhraní {@code GpuRenderer} představují vykreslovací engine.
 *
 * @author Stanislav Čapek
 */
public interface GpuRenderer {
    /**
     * Vyčistí plátno
     */
    void clear();

    /**
     * Vykreslí objekty
     *
     * @param solids
     */
    void draw(Solid... solids);

    /**
     * Nastaví matici pro model
     *
     * @param model
     */
    void setModel(Mat4 model);

    /**
     * Nastaví matici pro view
     *
     * @param view
     */
    void setView(Mat4 view);

    /**
     * Nastaví matici pro projektci
     *
     * @param projection
     */
    void setProjection(Mat4 projection);

}
