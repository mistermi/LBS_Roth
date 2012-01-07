package ohm.roth;

/**
 * Beschreibt eine Node (Knotenpunkt) in einem Weg
 * Verkettete Liste
 */
public class PathNode {
    protected String name;

    protected Node node;
    protected PathNode prefNode;
    protected Edge prefEdge;
    protected PathNode nextNode;
    protected Edge nextEdge;

    /**
     * Konstruktor
     * @param name Der Name (ID) Des Knotenpunktes
     * @param node Der eigentliche Knotenpunkt
     */
    public PathNode(String name, Node node) {
        this.name = name;
        this.node = node;
    }

    /**
     * Der Name des Knotenpunktes
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Das Node (Knotenpunkt) Object
     * @return Node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Die vorhaerige Node
     * @return PathNode
     */
    public PathNode getPrefNode() {
        return prefNode;
    }

    /**
     * Aendert die vorhaerige Node
     * @param prefNode PathNode
     */
    public void setPrefNode(PathNode prefNode) {
        this.prefNode = prefNode;
    }

    /**
     * Die Kante zur vorhaerigen Node
     * @return Edge
     */
    public Edge getPrefEdge() {
        return prefEdge;
    }

    /**
     * Aendert die Kante zur vorhaerigen Node
     * @param prefEdge Die neue Edge
     */
    public void setPrefEdge(Edge prefEdge) {
        this.prefEdge = prefEdge;
    }

    /**
     * Die naechste Node
     * @return PathNode
     */
    public PathNode getNextNode() {
        return nextNode;
    }

    /**
     * Aendert die naechste Node
     * @param nextNode PathNode
     */
    public void setNextNode(PathNode nextNode) {
        this.nextNode = nextNode;
    }

    /**
     * Die Kante zur naechste Node
     * @return Edge
     */
    public Edge getNextEdge() {
        return nextEdge;
    }

    /**
     * Aendert die Kante zur naechsten Node
     * @param nextEdge Die neue Edge
     */
    public void setNextEdge(Edge nextEdge) {
        this.nextEdge = nextEdge;
    }
}
