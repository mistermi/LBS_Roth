package ohm.roth;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Beschreibt einen Weg bestehend aus mehreren Segmenten
 */
public class Path {
    protected String name;
    protected List<PathSegment> segments;

    /**
     * Konstruktor
     *
     * @param name Der Name der Weges
     */
    public Path(String name) {
        this.segments = new ArrayList<PathSegment>();
        this.name = name;
    }

    /**
     * Gibt den Namen des Graphen zurueck
     *
     * @return Der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Aendert den namen des Graphen
     *
     * @param name Der neue Name des Graphen
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die Liste aller im Weg enthaltenen Segmenten zurueck
     *
     * @return Die Liste
     */
    public List<PathSegment> getSegments() {
        return segments;
    }

    /**
     * Fuegt dem Graphen ein neues Segment an
     *
     * @param segment Das anzufuegende Segment
     */
    public void addSegment(PathSegment segment) {
        this.segments.add(segment);
    }

    /**
     * Gibt die Liste aller im Weg enthaltenen POIs zurueck
     *
     * @return Die Liste
     */
    public List<Waypoint> getWaypoints() {
        List<Waypoint> retList = new ArrayList<Waypoint>();
        for (PathSegment seg : this.segments) {
            if (!retList.contains(seg.startWaypoint)) retList.add(seg.startWaypoint);
            if (!retList.contains(seg.endWaypoint)) retList.add(seg.endWaypoint);
        }
        return retList;
    }

    /**
     * Gibt den Pfad als LineString zurueck
     *
     * @param includingWaypoints Gibt an ob der Weg zu den POIs im LineString enthalten soll
     * @return Der Line String
     */
    public LineString getGeoLineString(boolean includingWaypoints) {
        GeometryFactory fac = new GeometryFactory();
        List<Coordinate> coords = new ArrayList<Coordinate>();
        for (PathSegment seg : this.segments) {
            coords.addAll(seg.getSegmentCoordinates(includingWaypoints));
        }

        Coordinate[] coordarray = coords.toArray(new Coordinate[coords.size()]);
        if (coordarray.length > 1)
            return fac.createLineString(coordarray);
        else
            return fac.createLineString(new Coordinate[0]);
    }

    /**
     * Gibt den Pfad als LineString zurueck (nur die Topologischen Informationen)
     * @return Der Line String
     */
    public LineString getTopoLineString() {
        GeometryFactory fac = new GeometryFactory();
        List<Coordinate> coords = new ArrayList<Coordinate>();
        for (PathSegment seg : this.segments) {
            coords.add(seg.getStartWaypoint());
            PathNode currNode = seg.getFirstNode();
            while (currNode != null) {
                coords.add(currNode.getNode().getPosition());
                currNode = currNode.getNextNode();
            }
            coords.add(seg.getEndWaypoint());
        }

        Coordinate[] coordarray = coords.toArray(new Coordinate[coords.size()]);
        if (coordarray.length > 1)
            return fac.createLineString(coordarray);
        else
            return fac.createLineString(new Coordinate[0]);
    }
}

