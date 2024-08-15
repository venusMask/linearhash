package org.venus.linearhash.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.venus.linearhash.store.MemoryPage;
import org.venus.linearhash.store.OverflowPage;

/**
 * Create overflow page factory.
 *
 * @Author venus
 * @Date 2024/8/15
 * @Version 1.0
 */
public class OverflowPageFactory<KeyT, ValueT>
        implements PooledObjectFactory<OverflowPage<KeyT, ValueT>> {


    @Override
    public void activateObject(PooledObject<OverflowPage<KeyT, ValueT>> p) throws Exception {

    }

    @Override
    public void destroyObject(PooledObject<OverflowPage<KeyT, ValueT>> p) throws Exception {

    }

    @Override
    public PooledObject<OverflowPage<KeyT, ValueT>> makeObject() throws Exception {
        MemoryPage<KeyT, ValueT> page = new MemoryPage<>();
        return new DefaultPooledObject<>(page);
    }

    @Override
    public void passivateObject(PooledObject<OverflowPage<KeyT, ValueT>> p) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<OverflowPage<KeyT, ValueT>> p) {
        return false;
    }

}
