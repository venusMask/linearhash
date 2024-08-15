package org.venus.linearhash.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.linearhash.core.CombineIterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MemoryBucket<KeyT, ValueT> extends AbstractBucket<KeyT, ValueT> {

    private static final Logger logger = LoggerFactory.getLogger(MemoryBucket.class);

    private final Map<KeyT, ValueT> bucketData;

    public MemoryBucket(Integer bucketID) {
        super();
        this.bucketID = bucketID;
        this.bucketData = new HashMap<>();
    }

    @Override
    public boolean put(KeyT key, ValueT value) {
        if (bucketData.size() > maxCap) {
            getPage().put(key, value);
        } else {
            bucketData.put(key, value);
        }
        return true;
    }

    @Override
    public boolean remove(KeyT key) {
        bucketData.remove(key);
        return bucketData.size() < minCap;
    }

    @Override
    public ValueT get(KeyT key) {
        return bucketData.get(key);
    }

    @Override
    public void clearAll() {
        bucketData.clear();
        context.getOverflowPool().release(getPage());
        overflowPage = null;
    }

    @Override
    public Iterator<Map.Entry<KeyT, ValueT>> getBucketData() {
        Iterator<Map.Entry<KeyT, ValueT>> first = bucketData.entrySet().iterator();
        Iterator<Map.Entry<KeyT, ValueT>> second = null;
        if(getPage() != null) {
            second = getPage().getBucketData();
        }
        return new CombineIterator<>(first, second);
    }

    @Override
    public long size() {
        return bucketData.size();
    }
}
