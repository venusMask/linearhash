package org.venus.linearhash.store;

import org.venus.linearhash.core.Configuration;

import java.util.ArrayDeque;
import java.util.Deque;

public class OverflowPool<KeyT, ValueT> {

    private final Configuration configuration = Configuration.getInstance();

    private final Deque<OverflowPage<KeyT, ValueT>> pool = new ArrayDeque<>();

    public OverflowPool() {

    }

    private void initPool() {
        for (int i = 0; i < configuration.getOverflowPoolSize(); i++) {
            OverflowPage<KeyT, ValueT> overflowPage = new MemoryPage<>();
            pool.offer(overflowPage);
        }
    }

    public void release(OverflowPage<KeyT, ValueT> page) {
        pool.addLast(page);
    }

    public OverflowPage<KeyT, ValueT> poll() {
        if(!pool.isEmpty()) {
            OverflowPage<KeyT, ValueT> overflowPage = new MemoryPage<>();
            pool.offer(overflowPage);
        }
        return pool.poll();
    }

}
