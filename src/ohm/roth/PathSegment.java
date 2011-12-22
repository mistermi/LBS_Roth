package ohm.roth;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mischarohlederer
 * Date: 07.11.11
 * Time: 11:43
 */
public class PathSegment {
    protected Waypoint startWaypoint;
    protected Waypoint endWaypoint;
    protected PathNode firstNode;
    protected PathNode lastNode;

    public boolean isEmpty() {
        return firstNode == null;
    }

    public double getLength() {
        return this.getLength(false);
    }

    public double getLength(boolean includingWaypoints) {
        if (this.isEmpty()) return 0;
        double length = 0;
        PathNode currNode = this.firstNode;
        while (currNode.getNextNode() != null) {
            length += currNode.getNextEdge().getCost();
            currNode = currNode.getNextNode();
        }
        if (includingWaypoints) {
            length += distance.calcDist(firstNode.getNode().getPosition(), startWaypoint);
            length += distance.calcDist(lastNode.getNode().getPosition(), endWaypoint);
        }
        return length;
    }

    public List<Coordinate> getSegmentCoordinates(boolean includingWaypoints) {
        List<Coordinate> coords = new ArrayList<Coordinate>();
        PathNode currNode = this.getFirstNode();
        if (includingWaypoints) coords.add(startWaypoint);
        while (currNode != null) {
            if (currNode.getNextEdge() != null) {
                for (Coordinate coord : currNode.getNextEdge().getEdgeGeo().getCoordinates()) {
                    coords.add(coord);
                }
            }
            currNode = currNode.getNextNode();
        }
        if (includingWaypoints) coords.add(endWaypoint);
        return coords;
    }

    public LineString getSegmentLine(boolean includingWaypoints) {
        List<Coordinate> coords = this.getSegmentCoordinates(includingWaypoints);
        GeometryFactory fac = new GeometryFactory();
        Coordinate[] coordarray = coords.toArray(new Coordinate[coords.size()]);
        if (coordarray.length > 1)
            return fac.createLineString(coordarray);
        else
            return fac.createLineString(new Coordinate[0]);
    }

    public Waypoint getStartWaypoint() {
        return startWaypoint;
    }

    public Waypoint getEndWaypoint() {
        return endWaypoint;
    }

    public void setStartWaypoint(Waypoint startWaypoint) {
        this.startWaypoint = startWaypoint;
    }

    public void setEndWaypoint(Waypoint endWaypoint) {
        this.endWaypoint = endWaypoint;
    }

    public int getNodeCount() {
        if (this.isEmpty()) return 0;
        int count = 0;
        PathNode currNode = this.firstNode;
        while (currNode.getNextNode() != null) {
            count++;
            currNode = currNode.getNextNode();
        }
        return count;
    }

    public PathNode getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(PathNode firstNode) {
        this.firstNode = firstNode;
    }

    public PathNode getLastNode() {
        return lastNode;
    }

    public void setLastNode(PathNode lastNode) {
        this.lastNode = lastNode;
    }

}
