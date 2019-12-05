package model;

import transforms.Point3D;

import java.awt.Color;
import java.util.List;

/**
 * Instance rozhraní {@code Solid}
 *
 * @author Stanislav Čapek
 */
public interface Drawable {
    
    List<Point3D> getVerticies();

    List<Integer> getIndices();

    Color getColor();

    void setColor(Color color);
}
