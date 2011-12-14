package ohm.roth;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

import java.util.List;

public class distance {
    public static distance.unit selectedUnit = unit.meter;
    public static distance.distanceMethode selectedTyp = distanceMethode.roth;
    public static double earthRadius = 6371000;

    public static enum unit {
        meter, km, miles
    }

    public static enum distanceMethode {
        jts, manual, roth
    }

    /**
     * Coverts the given value in Meters into the given Unit
     * @param value Input Value in Meter
     * @param u Output Unit
     * @return Converted Value
     */
    public static double unitConvert(double value, unit u) {
        switch (u) {
            case meter:
                return value;
            case km:
                return value / 1000;
            case miles:
                return value / 1000 / 0.621371192;
            default:
                return 1;
        }
    }

    /**
     * Converts the given value in Meters into the default Unit
     * @param value Input Value in Meter
     * @return Converted Value
     */
    public static double unitConvert(double value) {
        return unitConvert(value, distance.selectedUnit);
    }


    public static double calcDist(Coordinate p1, Coordinate p2) {
        double dis;
        switch (distance.selectedTyp) {
            case jts:
                dis = p1.distance(p2) * (Math.PI / 180) * earthRadius;
                break;
            case manual:
                double dLat = Math.toRadians(p2.y - p1.y);
                double dLon = Math.toRadians(p2.x - p1.x);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +  Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(p1.y) * Math.cos(p2.y);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                dis = earthRadius * c;
                break;
            case roth:
                dis = fu.gps.Spherical.greatCircleMeters(p1.y, p1.x, p2.y, p2.x);
                break;
            default:
                dis = p1.distance(p2) * (Math.PI / 180) * earthRadius;
                break;
        }
        if (Double.isNaN(dis))
            dis = 0;
        return unitConvert(dis, selectedUnit);
    }

    public static double calcDist(Node n1, Node n2) {
        return calcDist(n1.getPosition(), n2.getPosition());
    }

    public static double calcDist(Node n, Position p) {
        return calcDist(n.getPosition(), p);
    }

    public static double distanceList(List<Waypoint> list) {
        double l = 0;
        for (int i = 1; i < list.size(); i++) {
            l += distance.calcDist(list.get(i - 1), list.get(i % list.size()));
        }
        return l;
    }

    public static double distanceLinestring(LineString line, int start, int end) {
        double dis = 0;
        if (end == start) return 0;
        if (end >= line.getNumPoints()) return -1;
        if (end < start) {
            int foo = end;
            end = start;
            start = foo;
        }
        for (int i = start; i < end - 1; i++) {
            dis += calcDist(line.getPointN(i).getCoordinate(), line.getPointN(i + 1).getCoordinate());
        }
        return dis;
    }

    public static double distanceLinestring(LineString line) {
        double dis = 0;
        for (int i = 0; i < line.getNumPoints() - 1; i++) {
            dis += calcDist(line.getPointN(i).getCoordinate(), line.getPointN(i + 1).getCoordinate());
        }
        return dis;
    }

}
