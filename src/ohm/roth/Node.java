package ohm.roth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Beschreibt Knotenpunkte in einem Graphen
 */
public class Node implements Serializable {
    protected String id;
    protected Position position;
    protected List<Edge> neighbors;

    /**
     * Die Id Der Node
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * Konstruktor
     * @param id Die ID der Node
     * @param position Die Position der Node
     */
    public Node(String id, Position position) {
        super();
        this.neighbors = new ArrayList<Edge>();
        this.id = id;
        this.position = position;
    }

    /**
     * Liste alle Kante welche mit dieser Node verbunden sind
     * @return Lisste aller Kanten
     */
    public List<Edge> getNeighbors() {
        return neighbors;
    }

    /**
     * Die Position des Knotenpunktes
     * @return Position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Aendert die Position des Knotenpunktes
     * @param position Die neue Position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Fuegt dem Knotenpunkt eine neue Nachbarkante hinzu
     * @param e Die Hinzuzufuegende Nachbarkante
     */
    public void addNeighbore(Edge e) {
        this.neighbors.add(e);
    }
}
