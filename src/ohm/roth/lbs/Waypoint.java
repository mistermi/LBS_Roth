package ohm.roth.lbs;

/**
 * User: mischarohlederer
 * Date: 26.10.11
 * Time: 13:15
 */
public class Waypoint extends Position {
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Waypoint(String name, double lon, double lat) {
        super(lon, lat);
        this.name = name;
    }
}
