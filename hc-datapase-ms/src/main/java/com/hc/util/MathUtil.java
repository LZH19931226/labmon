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

        String ruleone = "48439c1332303136313830303535c7ebc7ebfff0fff022aa2348439c1332303136313830303031cc09cbf3fff0fff02c5823 ";
        List<String> ruleone1 = cutOutString4843(ruleone);
        for (String s : ruleone1) {
            System.out.println(s);
        }
    }


    public static List<String> cutOutString4843(String data) {
        int i1 = data.lastIndexOf("23");
        List<String> strings = new ArrayList<>();
        char[] charArray = data.toCharArray();
        for (int i = 0; i < i1 - 3; i++) {
            char a = charArray[i];
            char b = charArray[i + 1];
            char c = charArray[i + 2];
            char d = charArray[i + 3];
            if (StringUtils.equals(a + String.valueOf(b) + c + d,
                    "4843")) {
                char l = charArray[i + 4];
                if (!commids.contains(String.valueOf(l))) {
                    continue;
                }
                int ta = i + 7;
                if (ta < data.length()) {
                    char e = charArray[i + 6];
                    char f = charArray[i + 7];
                    String lengtn = e + String.valueOf(f);
                    int parseInt = Integer.parseInt(lengtn, 16);
                    int g = parseInt * 2;
                    int t = i + 7 + g + 4;
                    if (t < data.length()) {
                        char h = charArray[(i + 7 + g + 3)];
                        char m = charArray[(i + 7 + g + 4)];
                        String end = h + String.valueOf(m);
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

    //16进制转二进制
    public static String hexadecimal(String string) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            s.append(g(string.charAt(i)));
        }
        return s.toString();
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
