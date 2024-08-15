package org.venus.linearhash.core;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @Author venus
 * @Date 2024/8/15
 * @Version 1.0
 */
public class CombineIterator<KeyT, ValueT>
        implements Iterator<Map.Entry<KeyT, ValueT>> {

    private final Iterator<Map.Entry<KeyT, ValueT>> first;
    private final Iterator<Map.Entry<KeyT, ValueT>> second;

    // Tracks which iterator provided the last element.
    private Iterator<Map.Entry<KeyT, ValueT>> current;

    public CombineIterator(Iterator<Map.Entry<KeyT, ValueT>> first,
                           Iterator<Map.Entry<KeyT, ValueT>> second) {
        this.first = first;
        this.second = second;
        this.current = null; // Initialize to null since no next() has been called yet.
    }

    @Override
    public boolean hasNext() {
        return (first != null && first.hasNext()) || (second != null && second.hasNext());
    }

    @Override
    public Map.Entry<KeyT, ValueT> next() {
        if (first != null && first.hasNext()) {
            current = first;
            return first.next();
        }
        if (second != null && second.hasNext()) {
            current = second;
            return second.next();
        }
        throw new NoSuchElementException("No more elements.");
    }

    @Override
    public void remove() {
        if (current == null) {
            throw new IllegalStateException("No elements have been returned yet.");
        }
        current.remove();
        current = null; // Reset after removal.
    }
}
