package com.education101.math.twentyfour.util;

import android.text.Editable;
import android.text.Selection;
import android.widget.Button;
import android.widget.EditText;


/**
 * 游戏按键管理类
 * @author Tonlin
 *
 */
public class KeyManager {
	
	/**
	 * 根据按键处理文本框内容
	 * @param edit
	 */
	public static void setEditText(EditText edit,KeyEnum keyType,Button btn){
		String expStr = edit.getText().toString();
		int expLength = expStr.length();		
		if(KeyEnum.DIGIT.equals(keyType)){ //按下数字
			if(expLength == 0){ //第一次输入
				edit.setText(btn.getText());				
				setEditTextCursor(edit);
				btn.setEnabled(false);				
			}else {
				if(KeyEnum.DIGIT.equals(getEditLastCharType(expStr)) ||
						KeyEnum.RIGHT_BRACKET.equals(getEditLastCharType(expStr))){//最后一个是数字、操作符、右括号
					return;
				}else{
					edit.setText(edit.getText()+""+btn.getText());				
					setEditTextCursor(edit);
					btn.setEnabled(false);		
				}
			}			
		}else if(KeyEnum.OPERATOR.equals(keyType)){//按下操作符
			if(expLength == 0){ //第一次输入
				return;
			}else{
				if(KeyEnum.LEFT_BRACKET.equals(getEditLastCharType(expStr)) ||
					KeyEnum.OPERATOR.equals(getEditLastCharType(expStr))){  //最后一个的操作符、左右括号
					return;
				}else{
					edit.setText(edit.getText()+""+btn.getText());				
					setEditTextCursor(edit);
				}
			}
		}else if(KeyEnum.LEFT_BRACKET.equals(keyType)){ //左括号
			if(expLength>0){
				if(KeyEnum.RIGHT_BRACKET.equals(getEditLastCharType(expStr)) ||
						KeyEnum.DIGIT.equals(getEditLastCharType(expStr))){   //最后一个是右括号或数字
					return;
				}else {
					if(hasCharCount(expStr,'(')>=2){ //已经拥有两个左括号
						return;
					}else{
						edit.setText(edit.getText()+""+btn.getText());				
						setEditTextCursor(edit);						
					}
				}
			}else{
				edit.setText(edit.getText()+""+btn.getText());				
				setEditTextCursor(edit);	
			}
			
		}else if(KeyEnum.RIGHT_BRACKET.equals(keyType)){ //右括号
			if(expLength==0) return;
			if(KeyEnum.DIGIT.equals(getEditLastCharType(expStr))||
				KeyEnum.RIGHT_BRACKET.equals(getEditLastCharType(expStr))){//只可能加在右括号和数字之后
				if(KeyEnum.RIGHT_BRACKET.equals(getEditLastCharType(expStr)) &&
						hasTwoLeftBracket(expStr)){
					return;
				}				
				if(hasCharCount(expStr, '(') <= hasCharCount(expStr, ')'))//如果左括号的个数小于等于右括号
					return;
				//如果回退先查找到左括号而不是操作符
				if(expStr.lastIndexOf("(") > oprLastIndex(expStr))
					return;
				if(hasCharCount(expStr, ')')>=2){//已经存在两个右括号
					return;
				}else{
					edit.setText(edit.getText()+""+btn.getText());				
					setEditTextCursor(edit);
				}
			}
		}
	}
	
	
	
	/**
	 * 将文本框光标移至最右边
	 * @param edit
	 */
	public static void setEditTextCursor(EditText edit){
		Editable etext = edit.getText(); 
		int position = etext.length(); 
		Selection.setSelection(etext, position);
	}
	
	//获得最后一个字符的类型
	private static KeyEnum getEditLastCharType(String str){
		char lastChar = str.charAt(str.length()-1);
		if(Character.isDigit(lastChar))
			return KeyEnum.DIGIT;
		else if(lastChar == '+' || lastChar == '-' ||
				lastChar == '*' || lastChar == '/' ){
			return KeyEnum.OPERATOR;
		}else if(lastChar == '('){
			return KeyEnum.LEFT_BRACKET;
		}else if(lastChar == ')'){
			return KeyEnum.RIGHT_BRACKET;
		}else{
			return null;
		}
	}
	
	//获取字符串拥有某字符的个数
	private static int hasCharCount(String exp,char ch){
		int count = 0;
		char[] expArray = exp.toCharArray();
		for(char c:expArray){
			if(c == ch)
				count++;
		}
		return count;
	}
	
	private static int oprLastIndex(String exp){
		int max = -1;
		int add = exp.lastIndexOf("+");
		int sub = exp.lastIndexOf("-");
		max = add>sub?add:sub;
		int mut = exp.lastIndexOf("*");
		max = mut>max?mut:max;
		int div = exp.lastIndexOf("/");		
		return max = max>div?max:div;
	}
	
	//是否存在重叠括号
	private static boolean hasTwoLeftBracket(String exp){
		int index = exp.lastIndexOf("((");
		return index == -1?false:true;
	}
}
