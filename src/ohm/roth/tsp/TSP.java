package ohm.roth.tsp;

import ohm.roth.Waypoint;
import ohm.roth.distance;

import java.util.ArrayList;
import java.util.List;

public class TSP {
    public static enum tspType {
        GENETIC_QUICK,
        GENETIC_MED,
        GENETIC_GOOD,
        ORGINAL,
        CLOSEST_NEIGHBOR
    }

    static public List<Waypoint> sort(List<Waypoint> places, tspType typ) {
        switch (typ) {
            case CLOSEST_NEIGHBOR:
                return closestNeighbor(places);
            case ORGINAL:
                return places;
            case GENETIC_QUICK:
                return geneticAlgoQuick(places);
            case GENETIC_MED:
                return geneticAlgoMed(places);
            case GENETIC_GOOD:
                return geneticAlgoGood(places);
            default:
                return null;
        }
    }

    static private List<Waypoint> closestNeighbor(List<Waypoint> places) throws IllegalArgumentException {
        List<Waypoint> workingCopy = new ArrayList<Waypoint>();
        List<Waypoint> orderdPlaces = new ArrayList<Waypoint>();
        workingCopy.addAll(places);
        Waypoint curr = workingCopy.get(0);
        Waypoint candy;
        workingCopy.remove(0);
        do {
            candy = null;
            for (Waypoint currTest : workingCopy) {
                if (candy == null) {
                    candy = currTest;
                    continue;
                }
                if (distance.calcDist(candy, curr) > distance.calcDist(currTest, curr)) {
                    candy = currTest;
                }
            }
            orderdPlaces.add(candy);
            workingCopy.remove(candy);
        } while (workingCopy.size() > 0);
        return orderdPlaces;
    }

    private static void testPath(List<Waypoint> best, List<Waypoint> source, List<Waypoint> curr, Waypoint start) {

        if (curr.size() == (source.size() + 1) && best.size() != (source.size() + 1)) {
            best.addAll(curr);
            return;
        }
        // neuer bester pfasd
        if (curr.size() == (source.size() + 1) && best.size() == (source.size() + 1) && distance.distanceList(best) > distance.distanceList(curr)) {
            best.clear();
            best.addAll(curr);
            return;
        }

        if (distance.distanceList(best) < distance.distanceList(curr)) {
            return;
        }
        if (curr.size() == source.size()) {
            curr.add(start);
            testPath(best, source, curr, start);
        }

        for (Waypoint currWaypoint : source) {
            if (curr.contains(currWaypoint)) {
                continue;
            }
            List<Waypoint> newCurr = new ArrayList<Waypoint>();
            newCurr.addAll(curr);
            newCurr.add(currWaypoint);
            testPath(best, source, newCurr, start);
        }
    }

    static private List<Waypoint> bruteForce(List<Waypoint> places) throws IllegalArgumentException {
        if (places.size() > 5) {
            throw new IllegalArgumentException("Size > 5 (" + places.size() + ")");
        }
        List<Waypoint> workingCopy = new ArrayList<Waypoint>();
        workingCopy.addAll(places);
        List<Waypoint> bestPlaces = new ArrayList<Waypoint>();
        for (Waypoint currTest : places) {
            List<Waypoint> curr = new ArrayList<Waypoint>();
            curr.add(currTest);
            testPath(workingCopy, places, curr, currTest);
        }
        return bestPlaces;
    }

    static private List<Waypoint> geneticAlgoQuick(List<Waypoint> places) {
        return geneticAlgoPara(places, 20, 100, 1);
    }

    static private List<Waypoint> geneticAlgoMed(List<Waypoint> places) {
        return geneticAlgoPara(places, 20, 500, 0);
    }

    static private List<Waypoint> geneticAlgoGood(List<Waypoint> places) {
        return geneticAlgoPara(places, 100, 20000, 0);
    }

    static public List<Waypoint> geneticAlgoPara(List<Waypoint> places, int populationSize, int generations, int randomMutations) {
        // Generiere distance Array
        double[][] distances = new double[places.size()][places.size()];
        for (int i = 0; i < places.size(); i++) {
            for (int j = 0; j < places.size(); j++) {
                if (i == j) {
                    distances[i][j] = distances[j][i] = 0;
                } else {
                    distances[i][j] = distances[j][i] = distance.calcDist(places.get(i), places.get(j));
                }
            }
        }
        GeneticPath.init(distances, places.size() - 1, 0);
        GeneticPath.setRandomMutations(randomMutations);
        GeneticPopulation population = new GeneticPopulation(populationSize);
        population.addInitialPaths();
        for (int i = 0; i < generations; i++) {
            population.addMutants();
            population.addBabies();
            population.select();
        }

        List<Waypoint> retList = new ArrayList<Waypoint>();
        retList.add(places.get(0));
        for (int i = 0; i < places.size() - 1; i++) {
            retList.add(places.get(population.population[0].path[i]));
        }
        retList.add(places.get(0));
        return retList;
    }
}
