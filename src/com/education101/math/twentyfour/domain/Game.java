package com.education101.math.twentyfour.domain;

import java.util.Date;
import java.util.List;

import com.education101.math.twentyfour.util.DateUtil;
import com.education101.math.twentyfour.util.GameModelEnum;
import com.education101.math.twentyfour.util.GameStatusEnum;


public class Game {	
	public  final static int MAX_COUNT = 10;
	public  final static int MAX_TIME  = 300;
	private Integer        gameId;       //游戏编号
	private Integer        gameScore;    //游戏分数(规则:xxxx)暂时未实现
	private Integer        gameRightTime;//正确题数
	private Integer        gameErrorTime;//错误题数
	private Integer        gameSkipTime; //跳过题数
	private int            gameTime;     //游戏总耗时(s)	
	private GameModelEnum  gameModel;    //游戏模式(0:固定时间5分钟  ,1:固定题目5道题) 	  
	private GameStatusEnum gameStatus;   //游戏状态(0:准备开始,1:已开始,2：暂停,3：结束)
	private Date           gameDate;     //结束日期(格式:yyyy-MM-dd hh:mm:ss)
	private List<Subject>  gameSub;      //游戏题目
	private int            currentSubIndex = 1; //当前做的题目
	
	public Game(){		
		gameRightTime = 0;
		gameErrorTime = 0;
		gameSkipTime  = 0;
		this.gameStatus = GameStatusEnum.START;
	}
	
	public Game(String createDate, int maxCount, int wasteTime) {
		
	}

	public Game(int id, String createDate, Integer wasteTime) {
		this.gameId = id;
		this.gameDate = DateUtil.stringToDate(createDate);
		this.gameTime = wasteTime;
	}

	public Integer getGameScore() {
		return gameScore;
	}
	public void setGameScore(Integer gameScore) {
		this.gameScore = gameScore;
	}
	public int getGameTime() {
		return gameTime;
	}
	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}		
	public List<Subject> getGameSub() {
		return gameSub;
	}
	public void setGameSub(List<Subject> gameSub) {
		this.gameSub = gameSub;
	}
	public Date getGameDate() {
		return gameDate;
	}
	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}
	public Integer getGameRightTime() {
		return gameRightTime;
	}
	public void setGameRightTime(Integer gameRightTime) {
		this.gameRightTime = gameRightTime;
	}
	public Integer getGameErrorTime() {
		return gameErrorTime;
	}
	public void setGameErrorTime(Integer gameErrorTime) {
		this.gameErrorTime = gameErrorTime;
	}
	public int getCurrentSubIndex() {
		return currentSubIndex;
	}
	public void setCurrentSubIndex(int currentSubIndex) {
		this.currentSubIndex = currentSubIndex;
	}

	public Integer getGameSkipTime() {
		return gameSkipTime;
	}

	public void setGameSkipTime(Integer gameSkipTime) {
		this.gameSkipTime = gameSkipTime;
	}

	public GameStatusEnum getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatusEnum gameStatus) {
		this.gameStatus = gameStatus;
	}

	public GameModelEnum getGameModel() {
		return gameModel;
	}

	public void setGameModel(GameModelEnum gameModel) {
		this.gameModel = gameModel;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
}
