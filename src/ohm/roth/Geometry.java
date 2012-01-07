package ohm.roth;

import com.vividsolutions.jts.geom.LineString;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Beschreibung einer Geometry innerhalb eines NavGraph
 */
public class Geometry implements Serializable {
    private String id;
    private String name;
    private String lsiclass;
    private LineString geometry;
    private HashMap<String, NodeGeoLink> connectedNodes;

    /**
     * Fuehgt dieser Geometrie einen Knoten hinzu
     * @param n Der Knoten
     * @param pos Die Position auf der sich der Knoten befindet
     */
    public void addNode(Node n, int pos) {
        if (!this.connectedNodes.containsKey(n.getId()))
            this.connectedNodes.put(n.getId(), new NodeGeoLink(n, pos));
    }

    /**
     * Die Id der Geometrie
     * @return GAOID
     */
    public String getId() {
        return id;
    }

    /**
     * Die Geometrie als LineString
     * @return LineString
     */
    public LineString getGeometry() {
        return geometry;
    }

    /**
     * Eine HashMap aller Knoten auf dieser Geometrie
     * @return Hashmap
     */
    public HashMap<String, NodeGeoLink> getConnectedNodes() {
        return connectedNodes;
    }

    /**
     * Der Name der Geometry
     * @return Der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Die LSI Klasse der Geometrie
     * @return LSI Klasse
     */
    public String getLsiclass() {
        return lsiclass;
    }

    /**
     * Konstruktor fuer eine Geometrie
     * @param id GAOID
     * @param name Real Name
     * @param lsi LSI Klasse
     * @param line Der Line String
     */
    public Geometry(String id, String name, String lsi, LineString line) {
        this.id = id;
        this.name = name;
        this.lsiclass = lsi;
        this.connectedNodes = new HashMap<String, NodeGeoLink>();
        this.geometry = line;
    }
}
