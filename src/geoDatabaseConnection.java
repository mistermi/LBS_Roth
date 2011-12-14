import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import gao.tools.SQL;
import ohm.roth.NavGraph;
import ohm.roth.NodeGeoLink;
import ohm.roth.Position;
import ohm.roth.distance;

import java.sql.*;
import java.util.UUID;

public class geoDatabaseConnection {
    private String dbhost = "geo.informatik.fh-nuernberg.de";
    private int dbport = 5432;
    private String dbuser = "dbuser";
    private String dbpasswd = "dbuser";
    private String dbname = "deproDBMittelfranken";

    private Connection connection = null;

    public geoDatabaseConnection(String dbhost, int dbport, String dbuser,
                                 String dbpasswd, String dbname) {
        super();
        this.dbhost = dbhost;
        this.dbport = dbport;
        this.dbuser = dbuser;
        this.dbpasswd = dbpasswd;
        this.dbname = dbname;
    }

    public void connect() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                String connectionString = "jdbc:postgresql://" + dbhost + ":"
                        + dbport + "/" + dbname;
                connection = DriverManager.getConnection(connectionString,
                        dbuser, dbpasswd);
                connection.setAutoCommit(false);
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
        }
    }

    public NavGraph getGraph(Position[] boundingCoords, String name) throws Exception {
        // Runtime Vars
        ResultSet resultSet;
        Statement statement;
        com.vividsolutions.jts.geom.Geometry triangle;
        GeometryFactory geomfact = new GeometryFactory();
        NavGraph graph = new NavGraph(name);

        //Nodes and Geometry
        System.out.println("Fetching Node & Geometry Data");
        if (boundingCoords.length < 2) {
            throw new Exception("FU");
        } else if (boundingCoords.length == 2) {
            Coordinate[] coords = new Coordinate[5];
            coords[0] = new Coordinate(boundingCoords[0].x, boundingCoords[0].y);
            coords[1] = new Coordinate(boundingCoords[1].x, boundingCoords[0].y);
            coords[2] = new Coordinate(boundingCoords[1].x, boundingCoords[1].y);
            coords[3] = new Coordinate(boundingCoords[0].x, boundingCoords[1].y);
            coords[4] = new Coordinate(boundingCoords[0].x, boundingCoords[0].y);
            coords[boundingCoords.length] = boundingCoords[0];
            triangle = geomfact.createPolygon(geomfact.createLinearRing(coords), new LinearRing[0]);
        } else {
            Coordinate[] coords = new Coordinate[boundingCoords.length + 1];
            System.arraycopy(boundingCoords, 0, coords, 0, boundingCoords.length + 1);
            coords[boundingCoords.length] = boundingCoords[0];
            triangle = geomfact.createPolygon(geomfact.createLinearRing(coords), new LinearRing[0]);
        }

        Envelope boundingBox = triangle.getEnvelopeInternal();
        statement = connection.createStatement();
        statement.setFetchSize(1000000000);
        resultSet = statement.executeQuery("SELECT " +
                "domain.gao_id AS gaoid, domain.fullname AS gaoname, domain.geodata_line AS gaoline, domain.lsiclass AS gaolsi, " +
                "crossing.id AS id, crossing.long AS lon, crossing.lat AS lat, crossing.posnr AS pos " +
                "FROM " +
                "domain, crossing " +
                "WHERE " +
                "domain.geometry='L' AND " +
                "crossing.gao_id=domain.gao_id  AND" +
                SQL.createIndexQuery(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMinX(), boundingBox.getMinY(), SQL.COMPLETELY_INSIDE)
        );
        while (resultSet.next()) {
            // Geomerty Objects
            String gao_id = resultSet.getString("gaoid");
            if (!graph.getGeoList().containsKey(gao_id)) {
                // Geometry einfügen
                String gao_name = resultSet.getString("gaoname");
                String gao_lsi = resultSet.getString("gaolsi");
                com.vividsolutions.jts.geom.Geometry gao_geo;
                LineString gao_line;
                try {
                    byte[] gao_data = resultSet.getBytes("gaoline");
                    gao_geo = SQL.wkb2Geometry(gao_data);
                    if (gao_geo instanceof LineString) {
                        gao_line = (LineString) gao_geo;
                    } else {
                        gao_line = null;
                    }
                } catch (ParseException e) {
                    gao_line = null;
                }
                graph.addGeo(gao_id, new ohm.roth.Geometry(gao_id, gao_name, gao_lsi, gao_line));
            }
            Position Point = new Position(resultSet.getDouble("lon"), resultSet.getDouble("lat"));
            String id = resultSet.getString("id");
            Integer pos = resultSet.getInt("pos");
            graph.addNode(id, Point, gao_id, pos);
        }

        //Links
        System.out.println("Fetching Link Data");
        statement = this.connection.createStatement();
        statement.setFetchSize(1000000);
        String Query = "SELECT "
                + "link.id AS linkid, link.meters AS costs, link.gao_id AS gaoid, "
                + "from_cross.id AS from_id, from_cross.long AS from_long, from_cross.lat AS from_lat, from_cross.posnr AS from_pos, "
                + "to_cross.id AS to_id, to_cross.long AS to_long, to_cross.lat AS to_lat, to_cross.posnr AS to_pos "
                + "FROM "
                + "link ,crossing AS from_cross, crossing AS to_cross, domain "
                + "WHERE "
                + "from_cross.id = link.crossing_id_from AND "
                + "to_cross.id = link.crossing_id_to AND "
                + "domain.gao_id = to_cross.gao_id AND "
                + "domain.gao_id = from_cross.gao_id AND "
                + "domain.gao_id = link.gao_id AND "
                + SQL.createIndexQuery(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMinX(), boundingBox.getMinY(), SQL.COMPLETELY_INSIDE)
                + " ORDER BY link.id";
        resultSet = statement.executeQuery(Query);
        while (resultSet.next()) {
            String gao_id = resultSet.getString("gaoid");
            if (!graph.getGeoList().containsKey(gao_id)) System.out.println("FOOOO");
            // Nodes (sollten eigentlich schon exestieren aber ... sicher ist sicher)
            Position fromPoint = new Position(resultSet.getDouble("from_long"), resultSet.getDouble("from_lat"));
            String fromId = resultSet.getString("from_id");
            String toId = resultSet.getString("to_id");
            Integer fromPos = resultSet.getInt("from_pos");
            Integer toPos = resultSet.getInt("to_pos");
            graph.addNode(fromId, fromPoint, gao_id, fromPos);
            // Connections
            double meter = resultSet.getDouble("costs");
            graph.addConnection(fromId, toId, meter, gao_id, fromPos, toPos);
            graph.addConnection(toId, fromId, meter, gao_id, toPos, fromPos);
        }

        // Geometrische Endpunkte einfügen
        System.out.println("Adding Geo End Points");
        int addedPoints = 0;
        for (ohm.roth.Geometry geo : graph.geoList.values()) {
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
                addedPoints++;
                int smallID = geo.getGeometry().getNumPoints();
                NodeGeoLink goodNode = null;
                for (NodeGeoLink link : geo.getConnectedNodes().values()) {
                    if (link.getGeoPos() < smallID) {
                        smallID = link.getGeoPos();
                        goodNode = link;
                    }
                }
                graph.addNode(nodeId, new Position(geo.getGeometry().getStartPoint().getX(), geo.getGeometry().getStartPoint().getY()), geo.getId(), 0);
                if (goodNode != null) {
                    double dis = distance.distanceLinestring(geo.getGeometry(), 0, goodNode.getGeoPos());
                    graph.addConnection(goodNode.getNode().getId(), nodeId, dis, geo.getId(), smallID, 0);
                    graph.addConnection(nodeId, goodNode.getNode().getId(), dis, geo.getId(), 0, smallID);
                } else {
                    System.out.println("FU START");
                }
            }

            // Insert Last Point
            if (!lastValid) {
                String nodeId = "LAST" + UUID.randomUUID().toString();
                addedPoints++;
                int smallID = -1;
                NodeGeoLink goodNode = null;
                for (NodeGeoLink link : geo.getConnectedNodes().values()) {
                    if (link.getGeoPos() > smallID) {
                        smallID = link.getGeoPos();
                        goodNode = link;
                    }
                }
                graph.addNode(nodeId, new Position(geo.getGeometry().getEndPoint().getX(), geo.getGeometry().getEndPoint().getY()), geo.getId(), geo.getGeometry().getNumPoints() - 1);
                if (goodNode != null) {
                    double dis = distance.distanceLinestring(geo.getGeometry(), geo.getGeometry().getNumPoints() - 1, goodNode.getGeoPos());
                    graph.addConnection(goodNode.getNode().getId(), nodeId, dis, geo.getId(), smallID, geo.getGeometry().getNumPoints() - 1);
                    graph.addConnection(nodeId, goodNode.getNode().getId(), dis, geo.getId(), geo.getGeometry().getNumPoints() - 1, smallID);
                } else {
                    System.out.println("FU START");
                }
            }
        }
        resultSet.close();
        return graph;
    }
}
