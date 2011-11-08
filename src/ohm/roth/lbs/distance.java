package ohm.roth.lbs;

public class distance {
	public static enum unit {
		meter, km
	}

	private static double unitConvert(unit u) {
		switch (u) {
		case meter:
			return 1000;
		case km:
			return 1;
		default:
			return 1;
		}
	}

	public static double calcDist(Position p1, Position p2, unit u) {
        return distFrom(p1.getLat(), p1.getLon(), p2.getLat(), p2.getLon());
	}
	
	public static double calcDist(Position p1, Position p2) {
		return calcDist(p1, p2, unit.meter);
	}

	public static double calcDist(Node n1, Node n2) {
		return calcDist(n1.getPosition(), n2.getPosition(), unit.meter);
	}
	
	public static double calcDist(Node n1, Node n2, unit u) {
		return calcDist(n1.getPosition(), n2.getPosition(), u);
	}
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000.785;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }
	
	

}
