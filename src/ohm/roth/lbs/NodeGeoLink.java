package ohm.roth.lbs;

import java.io.Serializable;

/**
 * User: mischarohlederer
 * Date: 05.12.11
 * Time: 21:19
 */
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
