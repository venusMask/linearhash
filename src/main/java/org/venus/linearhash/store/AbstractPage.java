package org.venus.linearhash.store;

import org.venus.linearhash.core.Context;

/**
 * @Author venus
 * @Date 2024/8/8
 * @Version 1.0
 */
public abstract class AbstractPage<KeyT, ValueT> implements OverflowPage<KeyT, ValueT>  {

    protected final Context<KeyT, ValueT> context = Context.getInstance();

    @Override
    public Bucket<KeyT, ValueT> splitBucket() {
        throw new UnsupportedOperationException("Overflow don't support direct split.");
    }

    @Override
    public OverflowPage<KeyT, ValueT> getPage() {
        throw new UnsupportedOperationException("Overflow Page doesn't have over flow page.");
    }

    @Override
    public Integer getBucketID() {
        throw new UnsupportedOperationException("Overflow Page doesn't have over flow page.");
    }
}
