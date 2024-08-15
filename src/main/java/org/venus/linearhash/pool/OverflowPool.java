package org.venus.linearhash.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.venus.linearhash.core.Configuration;
import org.venus.linearhash.store.OverflowPage;

public class OverflowPool<KeyT, ValueT> {

    private final Configuration configuration = Configuration.getInstance();

    private final GenericObjectPool<OverflowPage<KeyT, ValueT>> pool;

    public OverflowPool() {
        OverflowPageFactory<KeyT, ValueT> factory = new OverflowPageFactory<>();
        pool = new GenericObjectPool<>(factory);
    }

    public void release(OverflowPage<KeyT, ValueT> page) {
        pool.returnObject(page);
    }

    public OverflowPage<KeyT, ValueT> borrow() {
        try {
            OverflowPage<KeyT, ValueT> overflowPage = pool.borrowObject();
            return overflowPage;
        } catch (Exception e) {
            try {
                pool.addObject();
                return pool.borrowObject();
            } catch (Exception ie) {
                throw new RuntimeException("Pool Exception");
            }
        }
    }

    public int size() {
        return pool.getNumWaiters();
    }
}
