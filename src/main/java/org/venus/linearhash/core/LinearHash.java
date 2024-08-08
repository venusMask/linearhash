package org.venus.linearhash.core;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.linearhash.store.Bucket;
import org.venus.linearhash.store.HashFunction;
import org.venus.linearhash.store.MemoryBucket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Linear Hashing!
 */
@Getter
public class LinearHash<KeyT, ValueT> {

    private static final Logger logger = LoggerFactory.getLogger(LinearHash.class);

    private final Context<KeyT, ValueT> context = Context.getInstance();

    private final Configuration configuration = context.getConfiguration();

    private final AtomicInteger assignBucketAssigner = new AtomicInteger(0);

    private final List<Bucket<KeyT, ValueT>> buckets;

    public LinearHash() {
        buckets = new ArrayList<>();
        initBuckets();
    }

    public LinearHash(HashFunction<KeyT> hashFunction) {
        this();
        context.setHashFunction(hashFunction);
    }

    private MemoryBucket<KeyT, ValueT> createNewBucket() {
        int bucketID = assignBucketAssigner.getAndIncrement();
        return new MemoryBucket<>(bucketID);
    }

    private void initBuckets() {
        for (int i = 0; i < configuration.getInitBuckets(); i++) {
            buckets.add(createNewBucket());
        }
    }

    /**
     * Get Value
     */
    public ValueT get(KeyT key) {
        int bucketIndex = context.getBucketIndex(key);
        Bucket<KeyT, ValueT> bucket = buckets.get(bucketIndex);
        return bucket.get(key);
    }

    /**
     * Remove Key
     */
    public void remove(KeyT key) {
        int bucketIndex = context.getBucketIndex(key);
        Bucket<KeyT, ValueT> bucket = buckets.get(bucketIndex);
        bucket.remove(key);
    }

    /**
     * Put (Key, Value)
     */
    public void put(KeyT key, ValueT value) {
        int bucketIndex = context.getBucketIndex(key);
        Bucket<KeyT, ValueT> bucket = buckets.get(bucketIndex);
        bucket.put(key, value);
        context.addNumKey();
        if(context.mayBeSplit()) {
            Integer splitBucketIndex = context.getSplitBucketIndex();
            Bucket<KeyT, ValueT> needSplitBucket = buckets.get(splitBucketIndex);
            Bucket<KeyT, ValueT> newBucket = needSplitBucket.splitBucket();
            buckets.add(newBucket);
        }
    }

}
