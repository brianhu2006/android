package com.education101.math.twentyfour.domain;

import java.util.Date;
import java.util.List;

import com.education101.math.twentyfour.util.DateUtil;
import com.education101.math.twentyfour.util.GameModelEnum;
import com.education101.math.twentyfour.util.GameStatusEnum;


public class Game {	
	public  final static int MAX_COUNT = 10;
	public  final static int MAX_TIME  = 300;
	private Integer        gameId;       //game ID
	private Integer        gameScore;    //game score	
	private Integer        gameRightTime;//right number
	private Integer        gameErrorTime;//wrong number
	private Integer        gameSkipTime; //jump number
	private int            gameTime;     //game consume time	
	private GameModelEnum  gameModel;    //game model(0:fix time 5 mins  ,1:fix subjects 5) 	  
	private GameStatusEnum gameStatus;   //Game Status(0:Ready,1:Start,2£∫Suspend,3£∫Over)
	private Date           gameDate;     //Game end date (∏Ò Ω:yyyy-MM-dd hh:mm:ss)
	private List<Subject>  gameSub;      //game subject
	private int            currentSubIndex = 1; //current subject number
	
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
