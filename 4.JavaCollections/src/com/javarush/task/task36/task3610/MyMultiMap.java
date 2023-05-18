package com.javarush.task.task36.task3610;

import java.io.Serializable;
import java.util.*;

public class MyMultiMap<K, V> extends HashMap<K, V> implements Cloneable, Serializable {
    static final long serialVersionUID = 123456789L;
    private HashMap<K, List<V>> map;
    private int repeatCount;

    public MyMultiMap(int repeatCount) {
        this.repeatCount = repeatCount;
        map = new HashMap<>();
    }

    @Override
    public int size() {
        ArrayList<V> arrayList = new ArrayList<>();
        for (List<V> list : map.values()) {
            arrayList.addAll(list);
        }
        return arrayList.size();
    }

    @Override
    public V put(K key, V value) {
        if (!map.containsKey(key)) {
            List<V> list = new LinkedList<>();
            list.add(value);
            map.put(key, list);
            return null;
        } else {
            List<V> last = new LinkedList<>(map.get(key));
            if (map.get(key).size() < repeatCount) {
                List<V> newList = new LinkedList<>(map.get(key));
                newList.add(value);
                map.put(key, newList);
                return last.get(last.size() - 1);
            } else if (map.get(key).size() == repeatCount) {
                List<V> newList = new LinkedList<>(map.get(key));
                newList.remove(0);
                newList.add(value);
                map.put(key, newList);
            }
            return last.get(last.size() - 1);
        }
    }

    @Override
    public V remove(Object key) {
        if (!map.containsKey(key)) {
            return null;
        }
        List<V> newList = new LinkedList<>(map.get(key));
        V value = newList.remove(0);
        if (newList.size() == 0) {
            map.remove(key, map.get(key));
        } else {
            map.put((K) key, newList);
        }
        return value;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        ArrayList<V> arrayList = new ArrayList<>();
        for (List<V> list : map.values()) {
            arrayList.addAll(list);
        }
        return arrayList;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        ArrayList<V> arrayList = new ArrayList<>();
        for (List<V> list : map.values()) {
            arrayList.addAll(list);
        }
        return arrayList.contains(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            for (V v : entry.getValue()) {
                sb.append(v);
                sb.append(", ");
            }
        }
        String substring = sb.substring(0, sb.length() - 2);
        return substring + "}";
    }
}