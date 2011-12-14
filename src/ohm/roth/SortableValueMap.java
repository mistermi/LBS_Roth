package ohm.roth;

import java.util.*;

public class SortableValueMap<K, V extends Comparable<V>> extends LinkedHashMap<K, V> {
    private int limit = 0;

    public SortableValueMap() {
    }

    public SortableValueMap(int l) {
        limit = l;
    }

    public SortableValueMap(Map<K, V> map) {
        super(map);
    }

    public SortableValueMap(Map<K, V> map, int l) {
        super(map);
        limit = l;
    }

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

    private static void print(String text, Map<String, Double> map) {
        System.out.println(text);
        for (String key : map.keySet()) {
            System.out.println("key/value: " + key + "/" + map.get(key));
        }
    }
}