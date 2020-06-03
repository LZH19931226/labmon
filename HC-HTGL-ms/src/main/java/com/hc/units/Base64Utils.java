package com.hc.units;

import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@SuppressWarnings("restriction")
public class Base64Utils {

	public static void decodeBase64ToImage(String xml, String filepath, String idName) throws Exception {
		// 对字节数组字符串进行Base64解码并生成图片
		BASE64Decoder decoder = new BASE64Decoder();
			// Base64解码
			byte[] b = decoder.decodeBuffer(xml);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}         			}
			String filename = filepath + "\\" + idName;
			File file = new File(filename);
			OutputStream out = new FileOutputStream(file);
			out.write(b);
			out.flush();
			out.close();
	}

 
}
