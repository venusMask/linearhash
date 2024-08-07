package org.venus.linearhash;

import junit.framework.TestCase;

import java.util.Objects;
import java.util.Random;

/**
 * @Author venus
 * @Date 2024/8/6
 * @Version 1.0
 */
public class MetaDataTest extends TestCase {

    public Random random = new Random();

    public void testMetaData() {
        HashFunction<Integer> hf = Object::hashCode;
        MetaData<Integer> metaData = new MetaData<>(hf, 4);
        for (int i = 0; i < 100; i++) {
            int nextInt = random.nextInt();
            int hashed = hf.hash(nextInt);
            // int bucketIndex = metaData.h(hashed, 0);
            // assertTrue(bucketIndex < 4);
        }
        for (int i = 0; i < 100; i++) {
            int nextInt = random.nextInt();
            int hashed = hf.hash(nextInt);
            // int bucketIndex = metaData.h(hashed, 2);
            // assertTrue(bucketIndex < 8);
        }
    }

    public void testMath() {
        System.out.println(1 << 0);
    }

}
