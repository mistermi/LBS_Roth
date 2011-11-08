import ohm.roth.lbs.Waypoint;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mischarohlederer
 * Date: 26.10.11
 * Time: 13:08
 */
public class CSVParse {
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
}
