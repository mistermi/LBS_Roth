package ohm.roth.tsp;

import java.util.Arrays;
import java.util.Random;

/**
 * Beschreibung einer Population von GeneticPath Objekten fuer den Genetischen Loesungsansatz des TSP
 */
public class GeneticPopulation {
    int size;
    int initialSize;
    public GeneticPath[] population;

    /**
     * Initialisiert die Population in angegebener Groesse
     * @param size Die Populationsgroesse
     */
    public GeneticPopulation(int size) {
        this.size = size;
        initialSize = size;
        population = new GeneticPath[initialSize * 4];
    }

    /**
     * Belegt die Initialpopulation mit zufaelligen Pfaden
     */
    public void addInitialPaths() {
        for (int i = 0; i < initialSize; i++) {
            population[i] = new GeneticPath();
            population[i].initRandomPath();
        }
    }

    /**
     * Fuegt der ausgangspopulation Mutationen hinzu
     */
    public void addMutants() {
        for (int i = 0; i < initialSize; i++) {
            population[size++] = population[i].mutate();
        }
    }

    /**
     * Verbindet jeden Pfad in der Population mit einem zufaelligen Partner
     */
    public void addBabies() {
        Random rnd = new Random();
        for (int i = 0; i < initialSize * 2; i++) {
            GeneticPath M = population[i];
            int random = rnd.nextInt(initialSize * 2);
            GeneticPath D = population[random];
            GeneticPath baby = M.breed(D);
            population[size++] = baby;
        }
    }

    /**
     * Sortiert die Population anhand der Kosten der einzelnen Pfade aus undschrumpft sie dadurch wieder auf ihre ausgangsgroesse
     */
    public void select() {
        Arrays.sort(population, 0, size);
        int current = 0;
        int nextDifferent = 1;
        while (current < initialSize && nextDifferent < size) {
            if (population[current].equals(population[nextDifferent])) {
                while (nextDifferent < size - 1 && population[current].equals(population[nextDifferent])) {
                    nextDifferent++;
                }
            }
            population[current + 1] = population[nextDifferent];
            current++;
            nextDifferent++;
        }

        size = initialSize;
    }
}
