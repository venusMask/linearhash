package org.venus.linearhash.store;

import java.util.Iterator;
import java.util.Map;

/**
 * Bucket
 *
 * @Author venus
 * @Date 2024/8/7
 * @Version 1.0
 */
public interface Bucket<KeyT, ValueT> {

    /**
     * Add Pair
     */
    boolean put(KeyT key, ValueT value);

    /**
     * Remove Key
     */
    boolean remove(KeyT key);

    /**
     * Get Value.
     */
    ValueT get(KeyT key);

    /**
     * Clear bucket data.
     */
    void clearAll();

    Iterator<Map.Entry<KeyT, ValueT>> getBucketData();

    Bucket<KeyT, ValueT> splitBucket();

    OverflowPage<KeyT, ValueT> getPage();

    Integer getBucketID();

    long size();

    String metric();

}
