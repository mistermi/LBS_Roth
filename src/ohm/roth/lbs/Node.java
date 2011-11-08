package ohm.roth.lbs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Node implements Serializable {
	protected String name;
    protected Position position;
    protected List<Edge> neighbors;
    
    
	public String getName() {
		return name;
	}
	public void setId(String name) {
		this.name = name;
	}
	public Node(String name, Position position) {
		super();
		this.neighbors = new ArrayList<Edge>();
		this.name = name;
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
