package ohm.roth.astar;

import ohm.roth.PathSegment;

import java.util.List;

/**
 * User: mischarohlederer
 * Date: 12.12.11
 * Time: 11:36
 */
public class AStarResult {
    String name;
    double runtime;
    double sorttime;
    int nodes;
    List<String> expandedNodes;
    int expandedNodesCount;
    int pathNodes;
    double w;
    double length;
    PathSegment segment;

    AStarResult(double runtime, double sorttime, int nodes, List<String> expandedNodes, int expandedNodesCount, int pathNodes, double w, double length, PathSegment segment, String n) {
        this.name = n;
        this.sorttime = sorttime;
        this.runtime = runtime;
        this.nodes = nodes;
        this.expandedNodesCount = expandedNodesCount;
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

    public double getSorttime() {
        return sorttime;
    }

    public int getNodes() {
        return nodes;
    }

    public List<String> getExpandedNodes() {
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

    public int getExpandedNodesCount() {
        return expandedNodesCount;
    }

    public void print() {
        System.out.println();
        System.out.println("Result for A-Stern: " + this.name);
        System.out.println("Length: " + this.length + " Meter");
        System.out
                .println("Nodes in Path: "
                        + pathNodes
                        + " ("
                        + ((double) this.pathNodes / this.nodes) * 100 + "%)");
        System.out
                .println("Nodes in Path: "
                        + this.pathNodes
                        + " ("
                        + ((double) this.pathNodes / this.nodes) * 100 + "%)");
        System.out
                .println("Expanded Nodes: "
                        + this.expandedNodesCount

                        + " ("
                        + ((double) this.expandedNodesCount / this.nodes) * 100 + "%)");
        System.out.println("Sorting the Open List took: "
                + this.sorttime / 1000
                + " s");
        System.out.println("A-Star Run Time: "
                + this.runtime / 1000
                + " s");
        System.out.println();
    }

}
