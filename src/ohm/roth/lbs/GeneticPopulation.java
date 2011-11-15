package ohm.roth.lbs;

import java.util.Arrays;

/**
 * User: mischarohlederer
 * Date: 15.11.11
 * Time: 09:03
 */

public class GeneticPopulation {
    int size;
    int initialSize;
    public GeneticPath[] population;

    public GeneticPopulation(int size) {
        this.size = size;
        initialSize = size;
        population = new GeneticPath[initialSize * 4];
    }

    public void addInitialPaths(int size) {
        for (int i = 0; i < initialSize; i++) {
            population[i] = new GeneticPath();
            population[i].initRandomPath();
        }
    }

    // Generiert Mutanten (1* die initiale Grösse)
    public void addMutants() {
        for (int i = 0; i < initialSize; i++) {
            population[size++] = population[i].mutate();
        }
    }

    // Generiert Babys (jeder mit einem zufälligen)
    public void addBabies() {
        for (int i = 0; i < initialSize * 2; i++) {
            GeneticPath M = population[i];
            int random = M.randomInt(initialSize * 2);
            GeneticPath D = population[random];
            GeneticPath baby = M.breed(D);
            population[size++] = baby;
        }
    }

    // Darwin
    // Population sortieren und gleiche "aussortieren"
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
