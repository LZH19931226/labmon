package com.hc.utils;

import org.apache.commons.lang.StringUtils;

//香港 852，也写作00852、00 852、00-852、+852、+852-、+00852、852、0852、000852等       阿里云规则  接收号码格式为00+国际区号+号码，如“0085200000000”
//台湾 886，也写作00886、00 886、00-886、+886、+886-、+00886、886、0886、000886等       阿里云规则  接收号码格式为00+国际区号+号码， 如“0088600000000”
//澳门 853，也写作00853、00 853、00-853、+853、+853-、+00853、853、0853、000853等      阿里云规则    接收号码格式为00+国际区号+号码 ，如“0085300000000”
//中国内地 阿里云规则    接收号码格式为00+国际区号+号码 ，如“008600000000”
public class validPhoneNum {

	
	
	
	//判断号码是否为港澳台号码
	public static String CheckPhone(String phone) {
		String areacode = phone.substring(2, 5);
		if (StringUtils.equals(areacode, "852")) {
			return "HongKong";
		}
		if (StringUtils.equals(areacode, "853")) {
			return "macau";
		}
		if (StringUtils.equals(areacode, "886")) {
			return "Taiwan";
		}
		return "mainland";
	}
	
	
	
}
