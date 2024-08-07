package org.venus.linearhash;

import junit.framework.TestCase;

/**
 * @Author venus
 * @Date 2024/8/5
 * @Version 1.0
 */
public class BucketTest extends TestCase {

    public void testBucket() {
        Bucket<Integer, String> bucket = new Bucket<>(1, 8, 2);
        for (int i = 0; i < 10; i++) {
            boolean flag = bucket.add(i, "A" + i);
            System.out.println(flag);
        }
    }

}
