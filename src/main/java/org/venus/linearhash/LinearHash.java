package org.venus.linearhash;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.linearhash.store.Bucket;
import org.venus.linearhash.store.MemoryDataBucket;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Linear Hashing!
 */
@Getter
public class LinearHash<KeyT, ValueT> {

    // private static final Logger logger = LoggerFactory.getLogger(LinearHash.class);

    private final MetaData<KeyT> metaData;

    private final AtomicInteger assignBucketAssigner = new AtomicInteger(0);

    private final List<Bucket<KeyT, ValueT>> buckets;

    public LinearHash(Integer initBuckets, HashFunction<KeyT> hashFunction) {
        this.metaData = new MetaData<>(hashFunction, MathUtil.powOf2(initBuckets));
        buckets = new ArrayList<>();
        initBuckets();
    }

    private MemoryDataBucket<KeyT, ValueT> createNewBucket() {
        int bucketID = assignBucketAssigner.getAndIncrement();
        return new MemoryDataBucket<>(bucketID);
    }

    private void initBuckets() {
        for (int i = 0; i < metaData.getInitBuckets(); i++) {
            buckets.add(createNewBucket());
        }
    }

    /**
     * Get Value
     */
    public ValueT get(KeyT key) {
        int bucketIndex = metaData.getBucketIndex(key);
        Bucket<KeyT, ValueT> bucket = buckets.get(bucketIndex);
        return bucket.get(key);
    }

    /**
     * Remove Key
     */
    public void remove(KeyT key) {
        int bucketIndex = metaData.getBucketIndex(key);
        Bucket<KeyT, ValueT> bucket = buckets.get(bucketIndex);
        bucket.remove(key);
    }

    /**
     * Put (Key, Value)
     */
    public void put(KeyT key, ValueT value) {
        int bucketIndex = metaData.getBucketIndex(key);
        Bucket<KeyT, ValueT> bucket = buckets.get(bucketIndex);
        bucket.put(key, value);
        metaData.addNumKey();
        if(metaData.mayBeSplit()) {
            addNewBucket();
            split();
        }
    }

    /**
     * 分裂一个桶
     */
    private void split() {
        Integer splitBucketIndex = metaData.getSplitBucketIndex();
        Bucket<KeyT, ValueT> needSplit = buckets.get(metaData.getSplitBucketIndex());
        metaData.split();
        Map<KeyT, ValueT> data = (Map<KeyT, ValueT>) needSplit.getBucketData();
        for (Map.Entry<KeyT, ValueT> entry : data.entrySet()) {
            KeyT key = entry.getKey();
            int newBucketIndex = metaData.getBucketIndex(key);
            if (newBucketIndex != splitBucketIndex) {
                needSplit.remove(key);
                buckets.get(newBucketIndex).put(key, entry.getValue());
            }
        }
    }

    private void addNewBucket() {
        buckets.add(createNewBucket());
    }
}
