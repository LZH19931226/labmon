package com.hc.my.common.core.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @author LiuZhiHao
 * @date 2019/10/21 14:10
 * 描述: base加密解密
 **/
public class Base64Util {

    private static final String CHARSET = "utf-8";

    /**
     * 解密
     * @param data
     * @return
     */
    public static String decode(String data) {
        try {
            if (null == data) {
                return null;
            }

            return new String(Base64.decodeBase64(data.getBytes(CHARSET)), CHARSET);
        } catch (UnsupportedEncodingException e) {

        }

        return null;
    }

    /**
     * 加密
     * @param data
     * @return
     */
    public static String encode(String data) {
        try {
            if (null == data) {
                return null;
            }
            return new String(Base64.encodeBase64(data.getBytes(CHARSET)), CHARSET);
        } catch (UnsupportedEncodingException e) {

        }

        return null;
    }

}