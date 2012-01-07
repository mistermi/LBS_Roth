package ohm.roth.dorenda;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import ohm.roth.*;
import ohm.roth.astar.AStarResult;

import java.awt.*;
import java.util.List;

import static java.awt.Color.*;

/**
 * Erstellt Dorender Documente aus Verschiedenen Klassen
 */
public class DORENDABuilder {

    /**
     * Erstellt ein Dorenda Dokument aus einem Path Objekt
     * @param path Das Path Objekt aus dem das Dokument erstellt werden soll
     * @return Dorenda Dokument als String
     */
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

            dorendaLine line = new dorendaLine(Color.BLUE, dorendaLine.Markers.No_Marker);
            line.addLineString(seg.getSegmentLine(false));

            doc.addLine(lineToStart);
            doc.addLine(lineToEnd);
            doc.addLine(line);
        }


        return doc.toString();
    }

    /**
     * Erstellt ein Dorenda Dokument aus einem Result Objekt (Expandierter A-Stern Graph)
     * @param result Das Result Object
     * @param graph Der dazugehoerige Graph
     * @return Dorenda Dokument als String
     */
    public static String build(AStarResult result, NavGraph graph) {
        dorendaDocument doc = new dorendaDocument();
        for (String edgename : result.getExpandedEdges()) {
            Edge curr = graph.getEdgeList().get(edgename);
            dorendaLine currLine = new dorendaLine(Color.blue, curr.getEdgeGeo(), dorendaLine.Markers.No_Marker);
            doc.addLine(currLine);
        }

        dorendaLine finalLine = new dorendaLine(Color.red, result.getPath().getSegmentLine(false), dorendaLine.Markers.No_Marker);
        doc.addLine(finalLine);
        doc.addPoints(new dorendaPoints(
                RED,
                "Start",
                result.getPath().getFirstNode().getNode().getPosition()));
         doc.addPoints(new dorendaPoints(
                RED,
                "End",
                result.getPath().getLastNode().getNode().getPosition()));


        return doc.toString();

    }

    /**
     * Erstellt ein Dorenda Dokument aus einem LineString
     * @param lineString Der Line String
     * @return Dorenda Dokument als String
     */
    public static String build(LineString lineString) {
        dorendaDocument doc = new dorendaDocument();
            dorendaLine line = new dorendaLine(BLUE, dorendaLine.Markers.No_Marker);
            line.addLineString(lineString);
            doc.addLine(line);

        return doc.toString();
    }

    /**
     * Erstellt Ein Dorenda Object aus einer Liste von Edges
     * Die Edges werden nur als Id angegeben
     * @param list Liste mit den Edge Ids
     * @param graph Der zugehoerige Graph
     * @return Dorenda Dokument als String
     */
    public static String build(List<String> list, NavGraph graph) {
        dorendaDocument doc = new dorendaDocument();
        for (String curr : list) {
            Edge currEde = graph.getEdgeList().get(curr);
            dorendaLine line = new dorendaLine(BLUE, dorendaLine.Markers.No_Marker);
            line.addLineString(currEde.getEdgeGeo());
            doc.addLine(line);
        }
        return doc.toString();
    }

    /**
     * Erstellt ein Dorenda Objekt aus einem Graphen
     * @param graph Der Graph
     * @param endPoints Gibt an ob endpunkte in dem Graphen enthalten sein sollen
     * @param geo Gibt an ob Die exakte Geometrie ausgegeben werden soll
     * @return Dorenda Dokument als String
     */
    public static String build(NavGraph graph, boolean endPoints, boolean geo) {
        dorendaDocument doc = new dorendaDocument();
        for (Edge currEdge : graph.getEdgeList().values()) {
            if (endPoints && !(currEdge.getFrom().startsWith("FIRST") || currEdge.getFrom().startsWith("LAST") || currEdge.getTo().startsWith("FIRST") || currEdge.getTo().startsWith("LAST")) || !endPoints) {
                dorendaLine line = new dorendaLine(BLUE, dorendaLine.Markers.No_Marker);
                if (geo) {
                    line.addLineString(currEdge.getEdgeGeo());
                } else {
                    line.addPosition(graph.getNode(currEdge.getFrom()).getPosition());
                    line.addPosition(graph.getNode(currEdge.getTo()).getPosition());
                }
                doc.addLine(line);
            }
        }
        return doc.toString();
    }
}
