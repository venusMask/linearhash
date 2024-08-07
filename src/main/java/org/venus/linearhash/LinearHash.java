package org.venus.linearhash;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Linear Hashing!
 */
@Getter
public class LinearHash<KeyT, ValueT> {

    private static final Logger logger = LoggerFactory.getLogger(LinearHash.class);

    private final MetaData<KeyT> metaData;

    private final Integer maxCapacity;

    private final Integer minCapacity;

    private final AtomicInteger assignBucketAssigner = new AtomicInteger(0);

    private final List<Bucket<KeyT, ValueT>> buckets;

    public LinearHash(Integer maxCapacity,
                      Integer minCapacity,
                      Integer initBuckets,
                      HashFunction<KeyT> hashFunction) {
        this.maxCapacity = maxCapacity;
        this.minCapacity = minCapacity;
        this.metaData = new MetaData<>(hashFunction, MathUtil.powOf2(initBuckets));
        buckets = new ArrayList<>();
        initBuckets();
    }

    private Bucket<KeyT, ValueT> createNewBucket() {
        int bucketID = assignBucketAssigner.getAndIncrement();
        return new Bucket<>(bucketID, maxCapacity, minCapacity);
    }

    private void initBuckets() {
        for (int i = 0; i < metaData.getInitBuckets(); i++) {
            buckets.add(createNewBucket());
        }
    }

    public void add(KeyT key, ValueT value) {
        int bucketIndex = metaData.getBucketIndex(key);
        Bucket<KeyT, ValueT> bucket = buckets.get(bucketIndex);
        boolean splitFlag = bucket.add(key, value);
        // 需要进行分裂
        if(splitFlag) {
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
        Map<KeyT, ValueT> data = needSplit.getData();
        for (Map.Entry<KeyT, ValueT> entry : data.entrySet()) {
            KeyT key = entry.getKey();
            int newBucketIndex = metaData.getBucketIndex(key);
            if (newBucketIndex != splitBucketIndex) {
                needSplit.delete(key);
                buckets.get(newBucketIndex).add(entry);
            }
        }
    }

    private void addNewBucket() {
        buckets.add(createNewBucket());
    }
}
