package model;

import transforms.Point3D;

import java.awt.Color;
import java.util.List;

/**
 * Instance rozhraní {@code Solid} představuje objekty, které se dokáží vykreslit
 * pomocí rozhraní {@link renderer.GpuRenderer}.
 *
 * @author Stanislav Čapek
 */
public interface Drawable {

    /**
     * Seznam bodů, ze kterého je sestaven geometrický objekt.
     *
     * @return seznam vrcholů
     */
    List<Point3D> getVerticies();

    /**
     * Seznam odkazů na body, ze kterých je geometrický objekt sestaven.
     *
     * @return seznam odkazů na vrcholy
     */
    List<Integer> getIndices();

    /**
     * Vrátí barvu objektu.
     *
     * @return barva objektu
     */
    Color getColor();

    /**
     * Nastaví barvu objektu.
     *
     * @param color nová barva objektu
     */
    void setColor(Color color);
}
