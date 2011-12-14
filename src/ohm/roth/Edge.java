package ohm.roth;

import com.vividsolutions.jts.geom.LineString;

import java.io.Serializable;

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

    public void setEdgeGeo(LineString edgeGeo) {
        this.edgeGeo = edgeGeo;
    }

    public void setGeo(Geometry geo) {
        this.geo = geo;
    }

    public LineString getEdgeGeo() {
        return edgeGeo;
    }

    public double getCost() {
        return distance.distanceLinestring(edgeGeo);
    }

    public String getGeo_id() {
        return geo_id;
    }

    public Geometry getGeo() {
        return geo;
    }

    public int getFromPos() {
        return fromPos;
    }

    public int getToPos() {
        return toPos;
    }

    public String getId() {
        return this.id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
