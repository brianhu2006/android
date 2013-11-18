package com.education101.math.twentyfour.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ���ڹ�����
 * @author Tonlin
 *
 */
public class DateUtil {
	//private static DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");        
	//private static DateFormat format2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");   
	
	/**
	 * �ַ���ת����
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
	public static Date stringToDate(String dateStr){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");    
		Date date = null; 
		try {   
		    date = format.parse(dateStr); 
		} catch (ParseException e) {   
		    e.printStackTrace();   
		}  
		return date;
	}
	
	/**
	 * ����ת�ַ���
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static String dateToString(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return dateFormat.format(date);
	}
}
