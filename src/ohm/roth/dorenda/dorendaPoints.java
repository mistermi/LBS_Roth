package ohm.roth.dorenda;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import ohm.roth.Position;

import java.awt.*;
import java.util.*;

/**
 * Beschreibung einer Sammlung von Punkten fuer ein Dorenda Dokument
 */
public class dorendaPoints {
    private Color lineColor;
    private java.util.List<Position> positions;
    private boolean useText;
    private int radius;
    private String text;

    /**
     * Konstruktor einer Sammlung von Punkten fuer ein Dorenda Dokument
     * @param pointsColor Farbe der Punkte
     * @param positions Liste mit Punkten (Positionen)
     */
    public dorendaPoints(Color pointsColor, java.util.List<Position> positions) {
        this.lineColor = pointsColor;
        this.positions = positions;
        this.radius = 20;
        this.useText = false;
    }

    /**
     * Konstruktor einer Sammlung von Punkten fuer ein Dorenda Dokument
     * @param pointsColor Farbe der Punkte
     * @param position Position des Punktes
     */
    public dorendaPoints(Color pointsColor, Position position) {
        this.lineColor = pointsColor;
        this.positions = new ArrayList<Position>();
        this.positions.add(position);
        this.radius = 20;
        this.useText = false;
    }

    /**
     * Konstruktor einer Sammlung von Punkten fuer ein Dorenda Dokument
     * @param pointsColor Farbe der Punkte
     * @param radius Radius der Markierung
     * @param position Position des Punktes
     */
    public dorendaPoints(Color pointsColor, int radius, Position position) {
        this.lineColor = pointsColor;
        this.positions = new ArrayList<Position>();
        this.positions.add(position);
        this.radius = radius;
        this.useText = false;
    }

    /**
     * Konstruktor einer Sammlung von Punkten fuer ein Dorenda Dokument
     * @param pointsColor Farbe der Punkte
     * @param text Beschrifftung der Punkte
     * @param position Position des Punktes
     */
    public dorendaPoints(Color pointsColor, String text, Position position) {
        this.lineColor = pointsColor;
        this.positions = new ArrayList<Position>();
        this.positions.add(position);
        this.radius = 20;
        this.useText = true;
        this.text = text;
    }

    /**
     * Konstruktor einer Sammlung von Punkten fuer ein Dorenda Dokument
     * @param pointsColor Farbe der Punkte
     * @param text Beschrifftung der Punkte
     * @param radius Radius der Markierung
     * @param position Position des Punktes
     */
    public dorendaPoints(Color pointsColor, String text, int radius, Position position) {
        this.lineColor = pointsColor;
        this.positions = new ArrayList<Position>();
        this.positions.add(position);
        this.radius = radius;
        this.useText = true;
        this.text = text;
    }

    /**
     * Fuegt der Sammlung einen Punkt all Position hinzu
     * @param p Die hinzuzufuegende Position
     */
    public void addPosition(Position p) {
        positions.add(p);
    }

    /**
     * Fuegt der Sammlung alle Punkte eines Line Strings hinzu
     * @param line Der Hinzuzufuegende LineString
     */
    public void addLineString(LineString line) {
        for (Coordinate coor : line.getCoordinates()) {
            positions.add((Position) coor);
        }
    }

    /**
     * Wandelt die Sammlung von Punkten in das Dorenda Format um
     * @return Der Text der die Punkte in Doranda beschreibt
     */
    @Override
    public String toString() {
        if (positions.size() == 0) return "";
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        result.append("POSITIONS col=").append(this.lineColor.getRed()).append(",").append(this.lineColor.getGreen()).append(",").append(this.lineColor.getBlue()).append(",").append(this.lineColor.getAlpha()).append(" rad=").append(this.radius);
        if (useText) result.append(" text=\"").append(text).append("\"");
        result.append(NEW_LINE);
        for (Position pos : positions) {
            result.append(pos.x).append(",").append(pos.y).append(NEW_LINE);
        }
        return result.toString();
    }

}
