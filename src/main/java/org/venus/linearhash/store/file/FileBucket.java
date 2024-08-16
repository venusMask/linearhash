package org.venus.linearhash.store.file;

import org.venus.linearhash.core.Configuration;
import org.venus.linearhash.store.AbstractBucket;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileBucket extends AbstractBucket<byte[], byte[]> {

    private final Configuration configuration;
    private final FileChannel fileChannel;
    private final Map<Long, Long> deletedSlots = new HashMap<>(); // 用于跟踪已删除的slot位置

    public FileBucket(Integer bucketID) {
        configuration = context.getConfiguration();
        String dataDir = configuration.getDataDir();
        String newBucketPath = dataDir + "/" + bucketID + ".bucket";
        try {
            Files.createFile(Paths.get(newBucketPath));
            this.fileChannel = new FileOutputStream(newBucketPath).getChannel();
        } catch (IOException e) {
            throw new RuntimeException("Create file error", e);
        }
    }

    @Override
    public boolean put(byte[] key, byte[] value) {
        Slot slot = new Slot(key, value);
        ByteBuffer slotBuffer = slot.getSlotBuffer();
        slotBuffer.flip();
        try {
            fileChannel.write(slotBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean remove(byte[] key) {
        long position = findPosition(key);
        if (position == -1) {
            return false;
        }
        deletedSlots.put(position, fileChannel.size()); // 标记为删除
        return true;
    }

    @Override
    public byte[] get(byte[] key) {
        long position = findPosition(key);
        if (position == -1) {
            return null;
        }
        Slot slot = readSlot(position);
        if (slot != null) {
            return slot.getValue();
        }
        return null;
    }

    @Override
    public void clearAll() {
        try {
            fileChannel.truncate(0);
            fileChannel.force(true);
            deletedSlots.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<Map.Entry<byte[], byte[]>> getBucketData() {
        return readAllEntries().entrySet().iterator();
    }

    @Override
    public long size() {
        return readAllEntries().size();
    }

    // 查找键对应的位置
    private long findPosition(byte[] key) {
        try (FileChannel channel = FileChannel.open(Paths.get(fileChannel.map().address()))) {
            long position = 0;
            while (position < channel.size()) {
                Slot slot = readSlot(position);
                if (slot != null && Arrays.equals(slot.getKey(), key)) {
                    if (!deletedSlots.containsKey(position)) {
                        return position;
                    }
                }
                position += slot.getSlotSize();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    // 读取指定位置的Slot
    private Slot readSlot(long position) {
        try (FileChannel channel = FileChannel.open(Paths.get(fileChannel.map().address()))) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(Slot.getSize(position));
            channel.read(byteBuffer, position);
            byteBuffer.flip();
            int slotSize = byteBuffer.get() & 0xFF;
            int keySize = byteBuffer.get() & 0xFF;
            int valueSize = byteBuffer.get() & 0xFF;
            byte[] key = new byte[keySize];
            byte[] value = new byte[valueSize];
            byteBuffer.get(key);
            byteBuffer.get(value);
            return new Slot(key, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 读取文件中的所有条目
    private Map<byte[], byte[]> readAllEntries() {
        Map<byte[], byte[]> entries = new HashMap<>();
        try (FileChannel channel = FileChannel.open(Paths.get(fileChannel.map().address()))) {
            long position = 0;
            while (position < channel.size()) {
                Slot slot = readSlot(position);
                if (slot != null && !deletedSlots.containsKey(position)) {
                    entries.put(slot.getKey(), slot.getValue());
                }
                position += slot.getSlotSize();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return entries;
    }

    // 获取Slot的总大小
    private static int getSize(long position) {
        try (FileChannel channel = FileChannel.open(Paths.get(position))) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1);
            channel.read(byteBuffer, position);
            byteBuffer.flip();
            return byteBuffer.get() & 0xFF;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
