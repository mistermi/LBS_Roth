package ohm.roth;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

import java.util.List;

/**
 * Berechnung von Distanzen
 */
public class distance {
    /**
     *  Standart Einheit fuehr Distanzberechnungen
     */
    public static distance.unit selectedUnit = unit.meter;

    /**
     * Standart Methode fuer die Distanzberechnungen
     */
    public static distance.distanceMethode selectedTyp = distanceMethode.roth;

    /**
     * Radius der Erde
     */
    public static double earthRadius = 6371000;


    /**
     * Beschreibung von Laengeneinheiten
     */
    public static enum unit {
        meter, km, miles
    }

    /**
     * Beschreibung von Methoden zur Distanzberechnungen
     */
    public static enum distanceMethode {
        jts, manual, roth
    }

    /**
     * Konvertiert einen Wert in die Angegebene Einheit
     * @param value Ner zu Konvertierende Wert
     * @param u Output Die Einheit
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
     * Konvertiert einen Wert in die Standart Einheit
     * @param value Der zu Konvertierende Wert
     * @return Converted Value
     */
    public static double unitConvert(double value) {
        return unitConvert(value, distance.selectedUnit);
    }

    /**
     * Berechnet den Abstand zwischen 2 Coordinaten
     * @param p1 Coordinate 1
     * @param p2 Coordinate 2
     * @return Der Abstand in der Standarteinheit
     */
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

    /**
     * Berechnet den Abstand zwischen 2 Nodes
     * @param n1 Node 1
     * @param n2 Node 2
     * @return Der Abstand in der Standarteinheit
     */
    public static double calcDist(Node n1, Node n2) {
        return calcDist(n1.getPosition(), n2.getPosition());
    }

    /**
     * Berechnet den Abstand zwischen einer node und einer Position
     * @param n Node
     * @param p Position
     * @return Der Abstand in der Standarteinheit
     */
    public static double calcDist(Node n, Position p) {
        return calcDist(n.getPosition(), p);
    }

    /**
     * Berechnet die Laenge einer Liste von WEgpunkten
     * @param list Die Liste
     * @return Der Abstand in der Standarteinheit
     */
    public static double distanceList(List<Waypoint> list) {
        double l = 0;
        for (int i = 1; i < list.size()-1; i++) {
            l += distance.calcDist(list.get(i - 1), list.get(i));
        }
        return l;
    }

    /**
     * Berechnet die Laenge eines Segments eines LineStrings
     * @param line Der LineString
     * @param start Der erste Punkt des LineStrings
     * @param end Der Letzte Punkt des Linestrings
     * @return Der Abstand in der Standarteinheit
     */
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

    /**
     * Berechnet die Laenge eines Linestrings
     * @param line Der LineString
     * @return Der Abstand in der Standarteinheit
     */
    public static double distanceLinestring(LineString line) {
        double dis = 0;
        for (int i = 0; i < line.getNumPoints() - 1; i++) {
            dis += calcDist(line.getPointN(i).getCoordinate(), line.getPointN(i + 1).getCoordinate());
        }
        return dis;
    }

}
