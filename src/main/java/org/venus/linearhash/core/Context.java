package org.venus.linearhash.core;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.linearhash.store.HashFunction;
import org.venus.linearhash.store.OverflowPool;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Linear Hash Context
 *
 * @param <KeyT>
 */
@Getter
public class Context<KeyT, ValueT> {

    private final static Logger logger = LoggerFactory.getLogger(Context.class);

    private static final Context<?, ?> instance = new Context<>();

    @SuppressWarnings("unchecked")
    public static <KeyT, ValueT> Context<KeyT, ValueT> getInstance() {
        return (Context<KeyT, ValueT>) instance;
    }

    private final Configuration configuration = Configuration.getInstance();

    private final AtomicInteger bucketIDAssigner = new AtomicInteger(0);

    /**
     *
     */
    private final OverflowPool<KeyT, ValueT> overflowPool = new OverflowPool<>();

    // 记录数量
    private final AtomicLong numKeys = new AtomicLong(0L);

    private final AtomicLong bucketNum = new AtomicLong(0L);

    @Setter
    private HashFunction<KeyT> hashFunction;

    private Integer roundTimes;

    private Integer initBuckets;

    private Integer splitBucketIndex = 0;

    private Context() {
        this.initBuckets = configuration.getInitBuckets();
        bucketNum.set(initBuckets);
        this.roundTimes = 0;
    }

    public void init(HashFunction<KeyT> hashFunction) {
        this.hashFunction = hashFunction;
    }

    private int calBucketIndex(int hashCode, int levelNumber) {
        return hashCode & ((1 << levelNumber) * initBuckets - 1);
    }

    public int getBucketIndex(KeyT key) {
        int hashCode = hashFunction.hash(key);
        int bucketIndex = calBucketIndex(hashCode, roundTimes);
        if(bucketIndex < splitBucketIndex) {
            int newBucketIndex = calBucketIndex(hashCode, roundTimes + 1);
            logger.info("输入Key的hashCode: {}, 初次计算bucketIndex: {}, 分裂指针: {}, 新bucketIndex: {}.",
                    hashCode, bucketIndex, splitBucketIndex, newBucketIndex);
            return newBucketIndex;
        } else {
            logger.info("输入Key的hashCode: {}, 计算bucketIndex: {}, 分裂指针: {}.",
                    hashCode, bucketIndex, splitBucketIndex);
            return bucketIndex;
        }
    }

    public void split() {
        bucketNum.getAndIncrement();
        splitBucketIndex++;
        if(initBuckets == 1) {
            splitWithOne();
        } else {
            splitWithNon();
        }
    }

    private void roundIncrement() {
        logger.info(
                "分裂轮数增加, 开始新一轮分裂. 上一轮轮数: {}, 分裂指针: {}. 新一轮轮数: {}, 分裂指针: 0",
                roundTimes, splitBucketIndex, roundTimes + 1);
        roundTimes++;
        splitBucketIndex = 0;
    }

    private void splitWithOne() {
        logger.debug("初始化桶数为1, 使用splitWithOne计算.");
        if(splitBucketIndex == (1 << roundTimes)) {
            roundIncrement();
        }
    }

    private void splitWithNon() {
        logger.debug("初始化桶数不为1, 使用splitWithNon计算.");
        int newRoundTimes = (int) Math.floor(Math.log((double) bucketNum.get() / initBuckets) / Math.log(2));
        if(newRoundTimes > roundTimes) {
            roundIncrement();
        }
    }

    public void addNumKey() {
        numKeys.getAndIncrement();
    }

    /**
     * Controlled Split.
     * 当元素的总数达到总容量的阈值时才进行分裂.
     */
    public boolean mayBeSplit() {
        long maxCap = bucketNum.get() * configuration.getMaxBucketCap();
        return (numKeys.get() * 1.0 / maxCap) > configuration.getLoadFactory();
    }

}
