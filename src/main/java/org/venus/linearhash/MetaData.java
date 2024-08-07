package org.venus.linearhash;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author venus
 * @Date 2024/8/6
 * @Version 1.0
 */
@Getter
public class MetaData<KeyT> {

    private final static Logger logger = LoggerFactory.getLogger(MetaData.class);

    private final HashFunction<KeyT> hashFunction;

    private Integer roundTimes;

    private final Integer initBuckets;

    private Integer bucketNumber;

    private Integer splitBucketIndex = 0;

    public MetaData(HashFunction<KeyT> hashFunction, Integer initBuckets) {
        this.hashFunction = hashFunction;
        this.bucketNumber = initBuckets;
        this.initBuckets = initBuckets;
        this.roundTimes = 0;
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
        bucketNumber++;
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
        int newRoundTimes = (int) Math.floor(Math.log((double) bucketNumber / initBuckets) / Math.log(2));
        if(newRoundTimes > roundTimes) {
            roundIncrement();
        }
    }

}
