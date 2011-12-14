import com.vividsolutions.jts.geom.LineString;
import ohm.roth.*;
import ohm.roth.astar.AStarAlgorithm;
import ohm.roth.astar.AStarResult;
import ohm.roth.dorenda.DORENDABuilder;
import ohm.roth.gpx.GPXBuilder;
import ohm.roth.tsp.TSP;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * User: mischarohlederer
 * Date: 27.10.11
 * Time: 11:51
 */
@SuppressWarnings({"ConstantConditions"})
public class Application {
    static Scanner scan = new Scanner(System.in);
    static boolean verbose = true;
    static NavGraph graph;
    static geoDatabaseConnection connection;

    static String dbhost;
    static int dbport;
    static String dbuser;
    static String dbpasswd;
    static String dbname;
    static String graphname = "NBG";
    static String lsiWeightsFilename = "lsi_foot.csv";

    static Position[] boundingBox;
    static TSP.tspType defaultTspType = TSP.tspType.GENETIC_GOOD;
    static HashMap<String, Double> lsiWeights;

    // Menus
    static void mainMenu() {
        System.out.println("============================================================");
        System.out.println("Roth LBS Aufgabe 2011");
        System.out.println("Fuchs Daniel, Rohlederer Mischa");
        System.out.println("============================================================");
        AStarAlgorithm.defaultW = 1;
        AStarAlgorithm.visitedW = 2;

        // Datenbank zugang
        File file = new File(Application.graphname + ".DAT");
        if (file.exists()) {
            Application.loadGraph_File(graphname);
        } else {
            Application.loadGraph_Database(boundingBox, graphname);
        }

        double time_init = System.currentTimeMillis();
        Application.println("Init Graph");
        graph.initGraph();
        time_init = (System.currentTimeMillis() - time_init) / 1000;
        Application.println("Init Graph took " + time_init + " Secs");

        try {
            Application.lsiWeights = new HashMap<String, Double>();
            lsiWeights = CSV.readLSIWeight(Application.lsiWeightsFilename);
        } catch (Exception e) {
            System.err.println("Error reading " + Application.lsiWeightsFilename);
        }

        boolean quit = false;
        do {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("Please Make a selection:");
            System.out.println("============================================================");
            System.out.println("[1] Build Path");
            System.out.println("[2] Benchmarks");
            System.out.println("[0] exit");
            System.out.print("Select: ");
            int menu = scan.nextInt();
            switch (menu) {
                case 1:
                    Application.pathMenu();
                    break;
                case 2:
                    benchmarkMenu();
                    break;
                case 0:
                    quit = true;
                    break;
            }
        }
        while (!quit);
        System.exit(0);
    }

    static void pathMenu() {
        boolean quit = false;
        do {
            System.out.println("==================================================");
            System.out.println("Building Path Menu");
            System.out.println("==================================================");
            System.out.println();
            System.out.println("Please Make a selection:");
            System.out.println("[1] Standard");
            System.out.println("[2] Reduce Double Edges");
            System.out.println("[3] Reduce Double Edges & and use LSI Weights");
            System.out.println("[0] back");
            System.out.println("Selection: ");
            int menu = scan.nextInt();
            String name, filename;
            double w;
            switch (menu) {
                case 1:
                    System.out.print("Name: ");
                    name = scan.next();
                    System.out.print("Filename: ");
                    filename = scan.next();
                    System.out.print("W: ");
                    w = scan.nextDouble();
                    Application.findPath(filename, name, w, true, false, false);
                    break;
                case 2:
                    System.out.print("Name: ");
                    name = scan.next();
                    System.out.print("Filename: ");
                    filename = scan.next();
                    System.out.print("W: ");
                    w = scan.nextDouble();
                    Application.findPath(filename, name, w, true, true, false);
                    break;
                case 3:
                    System.out.print("Name: ");
                    name = scan.next();
                    System.out.print("Filename: ");
                    filename = scan.next();
                    System.out.print("W: ");
                    w = scan.nextDouble();
                    Application.findPath(filename, name, w, true, true, true);
                    break;
                case 0:
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid Entry!");
            }
        }
        while (!quit);
    }

    static void findPath(String filename, String name, double w, boolean loop, boolean reduceDoubleEdges, boolean weightedLsi) {
        Application.println("==================================================");
        Application.println("Finding Path");
        Application.println("Filename: " + filename);
        Application.println("w: " + w);
        Application.println("==================================================");

        // Load Waypoints
        double time_waypoints = System.currentTimeMillis();
        List<Waypoint> waypoints;
        try {
            waypoints = CSV.readWaypointList(filename);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (waypoints.size() == 0) return;
        List<Waypoint> orderdWaypoints;
        time_waypoints = (System.currentTimeMillis() - time_waypoints) / 1000;
        Application.println("Loading Waypoints took " + time_waypoints + " Secs");
        Application.println("");

        // Rundweg
        if (waypoints.size() > 2) {
            double time_tsp = System.currentTimeMillis();
            Application.println("Running TSP with " + (waypoints.size()) + " Points");
            orderdWaypoints = TSP.sort(waypoints, defaultTspType);
            time_tsp = (System.currentTimeMillis() - time_tsp) / 1000;
            Application.println("TSP took " + time_tsp + " Secs");
            Application.println("");
        } else {
            orderdWaypoints = waypoints;
        }
        try {
            FileWriter fstream = new FileWriter(name + "-TSP.GPX");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(GPXBuilder.build(orderdWaypoints, ""));
            out.close();
        } catch (IOException e) {
            Application.println("Error writing GPX file");
        }


        // Cosest points
        System.out.println("Finding nearest Node for " + (orderdWaypoints.size()) + " Points");
        double nearest_time = System.currentTimeMillis();
        Node[] waynotes = new Node[orderdWaypoints.size()];
        for (int i = 0; i < orderdWaypoints.size(); i++) {
            waynotes[i] = graph.findClosest(orderdWaypoints.get(i), true);
        }
        nearest_time = (System.currentTimeMillis() - nearest_time) / 1000;
        System.out.println("Finding Nodes took " + nearest_time + " Secs");
        System.out.println("");

        // A-Stern
        double globalLength = 0;
        Path path = new Path(name);
        double time_astar = System.currentTimeMillis();
        System.out.println("Running A-Star " + (orderdWaypoints.size() - 1) + " times");
        List<String> visitedEdges = new ArrayList<String>();
        if (orderdWaypoints.size() > 2) {
            // A Stern im Rundweg
            int loopcount;
            if (loop) loopcount = orderdWaypoints.size() - 1;
            else loopcount = orderdWaypoints.size() - 2;
            try {
                for (int i = 0; i < loopcount; i++) {
                    Node start = waynotes[i];
                    Node end = waynotes[(i + 1) % (orderdWaypoints.size())];
                    String segmentName = orderdWaypoints.get(i).getName() + " - " + orderdWaypoints.get((i + 1) % (orderdWaypoints.size())).getName();
                    AStarResult result = AStarAlgorithm.search(graph, start, end, segmentName, w, 0, Application.lsiWeights, weightedLsi, visitedEdges, reduceDoubleEdges);
                    result.print();

                    PathSegment segment = result.getPath();
                    segment.setStartWaypoint(orderdWaypoints.get(i));
                    segment.setEndWaypoint(orderdWaypoints.get((i + 1) % (orderdWaypoints.size())));
                    globalLength += distance.distanceLinestring(segment.getSegmentLine(true));
                    path.addSegment(segment);
                }
            } catch (Exception e) {
                System.out.println("Error running A-Star: " + e.toString());
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            try {
                Node start = waynotes[0];
                Node end = waynotes[1];
                String segmentName = orderdWaypoints.get(0).getName() + " - " + orderdWaypoints.get(1).getName();
                AStarResult result = AStarAlgorithm.search(graph, start, end, segmentName, w, 0, Application.lsiWeights, weightedLsi, visitedEdges, reduceDoubleEdges);
                result.print();
                PathSegment segment = result.getPath();
                segment.setStartWaypoint(orderdWaypoints.get(0));
                segment.setEndWaypoint(orderdWaypoints.get(1));
                globalLength += result.getLength();
                path.addSegment(segment);
                System.out.println(result.getPath().getLength());
            } catch (Exception e) {
                System.out.println("Error running A-Star: " + e.toString());
                e.printStackTrace();
                System.exit(1);
            }
        }
        time_astar = (System.currentTimeMillis() - time_astar) / 1000;
        System.out.println();
        System.out.println("A-Star took " + time_astar + " Secs");
        System.out.println("Global Length: " + globalLength + " Meter");
        System.out.println("Global Length (including Waypoints): " + distance.distanceLinestring(path.getGeoLineString()) + " Meter");

        try {
            LineString line = path.getGeoLineString();

            FileWriter fstream = new FileWriter(name + "-Topo.GPX");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(GPXBuilder.build(path.getTopoLineString(), path.getName()));
            out.close();
            fstream.close();
            fstream = new FileWriter(name + "-Topo.txt");
            out = new BufferedWriter(fstream);
            out.write(DORENDABuilder.build(path.getTopoLineString()));
            out.close();
            fstream.close();
            fstream = new FileWriter(name + "-Geo.gpx");
            out = new BufferedWriter(fstream);
            out.write(GPXBuilder.build(path.getGeoLineString(), path.getName()));
            out.close();
            fstream.close();
            fstream = new FileWriter(name + "-Geo.txt");
            out = new BufferedWriter(fstream);
            out.write(DORENDABuilder.build(path));
            out.close();
            fstream.close();
        } catch (IOException e) {
            System.out.println("Error writing GPX file");
        }
    }

    // Benchmarks
    static void benchmarkMenu() {
        List<Position> places = new ArrayList<Position>();
        places.add(new Position(-10.99845000, 49.39349500));
        places.add(new Position(-11.18493167, 49.49496333));
        boolean quit = false;
        do {
            System.out.println("==================================================");
            System.out.println("Benchmarks");
            System.out.println("==================================================");
            System.out.println();
            System.out.println("Please Make a selection:");
            System.out.println("[1] Weighted A-Star");
            System.out.println("[2] A-Star with limited OpenList");
            System.out.println("[3] TSP Methodes");
            System.out.println("[0] exit");

            System.out.println("Selection: ");
            System.out.print("Please enter your choice: ");
            System.out.println();
            int menu = scan.nextInt();
            switch (menu) {
                case 1:
                    System.out.print("Start W: ");
                    double wStart = scan.nextDouble();
                    System.out.print("End W: ");
                    double wEnd = scan.nextDouble();
                    System.out.print("Steps: ");
                    double wStep = scan.nextDouble();
                    compareAStarW(graph, places, wStart, wEnd, wStep);
                    break;
                case 2:
                    System.out.print("Start W: ");
                    int lStart = scan.nextInt();
                    System.out.print("End W: ");
                    int lEnd = scan.nextInt();
                    System.out.print("Steps: ");
                    int lStep = scan.nextInt();
                    compareAStarLimitedOpen(graph, places, lStart, lEnd, lStep);
                    break;
                case 3:
                    compareTSP();
                    break;
                case 0:
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid Entry!");
            }
        }
        while (!quit);
    }

    static void compareAStarW(NavGraph graph, List<Position> places, double start, double end, double step) {
        if (end < start) {
            double tmp = end;
            end = start;
            start = tmp;
        }
        Node startNode = graph.findClosest(places.get(0), false);
        Node endNode = graph.findClosest(places.get((1)), false);
        System.out.println("==================================================");
        System.out.println("Benchmark A-Star with weighted h");
        System.out.println("From " + start + " to " + end + " steps " + step);
        System.out.println(distance.calcDist(startNode, endNode));
        System.out.println("==================================================");
        System.out.println();

        // Vergleichs A-Stern mit w = 1
        AStarResult compareResult;
        try {
            compareResult = AStarAlgorithm.search(graph, startNode, endNode, "Compare", 1);
        } catch (Exception e) {
            return;
        }

        //
        double curr;
        for (curr = start; curr <= end; curr += step) {
            Path path = new Path("");
            AStarResult result;
            try {
                result = AStarAlgorithm.search(graph, startNode, endNode, "Compare " + curr, curr);
                path.addSegment(result.getPath());
            } catch (Exception e) {
                return;
            }
            try {
                FileWriter fstream = new FileWriter(curr + ".gpx");
                BufferedWriter out = new BufferedWriter(fstream);
                String gpx = GPXBuilder.build(path.getGeoLineString(), path.getName());
                out.write(gpx);
                out.close();
                fstream.close();
            } catch (Exception e) {
                return;
            }

            System.out.println("Result for w=" + curr);
            System.out.println("Expanded Nodes: " + result.getExpandedNodes() + " (" + (result.getExpandedNodesCount() - compareResult.getExpandedNodesCount()) + ")");
            System.out.println("Path Length: " + result.getPath().getLength() + " (" + (result.getPath().getLength() - compareResult.getPath().getLength()) + ")");
            System.out.println("SortTime: " + result.getSorttime() + " (" + (result.getSorttime() - compareResult.getSorttime()) + ")");
            System.out.println("Runtime: " + result.getRuntime() + " (" + (result.getRuntime() - compareResult.getRuntime()) + ")");
            System.out.println();
        }
    }

    static void compareAStarLimitedOpen(NavGraph graph, List<Position> places, int start, int end, int step) {
        if (end < start) {
            int tmp = end;
            end = start;
            start = tmp;
        }
        Node startNode = graph.findClosest(places.get(0), false);
        Node endNode = graph.findClosest(places.get((1)), false);
        System.out.println("==================================================");
        System.out.println("Benchmark A-Star with weighted h");
        System.out.println("From " + start + " to " + end + " steps " + step);
        System.out.println(distance.calcDist(startNode, endNode));
        System.out.println("==================================================");
        System.out.println();

        AStarResult compareResult;
        try {
            compareResult = AStarAlgorithm.search(graph, startNode, endNode, "Compare", 1, 0);
        } catch (Exception e) {
            return;
        }

        //
        int curr;
        for (curr = start; curr <= end; curr += step) {
            Path path = new Path("");
            AStarResult result;
            try {
                result = AStarAlgorithm.search(graph, startNode, endNode, "Compare " + curr, 1, curr);
                path.addSegment(result.getPath());
            } catch (Exception e) {
                return;
            }
            try {
                FileWriter fstream = new FileWriter(curr + ".gpx");
                BufferedWriter out = new BufferedWriter(fstream);
                String gpx = GPXBuilder.build(path.getGeoLineString(), path.getName());
                out.write(gpx);
                out.close();
                fstream.close();
            } catch (Exception e) {
                return;
            }

            System.out.println("Result for w=" + curr);
            System.out.println("Expanded Nodes: " + result.getExpandedNodes() + " (" + (result.getExpandedNodesCount() - compareResult.getExpandedNodesCount()) + ")");
            System.out.println("Path Length: " + result.getPath().getLength() + " (" + (result.getPath().getLength() - compareResult.getPath().getLength()) + ")");
            System.out.println("SortTime: " + result.getSorttime() + " (" + (result.getSorttime() - compareResult.getSorttime()) + ")");
            System.out.println("Runtime: " + result.getRuntime() + " (" + (result.getRuntime() - compareResult.getRuntime()) + ")");
            System.out.println();
        }
    }

    static void compareTSP() {
        List<Waypoint> waypoints;
        List<Waypoint> orderdWaypoints;
        try {
            waypoints = CSV.readWaypointList("01.csv");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("==================================================");
        System.out.println("TSP Compare");
        System.out.println("Number of Waypoints: " + waypoints.size());
        System.out.println("==================================================");
        System.out.println();
        for (TSP.tspType currTSP : TSP.tspType.values()) {
            double time_tsp = System.currentTimeMillis();
            System.out.println("Running " + currTSP + " with " + (waypoints.size()) + " Points");
            try {
                orderdWaypoints = TSP.sort(waypoints, currTSP);
                time_tsp = (System.currentTimeMillis() - time_tsp) / 1000;
                FileWriter fstream;
                try {
                    fstream = new FileWriter(currTSP.toString() + ".GPX");
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(GPXBuilder.build(orderdWaypoints, currTSP.toString()));
                    out.close();
                } catch (IOException e) {
                    System.out.println("Error writing GPX File");
                }
                System.out.println("Path length: " + distance.distanceList(orderdWaypoints));
                System.out.println(currTSP + " took " + time_tsp + " Secs");
                System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
        }
        System.out.println();
    }

    // Graph loader
    static void loadGraph_File(String filename) {
        // Graph aus Datei laden
        double time_graph = System.currentTimeMillis();
        filename = filename + ".DAT";
        Application.println("============================================================");
        Application.println("Fetching Data from " + filename + " and building graph");
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream obj_in = new ObjectInputStream(fis);
            Object obj = obj_in.readObject();
            if (obj instanceof NavGraph) {
                Application.graph = (NavGraph) obj;
            } else {
                throw new Exception("Saved Data != NavGraph");
            }
            obj_in.close();
            fis.close();
            time_graph = (System.currentTimeMillis() - time_graph) / 1000;
            Application.println("Fetching Data and building graph took: " + time_graph + "sec");
            Application.println("============================================================");
        } catch (Exception e) {
            Application.println("Error: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }
    }

    static void loadGraph_Database(Position[] bBox, String name) {
        // Graph aus Datei laden
        double time_graph = System.currentTimeMillis();
        System.out.println("============================================================");
        Application.connection = new geoDatabaseConnection(dbhost, dbport, dbuser, dbpasswd, dbname);

        // Graph generieren
        File file = new File(name + ".DAT");
        // Graph von DB laden
        Application.println("Fetching Data from DB and building graph");
        try {
            Application.connection.connect();
            graph = Application.connection.getGraph(bBox, name);
            FileOutputStream fos = new FileOutputStream(file.getName());
            ObjectOutputStream out2 = new ObjectOutputStream(fos);
            out2.writeObject(graph);
            out2.close();
            fos.close();
            time_graph = (System.currentTimeMillis() - time_graph) / 1000;
            Application.println("Fetching Data and building graph took: " + time_graph + "sec");
            Application.println("============================================================");
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }
    }

    static void println(String line) {
        if (verbose) System.out.println(line);
    }
}
