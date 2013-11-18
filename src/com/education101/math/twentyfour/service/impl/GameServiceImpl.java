package com.education101.math.twentyfour.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.education101.math.twentyfour.data.RankDAO;
import com.education101.math.twentyfour.data.impl.RankDAOImpl;
import com.education101.math.twentyfour.domain.Game;
import com.education101.math.twentyfour.domain.Subject;
import com.education101.math.twentyfour.service.GameService;
import com.education101.math.twentyfour.util.GameModelEnum;
import com.education101.math.twentyfour.util.GameStatusEnum;
import com.education101.math.twentyfour.util.SubStatusEnum;

import android.content.Context;
import android.util.Log;


public class GameServiceImpl implements GameService{	
	private final static String TAG = "NO.24";
	private RankDAO rankDAO = new RankDAOImpl();
	
	//创建一个新游戏
	public Game createGame(String gameModel) {
		Game game = new Game();        
        if("Time limited".equals(gameModel)){
        	Log.i(TAG, "游戏模式为[争分夺秒]...");	        	
        	game.setGameModel(GameModelEnum.PLAY_IN_TIME);
        }else if("Time Unlimited".equals(gameModel)){
        	Log.i(TAG, "游戏模式为[题海无边]...");	
        	game.setGameModel(GameModelEnum.PLAY_IN_COUNT); 
        } 
        List<Subject> subList = new ArrayList<Subject>();
        subList.add(new Subject());
        game.setGameSub(subList);
        game.setGameStatus(GameStatusEnum.START);	
        Log.i(TAG, "游戏当前题量:"+game.getGameSub().size());
		return game;
	}

	//保存游戏结果
	public int saveRecord(Context context,Game game) {
    	if(game.getGameModel().equals(GameModelEnum.PLAY_IN_COUNT)){//固定题目模式
    		for(Subject sub:game.getGameSub()){
    			if(sub.getSubStatus().equals(SubStatusEnum.ERROR)
    					||sub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
    				return -1;//不能进入排行榜
    			}
    		}
			List<Game> gameList = rankDAO.findAllRankCountGame(context);//取得所有排行版记录
			if(0 <= gameList.size()|| gameList.size()<10){//排行榜名额没满
    			rankDAO.insertNewRankCountGame(game, context);
    			return 1;
    		}
			for(int i = 0;i<gameList.size();i++){//判断能否进入排行榜
				Game g = gameList.get(i);
				if(game.getGameTime() > g.getGameTime()){
					Log.i(TAG, "成功进入排行榜...");
					if(gameList.size()>=10){//如果排行榜记录大于10个   
						Log.i(TAG, "排行榜记录大于10，最后一名出局...");
					    //1.删除最后一名该记录
						Integer delId = gameList.get(gameList.size()-2).getGameId();
						rankDAO.deleteRankCountGame(delId, context);
					}
					rankDAO.insertNewRankCountGame(game, context);
					return 1;
				}
			} 
    	}else if(game.getGameModel().equals(GameModelEnum.PLAY_IN_TIME)){
    		Log.i(TAG, "用户算得正确的题数:"+game.getGameRightTime());
    		if(game.getGameRightTime()<=0) //如果一题都没答出来
    			return -1;
    		List<Game> gameList = rankDAO.findAllRankTimeGame(context);//获取所有
    		if(0 <= gameList.size()|| gameList.size()<10){//排行榜名额没满
    			rankDAO.insertNewRankTimeGame(game, context);
    			return 1;
    		}    		
    		for(int i = 0;i<gameList.size();i++){//检查能否进入排行版
				Game g = gameList.get(i);
				if(game.getGameRightTime() > g.getGameRightTime()){
					Log.i(TAG, "成功进入排行榜...");
					if(gameList.size()>=10){//如果排行榜记录大于10个    	
						Log.i(TAG, "排行榜记录大于10，最后一名出局...");
						//1.删除最后一名该记录//2.查询出最后一名编号(调用dao)
						Integer delId = gameList.get(gameList.size()-2).getGameId();
						rankDAO.deleteRankTimeGame(delId, context);
					}
					rankDAO.insertNewRankTimeGame(game, context);
					return 1;
				}
    		}    		
    	} 
    	return -1;
	}

	//加载固定题量排行榜
	public List<Game> loadRankCountList(Context context) {		
		return rankDAO.findAllRankCountGame(context);
	}

	//加载固定时间排行榜
	public List<Game> loadRankTimeList(Context context) {		
		return rankDAO.findAllRankTimeGame(context);
	}

}
