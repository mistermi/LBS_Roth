package ohm.roth.tsp;

import java.util.Random;

/**
 * Beschreibung eines Rundweges fuer den Genetisch Algo fuer das TSP
 */
public class GeneticPath implements Comparable<GeneticPath> {
    static double[][] distances;
    static Random random;
    static int length;
    static int randomMutations = 0;

    /**
     * Bestimmt die art der zu verwendeten Mutationen
     * n: Mutation Nummer n
     * 0: Zufaellig
     * @param randomMutations Die art der Mutationen
     */
    public static void setRandomMutations(int randomMutations) {
        GeneticPath.randomMutations = randomMutations;
    }

    /**
     * Initialisiert die Klasse mit neuen Kosten
     * @param d 2 Diminsionales Array [i][j] das jeweils die Kosten zwischen 2 Punkten i und j angiebt
     * @param n Anzahl der Punkte
     * @param seed Seed fuer den Zufallszahlen generator
     */
    public static void init(double[][] d, int n, long seed) {
        GeneticPath.distances = d;
        GeneticPath.random = new Random(seed);
        length = n;
    }

    public int[] path;

    /**
     * Konstruktor fuer ein neues Objekt
     */
    GeneticPath() {
        path = new int[length];
    }

    /**
     * Kopier Konstruktor
     * @param p Der zu kopierende Pfad
     */
    GeneticPath(GeneticPath p) {
        this();
        System.arraycopy(p.path, 0, path, 0, length);
    }

    /**
     * Vertauscht 2 Werte aus einem int Array
     * @param a Das Array
     * @param i Der erste Wert
     * @param j Der zweite Wert
     */
    void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    /**
     * Auswahl eines Zufaelligen Wegpunktes
     * @return Die ID eines Zufaelligen Wegpunktes
     */
    int randomWaypoint() {
        return random.nextInt(length);
    }

    /**
     * Initialisiert den Pfad mit einer Zufaelligen Loesung
     */
    void initRandomPath() {
        for (int i = 0; i < length; i++)
            path[i] = i + 1;

        for (int i = 0; i < length; i++)
            swap(path, i, randomWaypoint());
    }

    /**
     * Giebt eine Mutation des aktuellen Pfades zurueck
     * @return Der Mutierte Pfad
     */
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
            int rnd = random.nextInt(1) + 1;
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

   /**
     * Swap Mutation
     * Vertauscht 2 Wegpunkte innerhalb des Pfades
     * @return Der Mutierte Pfad
     */
    GeneticPath mutateSwap() {
        GeneticPath mutant = new GeneticPath(this);
        int mut1 = random.nextInt(length);
        int mut2 = random.nextInt(length);
        swap(mutant.path, mut1, mut2);
        return mutant;
    }

    /**
     * Subpath Mutation
     * Aendert die Reihenfolge eines Teilpfades innerhalb des Pfades
     * @return Der Mutierte Pfad
     */
    GeneticPath mutateSubpath() {
        GeneticPath mutant = new GeneticPath(this);
        int len = random.nextInt(length - 2) + 2;
        int start = random.nextInt(length - len);
        int newPos = random.nextInt(length - len);

        for (int i = 0, end = len - 1; i < len; i++, end--) {
            mutant.path[start + end] = path[newPos + i];
        }

        for (int i = 0, end = len - 1; i < len; i++, end--) {
            mutant.path[newPos + i] = path[start + i];
        }

        return mutant;
    }

    /**
     * Verbindest diesen und einen anderen Angegeben Pfad zu einem neuen
     * Aus diesem Pfad wird die Reihenfolge der ersten n (n: Zufaellig) Wegpunkte uebernommen. Die Reihenfolge der restlichen wird aus dem 2. Pfad uebernommen
     * @param D Der 2. Pfad
     * @return Der Zusammengefuehrte Pfad
     */
    GeneticPath breed(GeneticPath D) {
        GeneticPath M = this;
        int crossoverPoint = random.nextInt(length - 2) + 1;
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

    /**
     * Prueft ob 2 Pfade gleich sind
     * @param other Der zu pruefende Pfad
     * @return true | false
     */
    public boolean equals(Object other) {
        if (other instanceof GeneticPath) {
            int[] p = ((GeneticPath) other).path;

            for (int i = 0; i < p.length; i++)
                if (path[i] != p[i])
                    return false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Vergleicht 2 Pfade anhade der Kosten des durch sie beschriebenen Pfades
     * @param other Der zu vergleichende Pfad
     * @return Vergleichsrueckgabe
     */
    public int compareTo(GeneticPath other) {
        if (getLength() < other.getLength())
            return -1;
        if (other.getLength() < getLength())
            return 1;
        return 0;
    }

    /**
     * Giebt die Kosten des durch den Pfad beschriebenen Weges Zurueck
     * @return Die Kosten
     */
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
