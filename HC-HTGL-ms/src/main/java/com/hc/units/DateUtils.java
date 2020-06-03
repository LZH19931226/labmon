package com.hc.units;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	/**@param format 根据指定的格式时间类型返回标准时间类型
	 * 返回格式：2007-08-14
	 * @return
	 */
	public static String  changeTime(String time) {
		
		if (StringUtils.isEmpty(time)) {
			return null;
		}
		 String  aString = null;
		if (time.contains("/")) {
			 aString = time.replaceAll("/", "-");
		}else {
			   aString = time;
		}
		return aString;
	}
	
	
	
	public static String  changeTime2(String time) {

		String abc = null;
		if (time.contains("-")) {
			 abc = time.replaceAll("-", "");
		}
		
		String replaceAll2 = abc.replaceAll(" ", "");
		String substring = replaceAll2.substring(0,8);
		return substring+"000000";
	}
	
	public static Date changeTime3(String time) throws ParseException {
		
		if (StringUtils.isEmpty(time)) {
			return null;
		}
		if (time.contains("-")) {
			 Date date2 = getDate(time, "yyyy-MM-dd");
			return date2;
		}
		String substring = time.substring(0, 4);
		String substring2 = time.substring(4, 6);
		String substring3 = time.substring(6, 8);
		String date = substring+"-"+substring2+"-"+substring3;
		 Date date2 = getDate(date, "yyyy-MM-dd");
		return date2;
		
	}
	
	public static String getNowTime1() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String format = sdf.format(new Date());
		String abc = format.replaceAll("-", "");
		String replaceAll = abc.replaceAll(":", "");
		String replaceAll2 = replaceAll.replaceAll(" ", "");
		return replaceAll2;
	}
	
	public static void main(String[] args) {
		
		String nowTime1 = getNowTime1();
		System.out.println(nowTime1);
		
	}
	/**
	 * 返回格式：2007-08-14
	 * @return
	 */
	public static String getToday(){
		String time = "";
		time = getToday("yyyy-MM-dd");
		return time;
	}
	/**
	 * 
	 * @param format 根据指定的格式时间类型返回当前时间
	 * @return
	 */
	public static String getToday(String format){
		return getDateStr(Calendar.getInstance().getTime(),format);
	}
	
	/**
	 * 日期转字符
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateStr(Date date,String format){
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static Date getDate(String date,String format) throws ParseException{
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.parse(date);
	}
	
	/**
	 * @param millis
	 * @return
	 */
	public static Date parseMills(long millis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
	
	public static Date getDateToday(String format) throws ParseException{
		String str = getDateStr(Calendar.getInstance().getTime(),format);
		return getDate(str,format);
	}
	
	public static long getTimes(){
		return new Date().getTime();
	}
	
//	/**
//	 * 考试年份编码
//	 * @return
//	 */
//	public static String getYearCode(){
//		Calendar cal = Calendar.getInstance();
//    	int year = cal.get(Calendar.YEAR);
//    	String yearStr = new Integer(year).toString();
//    	return yearStr.substring(2,4);//当前年份后两位
//	}
	
	/*public static void main(String[] args) throws ParseException{
      
		String aString  = "20180604115701";
		String changeTime3 = changeTime3(aString);
		System.out.println(changeTime3);
		
		
	}*/
	public static String getNowTime() {
		return getToday("yyyy-MM-dd HH:mm:ss");
	}
	public static boolean checkTxnTime(String startTime, String endTime) {
		String nowDate = getDateStr(new Date(), "HH:mm:ss");
		if(nowDate.compareTo(startTime) >= 0 && nowDate.compareTo(endTime) <= 0){
			return true;
		}
		return false; 
	}
}
