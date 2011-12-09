package ohm.roth.lbs;

import com.vividsolutions.jts.geom.LineString;

import java.io.Serializable;
import java.util.HashMap;

/**
 * User: mischarohlederer
 * Date: 02.12.11
 * Time: 09:01
 */
public class Geometry  implements Serializable {
    private String id;
    private String name;
    private String lsiclass;
    private LineString geometry;
    private HashMap<String, Edge> connectedEdges;
    private HashMap<String, NodeGeoLink> connectedNodes;

    public void addEdge(Edge e) {
        if (!this.connectedEdges.containsKey(e.getId()))
            this.connectedEdges.put(e.getId(), e);
    }

    public void addNode(Node n, int pos) {
        if (!this.connectedNodes.containsKey(n.getId()))
            this.connectedNodes.put(n.getId(), new NodeGeoLink(n, pos));
    }

    public String getId() {
        return id;
    }

    public LineString getGeometry() {
        return geometry;
    }

    public HashMap<String, Edge> getConnectedEdges() {
        return connectedEdges;
    }

    public void setGeometry(LineString geometry) {
        this.geometry = geometry;
    }

    public HashMap<String, NodeGeoLink> getConnectedNodes() {
        return connectedNodes;
    }

    public String getName() {
        return name;
    }

    public String getLsiclass() {
        return lsiclass;
    }

    public Geometry(String id, String name, String lsi) {
        this.id = id;
        this.name = name;
        this.lsiclass = lsi;
        this.connectedEdges = new HashMap<String, Edge>();
        this.connectedNodes = new HashMap<String, NodeGeoLink>();
    }

    public Geometry(String id, String name, String lsi, LineString line) {
        this.id = id;
        this.name = name;
        this.lsiclass = lsi;
        this.connectedEdges = new HashMap<String, Edge>();
        this.connectedNodes = new HashMap<String, NodeGeoLink>();
        this.geometry = line;
    }
}
