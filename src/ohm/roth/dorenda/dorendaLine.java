package ohm.roth.dorenda;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import ohm.roth.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * User: mischarohlederer
 * Date: 12.12.11
 * Time: 11:28
 */
public class dorendaLine {
    private Color lineColor;
    private List<Position> positions;
    private Markers marker;
    private boolean useStartFlag;
    private String startFlag;
    private boolean useEndFlag;
    private String endFlag;

    public dorendaLine(Color lineColor, List<Position> positions, Markers marker, String startFlag, String endFlag) {
        this.lineColor = lineColor;
        this.positions = positions;
        this.marker = marker;
        this.useStartFlag = true;
        this.startFlag = startFlag;
        this.useEndFlag = true;
        this.endFlag = endFlag;
    }

    public dorendaLine(Color lineColor, Markers marker, String startFlag, String endFlag) {
        this.lineColor = lineColor;
        this.positions = new ArrayList<Position>();
        this.marker = marker;
        this.useStartFlag = true;
        this.startFlag = startFlag;
        this.useEndFlag = true;
        this.endFlag = endFlag;
    }

    public dorendaLine(Color lineColor, List<Position> list, Markers marker) {
        this.lineColor = lineColor;
        this.positions = list;
        this.marker = marker;
        this.useStartFlag = false;
        this.useEndFlag = false;
    }

    public dorendaLine(Color lineColor, LineString line, Markers marker) {
        this.lineColor = lineColor;
        this.positions = new ArrayList<Position>();
        this.marker = marker;
        this.useStartFlag = false;
        this.useEndFlag = false;
        this.addLineString(line);
    }

    public dorendaLine(Color lineColor, Markers marker) {
        this.lineColor = lineColor;
        this.positions = new ArrayList<Position>();
        this.marker = marker;
        this.useStartFlag = false;
        this.useEndFlag = false;
    }

    public void addPosition(Position p) {
        positions.add(p);
    }

    public void addLineString(LineString line) {
        for (Coordinate coor : line.getCoordinates()) {
            positions.add(new Position(coor.x, coor.y));
        }
    }

    @Override
    public String toString() {
        if (positions.size() == 0) return "";
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        result.append("LINE col=").append(this.lineColor.getRed()).append(",").append(this.lineColor.getBlue()).append(",").append(this.lineColor.getGreen()).append(",").append(50).append(" mode=").append(this.marker.getCode());
        if (useStartFlag) result.append(" startflag=\"").append(startFlag).append("\"");
        if (useEndFlag) result.append(" endflag=\"").append(endFlag).append("\"");
        result.append(NEW_LINE);
        for (Position pos : positions) {
            result.append(pos.x).append(",").append(pos.y).append(NEW_LINE);
        }
        return result.toString();
    }

    public enum Markers {
        No_Marker(0), Lowdens_Marker(1), All_Markers(2), Number_Markers(3);

        private int code;

        private Markers(int c) {
            code = c;
        }

        public int getCode() {
            return code;
        }
    }

}
