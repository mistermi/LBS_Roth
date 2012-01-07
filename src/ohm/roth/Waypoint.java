package ohm.roth;

/**
 * Beschreibung eines POIs
 */
public class Waypoint extends Position {
    protected String name;

    /**
     * Der Name des POIs
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Aendert den Namen
     * @param name Der neue Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Konstruktor
     * @param name Name des POIs
     * @param lon Longitude
     * @param lat Latitude
     */
    public Waypoint(String name, double lon, double lat) {
        super(lon, lat);
        this.name = name;
    }
}
