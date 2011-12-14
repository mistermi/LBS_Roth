package ohm.roth;

import java.io.Serializable;

public class NodeGeoLink implements Serializable {
    private Node node;
    private int geoPos;

    public NodeGeoLink(Node node, int geoPos) {
        this.node = node;
        this.geoPos = geoPos;
    }

    public Node getNode() {

        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getGeoPos() {
        return geoPos;
    }

    public void setGeoPos(int geoPos) {
        this.geoPos = geoPos;
    }
}
