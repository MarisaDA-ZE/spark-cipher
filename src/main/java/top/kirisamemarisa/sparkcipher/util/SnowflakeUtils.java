package top.kirisamemarisa.sparkcipher.util;


import java.util.*;

/**
 * @Author Marisa
 * @Description 雪花ID工具类
 * @Date 2023/11/27
 */
public class SnowflakeUtils {
    // 开始时间戳（2023-01-01）
    private final static long twepoch = 1672502400000L;

    // 机器id所占的位数
    private final static long workerIdBits = 5L;

    // 数据标识id所占的位数
    private final static long datacenterIdBits = 5L;

    // 支持的最大机器id，结果是31（这个移位算法可以很快地计算出几位二进制数所能表示的最大十进制数）
    private final static long maxWorkerId = ~(-1L << workerIdBits);

    // 支持的最大数据标识id，结果是31
    private final static long maxDatacenterId = ~(-1L << datacenterIdBits);

    // 序列在id中占的位数
    private final static long sequenceBits = 12L;

    // 机器ID向左移12位
    private final static long workerIdShift = sequenceBits;

    // 数据标识id向左移17位（12+5）
    private final static long datacenterIdShift = sequenceBits + workerIdBits;

    // 时间截向左移22位（5+5+12）
    private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    // 生成序列的掩码，这里为4095（二进制的111111111111=0xfff=4095）
    private final static long sequenceMask = ~(-1L << sequenceBits);

    private long workerId;
    private long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private static SnowflakeUtils instance = null;

    public void SnowflakeIdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("工作ID不能大于 %d 或小于0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter ID不能大于 %d 或小于0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long next() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("时间后退,拒绝生成ID %d毫秒", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    public static String nextId() {
        if (instance == null) {
            instance = new SnowflakeUtils();
        }
        return String.valueOf(instance.next());
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {

        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            String s = SnowflakeUtils.nextId();
            ids.add(s);
            int size = ids.size();
            if (size - 1 != i) {
                System.out.println("存在重复！");
                break;
            }
            System.out.println((i + 1) + ": " + s);
        }
    }

}
