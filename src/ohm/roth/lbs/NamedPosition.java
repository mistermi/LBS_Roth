package ohm.roth.lbs;


public class NamedPosition extends Position {
	private String name;
	
	public NamedPosition(double lon, double lat, String n) {
		super(lon, lat);
		this.name = n;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
