package ohm.roth.lbs;
import java.io.Serializable;

public class Position implements Serializable {
    private double lon;
    private double lat;
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public Position(double lon, double lat) {
		super();
		this.lon = lon;
		this.lat = lat;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}

}
