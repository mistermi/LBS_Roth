package ohm.roth;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@SuppressWarnings({"ConstantConditions"})
public class NavGraph implements Serializable {
    private String name;
    private Date genDate;
    private int endPoints = 0;
    private HashMap<String, Node> nodeList;
    private HashMap<String, Geometry> geoList;
    private HashMap<String, Edge> edgeList;

    public NavGraph(String name) {
        nodeList = new HashMap<String, Node>();
        geoList = new HashMap<String, Geometry>();
        edgeList = new HashMap<String, Edge>();
        this.name = name;
        genDate = new Date();
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return name + ".DAT";
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

    public int getGeometryCount() {
        return geoList.size();
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

    public int getNodeCount() {
        return nodeList.size();
    }

    // edgelist
    public void addEdge(Edge e) {
        this.edgeList.put(e.getId(), e);
    }

    public HashMap<String, Edge> getEdgeList() {
        return edgeList;
    }

    public int getEdgeCount() {
        return edgeList.size();
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
            if ((distance.calcDist(p, nodeFrom.getPosition()) + edge.getCost()) > distance.unitConvert(1000) || (distance.calcDist(p, nodeTo.getPosition()) + edge.getCost()) > distance.unitConvert(1000))
                continue;
            Coordinate pts;
            for (int i = 0; i < edge.getEdgeGeo().getNumPoints() - 1; i++) {
                Coordinate[] coords = new Coordinate[2];
                LineString testLine;
                coords[0] = edge.getEdgeGeo().getPointN(i).getCoordinate();
                coords[1] = edge.getEdgeGeo().getPointN(i + 1).getCoordinate();
                LineSegment test = new LineSegment(coords[0], coords[1]);
                pts = test.closestPoint(p);
                if (distance.calcDist(new Position(pts.x, pts.y), p) < dis) {

                    // Test ob Kante im Graph ist
                    // Teste auf 10 Ebenen
                    // Bessere Ansatz: Graph in Subgraphen aufteilen
                    boolean single = true;
                    List<String> testedNodes = new ArrayList<String>();
                    List<Node> testNodes = new ArrayList<Node>();
                    List<Node> tmp = new ArrayList<Node>();
                    testNodes.add(this.getNode(edge.getFrom()));
                    testNodes.add(this.getNode(edge.getTo()));
                    for (int ii = 0; ii < 10; ii++) {
                        single = true;
                        for (Node testNode : testNodes) {
                            testedNodes.add(testNode.getId());
                            if (testNode.neighbors.size() > 1) {
                                single = false;
                                continue;
                            }
                        }
                        if (single) break;
                        tmp.clear();
                        tmp.addAll(testNodes);
                        testNodes.clear();
                        for (Node n : tmp) {
                            for (Edge e : n.getNeighbors()) {
                                if (!testedNodes.contains(e.getTo()))
                                    testNodes.add(this.getNode(e.getTo()));
                                if (!testedNodes.contains(e.getFrom()))
                                    testNodes.add(this.getNode(e.getFrom()));
                            }
                        }
                    }

                    if (!single) {
                        Node tmpNode = new Node("INSERTD" + UUID.randomUUID().toString(), new Position(pts.x, pts.y));
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

            Node t1 = this.getNode(bEdge.getTo());
            Node t2 = this.getNode(bEdge.getFrom());

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

    // EndPoints
    public void addEndPoints() {
        System.out.println("Adding Geo End Points");
        for (ohm.roth.Geometry geo : this.geoList.values()) {
            if (geo.getConnectedNodes().size() == 0)
                System.out.println("Empty Geometry");
            // Check for First & Last Point
            boolean firstValid = false;
            boolean lastValid = false;
            for (NodeGeoLink link : geo.getConnectedNodes().values()) {
                if (link.getGeoPos() == 0) {
                    firstValid = true;
                }
                if (link.getGeoPos() == geo.getGeometry().getNumPoints() - 1) {
                    lastValid = true;
                }
            }

            // Insert First Point
            if (!firstValid) {
                String nodeId = "FIRST" + UUID.randomUUID().toString();
                endPoints++;
                int smallID = geo.getGeometry().getNumPoints();
                NodeGeoLink goodNode = null;
                for (NodeGeoLink link : geo.getConnectedNodes().values()) {
                    if (link.getGeoPos() < smallID) {
                        smallID = link.getGeoPos();
                        goodNode = link;
                    }
                }
                this.addNode(nodeId, new Position(geo.getGeometry().getStartPoint().getX(), geo.getGeometry().getStartPoint().getY()), geo.getId(), 0);
                if (goodNode != null) {
                    double dis = distance.distanceLinestring(geo.getGeometry(), 0, goodNode.getGeoPos());
                    this.addConnection(goodNode.getNode().getId(), nodeId, dis, geo.getId(), smallID, 0);
                    this.addConnection(nodeId, goodNode.getNode().getId(), dis, geo.getId(), 0, smallID);
                } else {
                    System.out.println("FU START");
                }
            }

            // Insert Last Point
            if (!lastValid) {
                String nodeId = "LAST" + UUID.randomUUID().toString();
                endPoints++;
                int smallID = -1;
                NodeGeoLink goodNode = null;
                for (NodeGeoLink link : geo.getConnectedNodes().values()) {
                    if (link.getGeoPos() > smallID) {
                        smallID = link.getGeoPos();
                        goodNode = link;
                    }
                }
                this.addNode(nodeId, new Position(geo.getGeometry().getEndPoint().getX(), geo.getGeometry().getEndPoint().getY()), geo.getId(), geo.getGeometry().getNumPoints() - 1);
                if (goodNode != null) {
                    double dis = distance.distanceLinestring(geo.getGeometry(), geo.getGeometry().getNumPoints() - 1, goodNode.getGeoPos());
                    this.addConnection(goodNode.getNode().getId(), nodeId, dis, geo.getId(), smallID, geo.getGeometry().getNumPoints() - 1);
                    this.addConnection(nodeId, goodNode.getNode().getId(), dis, geo.getId(), geo.getGeometry().getNumPoints() - 1, smallID);
                } else {
                    System.out.println("FU START");
                }
            }
        }
    }

    public int getEndPoints() {
        return endPoints;
    }

    // Generation Date
    public Date getGenDate() {
        return genDate;
    }

    public String getGenDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return (dateFormat.format(genDate));
    }
}