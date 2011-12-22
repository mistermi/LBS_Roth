package ohm.roth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: mischarohlederer
 * Date: 22.12.11
 * Time: 09:42
 */
public class random {
    private static Random rnd = new Random();

    /**
     * Initialisiert den Zufallszahlengenerator mit einem neuen Seed
     * @param seed Der zu benutzende Seed
     */
    public static void newRandomSeed(long seed) {
        random.rnd = new Random(seed);
    }

    /**
     * Generiert eine Zufällige Double Zahl zwischen 2 gegeben Werten
     * @param min Die untergrenze
     * @param max Die obergrenze
     * @return Die Zufallszahl
     */
    public static double doubleBetween(double min, double max) {
        return rnd.nextDouble() * (max - min) + min;
    }

    /**
     * Generiert eine zufällige Position
     * @return Die Generierte Position
     */
    public static Position position() {
        return new Position(random.doubleBetween(-180,180), random.doubleBetween(-90,90));
    }

    /**
     * Genertiert eine Liste von n Zufälligen Wegpunkten
     * @param count Anzahl der zu generierenden Wegpunkte
     * @return Liste der generierten Wegpunkte
     */
    public static List<Waypoint> randomWaypoints(int count) {
        List<Waypoint> ret = new ArrayList<Waypoint>();
        for (int i = 0; i<count; i++) {
            ret.add(new Waypoint("Waypoint "+i+1,random.doubleBetween(-180,180), random.doubleBetween(-90,90)));
        }
        return ret;
    }

}
