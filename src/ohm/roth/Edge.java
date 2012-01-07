package ohm.roth;

import com.vividsolutions.jts.geom.LineString;

import java.io.Serializable;

/**
 * Beschreibt eine Kante in einem Graphen
 */
public class Edge implements Serializable {
    protected String id;
    protected String from;
    protected String to;
    protected double cost;
    private LineString edgeGeo;
    private String geo_id;
    private Geometry geo;
    private int fromPos;
    private int toPos;

    /**
     * Constructopr for Edge with all Arguments.
     * Geometry and EdgeGeometry is loaded via NavGraph initGraph()
     *
     * @param id      Edge ID
     * @param from    ID of the First Node
     * @param to      ID of the Second Node
     * @param cost    precalculated Edge Cost
     * @param geo     Geometry Object (only the id is stored)
     * @param fromPos First Node of the Geometry
     * @param toPos   Second Node of the Geometry
     */
    public Edge(String id, String from, String to, double cost, Geometry geo, int fromPos, int toPos) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.geo_id = geo.getId();
        this.fromPos = fromPos;
        this.toPos = toPos;
    }

    /**
     * Legt die Geomerie der Kante fest
     * @param edgeGeo Die Geometrie
     */
    public void setEdgeGeo(LineString edgeGeo) {
        this.edgeGeo = edgeGeo;
        this.cost = distance.distanceLinestring(this.edgeGeo);
    }

    /**
     * Legt die Geometrie der Kante fest
     * @param geo Die Geometrie
     */
    public void setGeo(Geometry geo) {
        this.geo = geo;
    }

    /**
     * Der LineString der die Geometrie der Kante beschreibt
     * @return Der Linestring
     */
    public LineString getEdgeGeo() {
        return edgeGeo;
    }

    /**
     * Die Laenge der durch die Kante beschriebene Geometrie
     * @return Die Kosten
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Die ID der Geometrie aud der sich die Kante befindet
     * @return GAOID
     */
    public String getGeo_id() {
        return geo_id;
    }

    /**
     * Die Geometrie auf der sich die Kante befindet
     * @return Geometry Object
     */
    public Geometry getGeo() {
        return geo;
    }

    /**
     * Der erste Punkt auf der Geometrie der zu dieser Kante gehoert
     * @return Punkt id
     */
    public int getFromPos() {
        return fromPos;
    }

    /**
     * Der letzte Punkt auf der der zu dieser Kante Gehoert
     * @return Punkt Id
     */
    public int getToPos() {
        return toPos;
    }

    /**
     * Die Id der Kante
     * StarNodeId___EndNodeId
     * @return Die Id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Die Id der Knoten von dem diese Kante ausgeht
     * @return Knoten Id
     */
    public String getFrom() {
        return from;
    }

    /**
     * Die Id des Knoten zu dem diese Kante fuehrt
     * @return Knoten Id
     */
    public String getTo() {
        return to;
    }
}
