package ohm.roth;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import java.io.Serializable;

public class Position extends Coordinate implements Serializable {
    public Point getPoint() {
        GeometryFactory fac = new GeometryFactory();
        return fac.createPoint(this);
    }

    public Position(double x, double y) {
        super(x, y);
    }

    public double getLat() {
        return this.x;
    }

    public double getLon() {
        return this.y;
    }
}
