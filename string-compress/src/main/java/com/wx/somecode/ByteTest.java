package com.wx.somecode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 * @author xinquan.huangxq
 */
public class ByteTest {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String issueIds = "10068111,10068112,10068113,10068114,10068115,10068116,"
                + "10068117,10068118,10068119,10068120,10068121,10068122,10068123,"
                + "10068124,10068125,10068126,10068127,10068128,10068129,10068130,"
                + "10068131,10068132,10068133,10068134,10068135,10068136,10068137,"
                + "10068138,10068139,10068140,10068141,10068142,10068143,10068144,"
                + "10068145,10068146,10068147,10068148,10068149,10068150,10068151";
        System.err.println("彩期数:" + (issueIds.split(",")).length);
        System.err.println("源字符串长度:" + issueIds.length());

        String comp = compress(issueIds);
        System.err.println("压缩后字符串长度:" + comp.length());

        String uncomp = uncompress(comp);
        System.err.println("解压后字符串长度:" + uncomp.length() );

        System.err.println("解压是否丢失：" + !issueIds.equals(uncomp));
    }

    // 压缩
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    // 解压缩
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(
                str.getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString();
    }
    public static byte[] encode(String srcString) throws UnsupportedEncodingException {
        byte[] input = srcString.getBytes("UTF-8");
        byte[] output = new byte[input.length * 2];
        Deflater compresser = new Deflater();
        try {
            compresser.setInput(input);
            compresser.finish();
            int compressedDataLength = compresser.deflate(output);
            byte[] contentByte = new byte[compressedDataLength];
            System.arraycopy(output, 0, contentByte, 0, compressedDataLength);
            return contentByte;
        } finally {
            compresser.end();
        }
    }

    public static String decode(byte[] encodeByte) {
        String outputString = "";
        try {
            boolean flag = true;
            int resultLength = 0;
            byte[] result = null;
            int i = 1;

            while (flag) {
                Inflater decompresser = new Inflater();
                try {
                    result = new byte[encodeByte.length * 20 * i];
                    decompresser.setInput(encodeByte, 0, encodeByte.length);
                    resultLength = decompresser.inflate(result);
                    if (decompresser.getRemaining() == 0) {
                        flag = false;
                    }
                } finally {
                    decompresser.end();
                }
                i++;
            }

            outputString = new String(result, 0, resultLength, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputString;
    }
}
