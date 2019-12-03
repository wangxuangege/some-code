package com.wx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

public class MappedFileReadingTest {

    public static void main(String[] args) throws IOException {
        if (args == null || args.length <= 1) {
            throw new IllegalArgumentException("接受2个参数，第一个参数为类型(CONSUME、INDEX、COMMIT），第二个参数为文件路径");
        }
        String type = args[0];
        switch (type) {
            case "CONSUME":
                readConsumeQueue(args[1]);
                break;
            case "INDEX":
                readIndexFile(args[1]);
                break;
            case "COMMIT":
                readCommitLog(args[1]);
                break;
            default:
                throw new IllegalArgumentException("没有定义的类型");
        }
    }

    private static void readConsumeQueue(String path) throws IOException {
        MappedByteBuffer mappedByteBuffer = init(path, 0, 1000 * 1000 * 6);
        do {
            long offset = mappedByteBuffer.getLong();
            if (offset == 0) {
                break;
            }
            int size = mappedByteBuffer.getInt();
            long code = mappedByteBuffer.getLong();
            System.out.println(String.format("CommitLog Offset:[%d],TotalSize:[%d],TagsCode:[%d]", offset, size, code));
        } while (true);
    }

    private static void readIndexFile(String path) throws IOException {
        MappedByteBuffer mappedByteBuffer = init(path, 0, 40 + 5000000 * 4 + 5000000 * 4 * 20);
        readIndexFileHeader(mappedByteBuffer);
        mappedByteBuffer.position(40 + 5000000 * 4);
        for (int i = 0; i < 100; i++) {
            System.out.println(String.format("keyHash:[%d],phyOffset:[%d],timeDiff:[%d],slotValue:[%d]", mappedByteBuffer.getInt(), mappedByteBuffer.getInt(),
                    mappedByteBuffer.getLong(), mappedByteBuffer.getInt()));
        }
    }

    private static void readCommitLog(String path) throws IOException {
        MappedByteBuffer mappedByteBuffer = init(path, 6078, 1024 * 1024 * 1024);
        readCommitLog(mappedByteBuffer);
    }

    private static void readCommitLog(MappedByteBuffer mappedByteBuffer) {
        int size = mappedByteBuffer.getInt();
        System.out.println(String.format("Message TotalSize:[%d]", size));
        int magicCode = mappedByteBuffer.getInt();
        System.out.println(String.format("Message MagicCode:[%d]", magicCode));
        int bodyRPC = mappedByteBuffer.getInt();
        System.out.println(String.format("Message BodyRPC:[%d]", bodyRPC));
        int queueId = mappedByteBuffer.getInt();
        System.out.println(String.format("Message QueueId:[%d]", queueId));
        int flag = mappedByteBuffer.getInt();
        System.out.println(String.format("Message Flag:[%d]", flag));
        long queueOffset = mappedByteBuffer.getLong();
        System.out.println(String.format("Message ConsumeQueue Offset:[%d]", queueOffset));
        long physicalOffset = mappedByteBuffer.getLong();
        System.out.println(String.format("Message CommitLog Offset:[%d]", physicalOffset));
        int sysFlag = mappedByteBuffer.getInt();
        System.out.println(String.format("Message SysFlag:[%d]", sysFlag));
        long bornTimestamp = mappedByteBuffer.getLong();
        System.out.println(String.format("Message BornTimestamp:[%s]", new Date(bornTimestamp).toString()));
        long bornHost = mappedByteBuffer.getLong();
        System.out.println(String.format("Message BornHost:[%d]", bornHost));
        long storeTimestamp = mappedByteBuffer.getLong();
        System.out.println(String.format("Message BornTimestamp:[%s]", new Date(storeTimestamp).toString()));
        long storeHost = mappedByteBuffer.getLong();
        System.out.println(String.format("Message BornHost:[%d]", storeHost));
        int reconsumeTimes = mappedByteBuffer.getInt();
        System.out.println(String.format("Message ReconsumeTimes:[%d]", reconsumeTimes));
        long prepareTransactionOffset = mappedByteBuffer.getLong();
        System.out.println(String.format("Message PrepareTransactionOffset:[%d]", prepareTransactionOffset));
        int bodyLength = mappedByteBuffer.getInt();
        System.out.println(String.format("Message BodyLength:[%d]", bodyLength));
        byte[] body = new byte[bodyLength];
        mappedByteBuffer.get(body);
        System.out.println(String.format("Message Body:[%s]", new String(body)));
        int topicLentgh = mappedByteBuffer.get();
        System.out.println(String.format("Message TopicLentgh:[%d]", topicLentgh));
        byte[] topic = new byte[topicLentgh];
        mappedByteBuffer.get(topic);
        System.out.println(String.format("Message Topic:[%s]", new String(topic)));
        int propertiesLength = mappedByteBuffer.getShort();
        System.out.println(String.format("Message PropertiesLength:[%d]", propertiesLength));
        byte[] properties = new byte[propertiesLength];
        mappedByteBuffer.get(properties);
        System.out.println(String.format("Message Properties:[%s]", new String(properties)));
    }

    private static void readIndexFileHeader(MappedByteBuffer mappedByteBuffer) {
        long beginTimestamp = mappedByteBuffer.getLong();
        System.out.println(String.format("IndexFile Header BeginTimestamp:[%s]", new Date(beginTimestamp)));
        long endTimestamp = mappedByteBuffer.getLong();
        System.out.println(String.format("IndexFile Header EndTimestamp:[%s]", new Date(endTimestamp).toString()));
        long beginPhyOffset = mappedByteBuffer.getLong();
        System.out.println(String.format("IndexFile Header BeginPhyOffset:[%d]", beginPhyOffset));
        long endPhyOffset = mappedByteBuffer.getLong();
        System.out.println(String.format("IndexFile Header EndPhyOffset:[%d]", endPhyOffset));
        int hashSlotCount = mappedByteBuffer.getInt();
        System.out.println(String.format("IndexFile Header HashSlotCount:[%d]", hashSlotCount));
    }

    private static MappedByteBuffer init(String filePath, int position, long size) throws IOException {
        FileChannel channel = new RandomAccessFile(new File(filePath), "r").getChannel();
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
        mappedByteBuffer.position(position);
        return mappedByteBuffer;
    }
}
