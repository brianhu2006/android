package com.education101.math.twentyfour.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import com.education101.math.twentyfour.util.SubStatusEnum;




//import net.fjut.util.CoreAlgorithm;


/**
 * 题目
 * @author Tonlin
 *
 */
public class Subject implements Serializable{	
	
	private static final long serialVersionUID = -8650807091582953667L;
	
	private Integer    subScore; //题目得分(基本分+额外得分)//没用到
	private Long       subTime;  //比赛用时   //没用到
	private Character  subType;  //比赛模式   //没用到
	private SubStatusEnum  subStatus; //题目状态(0:未作答,1:答对,2:答错)
	private String     subExp;   //玩家输入的表达式
	private Integer[]  subNums;  //随机数4个(1-13)
	private Integer[]  numTypes;  //随机数花色(1-4)
	private List<String> subRightExp;//正确的表达式集合
	private boolean    subCalculated;//是否计算完成
	
	public Subject(){
		setSubStatus(SubStatusEnum.UNANSWER);
		subCalculated = false;
		numTypes = new Integer[4];
		subNums = new Integer[4];
		for(int i=0;i<4;i++){
			int card = generateNum(51);//(0-51)
			numTypes[i] = card/13+1; //花色
			subNums[i] = card%13+1;  //点数			
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
	
	//产生一个(0-51)之间的整数
	private int generateNum(int n){
		int num = 1;
		do{
			num = new Random().nextInt(n+1);
		}while(num<0);
		return num;
	}
}
