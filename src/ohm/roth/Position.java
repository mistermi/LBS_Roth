package ohm.roth;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import java.io.Serializable;

/**
 * Beschreibung einer Position
 * Abgeleitet von Coordinate aus jts
 */
public class Position extends Coordinate implements Serializable {

    /**
     * Die Position als Point Object
     * @return Der Point
     */
    public Point getPoint() {
        GeometryFactory fac = new GeometryFactory();
        return fac.createPoint(this);
    }

    /**
     * Konstruktor
     * @param x Lat
     * @param y Lon
     */
    public Position(double x, double y) {
        super(x, y);
    }

    /**
     * latitude
     * @return latitude
     */
    public double getLat() {
        return this.x;
    }

    /**
     * longitude
     * @return longitude
     */
    public double getLon() {
        return this.y;
    }
}
