package ohm.roth;

import java.util.*;

/**
 * Sortierbare HashMap
 * @param <K> Typ von Key
 * @param <V> Typ von Value
 */
public class SortableValueMap<K, V extends Comparable<V>> extends LinkedHashMap<K, V> {
    private int limit = 0;

    /**
     * Konstruktor
     */
    public SortableValueMap() {
    }

    /**
     * Konstruktor mit Limit (maximale groesse der Hashmap
     * @param l Das Limit
     */
    public SortableValueMap(int l) {
        limit = l;
    }

    /**
     * Copy Konstruktor
     * @param map Die zu kopiernende HashMap
     */
    public SortableValueMap(Map<K, V> map) {
        super(map);
    }

    /**
     * Copy Konstruktor mit neuem Limit
     * @param map Die zu kopiernende HashMap
     * @param l Das neue Limit
     */
    public SortableValueMap(Map<K, V> map, int l) {
        super(map);
        limit = l;
    }

    /**
     * Sortiert nie Hashmap (und wendet das Limit an)
     */
    public void sortByValue() {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> entry1, Map.Entry<K, V> entry2) {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });
        clear();

        int count = 0;
        for (Map.Entry<K, V> entry : list) {
            put(entry.getKey(), entry.getValue());
            count++;
            if (count >= limit && limit != 0) break;
        }
    }
}