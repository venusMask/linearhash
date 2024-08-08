package org.venus.linearhash;

import junit.framework.TestCase;
import org.venus.linearhash.core.LinearHash;

import java.util.Objects;
import java.util.Random;

/**
 * @Author venus
 * @Date 2024/8/5
 * @Version 1.0
 */
public class LinearHashTest extends TestCase {

    private Random random = new Random();

    private String generateRandomString(int minLength, int maxLength) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(letters.length());
            char randomChar = letters.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public void put(LinearHash<String, String> linearHash, int times) {
        for (int i = 0; i < times; i++) {
            String key = generateRandomString(1, 10);
            linearHash.put(key, key);
        }
    }

    public void testLinearHash() {
        LinearHash<String, String> linearHash = new LinearHash<>();
        put(linearHash, 100);
        int a = 1 + 1;
    }



    public void testMath() {
        System.out.println(1 << 2);
    }

}
