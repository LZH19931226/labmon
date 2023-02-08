package com.hc.my.common.core.util;


import org.apache.commons.lang3.StringUtils;

public class HexUtils {
    /**
     * 高效写法 16进制字符串转成byte数组
     *
     * @param hex 16进制字符串，支持大小写
     * @return byte数组
     */
    public static byte[] hexStringToBytes(String hex) {
        byte[] result = new byte[hex.length() / 2];
        char[] chars = hex.toCharArray();
        for (int i = 0, j = 0; i < result.length; i++) {
            result[i] = (byte) (toByte(chars[j++]) << 4 | toByte(chars[j++]));
        }
        return result;
    }

    private static int toByte(char c) {
        if (c >= '0' && c <= '9') return (c - '0');
        if (c >= 'A' && c <= 'F') return (c - 'A' + 0x0A);
        if (c >= 'a' && c <= 'f') return (c - 'a' + 0x0a);
        throw new RuntimeException("invalid hex char '" + c + "'");
    }

    public static String getHexCurrentTimeUnix4byte() {
        String six = Long.toHexString(System.currentTimeMillis()/1000);
        StringBuilder zero = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (six.length() < 8) {
                zero.append("0");
            }
        }
        return zero + six;
    }

    /**
     * 高效写法 byte数组转成16进制字符串
     *
     * @param bytes byte数组
     * @return 16进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int c = 0;
        for (byte b : bytes) {
            buf[c++] = digits[(b >> 4) & 0x0F];
            buf[c++] = digits[b & 0x0F];
        }
        return new String(buf);
    }

    private final static char[] digits = "0123456789ABCDEF".toCharArray();





    public static String intToHex(long n) {
        StringBuilder sb = new StringBuilder(8);
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            sb.append(b[(int) (n % 16)]);
            n = n/16;
        }
        a = sb.reverse().toString();
        return a;
    }

    public static String hexToInt(String data,double unit) {
        // 16进制转10进制
        int parseInt1 = Integer.parseInt(data, 16);

        return String.valueOf(parseInt1/(unit));

    }
    public static String hexToInt(String data) {
        // 16进制转10进制
        int parseInt1 = Integer.parseInt(data, 16);
        return String.valueOf(parseInt1);

    }

    public static int hexTempToString(String hexData){
        return Integer.valueOf(hexData, 16).shortValue();
    }

}
