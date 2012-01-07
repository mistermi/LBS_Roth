package ohm.roth;

import java.io.Serializable;

/**
 * Beschreibt die Verbindung zwischen einen Knotenpunkt und einer Geometry
  */
public class NodeGeoLink implements Serializable {
    private Node node;
    private int geoPos;

    /**
     * Konstruktor
     * @param node Der Knotenpunkt
     * @param geoPos Die Position auf der Geometry
     */
    public NodeGeoLink(Node node, int geoPos) {
        this.node = node;
        this.geoPos = geoPos;
    }

    /**
     * Der Knotenpunkt
     * @return Knotenpunkt
     */
    public Node getNode() {
        return node;
    }

    /**
     * Die Position des Knotenpunkt auf der Geometry
     * @return Die Position
     */
    public int getGeoPos() {
        return geoPos;
    }
}
