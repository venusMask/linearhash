package org.venus.linearhash.store;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.linearhash.core.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Store Bucket.
 * Every bucket have a overflow page.
 */
public class MemoryDataBucket<KeyT, ValueT> implements Bucket<KeyT, ValueT> {

    private static final Logger logger = LoggerFactory.getLogger(MemoryDataBucket.class);

    private final Object lock = new Object();

    private final Context<KeyT, ValueT> context = Context.getInstance();

    /**
     * Bucket ID
     */
    @Getter
    private final Integer bucketID;

    private final Map<KeyT, ValueT> bucketData;

    @Getter
    private OverflowPage<KeyT, ValueT> overflowPage;

    public MemoryDataBucket(Integer bucketID) {
        this.bucketID = bucketID;
        this.bucketData = new HashMap<>();
    }

    @Override
    public boolean put(KeyT key, ValueT value) {
        if (bucketData.size() > configuration.getMaxBucketCap()) {
            if (overflowPage == null) {
                overflowPage = context.getOverflowPool().poll();
            }
            overflowPage.put(key, value);
        } else {
            bucketData.put(key, value);
        }
        return true;
    }

    @Override
    public boolean remove(KeyT key) {
        bucketData.remove(key);
        return bucketData.size() < configuration.getMinBucketCap();
    }

    @Override
    public ValueT get(KeyT key) {
        return bucketData.get(key);
    }

    @Override
    public void clearAll() {
        bucketData.clear();
        context.getOverflowPool().release(overflowPage);
        overflowPage = null;
    }

    public Map<KeyT, ValueT> getBucketData() {
        return new HashMap<>(bucketData);
    }
}
