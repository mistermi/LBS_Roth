package ohm.roth.lbs;

/**
 * User: mischarohlederer
 * Date: 25.10.11
 * Time: 19:41
 */
public class PathNode {
    protected String name;

    protected Node node;
    protected PathNode prefNode;
    protected Edge prefEdge;
    protected PathNode nextNode;
    protected Edge nextEdge;

    public PathNode(String name, Node node) {
        this.name = name;
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public PathNode getPrefNode() {
        return prefNode;
    }

    public void setPrefNode(PathNode prefNode) {
        this.prefNode = prefNode;
    }

    public Edge getPrefEdge() {
        return prefEdge;
    }

    public void setPrefEdge(Edge prefEdge) {
        this.prefEdge = prefEdge;
    }

    public PathNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(PathNode nextNode) {
        this.nextNode = nextNode;
    }

    public Edge getNextEdge() {
        return nextEdge;
    }

    public void setNextEdge(Edge nextEdge) {
        this.nextEdge = nextEdge;
    }
}
