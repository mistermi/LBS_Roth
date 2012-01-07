package ohm.roth.astar;

import ohm.roth.PathSegment;

import java.util.List;

/**
 * Beschreibung des Resultats des A-Stern Algo
 */
public class AStarResult {
    String name;
    double runtime;
    double sorttime;
    int nodes;
    List<String> expandedEdges;
    int pathNodes;
    double w;
    double length;
    PathSegment segment;

    /**
     * Konstruktor fuer ein AStarResult Objekt
     * @param runtime Die gesammte Laufzeit
     * @param sorttime Die Zeit fuer das Sortieren der Open liste
     * @param nodes Anzahl der Nodes im Gefundenen Pfad
     * @param expandedEdges Liste mit der im zuge von A-Stern expandierte Nodes
     * @param pathNodes Anzahl der Nodes im gefundenen Weg
     * @param w Der OverDo Faktor fuer Den A-Stern Algo.
     * @param length Die Kosten des Gesammten Pfads
     * @param segment Der Pfad als PathSegment
     * @param n Name
     */
    AStarResult(double runtime, double sorttime, int nodes, List<String> expandedEdges, int pathNodes, double w, double length, PathSegment segment, String n) {
        this.name = n;
        this.sorttime = sorttime;
        this.runtime = runtime;
        this.nodes = nodes;
        this.expandedEdges = expandedEdges;
        this.pathNodes = pathNodes;
        this.w = w;
        this.length = length;
        this.segment = segment;
    }

    /**
     * Giebt den Namen des Pfades zurueck
     * @return Der Name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gibt die Laufzeit zurueck
     * @return Die Laufzeit
     */
    public double getRuntime() {
        return runtime;
    }

    /**
     * Gibt die Zeit die das Sortieren der Open Liste gedauert hat zurueck
     * @return Die Laufzeit
     */
    public double getSorttime() {
        return sorttime;
    }

    /**
     * Gibt die Anzahl der nodes im gefundenen Pfad zurueck
     * @return Die Anzahl der Nodes
     */
    public int getNodes() {
        return nodes;
    }

    /**
     * Gibt eine Liste mit der IDs aller expandierten Edges zurueck
     * @return Liste aller Edges
     */
    public List<String> getExpandedEdges() {
        return expandedEdges;
    }

    public double getLength() {
        return length;
    }

    /**
     * Giebt das Pfad Segment zurueck
     * @return Pfad Segment
     */
    public PathSegment getPath() {
        return segment;
    }

    /**
     * Giebt die Anzahl der Expandierten Nodes / Kanten zurueck
     * @return Die Anzahl
     */
    public int getExpandedNodesCount() {
        return this.expandedEdges.size();
    }

    /**
     * Giebt Informationen ueber die Laufzeit aus
     */
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
                        + this.getExpandedNodesCount()

                        + " ("
                        + ((double) this.getExpandedNodesCount() / this.nodes) * 100 + "%)");
        System.out.println("Sorting the Open List took: "
                + this.sorttime / 1000
                + " s");
        System.out.println("A-Star Run Time: "
                + this.runtime / 1000
                + " s");
        System.out.println();
    }

}
