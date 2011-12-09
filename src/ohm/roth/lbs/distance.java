package ohm.roth.lbs;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

import java.util.List;

public class distance {
    public static double calcDist(Node n, Position p) {
         return calcDist(n.getPosition(), p);
    }

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

	public static double calcDist(Position p1, Position p2) {
        return p1.distance(p2) * (Math.PI/180) * 6371000.785;
	}

    public static double calcDist(Coordinate p1, Coordinate p2) {
        return p1.distance(p2) * (Math.PI/180) * 6371000.785;
	}

	public static double calcDist(Node n1, Node n2) {
		return calcDist(n1.getPosition(), n2.getPosition());
	}
	
	/*public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000.785;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }*/
	
	public static double distanceList(List<Waypoint> list) {
        double l = 0;
        for (int i=1; i<list.size(); i++) {
            l+=distance.calcDist(list.get(i-1), list.get(i%list.size()));
        }
        return l;
    }

    public static double distanceLinestring(LineString line, int start, int end) {
        double dis = 0;
        if (end == start) return 0;
        if (end >= line.getNumPoints()) return -1;
        if (end < start) {
            int foo = end;
            end = start;
            start = foo;
        }
        for (int i=start; i<end - 1; i++) {
            dis += calcDist(line.getPointN(i).getCoordinate(), line.getPointN(i+1).getCoordinate());
        }
        return dis;
    }
    public static double distanceLinestring(LineString line) {
        double dis = 0;
        for (int i=0; i<line.getNumPoints()-2; i++) {
            dis += calcDist(line.getPointN(i).getCoordinate(), line.getPointN(i+1).getCoordinate());
        }
        return dis;
    }

}
