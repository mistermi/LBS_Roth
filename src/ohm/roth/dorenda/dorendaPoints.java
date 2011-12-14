package ohm.roth.dorenda;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import ohm.roth.Position;

import java.awt.*;
import java.util.ArrayList;

/**
 * User: mischarohlederer
 * Date: 12.12.11
 * Time: 11:28
 */
public class dorendaPoints {
    private Color lineColor;
    private java.util.List<Position> positions;
    private boolean useText;
    private int radius;
    private String text;

    public dorendaPoints(Color lineColor, java.util.List<Position> positions) {
        this.lineColor = lineColor;
        this.positions = positions;
        this.radius = 20;
        this.useText = false;
    }

    public dorendaPoints(Color lineColor, Position position) {
        this.lineColor = lineColor;
        this.positions = new ArrayList<Position>();
        this.positions.add(position);
        this.radius = 20;
        this.useText = false;
    }

    public dorendaPoints(Color lineColor, int radius, Position position) {
        this.lineColor = lineColor;
        this.positions = new ArrayList<Position>();
        this.positions.add(position);
        this.radius = radius;
        this.useText = false;
    }

    public dorendaPoints(Color lineColor, String text, Position position) {
        this.lineColor = lineColor;
        this.positions = new ArrayList<Position>();
        this.positions.add(position);
        this.radius = 20;
        this.useText = true;
        this.text = text;
    }

    public dorendaPoints(Color lineColor, String text, int radius, Position position) {
        this.lineColor = lineColor;
        this.positions = new ArrayList<Position>();
        this.positions.add(position);
        this.radius = radius;
        this.useText = true;
        this.text = text;
    }

    public void addPosition(Position p) {
        positions.add(p);
    }

    public void addLineString(LineString line) {
        for (Coordinate coor : line.getCoordinates()) {
            positions.add((Position) coor);
        }
    }

    @Override
    public String toString() {
        if (positions.size() == 0) return "";
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        result.append("POSITIONS col=").append(this.lineColor.getRed()).append(",").append(this.lineColor.getBlue()).append(",").append(this.lineColor.getGreen()).append(",").append(this.lineColor.getAlpha()).append(" rad=").append(this.radius);
        if (useText) result.append(" text=\"").append(text).append("\"");
        result.append(NEW_LINE);
        for (Position pos : positions) {
            result.append(pos.x).append(",").append(pos.y).append(NEW_LINE);
        }
        return result.toString();
    }

}
