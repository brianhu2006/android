package com.education101.math.twentyfour.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import com.education101.math.twentyfour.util.SubStatusEnum;




//import net.fjut.util.CoreAlgorithm;


/**
 * ��Ŀ
 * @author Tonlin
 *
 */
public class Subject implements Serializable{	
	
	private static final long serialVersionUID = -8650807091582953667L;
	
	private Integer    subScore; //��Ŀ�÷�(������+����÷�)//û�õ�
	private Long       subTime;  //������ʱ   //û�õ�
	private Character  subType;  //����ģʽ   //û�õ�
	private SubStatusEnum  subStatus; //��Ŀ״̬(0:δ����,1:���,2:���)
	private String     subExp;   //�������ı��ʽ
	private Integer[]  subNums;  //�����4��(1-13)
	private Integer[]  numTypes;  //�������ɫ(1-4)
	private List<String> subRightExp;//��ȷ�ı��ʽ����
	private boolean    subCalculated;//�Ƿ�������
	
	public Subject(){
		setSubStatus(SubStatusEnum.UNANSWER);
		subCalculated = false;
		numTypes = new Integer[4];
		subNums = new Integer[4];
		for(int i=0;i<4;i++){
			int card = generateNum(51);//(0-51)
			numTypes[i] = card/13+1; //��ɫ
			subNums[i] = card%13+1;  //����			
		}
	}
	
	public Integer[] getNumTypes() {
		return numTypes;
	}

	public void setNumTypes(Integer[] numTypes) {
		this.numTypes = numTypes;
	}

	public Integer getSubScore() {
		return subScore;
	}
	public void setSubScore(Integer subScore) {
		this.subScore = subScore;
	}
	public Long getSubTime() {
		return subTime;
	}
	public void setSubTime(Long subTime) {
		this.subTime = subTime;
	}
	public Character getSubType() {
		return subType;
	}
	public void setSubType(Character subType) {
		this.subType = subType;
	}
	public String getSubExp() {
		return subExp;
	}
	public void setSubExp(String subExp) {
		this.subExp = subExp;
	}
	public Integer[] getSubNums() {
		return subNums;
	}
	public void setSubNums(Integer[] subNums) {
		this.subNums = subNums;
	}	

	public List<String> getSubRightExp() {
		return subRightExp;
	}

	public void setSubRightExp(List<String> subRightExp) {
		this.subRightExp = subRightExp;
	}
	
	public boolean isSubCalculated() {
		return subCalculated;
	}

	public void setSubCalculated(boolean subCalculated) {
		this.subCalculated = subCalculated;
	}

	public SubStatusEnum getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(SubStatusEnum subStatus) {
		this.subStatus = subStatus;
	}
	
	//����һ��(0-51)֮�������
	private int generateNum(int n){
		int num = 1;
		do{
			num = new Random().nextInt(n+1);
		}while(num<0);
		return num;
	}
}
