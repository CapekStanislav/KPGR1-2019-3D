package model;

import transforms.Point3D;

/**
 * Instance rozhraní {@code Transformable}
 *
 * @author Stanislav Čapek
 */
public interface Transformable extends Drawable {

    Point3D getCenter();

    void setCenter(Point3D point);
}
