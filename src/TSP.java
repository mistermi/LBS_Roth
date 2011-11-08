import ohm.roth.lbs.Waypoint;
import ohm.roth.lbs.distance;

import java.util.ArrayList;
import java.util.List;

// Klasse f�r das Traveling Salesman Problem
public class TSP {
    public static enum tspType {
		CLOSEST_NEIGHBOR
	}
	private tspType selectedType;

	public TSP(tspType selectedType) {
		super();
		this.selectedType = selectedType;
	}

	public tspType getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(tspType selectedType) {
		this.selectedType = selectedType;
	}

	public List<Waypoint> sort(List<Waypoint> places) {
		switch (this.selectedType) {
		case CLOSEST_NEIGHBOR:
			return this.closestNeighbor(places);
		default:
			return null;
		}
	}

    private List<Waypoint> closestNeighbor(List<Waypoint> places) {
		List<Waypoint> orderdPlaces = new ArrayList<Waypoint>();
		Waypoint curr = places.get(0);
		Waypoint candy = null;
		places.remove(0);
		do {
			candy = null;
			for (Waypoint currTest : places) {
				if (candy == null) {
					candy = currTest;
					continue;
				}
				if (distance.calcDist(candy, curr) > distance.calcDist(currTest, curr)) {
					candy = currTest;
				}
			}
			orderdPlaces.add(candy);
			places.remove(candy);
		} while (places.size() > 0);
		return orderdPlaces;
	}

}
