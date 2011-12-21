import ohm.roth.Position;
import ohm.roth.distance;

/**
 * User: mischarohlederer
 * Date: 27.10.11
 * Time: 11:56
 */
public class LBS_Roth {
    public static void main(String[] args) {
        // Standart Einstellungen
        Application.dbhost = "geo.informatik.fh-nuernberg.de";
        Application.dbport = 5432;
        Application.dbuser = "dbuser";
        Application.dbpasswd = "dbuser";
        Application.dbname = "deproDBMittelfranken";
        Application.graphname = "Data";
        Application.boundingBox = new Position[2];
        Application.boundingBox[0] = new Position(-11.18493167, 49.39349500);
        Application.boundingBox[1] = new Position(-10.99845000, 49.49496333);
        // Dis = 67423.2255363751
        Application.mainMenu();
    }
}
