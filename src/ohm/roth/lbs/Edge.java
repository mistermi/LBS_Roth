package ohm.roth.lbs;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import java.io.Serializable;

public class Edge implements Serializable {
    protected String id;
	protected String from;
    protected String to;
    protected double cost;
    protected int from_node;
    protected int to_node;
    private LineString edgeGeo;

    public void setEdgeGeo(LineString edgeGeo) {
        this.edgeGeo = edgeGeo;
    }

    public void setGeo(Geometry geo) {
        this.geo = geo;
    }

    private String geo_id;
    private Geometry geo;
    private int fromPos;
    private int toPos;

    public Edge(String id, String from, String to, double cost, Geometry geo, int fromPos, int toPos) {
        this.id = id;
		this.from = from;
		this.to = to;
		this.cost = cost;
        this.geo_id = geo.getId();
        this.fromPos = fromPos;
        this.toPos = toPos;
    }

    public int getFrom_node() {
        return from_node;
    }

    public int getTo_node() {
        return to_node;
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

	public Edge(String id, String from, String to, double cost) {
		super();
        this.id = id;
		this.from = from;
		this.to = to;
		this.cost = cost;
	}
}
