package org.venus.linearhash.store;

import lombok.Getter;
import org.venus.linearhash.Configuration;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 *
 * @Author venus
 * @Date 2024/8/7
 * @Version 1.0
 */
public class OverflowPool<KeyT, ValueT> {

    private final Configuration configuration = Configuration.getInstance();

    private final Deque<OverflowPage<KeyT, ValueT>> pool = new ArrayDeque<>();

    public OverflowPool() {

    }

    private void initPool() {
        for (int i = 0; i < configuration.getOverflowPoolSize(); i++) {
            OverflowPage<KeyT, ValueT> overflowPage = new MemoryOverflowPage<>();
            pool.offer(overflowPage);
        }
    }

    public void release(OverflowPage<KeyT, ValueT> page) {
        pool.addLast(page);
    }

    public OverflowPage<KeyT, ValueT> poll() {
        if(!pool.isEmpty()) {
            OverflowPage<KeyT, ValueT> overflowPage = new MemoryOverflowPage<>();
            pool.offer(overflowPage);
        }
        return pool.poll();
    }

}
