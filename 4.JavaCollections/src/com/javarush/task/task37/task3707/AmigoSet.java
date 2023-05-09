package com.javarush.task.task37.task3707;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class AmigoSet<E> extends AbstractSet<E> implements Serializable, Cloneable, Set<E> {

    private static final Object PRESENT = new Object();

    private transient HashMap<E, Object> map;

    public AmigoSet() {
        this.map = new HashMap<>();
    }

    public AmigoSet(Collection<? extends E> collection) {
        int capacity = Math.max(16, (int) (Math.ceil(collection.size()/.75f)));
        this.map = new HashMap<>(capacity);
        addAll(collection);

    }

    @Override
    public boolean add(E e) {

        return this.map.put(e, PRESENT) == null;
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Object clone() {
        try {
            AmigoSet<E> amigoSet = (AmigoSet<E>) super.clone();
            amigoSet.map = (HashMap<E, Object>) map.clone();
            return amigoSet;
        } catch (Exception e) {
            throw new InternalError();
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(HashMapReflectionHelper.<Integer>callHiddenMethod(this.map, "capacity"));
        s.writeObject(HashMapReflectionHelper.<Float>callHiddenMethod(this.map, "loadFactor"));
        s.writeObject(map.size());
        for (E e : map.keySet()) {
            s.writeObject(e);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int capasity = (int) s.readObject();
        float loadFactor = (float) s.readObject();
        this.map = new HashMap<>(capasity, loadFactor);
        int size = (int) s.readObject();
        for (int i = 0; i < size; i++) {
            this.map.put((E) s.readObject(), PRESENT);
        }

    }
}
