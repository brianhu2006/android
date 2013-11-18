package com.education101.math.twentyfour.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 24点算法,去重复
 * @author Tonlin
 *
 */
public class TwentyFour {
	private static List<String> exps = new ArrayList<String>();
	private final static char[] op={'+','-','*','/'};
	
	public static List<String> getExpression(Integer[] v){
		exps.clear();
		int[] nums = new int[]{v[0],v[1],v[2],v[3]};
		evaluate(nums);
		return exps;
	}
	
	private static void evaluate(int[] v){
        for(int a=0;a<4;a++){ 
            for(int b=0;b<4;b++){ 
                if (a==b) continue;  
                for (int c=0;c<4;c++){
                    if (a==c||b==c) continue; 
                    for (int d=0;d<4;d++){ 
                        if (a==d || b==d || c==d ) continue;
                        check(v,new int[]{a,b,c,d});
                    } 
                } 
            }  
        }
        evaluate(new int[]{v[0],v[1],v[2],v[3]},new char[]{'+','+','+'}); 
        evaluate(new int[]{v[0],v[1],v[2],v[3]},new char[]{'*','*','*'});
    }  

	private static void check(int[] v,int[] idx){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){ 
                for(int k=0;k<4;k++){ 
                    if (i==j && j==k) continue;
                    evaluate(new int[]{v[idx[0]],v[idx[1]],v[idx[2]],v[idx[3]]},new char[]{op[i],op[j],op[k]});
                } 
            } 
        } 
    }  

  

    /** 
     * 计算四个数字排列与操作符的运算结果 
     * @param num  数字排列 
     * @param op  操作符排列 
     */  
	private static void evaluate(int[] num,char[] op){ 
        MyStack stack=new MyStack();
        //要入栈的操作数个数1-4  
        int dataNum=0;  
        if (op[0]==op[1] && op[0]==op[2]) dataNum=num.length - 1; 
        for(;dataNum<num.length;dataNum++){ 
            //要入栈的操作符个数1-3  
            int opNum=0;  
            if (dataNum+1==num.length) opNum=op.length-1;  
            int maxOpNum=dataNum;  
            if (dataNum==0) maxOpNum=1;
            repeat:
            for(;opNum<maxOpNum;opNum++){ 
                int numCount=0;  
                int dataIndex=0;
                int opIndex=0;
                stack.clear(); 
                while(dataIndex<num.length || opIndex<op.length){
                    //操作数入栈  
                    for(int i=0;dataIndex<num.length && i<dataNum+1;i++){ 
                        stack.push(num[dataIndex]);  
                        dataIndex++;  
                        numCount++;  
                    }  
                    //操作符入栈  
                    for(int k=0;opIndex<op.length && k<opNum+1;k++){
                        if (numCount>1){  
                            stack.push(op[opIndex]);  
                            if (stack.isStop()) break repeat;
                            opIndex++;  
                            numCount--;  
                        }
                    }  
                }  
                if ((Integer)stack.pop()==24){
                    //System.out.println(stack.toString()); 
                	String str = stack.toString();
                	str = str.substring(0, str.length()-1);
                	str = str.substring(1, str.length());
                	exps.add(str);
                } 
            } 
        }
    }  

  

    @SuppressWarnings("rawtypes")
    private static class MyStack extends Stack{ 
		private static final long serialVersionUID = -827897600510525888L;
		boolean stop=false;  
        private Stack stack=new Stack();

        public String toString(){ 
            return getExpression();
        }  
        
        public boolean isStop(){ 
            return stop; 
        }  

        public String getExpression(){
            Object v=stack.pop();
            if (v instanceof Character){ 
                String right=getExpression();
                String left=getExpression(); 
                return "("+left+v+right+")"; 
                //return left+v+right;
            }
            return v.toString();  

        }  

        public void clear(){  
            super.clear();  
            stack.clear(); 
            stop=false;
        }  

        @SuppressWarnings("unchecked")
		public Object push(Object v){
            stack.push(v);
            if (v instanceof Character){  
                Integer v1=(Integer)pop();
                Integer v2=(Integer)pop();
                Integer v3=0;  
                switch((Character)v){  
                case '+':
                    v3=v2+v1;break;
                case '-':  
                    v3=v2-v1;
                    if (v3<0) stop=true;
                    break; 
                case '*':
                    v3=v2*v1;break;
                case '/':
                    if (v1!=0 && v2%v1==0){
                        v3=v2/v1; 
                    }else{
                        stop=true;
                    }  
                    break; 
                } 
                return super.push(v3);
            }else{ 
                return super.push(v); 
            } 
        } 
    }
}
