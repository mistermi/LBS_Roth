package ohm.roth.dorenda;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import ohm.roth.Path;
import ohm.roth.PathSegment;
import ohm.roth.Waypoint;

import static java.awt.Color.*;

public class DORENDABuilder {
    public static String build(Path path) {
        dorendaDocument doc = new dorendaDocument();
        // Waypoints
        for (Waypoint way : path.getWaypoints()) {
            doc.addPoints(new dorendaPoints(
                    RED,
                    way.getName(),
                    way
            ));
        }

        // Segments
        for (PathSegment seg : path.getSegments()) {
            dorendaLine lineToStart = new dorendaLine(CYAN, dorendaLine.Markers.No_Marker);
            lineToStart.addPosition(seg.getStartWaypoint());
            lineToStart.addPosition(seg.getFirstNode().getNode().getPosition());

            dorendaLine lineToEnd = new dorendaLine(CYAN, dorendaLine.Markers.No_Marker);
            lineToEnd.addPosition(seg.getEndWaypoint());
            lineToEnd.addPosition(seg.getLastNode().getNode().getPosition());

            dorendaLine line = new dorendaLine(BLUE, dorendaLine.Markers.No_Marker);
            line.addLineString(seg.getSegmentLine(false));

            doc.addLine(lineToStart);
            doc.addLine(lineToEnd);
            doc.addLine(line);
        }


        return doc.toString();
    }


    public static String build(LineString lineString) {
        dorendaDocument doc = new dorendaDocument();
            dorendaLine line = new dorendaLine(BLUE, dorendaLine.Markers.No_Marker);
            line.addLineString(lineString);
            doc.addLine(line);

        return doc.toString();
    }
}
