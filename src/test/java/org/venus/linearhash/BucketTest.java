package org.venus.linearhash;

import junit.framework.TestCase;
import org.venus.linearhash.store.MemoryDataBucket;

/**
 * @Author venus
 * @Date 2024/8/5
 * @Version 1.0
 */
public class BucketTest extends TestCase {

    public void testBucket() {
        MemoryDataBucket<Integer, String> bucket = new MemoryDataBucket<>(1);
        for (int i = 0; i < 10; i++) {
            boolean flag = bucket.put(i, "A" + i);
            System.out.println(flag);
        }
    }

}
