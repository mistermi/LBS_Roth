package ohm.roth.lbs;

/**
 * User: mischarohlederer
 * Date: 07.11.11
 * Time: 11:43
 */
public class PathSegment {
    protected PathNode firstNode;
    protected PathNode lastNode;

    public boolean isEmpty() {
        if (firstNode == null) return true; else return false;
    }

    public double getLength() {
        if (this.isEmpty()) return 0;
        double length=0;
        PathNode currNode = this.firstNode;
        while (currNode.getNextNode() != null) {
            length += currNode.getNextEdge().getCost();
            currNode = currNode.getNextNode();
        }
        return length;
    }

    public int getNodeCount() {
        if (this.isEmpty()) return 0;
        int count=0;
        PathNode currNode = this.firstNode;
        while (currNode.getNextNode() != null) {
            count++;
            currNode = currNode.getNextNode();
        }
        return count;
    }

    public PathNode getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(PathNode firstNode) {
        this.firstNode = firstNode;
    }

    public PathNode getLastNode() {
        return lastNode;
    }

    public void setLastNode(PathNode lastNode) {
        this.lastNode = lastNode;
    }

}
