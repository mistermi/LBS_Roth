package ohm.roth;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@SuppressWarnings({"ConstantConditions"})
public class NavGraph implements Serializable {
    public String name;
    public HashMap<String, Node> nodeList;
    public HashMap<String, Geometry> geoList;
    public HashMap<String, Edge> edgeList;

    public NavGraph(String name) {
        nodeList = new HashMap<String, Node>();
        geoList = new HashMap<String, Geometry>();
        edgeList = new HashMap<String, Edge>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //geoList
    public void addGeo(String id, Geometry geo) {
        if (!this.geoList.containsKey(id))
            this.geoList.put(id, geo);
    }

    public Geometry getGeo(String id) {
        return this.geoList.get(id);
    }

    public HashMap<String, Geometry> getGeoList() {
        return geoList;
    }

    public void setGeoList(HashMap<String, Geometry> geoList) {
        this.geoList = geoList;
    }

    // nodelist
    public void addNode(String id, Position pos, String geoId, int geoPos) {
        if (!this.nodeList.containsKey(id)) {
            this.nodeList.put(id, new Node(id, pos));
        }
        if (this.geoList.containsKey(geoId))
            if (!this.getGeo(geoId).getConnectedNodes().containsKey(id)) {
                this.getGeo(geoId).addNode(this.nodeList.get(id), geoPos);
            }
    }


    public Node getNode(String id) {
        return this.nodeList.get(id);
    }

    public HashMap<String, Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(HashMap<String, Node> nodeList) {
        this.nodeList = nodeList;
    }

    // edgelist
    public void addEdge(Edge e) {
        this.edgeList.put(e.getId(), e);
    }

    public HashMap<String, Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(HashMap<String, Edge> edgeList) {
        this.edgeList = edgeList;
    }

    public void initGraph() {
        for (Edge edge : this.edgeList.values()) {
            edge.setGeo(this.getGeo(edge.getGeo_id()));
            Coordinate[] coordinates;
            GeometryFactory fac = new GeometryFactory();
            int ii = 0;
            if (edge.getToPos() > edge.getFromPos()) {
                coordinates = new Coordinate[(edge.getToPos() - edge.getFromPos()) + 1];
                for (int i = edge.getFromPos(); i <= edge.getToPos(); i++) {
                    coordinates[ii] = edge.getGeo().getGeometry().getPointN(i).getCoordinate();
                    ii++;
                }
            } else if (edge.getFromPos() > edge.getToPos()) {
                coordinates = new Coordinate[(edge.getFromPos() - edge.getToPos()) + 1];
                for (int i = edge.getFromPos(); i >= edge.getToPos(); i--) {
                    coordinates[ii] = edge.getGeo().getGeometry().getPointN(i).getCoordinate();
                    ii++;
                }
            } else {
                coordinates = new Coordinate[0];
            }
            edge.setEdgeGeo(fac.createLineString(coordinates));
        }
    }

    public Node findClosest(Position p, boolean useGeo) {
        if (useGeo) {
            return findClosestGeo(p);
        } else {
            return findClosestTopo(p);
        }
    }

    public Node findClosestTopo(Position p) {
        double dis = 999999999;
        Node closest = null;
        for (Node node : this.nodeList.values()) {
            if (distance.calcDist(node.getPosition(), p) < dis) {
                dis = distance.calcDist(node.getPosition(), p);
                closest = node;
            }
        }
        return closest;
    }

    public Node findClosestGeo(Position p) {
        double dis = Double.MAX_VALUE;
        boolean node = false;

        Node bNode = null;
        Edge bEdge = null;
        String bGeo = null;
        GeometryFactory fac = new GeometryFactory();
        int lastPos = -1;

        for (Edge edge : this.edgeList.values()) {
            Node nodeTo = this.nodeList.get(edge.getTo());
            Node nodeFrom = this.nodeList.get(edge.getFrom());
            if (nodeTo.getPosition().getLat() == p.getLat() && nodeTo.getPosition().getLon() == p.getLon())
                return nodeTo;
            if (nodeFrom.getPosition().getLat() == p.getLat() && nodeFrom.getPosition().getLon() == p.getLon())
                return nodeFrom;
            if ((distance.calcDist(p,nodeFrom.getPosition()) + edge.getCost()) > distance.unitConvert(1000) || (distance.calcDist(p,nodeTo.getPosition()) + edge.getCost()) > distance.unitConvert(1000))
                continue;

            for (int i = 0; i < edge.getEdgeGeo().getNumPoints() - 1; i++) {
                Coordinate[] coords = new Coordinate[2];
                LineString testLine;
                coords[0] = edge.getEdgeGeo().getPointN(i).getCoordinate();
                coords[1] = edge.getEdgeGeo().getPointN(i + 1).getCoordinate();
                testLine = fac.createLineString(coords);
                Coordinate[] pts = DistanceOp.closestPoints(p.getPoint(), testLine);
                if (distance.calcDist(pts[0], pts[1]) >= dis) continue;

                if (coords[0].equals(pts[1])) {
                    if (distance.calcDist(edge.getEdgeGeo().getPointN(i).getCoordinate(), p) < dis) {
                        bNode = nodeFrom;
                        dis = distance.calcDist(edge.getEdgeGeo().getPointN(i).getCoordinate(), p);
                        node = true;
                    }
                } else if (coords[1].equals(pts[1])) {
                    if (distance.calcDist(edge.getEdgeGeo().getPointN(i + 1).getCoordinate(), p) < dis) {
                        bNode = nodeTo;
                        dis = distance.calcDist(edge.getEdgeGeo().getPointN(i + 1).getCoordinate(), p);
                        node = true;
                    }
                } else {
                    Node tmpNode = new Node("INSERTD" + UUID.randomUUID().toString(), new Position(pts[1].x, pts[1].y));
                    if (distance.calcDist(tmpNode, p) < dis) {
                        lastPos = i;
                        bNode = tmpNode;
                        bEdge = edge;
                        bGeo = edge.getGeo_id();
                        dis = distance.calcDist(tmpNode, p);
                        node = false;
                    }
                }
            }
        }
        if (!node) {
            // Node einfügen
            //Linestring 1
            LineString line1, line2;
            Coordinate[] coordinates = new Coordinate[lastPos + 2];
            int ii = 0;
            for (int i = 0; i <= lastPos; i++) {
                coordinates[ii] = bEdge.getEdgeGeo().getPointN(i).getCoordinate();
                ii++;
            }
            coordinates[ii] = bNode.getPosition();
            line1 = fac.createLineString(coordinates);

            coordinates = new Coordinate[bEdge.getEdgeGeo().getNumPoints() - lastPos];
            coordinates[0] = bNode.getPosition();
            ii = 1;
            for (int i = lastPos + 1; i < bEdge.getEdgeGeo().getNumPoints(); i++) {
                coordinates[ii] = bEdge.getEdgeGeo().getPointN(i).getCoordinate();
                ii++;

            }
            line2 = fac.createLineString(coordinates);

            addNode(bNode.getId(), bNode.getPosition(), bGeo, 0);
            addConnection(bNode.getId(), bEdge.getFrom(), distance.distanceLinestring(line1), bGeo, bEdge.getFromPos(), bEdge.getToPos());
            addConnection(bEdge.getFrom(), bNode.getId(), distance.distanceLinestring(line1), bGeo, bEdge.getFromPos(), bEdge.getToPos());
            addConnection(bNode.getId(), bEdge.getTo(), distance.distanceLinestring(line2), bGeo, bEdge.getFromPos(), bEdge.getToPos());
            addConnection(bEdge.getTo(), bNode.getId(), distance.distanceLinestring(line2), bGeo, bEdge.getFromPos(), bEdge.getToPos());
            this.getEdgeList().get(bNode.getId() + "___" + bEdge.getFrom()).setEdgeGeo(line1.reverse());
            this.getEdgeList().get(bEdge.getFrom() + "___" + bNode.getId()).setEdgeGeo(line1);
            this.getEdgeList().get(bNode.getId() + "___" + bEdge.getTo()).setEdgeGeo(line2);
            this.getEdgeList().get(bEdge.getTo() + "___" + bNode.getId()).setEdgeGeo(line2.reverse());
            this.getEdgeList().get(bNode.getId() + "___" + bEdge.getFrom()).setGeo(this.getGeo(bGeo));
            this.getEdgeList().get(bEdge.getFrom() + "___" + bNode.getId()).setGeo(this.getGeo(bGeo));
            this.getEdgeList().get(bNode.getId() + "___" + bEdge.getTo()).setGeo(this.getGeo(bGeo));
            this.getEdgeList().get(bEdge.getTo() + "___" + bNode.getId()).setGeo(this.getGeo(bGeo));
        }
        return getNode(bNode.getId());
    }

    public void dumpGraph(String filename) throws IOException {
        FileOutputStream fos;
        List<String> fooo = new ArrayList<String>();

        fos = new FileOutputStream(filename);

        OutputStreamWriter out2 = new OutputStreamWriter(fos);

        for (Edge e : this.getEdgeList().values()) {
            if ((!fooo.contains(this.getNode(e.getFrom()).getId() + "___" + this.getNode(e.getTo()).getId())) && (!fooo.contains(this.getNode(e.getTo()).getId() + "___" + this.getNode(e.getFrom()).getId()))) {
                out2.write("LINE mode=1 col=0,0,255,75\n");
                out2.write(this.getNode(e.getFrom()).getPosition().getLat() + ","
                        + this.getNode(e.getFrom()).getPosition().getLon() + "\n");
                out2.write(this.getNode(e.getTo()).getPosition().getLat() + ","
                        + this.getNode(e.getTo()).getPosition().getLon() + "\n");

                fooo.add((this.getNode(e.getTo()).getId() + "___" + this.getNode(e.getFrom()).getId()));
            }
        }
        out2.close();
        fos.close();
    }

    public void addConnection(String firstId, String secondId, double c, String gao, int firstPos, int secondPos) {
        Node node1 = this.getNode(firstId);
        Node node2 = this.getNode(secondId);
        if (node1 != null && node2 != null) {
            if (!this.edgeList.containsKey(node1.getId() + "___" + node2.getId())) {
                Edge edge1 = new Edge((node1.getId() + "___" + node2.getId()), node1.getId(), node2.getId(), c, this.getGeo(gao), firstPos, secondPos);
                node1.addNeighbore(edge1);
                this.addEdge(edge1);
            }

        }
    }
}