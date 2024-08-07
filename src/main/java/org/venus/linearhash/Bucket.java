package org.venus.linearhash;

import java.util.HashMap;
import java.util.Map;

/**
 * 每一个单独的存储桶
 *
 * @Author venus
 * @Date 2024/8/5
 * @Version 1.0
 */
public class Bucket<KeyT, ValueT> {

    /**
     * Bucket ID
     */
    private final Integer bucketID;

    private final Integer maxCapacity;

    private final Integer minCapacity;

    private final Map<KeyT, ValueT> data;

    public Bucket(Integer bucketID,
                  Integer maxCapacity,
                  Integer minCapacity) {
        this.bucketID = bucketID;
        this.maxCapacity = maxCapacity;
        this.minCapacity = minCapacity;
        data = new HashMap<>();
    }

    public boolean add(Map.Entry<KeyT, ValueT> entry) {
        return add(entry.getKey(), entry.getValue());
    }

    public boolean add(KeyT key, ValueT value) {
        data.put(key, value);
        // 超过最大容量则开始执行分裂操作
        return data.size() >= maxCapacity;
    }

    public ValueT get(KeyT key) {
        return data.get(key);
    }

    public boolean delete(KeyT key) {
        data.remove(key);
        return data.size() < minCapacity;
    }

    public Map<KeyT, ValueT> getData() {
        return new HashMap<>(data);
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "bucketID=" + bucketID +
                ", maxCapacity=" + maxCapacity +
                ", minCapacity=" + minCapacity +
                '}';
    }
}
