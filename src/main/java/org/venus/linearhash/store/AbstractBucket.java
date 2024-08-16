package org.venus.linearhash.store;

import org.venus.linearhash.core.Context;

import java.util.Iterator;
import java.util.Map;

/**
 * @Author venus
 * @Date 2024/8/8
 * @Version 1.0
 */
public abstract class AbstractBucket<KeyT, ValueT> implements Bucket<KeyT, ValueT> {

    protected final Context<KeyT, ValueT> context = Context.getInstance();

    protected OverflowPage<KeyT, ValueT> overflowPage = null;

    protected final Integer maxCap;

    protected final Integer minCap;

    protected Integer bucketID = null;

    public AbstractBucket() {
        this.maxCap = context.getConfiguration().getMaxBucketCap();
        this.minCap = context.getConfiguration().getMinBucketCap();
    }

    @Override
    public Bucket<KeyT, ValueT> splitBucket() {
        Integer splitBucketIndex = context.getSplitBucketIndex();
        context.split();
        int bucketID = context.getBucketIDAssigner().getAndIncrement();
        MemoryBucket<KeyT, ValueT> newBucket = new MemoryBucket<>(bucketID);
        Iterator<Map.Entry<KeyT, ValueT>> iterator = getBucketData();
        while (iterator.hasNext()) {
            Map.Entry<KeyT, ValueT> entry = iterator.next();
            KeyT key = entry.getKey();
            int newBucketIndex = context.getBucketIndex(key);
            if (newBucketIndex != splitBucketIndex) {
                iterator.remove();
                newBucket.put(key, entry.getValue());
            }
        }
        // 归还对象
        if(overflowPage != null) {
            context.getOverflowPool().release(overflowPage);
            overflowPage = null;
        }
        return newBucket;
    }

    @Override
    public OverflowPage<KeyT, ValueT> getPage() {
        if (overflowPage == null) {
            overflowPage = context.getOverflowPool().borrow();
        }
        return overflowPage;
    }

    @Override
    public Integer getBucketID() {
        if (bucketID == null) {
            bucketID = context.getBucketIDAssigner().getAndIncrement();
        }
        return bucketID;
    }

    @Override
    public String metric() {
        return String.format("Bucket id: %d, data size: %d, overflow page size: %d",
                getBucketID(), size(), overflowPage == null ? 0 : getPage().size());
    }
}
