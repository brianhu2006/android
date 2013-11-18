package com.education101.math.twentyfour.util;

import android.text.Editable;
import android.text.Selection;
import android.widget.Button;
import android.widget.EditText;


/**
 * ��Ϸ����������
 * @author Tonlin
 *
 */
public class KeyManager {
	
	/**
	 * ���ݰ��������ı�������
	 * @param edit
	 */
	public static void setEditText(EditText edit,KeyEnum keyType,Button btn){
		String expStr = edit.getText().toString();
		int expLength = expStr.length();		
		if(KeyEnum.DIGIT.equals(keyType)){ //��������
			if(expLength == 0){ //��һ������
				edit.setText(btn.getText());				
				setEditTextCursor(edit);
				btn.setEnabled(false);				
			}else {
				if(KeyEnum.DIGIT.equals(getEditLastCharType(expStr)) ||
						KeyEnum.RIGHT_BRACKET.equals(getEditLastCharType(expStr))){//���һ�������֡���������������
					return;
				}else{
					edit.setText(edit.getText()+""+btn.getText());				
					setEditTextCursor(edit);
					btn.setEnabled(false);		
				}
			}			
		}else if(KeyEnum.OPERATOR.equals(keyType)){//���²�����
			if(expLength == 0){ //��һ������
				return;
			}else{
				if(KeyEnum.LEFT_BRACKET.equals(getEditLastCharType(expStr)) ||
					KeyEnum.OPERATOR.equals(getEditLastCharType(expStr))){  //���һ���Ĳ���������������
					return;
				}else{
					edit.setText(edit.getText()+""+btn.getText());				
					setEditTextCursor(edit);
				}
			}
		}else if(KeyEnum.LEFT_BRACKET.equals(keyType)){ //������
			if(expLength>0){
				if(KeyEnum.RIGHT_BRACKET.equals(getEditLastCharType(expStr)) ||
						KeyEnum.DIGIT.equals(getEditLastCharType(expStr))){   //���һ���������Ż�����
					return;
				}else {
					if(hasCharCount(expStr,'(')>=2){ //�Ѿ�ӵ������������
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
			
		}else if(KeyEnum.RIGHT_BRACKET.equals(keyType)){ //������
			if(expLength==0) return;
			if(KeyEnum.DIGIT.equals(getEditLastCharType(expStr))||
				KeyEnum.RIGHT_BRACKET.equals(getEditLastCharType(expStr))){//ֻ���ܼ��������ź�����֮��
				if(KeyEnum.RIGHT_BRACKET.equals(getEditLastCharType(expStr)) &&
						hasTwoLeftBracket(expStr)){
					return;
				}				
				if(hasCharCount(expStr, '(') <= hasCharCount(expStr, ')'))//��������ŵĸ���С�ڵ���������
					return;
				//��������Ȳ��ҵ������Ŷ����ǲ�����
				if(expStr.lastIndexOf("(") > oprLastIndex(expStr))
					return;
				if(hasCharCount(expStr, ')')>=2){//�Ѿ���������������
					return;
				}else{
					edit.setText(edit.getText()+""+btn.getText());				
					setEditTextCursor(edit);
				}
			}
		}
	}
	
	
	
	/**
	 * ���ı������������ұ�
	 * @param edit
	 */
	public static void setEditTextCursor(EditText edit){
		Editable etext = edit.getText(); 
		int position = etext.length(); 
		Selection.setSelection(etext, position);
	}
	
	//������һ���ַ�������
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
	
	//��ȡ�ַ���ӵ��ĳ�ַ��ĸ���
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
	
	//�Ƿ�����ص�����
	private static boolean hasTwoLeftBracket(String exp){
		int index = exp.lastIndexOf("((");
		return index == -1?false:true;
	}
}
