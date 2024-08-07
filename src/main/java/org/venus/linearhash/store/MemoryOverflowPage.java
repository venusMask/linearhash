package org.venus.linearhash.store;

import java.util.HashMap;
import java.util.Map;

/**
 * Overflow bucket.
 * When bucket capacity more than max capacity(global configuration).
 *
 * @Author venus
 * @Date 2024/8/7
 * @Version 1.0
 */
public class MemoryOverflowPage<KeyT, ValueT> implements OverflowPage<KeyT, ValueT> {

    private final Map<KeyT, ValueT> overFlowData;

    public MemoryOverflowPage(){
        overFlowData = new HashMap<>();
    }

    @Override
    public boolean put(KeyT key, ValueT value) {
        overFlowData.put(key, value);
        return true;
    }

    @Override
    public boolean remove(KeyT key) {
        overFlowData.remove(key);
        return true;
    }

    @Override
    public ValueT get(KeyT key) {
        return overFlowData.get(key);
    }

    @Override
    public void clearAll() {
        overFlowData.clear();
    }

    @Override
    public Object getBucketData() {
        return null;
    }

}
