package ohm.roth.lbs;

import java.util.Random;

/**
 * User: mischarohlederer
 * Date: 14.11.11
 * Time: 11:44
 */
public class GeneticPath implements Comparable<GeneticPath> {
    static double[][] distances;
    static Random random;
    static int length;

    public static void setRandomMutations(int randomMutations) {
        GeneticPath.randomMutations = randomMutations;
    }

    static int randomMutations = 0;

    public static void init(double[][] d, int n, long seed) {
        GeneticPath.distances = d;
        GeneticPath.random = new Random(seed);
        length = n;
    }

    public int[] path;

    GeneticPath() {
        // constructor
        path = new int[length];
    }

    GeneticPath(GeneticPath p) {
        // copy constructor
        this();
        System.arraycopy(p.path, 0, path, 0, length);
    }

    void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    int randomInt(int x) {
        return random.nextInt(x);
    }

    int randomWaypoint() {
        return randomInt(length);
    }

    void initRandomPath() {
        for (int i = 0; i < length; i++)
            path[i] = i + 1;

        for (int i = 0; i < length; i++)
            swap(path, i, randomWaypoint());
    }

    GeneticPath mutate() {
        if (randomMutations != 0) {
            // random mutation
            switch (randomMutations) {
                case 1:
                    return mutateSwap();
                case 2:
                    return mutateSubpath();
                default:
                    return mutateSwap();
            }
        } else {
            int rnd = randomInt(1) + 1;
            switch (rnd) {
                case 1:
                    return mutateSwap();
                case 2:
                    return mutateSubpath();
                default:
                    return mutateSwap();
            }
        }
    }


    //Mutationen

    // Swap
    // vertauscht 2 Waypoints
    GeneticPath mutateSwap() {
        GeneticPath mutant = new GeneticPath(this);
        int mut1 = randomInt(length);
        int mut2 = randomInt(length);
        swap(mutant.path, mut1, mut2);
        return mutant;
    }

    // Subpath
    // Ändert die reihenvolge eines teiles der reihenfolge
    GeneticPath mutateSubpath() {
        GeneticPath mutant = new GeneticPath(this);
        int len = randomInt(length - 2) + 2;
        int start = randomInt(length - len);
        int newPos = randomInt(length - len);

        for (int i = 0, end = len - 1; i < len; i++, end--) {
            mutant.path[start + end] = path[newPos + i];
        }

        for (int i = 0, end = len - 1; i < len; i++, end--) {
            mutant.path[newPos + i] = path[start + i];
        }

        return mutant;
    }

    // Kombiniert Waypoint Liste von M&D
    // Bis zu einen zufälligen Punkt werden die Waypoints von M übernommen
    // Die Restlichen werden in der Reihenfolge von D angehängt
    GeneticPath breed(GeneticPath D) {
        GeneticPath M = this;
        int crossoverPoint = randomInt(length - 2) + 1;
        GeneticPath baby = new GeneticPath();

        // Anteil von M
        System.arraycopy(M.path, 0, baby.path, 0, crossoverPoint);

        int start = crossoverPoint;

        // Anteil von D
        for (int i = 0; i < length; i++) {
            int currWaypoint = D.path[i];

            boolean gotWaypoint = false;
            for (int j = 0; j < start; j++) {
                if (baby.path[j] == currWaypoint) {
                    gotWaypoint = true;
                    break;
                }
            }

            if (!gotWaypoint) {
                baby.path[start++] = currWaypoint;
            }
        }

        return baby;
    }

    // Vergleiche 2 Pathes
    public boolean equals(Object other) {
        int[] p = ((GeneticPath) other).path;

        for (int i = 0; i < p.length; i++)
            if (path[i] != p[i])
                return false;
        return true;
    }
    public int compareTo(GeneticPath other) {
        if (getLength() < other.getLength())
            return -1;
        if (other.getLength() < getLength())
            return 1;
        return 0;
    }

    // Länge des Pathes
    double getLength() {
        double distance = 0;

        int currWaypoint1 = 0;
        int currWaypoint2 = path[0];

        distance += distances[currWaypoint1][currWaypoint2];

        for (int i = 0; i < length - 1; i++) {
            currWaypoint1 = path[i];
            currWaypoint2 = path[i + 1];
            distance += distances[currWaypoint1][currWaypoint2];
        }
        currWaypoint1 = path[length - 1];
        currWaypoint2 = 0;

        distance += distances[currWaypoint1][currWaypoint2];
        return distance;
    }


}
