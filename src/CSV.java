import ohm.roth.Position;
import ohm.roth.Waypoint;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: mischarohlederer
 * Date: 26.10.11
 * Time: 13:08
 */
class CSV {
    public static List<Waypoint> readWaypointList(String filename) throws Exception {
        List<Waypoint> retList = new ArrayList<Waypoint>();
        FileInputStream fis = new FileInputStream(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        Waypoint currWaypoint;
        String strLine;
        while ((strLine = in.readLine()) != null) {
            String[] strArr = strLine.split(",");
            String name = strArr[0];
            double lon = Double.valueOf(strArr[1]);
            double lat = Double.valueOf(strArr[2]);
            currWaypoint = new Waypoint(name, lon, lat);
            retList.add(currWaypoint);
        }
        in.close();
        fis.close();
        return retList;
    }

    public static Position[] readPositionArray(String filename) throws Exception {
        List<Position> retList = new ArrayList<Position>();
        FileInputStream fis = new FileInputStream(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        Position currPos;
        String strLine;
        while ((strLine = in.readLine()) != null) {
            String[] strArr = strLine.split(",");
            double lon = Double.valueOf(strArr[0]);
            double lat = Double.valueOf(strArr[1]);
            currPos = new Position(lon, lat);
            retList.add(currPos);
        }
        in.close();
        fis.close();
        return retList.toArray(new Position[retList.size()]);
    }

    public static HashMap<String, Double> readLSIWeight(String filename) throws Exception {
        HashMap<String, Double> retList = new HashMap<String, Double>();
        FileInputStream fis = new FileInputStream(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        String strLine;
        while ((strLine = in.readLine()) != null) {
            String[] strArr = strLine.split(",");
            String id = strArr[0];
            double weight = Double.valueOf(strArr[1]);
            retList.put(id, weight);
        }
        in.close();
        fis.close();
        return retList;
    }

    public static void writeCSV(String filename, HashMap<String, String[]> map) throws Exception {
	    FileWriter writer = new FileWriter(filename);
        for (String key : map.keySet()) {
            writer.append(key);

            String[] val = map.get(key);
            for (String curr : val) {
                writer.append(',');
                writer.append(curr);
            }
            writer.append('\n');
            writer.flush();
        }
	    writer.close();
    }


}
