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
 * Klasse mit statischen Methode zum umgang mit CSV Datein
 */
class CSV {
    /**
     * Liest eine POI Liste ein
     * [name],lon,lat
     * @param filename Zu lesende Datei
     * @return Liste der eingelesenden POIs
     * @throws Exception IO Fehler
     */
    public static List<Waypoint> readWaypointList(String filename) throws Exception {
        List<Waypoint> retList = new ArrayList<Waypoint>();
        FileInputStream fis = new FileInputStream(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        Waypoint currWaypoint;
        String strLine;
        int i = 1;
        while ((strLine = in.readLine()) != null) {
            String[] strArr = strLine.split(",");
            String name;
            double lon, lat;
            if (strArr.length > 2) {
                name = strArr[0];
                lon = Double.valueOf(strArr[1]);
                lat = Double.valueOf(strArr[2]);
                i++;
                currWaypoint = new Waypoint(name, lon, lat);
                retList.add(currWaypoint);
            } else if (strArr.length == 2) {
                name = "Punkt " + i;
                lon = Double.valueOf(strArr[0]);
                lat = Double.valueOf(strArr[1]);
                i++;
                currWaypoint = new Waypoint(name, lon, lat);
                retList.add(currWaypoint);
            }
        }
        in.close();
        fis.close();
        return retList;
    }

    /**
     * Liest eine Liste mit Positionen ein
     * lon,lat
     * @param filename Zu lesende Datei
     * @return Liste der Positionen
     * @throws Exception IO Fehler
     */
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

    /**
     * Liest Gewichtungen fuer LSI Klassen als Hashmap ein
     * @param filename Zu lesende Datei
     * @return Hashmap: String(LSIKlasse), Double(Gewichtung)
     * @throws Exception IO Fehler
     */
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
}
