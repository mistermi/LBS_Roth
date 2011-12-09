package ohm.roth.lbs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Node implements Serializable {
	protected String id;
    protected Position position;
    protected List<Edge> neighbors;

    public String getId() {
		return id;
	}
	public void setId(String name) {
		this.id = name;
	}
	public Node(String id, Position position) {
		super();
		this.neighbors = new ArrayList<Edge>();
		this.id = id;
		this.position = position;
    }
	public List<Edge> getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(List<Edge> neighbors) {
		this.neighbors = neighbors;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public void addNeighbore(Edge e) {
		this.neighbors.add(e);
	}
}
