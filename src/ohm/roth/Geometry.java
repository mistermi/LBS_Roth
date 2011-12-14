package ohm.roth;

import com.vividsolutions.jts.geom.LineString;

import java.io.Serializable;
import java.util.HashMap;

public class Geometry implements Serializable {
    private String id;
    private String name;
    private String lsiclass;
    private LineString geometry;
    private HashMap<String, NodeGeoLink> connectedNodes;

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

    public HashMap<String, NodeGeoLink> getConnectedNodes() {
        return connectedNodes;
    }

    public String getName() {
        return name;
    }

    public String getLsiclass() {
        return lsiclass;
    }

    public Geometry(String id, String name, String lsi, LineString line) {
        this.id = id;
        this.name = name;
        this.lsiclass = lsi;
        this.connectedNodes = new HashMap<String, NodeGeoLink>();
        this.geometry = line;
    }
}
