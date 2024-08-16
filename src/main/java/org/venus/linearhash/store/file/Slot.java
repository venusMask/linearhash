package org.venus.linearhash.store.file;

import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * unit of bucket.
 * 四部分组成:
 * 1: 该Slot的总长度. (4)
 * 2: key的总长度
 * 2: key值. (4)
 * 3: value部分的总长度. (4)
 * 4: byte[] (byte.length)
 *
 * @Author venus
 * @Date 2024/8/16
 * @Version 1.0
 */
public class Slot {

    @Getter
    private final byte[] key;

    @Getter
    private final byte[] value;

    public Slot(byte[] key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return  key size
     */
    public int getKeySize() { return key.length; }

    /**
     * @return  value size
     */
    public int getValueSize() { return value.length; }

    /**
     * slot size
     */
    public int getSlotSize() {
        return 4 + 4 + key.length + 4 + value.length;
    }

    public ByteBuffer getSlotBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(getSlotSize());
        byteBuffer.put((byte) getSlotSize());
        byteBuffer.put((byte) getKeySize());
        byteBuffer.put((byte) getValueSize());
        byteBuffer.put(value);
        return byteBuffer;
    }
}
