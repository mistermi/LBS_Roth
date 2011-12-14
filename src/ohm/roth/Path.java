package ohm.roth;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Path {
    protected String name;
    protected List<PathSegment> segments;

    public Path(String name) {
        this.segments = new ArrayList<PathSegment>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PathSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<PathSegment> segments) {
        this.segments = segments;
    }

    public void addSegment(PathSegment segment) {
        this.segments.add(segment);
    }

    public List<Waypoint> getWaypoints() {
        List<Waypoint> retList = new ArrayList<Waypoint>();
        for (PathSegment seg : this.segments) {
            if (!retList.contains(seg.startWaypoint)) retList.add(seg.startWaypoint);
            if (!retList.contains(seg.endWaypoint)) retList.add(seg.endWaypoint);
        }
        return retList;
    }

    public LineString getGeoLineString() {
        GeometryFactory fac = new GeometryFactory();
        List<Coordinate> coords = new ArrayList<Coordinate>();
        for (PathSegment seg : this.segments) {
            coords.addAll(seg.getSegmentCoordinates(true));
        }

        Coordinate[] coordarray = coords.toArray(new Coordinate[coords.size()]);
        if (coordarray.length > 1)
            return fac.createLineString(coordarray);
        else
            return fac.createLineString(new Coordinate[0]);
    }

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

