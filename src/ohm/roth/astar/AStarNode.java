package ohm.roth.astar;

import ohm.roth.Edge;
import ohm.roth.Node;

/**
 * Beschreibung eines Knotenpunktes fuer den A-Stern Algo
 */
public class AStarNode implements Comparable<AStarNode> {
    private Node node;
    //used to construct the path after the search is done
    private AStarNode cameFrom;
    private Edge cameFromEdge;
    private double costCameFrom;
    private double g;
    private double f;
    // Distance from source along optimal path

    // Heuristic estimate of distance from the current node to the target node
    private double h;

    /**
     * Construktor
     * @param n Die Referenzierte Node
     */
    public AStarNode(Node n) {
        super();
        this.node = n;
    }

    /**
     * Die davor liegende Kante auf dem (bis jetzt) optimalen Weg zu dieser Node
     * @return Die kante
     */
    public Edge getCameFromEdge() {
        return cameFromEdge;
    }

    /**
     * Aendert die davor liegende Kante auf dem (bis jetzt) besten Weg zu dieser Node
     * @param cameFromEdge Die neue Kante
     */
    public void setCameFromEdge(Edge cameFromEdge) {
        this.cameFromEdge = cameFromEdge;
    }

    /**
     * Setzt ein neues F (Weg vom Start bis zu dieser Node + geschaetzter Weg zum Ziel)
     * @param f Der neue Wert
     */
    public void setF(double f) {
        this.f = f;
    }

    /**
     * Gibt F fuer die Node zurueck (Weg vom Start bis zu dieser Node + geschaetzter Weg zum Ziel)
     * @return F
     */
    public double getF() {
        return this.f;
    }

    /**
     * Gibt das Referenzierte Node Objekt zurueck
     * @return Die Node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Gibt die ID der Node Zurueck
     * @return Die ID
     */
    public String getName() {
        return node.getId();
    }

    /**
     * Gibt die davorliegende Node Zurueck (Null falls es die erste Node ist)
     * @return Die Node oder Null
     */
    public AStarNode getCameFrom() {
        return cameFrom;
    }

    /**
     * Setzt die Davorliegende Node auf dem (bis jetzt) besten weg zur Node
     * @param cameFrom Die Node
     */
    public void setCameFrom(AStarNode cameFrom) {
        this.cameFrom = cameFrom;
    }

    /**
     * Gibt den realen Weg vom Start bis zu dieser Node zurueck
     * @return G
     */
    public double getG() {
        if (this.cameFrom == null) {
            return 0;
        } else {
            return this.g;
        }
    }

    /**
     * Setzt die kosten vom Start bis zu dieser bis zu dieser Node
     * @param g Die Kosten
     */
    public void setG(double g) {
        this.g = g;
    }

    /**
     * Gibt die Kosten der kante zum davor liegenden Knoten zurueck
     * @return Die Kosten
     */
    public double getCostCameFrom() {
        if (this.cameFrom == null) {
            return 0;
        } else {
            return this.cameFromEdge.getCost();
        }
    }

    /**
     * Gibt die geschaetzen Kosten vom Aktuellen Knoten bis zum Ziel zurueck
     * @return H
     */
    public double getH() {
        return h;
    }

    /**
     * Setzt die geschaetzen Kosten vom Aktuellen Knoten bis zum Ziel
     * @param h H
     */
    public void setH(double h) {
        this.h = h;
    }

    /**
     * Vergleicht zwei Node auf Basis der Kosten
     * @param arg0 Die 2. Node
     * @return Vergleichsrueckgabe
     */
    public int compareTo(AStarNode arg0) {
        if (this.getF() < arg0.getF()) {
            return -1;
        } else if (this.getF() > arg0.getF()) {
            return 1;
        } else {
            return 0;
        }
    }
}
