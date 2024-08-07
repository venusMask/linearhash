package org.venus.linearhash.store;

import org.venus.linearhash.Configuration;
import org.venus.linearhash.core.Context;

/**
 * Bucket
 *
 * @Author venus
 * @Date 2024/8/7
 * @Version 1.0
 */
public interface Bucket<KeyT, ValueT> {

    Configuration configuration = Configuration.getInstance();

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

    Object getBucketData();

}
