package org.venus.linearhash.util;

/**
 * @Author venus
 * @Date 2024/8/7
 * @Version 1.0
 */
public class MathUtil {

    public static int powOf2(int number) {
        while ((number & (number - 1)) != 0) {
            number--;
        }
        return number;
    }

}
