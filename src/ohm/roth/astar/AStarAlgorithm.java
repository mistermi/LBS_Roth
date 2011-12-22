package ohm.roth.astar;

import ohm.roth.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AStarAlgorithm {
    public static double defaultW = 1;
    public static double visitedW = 200;


    public static AStarResult search(NavGraph graph, Node source, Node target, String name) throws Exception {
        return search(graph, source, target, name, defaultW, 0, null, false, null, false);
    }

    public static AStarResult search(NavGraph graph, Node source, Node target, String name, double w) throws Exception {
        return search(graph, source, target, name, w, 0, null, false, null, false);
    }

    public static AStarResult search(NavGraph graph, Node source, Node target, String name, double w, int limit) throws Exception {
        return search(graph, source, target, name, w, limit, null, false, null, false);
    }

    public static AStarResult search(NavGraph graph, Node source, Node target, String name, double w, int openSetLimit, HashMap<String, Double> lsiWeight, boolean useLsiWeight, List<String> visitedEdges, boolean useVisited) throws Exception {
        int expNodes = 1;
        int pathNodes = 0;
        double time_sort = 0;
        double time = System.currentTimeMillis();
        List<String> expandedEdges = new ArrayList<String>();
        SortableValueMap<String, AStarNode> openSet = new SortableValueMap<String, AStarNode>(openSetLimit);
        HashMap<String, AStarNode> closeSet = new HashMap<String, AStarNode>();
        AStarNode start = new AStarNode(source);
        openSet.put(source.getId(), start);
        AStarNode goal = null;
        while (openSet.size() > 0) {
            double time_sort_curr = System.currentTimeMillis();
            openSet.sortByValue();
            time_sort += (System.currentTimeMillis() - time_sort_curr);
            @SuppressWarnings({"SuspiciousMethodCalls"}) AStarNode x = openSet.get(openSet.keySet().toArray()[0]);
            openSet.remove(x.getName());
            if (x.getName().equals(target.getId())) {
                goal = x;
                break;
            } else {
                closeSet.put(x.getName(), x);
                expNodes++;
                List<Edge> neighborsEdges = x.getNode().getNeighbors();
                for (Edge neighborEdge : neighborsEdges) {
                    Node neighbor = graph.getNode(neighborEdge.getTo());
                    if (closeSet.containsKey(neighbor.getId()))
                        continue;
                    double edgeCost = neighborEdge.getCost();
                    double g = x.getG() + edgeCost;
                    double h = distance.calcDist(neighbor, target) * w;
                    if (useVisited && visitedEdges.contains(neighborEdge.getId())) h *= visitedW;
                    if (useVisited && !visitedEdges.contains(neighborEdge.getId())) visitedEdges.add(neighborEdge.getId());
                    if (useLsiWeight && lsiWeight.containsKey(neighborEdge.getGeo().getLsiclass())) h *= lsiWeight.get(neighborEdge.getGeo().getLsiclass());
                    double f = g + h;
                    AStarNode n = openSet.get(neighbor.getId());
                    if (n == null) {
                        // Node noch nicht in Open
                        expandedEdges.add(neighborEdge.getId());
                        n = new AStarNode(neighbor);
                        n.setCameFrom(x);
                        n.setCostCameFrom(neighborEdge.getCost());
                        n.setCameFromEdge(neighborEdge);
                        n.setG(g);
                        n.setF(f);
                        openSet.put(neighbor.getId(), n);
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
            return new AStarResult(System.currentTimeMillis() - time,
                    time_sort,
                    graph.getNodeList().size(),
                    expandedEdges,
                    expNodes,
                    pathNodes,
                    w,
                    length,
                    retPath,
                    name);
        }
        throw new Exception("No Way Found for " + name);
    }

}
