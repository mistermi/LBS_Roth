package ohm.roth.lbs;
import com.vividsolutions.jts.geom.Geometry;

import java.io.Serializable;

public class Edge implements Serializable {
	protected String from;
    protected String to;
    protected double cost;
    protected String gao_id;
    protected Geometry genometry;
    protected int from_node;
    protected int to_node;
    
	public String getGao_id() {
		return gao_id;
	}
	public void setGao_id(String gao_id) {
		this.gao_id = gao_id;
	}
	public int getFrom_node() {
		return from_node;
	}
	public void setFrom_node(int from_node) {
		this.from_node = from_node;
	}
	public int getTo_node() {
		return to_node;
	}
	public void setTo_node(int to_node) {
		this.to_node = to_node;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public Edge(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}
	
	public Edge(String from, String to, double cost, String gao_id, int from_node, int to_node) {
		super();
		this.from = from;
		this.to = to;
		this.cost = cost;
		this.gao_id = gao_id;
		this.from_node = from_node;
		this.to_node = to_node;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
}
