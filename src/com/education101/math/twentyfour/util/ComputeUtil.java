package com.education101.math.twentyfour.util;

import org.nfunk.jep.JEP;
  
/** 
 * ������ʽ������   
 */  
public final class ComputeUtil {  
	
	/**
	 * ������ַ����Ƿ���������intֵ
	 * @param str �������ʽ
	 * @param num ���ֵ
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
