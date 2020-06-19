package com.hc.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MathUtil {
    private static List<String> arrListt = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");

    private static List<String> commids = Arrays.asList("7", "8", "9", "a", "A","b", "B","c", "C","d", "D","e", "E","f", "F");

    public static void main(String[] args) {
//4843 71 0C 31 38 30 36 39 39 30 32 30 30 01EF 95 23
        //4843850F 31 38 30 34 30 35 30 30 30 35 CC 7D FF FF 56 6B23
        //List<String> ruleone = ruleone("4843870e313832353132303031370004082eab234843870e313832353132303031370004082eab23484372");
        //List<String> ruleone = ruleone("4843880a313831333131303030388a234843880a313830393131303030338a23");
        //4843880a313831333131303031378423 4843880a313832313131303033308023 4843880a313830393131303030348d23 4843850f313832353036303034340e15
        //4843 91 10 31383335303630303031 0E74 0258 01F4 57 23
        //4843 87 0e 31383136303630303031 0000 f000 7b23
        //4843 99 19 31383038393930303238 0E75 0E7C 0E7C 0E7C 0E78 0E81 0E7A 0E7C 0E78 0E78 01F7 0226 A4 23
        String ruleone = "4843210A313833353135303030312A23";
//		String   ruleone = "4843700c31383135303230313738cc4cf623";
        List<String> ruleone1 = ruleone(ruleone);
        List<String> strings = ruleTwo(ruleone1);
        for (String s : strings) {
            String substring = s.substring(8, 28);
            System.out.println(substring);
            System.out.println(s);
            String sn = HexStringUtils.fromHex(substring);
            if (!arrListt.contains(sn.substring(0, 1))) {
                System.out.println("没有sn");
            }
            System.out.println(sn);
        }
        // abcdef

//		char l='a';
//		System.out.println(String.valueOf(l));


    }


    public static List<String> ruleone(String data) {
        int i1 = data.lastIndexOf("23");
        List<String> strings = new ArrayList<>();
        char[] charArray = data.toCharArray();
        for (int i = 0; i < i1 - 3; i++) {
            char a = charArray[i];
            char b = charArray[i + 1];
            char c = charArray[i + 2];
            char d = charArray[i + 3];
            if (StringUtils.equals(String.valueOf(a) + String.valueOf(b) + String.valueOf(c) + String.valueOf(d),
                    "4843")) {
                char l = charArray[i + 4];
                if (!commids.contains(String.valueOf(l))) {
                    continue;
                }
                int ta = i + 7;
                if (ta < data.length()) {
                    char e = charArray[i + 6];
                    char f = charArray[i + 7];
                    String lengtn = String.valueOf(e) + String.valueOf(f);
                    int parseInt = Integer.parseInt(lengtn, 16);
                    int g = parseInt * 2;
                    int t = i + 7 + g + 4;
                    if (t < data.length()) {
                        char h = charArray[(i + 7 + g + 3)];
                        char m = charArray[(i + 7 + g + 4)];
                        String end = String.valueOf(h) + String.valueOf(m);
                        if (StringUtils.equals(end, "23")) {
                            String substring = data.substring(i, (i + 7 + g + 4 + 1));
                            strings.add(substring);
                        }
                    }
                }

            }

        }
        //去除重复
        return strings.stream().distinct().collect(Collectors.toList());
    }


    public static String change(String content) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            if (i % 2 == 0) {
                str.append(" ").append(content, i, i + 1);
            } else {
                str.append(content, i, i + 1);
            }
        }
        return str.toString().trim();
    }

    public static String yihuo(String content) {
        content = change(content);
        String[] b = content.split(" ");
        int a = 0;
        for (int i = 0; i < b.length; i++) {
            a = a ^ Integer.parseInt(b[i], 16);
        }
        if (a < 10) {
            StringBuffer sb = new StringBuffer();
            sb.append("0");
            sb.append(a);
            return sb.toString();
        }
        return Integer.toHexString(a);
    }

    public static List<String> ruleTwo(List<String> data) {
        List<String> list = new ArrayList<>();
        Boolean boolean1 = true;
        for (String string : data) {
            String substring = string.substring(string.length() - 4, string.length() - 2);
            int parseInt = Integer.parseInt(substring, 16);
            String substring2 = string.substring(0, string.length() - 4);
            String yihuo = yihuo(substring2);
            int parseInt2 = Integer.parseInt(yihuo, 16);
            boolean1 = (parseInt == parseInt2);
            if (boolean1) {
                list.add(string);
            }
        }

        return list;
    }

    public static String ruleMT(String sn) {
        String substring = sn.substring(4, 6);
        if (StringUtils.equals(substring, "11")) {
            return sn;
        }
        return null;
    }

    public static String ruleMT99(String sn) {
        String substring = sn.substring(4, 6);
        if (StringUtils.equals(substring, "99")) {
            return sn;
        }
        return null;
    }


    //16进制转二进制
    public static String hexadecimal(String string) {
        String s = "";
        for (int i = 0; i < string.length(); i++) {
            s += g(string.charAt(i));
        }
        return s;
    }

    public static String g(char charAt) {
        switch (charAt) {
            case '0':
                return "0000";
            case '1':
                return "0001";
            case '2':
                return "0010";
            case '3':
                return "0011";
            case '4':
                return "0100";
            case '5':
                return "0101";
            case '6':
                return "0110";
            case '7':
                return "0111";
            case '8':
                return "1000";
            case '9':
                return "1001";
            case 'A':
                return "1010";
            case 'B':
                return "1011";
            case 'C':
                return "1100";
            case 'D':
                return "1101";
            case 'E':
                return "1110";
            case 'F':
                return "1111";
            case 'a':
                return "1010";
            case 'b':
                return "1011";
            case 'c':
                return "1100";
            case 'd':
                return "1101";
            case 'e':
                return "1110";
            case 'f':
                return "1111";
            default:
                return null;
        }
    }


}
