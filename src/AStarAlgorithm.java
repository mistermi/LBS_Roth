import ohm.roth.lbs.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// A-Star

public class AStarAlgorithm {
    protected double w;
    protected int debug;

    public AStarAlgorithm(double w, int debug) {
        super();
        this.w = w;
        this.debug = debug;
    }

    public AStarAlgorithm() {
        super();
        this.w = 1;
        this.debug = 0;
    }

    public AStarResult search(NavGraph graph, Node source, Node target, String name) throws IOException {
        return search(graph, source, target, name, true);
    }

    public AStarResult search(NavGraph graph, Node source, Node target, String name, boolean buildExpanded) throws IOException {
        BufferedWriter outExpanded = null;
        FileWriter fstreamExpanded = null;
        if (buildExpanded) {
            fstreamExpanded = new FileWriter("Expanded Graph - " + name + ".txt");
            outExpanded = new BufferedWriter(fstreamExpanded);
        }

        long time;
        int expNodes = 1;
        int pathNodes = 0;
        time = System.currentTimeMillis();
        SortableValueMap<String, AStarNode> openSet = new SortableValueMap<String, AStarNode>();
        SortableValueMap<String, AStarNode> closeSet = new SortableValueMap<String, AStarNode>();
        AStarNode start = new AStarNode(source);
        openSet.put(source.getName(), start);

        AStarNode goal = null;
        while (openSet.size() > 0) {
            openSet.sortByValue();
            AStarNode x = openSet.get(openSet.keySet().toArray()[0]);
            openSet.remove(x.getName());
            if (x.getName().equals(target.getName())) {
                // found
                goal = x;
                break;
            } else {
                closeSet.put(x.getName(), x);
                expNodes++;
                List<Edge> neighborsEdges = x.getNode().getNeighbors();
                for (Edge neighborEdge : neighborsEdges) {
                    Node neighbor = graph.getNode(neighborEdge.getTo());
                    if (closeSet.containsKey(neighbor.getName()))
                        continue;
                    double g = x.getG() + neighborEdge.getCost();
                    double h = distance.calcDist(neighbor, target) * this.w;
                    double f = g + h;
                    AStarNode n = openSet.get(neighbor.getName());
                    if (n == null) {
                        // Node noch nicht in Open
                        if (buildExpanded) {
                            outExpanded.write("LINE mode=1 col=0,0,255,75\n");
                            outExpanded.write(x.getNode().getPosition().getLon() + ","
                                + x.getNode().getPosition().getLat() + "\n");
                            outExpanded.write(neighbor.getPosition().getLon() + ","
                                + neighbor.getPosition().getLat() + "\n");
                        }
                        n = new AStarNode(neighbor);
                        n.setCameFrom(x);
                        n.setCostCameFrom(neighborEdge.getCost());
                        n.setCameFromEdge(neighborEdge);
                        n.setG(g);
                        n.setF(f);
                        openSet.put(neighbor.getName(), n);
                    } else if (f < n.getF()) {
                        // Node in open aber "neuer" Weg ist kuerzer
                        n.setCameFrom(x);
                        n.setCostCameFrom(neighborEdge.getCost());
                        n.setCameFromEdge(neighborEdge);
                        n.setF(f);
                        n.setG(g);
                    }
                }

            }
        }
        if (goal != null) {
            PathSegment retPath = new PathSegment();
            PathNode lastNode = new PathNode("TODO NodeNames", goal.getNode());
            PathNode currNode = null;
            retPath.setLastNode(lastNode);


            AStarNode parent = goal;
            double length = 0;
            while (parent.getCameFrom() != null) {
                length += parent.getCostCameFrom();
                currNode = new PathNode("TODO NodeNames", parent.getCameFrom().getNode());
                lastNode.setPrefNode(currNode);
                currNode.setNextNode(lastNode);
                lastNode.setPrefEdge(parent.getCameFromEdge());
                currNode.setNextEdge(parent.getCameFromEdge());
                parent = parent.getCameFrom();
                lastNode = currNode;

                pathNodes++;
            }
            retPath.setFirstNode(currNode);
            if (buildExpanded) {
                outExpanded.close();
                fstreamExpanded.close();
            }
            return new AStarResult((double) (System.currentTimeMillis() - time),
                    graph.getNodeList().size(),
                    expNodes,
                    pathNodes,
                    w,
                    length,
                    retPath,
                    name);

        }
        if (buildExpanded) {
            outExpanded.close();
            fstreamExpanded.close();
        }
        return null;
    }

    static class AStarNode implements Comparable<AStarNode> {

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
            return node.getName();
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

    static class AStarResult {
        protected String name;
        protected double runtime;
        protected int nodes;
        protected int expandedNodes;
        protected int pathNodes;
        protected double w;
        protected double length;
        protected PathSegment segment;

        AStarResult(double runtime, int nodes, int expandedNodes, int pathNodes, double w, double length, PathSegment segment, String n) {
            this.name = n;
            this.runtime = runtime;
            this.nodes = nodes;
            this.expandedNodes = expandedNodes;
            this.pathNodes = pathNodes;
            this.w = w;
            this.length = length;
            this.segment = segment;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String n) {
            this.name = n;
        }

        public double getRuntime() {
            return runtime;
        }

        public int getNodes() {
            return nodes;
        }

        public int getExpandedNodes() {
            return expandedNodes;
        }

        public double getLength() {
            return length;
        }

        public PathSegment getPath() {
            return segment;
        }

        public void setSegment(PathSegment segment) {
            this.segment = segment;
        }

        public void print() {
            System.out.println();
            System.out.println("Result for A-Stern: " + this.name);
            System.out.println("Length: " + this.length + " Meter");
            System.out
                    .println("Nodes in Path: "
                            + pathNodes
                            + " ("
                            + (double) (((double) this.pathNodes / this.nodes) * 100) + "%)");
            System.out
                    .println("Nodes in Path: "
                            + this.pathNodes
                            + " ("
                            + (double) (((double) this.pathNodes / this.nodes) * 100) + "%)");
            System.out
                    .println("Expanded Nodes: "
                            + this.expandedNodes
                            + " ("
                            + (double) (((double) this.expandedNodes / this.nodes) * 100) + "%)");
            System.out.println("A-Star Run Time: "
                    + this.runtime / 1000
                    + " s");
            System.out.println();
        }
    }
}