package ohm.roth.astar;

import ohm.roth.Edge;
import ohm.roth.Node;

/**
 * User: mischarohlederer
 * Date: 12.12.11
 * Time: 11:36
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

    public AStarNode(Node n) {
        super();
        this.node = n;
    }

    public Edge getCameFromEdge() {
        return cameFromEdge;
    }

    public void setCameFromEdge(Edge cameFromEdge) {
        this.cameFromEdge = cameFromEdge;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getF() {
        return this.f;
    }

    public Node getNode() {
        return node;
    }

    public String getName() {
        return node.getId();
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public AStarNode getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(AStarNode cameFrom) {
        this.cameFrom = cameFrom;
    }

    public void refreshG() {
        this.g = this.cameFrom.getG() + this.cameFromEdge.getCost();
    }

    public double getG() {
        if (this.cameFrom == null) {
            return 0;
        } else {
            return this.g;
        }
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getCostCameFrom() {
        return costCameFrom;
    }


    public void setCostCameFrom(double costCameFrom) {
        this.costCameFrom = costCameFrom;
    }


    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

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
