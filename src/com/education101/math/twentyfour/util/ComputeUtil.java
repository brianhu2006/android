package com.education101.math.twentyfour.util;

import org.nfunk.jep.JEP;
  
/** 
 * 计算表达式工具类   
 */  
public final class ComputeUtil {  
	
	/**
	 * 输入的字符串是否等于输入的int值
	 * @param str 算术表达式
	 * @param num 结果值
	 * @return 
	 * @throws Exception
	 */
    public static boolean equalNum(String str,int num) throws Exception{
    	 JEP prase=new JEP();
    	 prase.parseExpression(str);  
         double a = prase.getValue(); 
         if(prase.hasError()){
        	 throw new Exception(prase.getErrorInfo());
         }
         return (int)a == num?true:false;
    }
} 
