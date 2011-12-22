/**
 * User: mischarohlederer
 * Date: 27.10.11
 * Time: 11:56
 */
public class LBS_Roth {
    public static void main(String[] args) {
        // Datenbank
        Application.dbhost = "geo.informatik.fh-nuernberg.de";
        Application.dbport = 5432;
        Application.dbuser = "dbuser";
        Application.dbpasswd = "dbuser";
        Application.dbname = "deproDB2";
        // Graph
        Application.graphname = "Nuernberg";
        Application.boundingBoxFile = "Nuernberg_BoundingBox.csv";


        Application.mainMenu();
    }
}
