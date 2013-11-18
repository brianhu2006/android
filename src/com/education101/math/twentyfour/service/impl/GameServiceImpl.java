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
	
	//����һ������Ϸ
	public Game createGame(String gameModel) {
		Game game = new Game();        
        if("Time limited".equals(gameModel)){
        	Log.i(TAG, "��ϷģʽΪ[���ֶ���]...");	        	
        	game.setGameModel(GameModelEnum.PLAY_IN_TIME);
        }else if("Time Unlimited".equals(gameModel)){
        	Log.i(TAG, "��ϷģʽΪ[�⺣�ޱ�]...");	
        	game.setGameModel(GameModelEnum.PLAY_IN_COUNT); 
        } 
        List<Subject> subList = new ArrayList<Subject>();
        subList.add(new Subject());
        game.setGameSub(subList);
        game.setGameStatus(GameStatusEnum.START);	
        Log.i(TAG, "��Ϸ��ǰ����:"+game.getGameSub().size());
		return game;
	}

	//������Ϸ���
	public int saveRecord(Context context,Game game) {
    	if(game.getGameModel().equals(GameModelEnum.PLAY_IN_COUNT)){//�̶���Ŀģʽ
    		for(Subject sub:game.getGameSub()){
    			if(sub.getSubStatus().equals(SubStatusEnum.ERROR)
    					||sub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
    				return -1;//���ܽ������а�
    			}
    		}
			List<Game> gameList = rankDAO.findAllRankCountGame(context);//ȡ���������а��¼
			if(0 <= gameList.size()|| gameList.size()<10){//���а�����û��
    			rankDAO.insertNewRankCountGame(game, context);
    			return 1;
    		}
			for(int i = 0;i<gameList.size();i++){//�ж��ܷ�������а�
				Game g = gameList.get(i);
				if(game.getGameTime() > g.getGameTime()){
					Log.i(TAG, "�ɹ��������а�...");
					if(gameList.size()>=10){//������а��¼����10��   
						Log.i(TAG, "���а��¼����10�����һ������...");
					    //1.ɾ�����һ���ü�¼
						Integer delId = gameList.get(gameList.size()-2).getGameId();
						rankDAO.deleteRankCountGame(delId, context);
					}
					rankDAO.insertNewRankCountGame(game, context);
					return 1;
				}
			} 
    	}else if(game.getGameModel().equals(GameModelEnum.PLAY_IN_TIME)){
    		Log.i(TAG, "�û������ȷ������:"+game.getGameRightTime());
    		if(game.getGameRightTime()<=0) //���һ�ⶼû�����
    			return -1;
    		List<Game> gameList = rankDAO.findAllRankTimeGame(context);//��ȡ����
    		if(0 <= gameList.size()|| gameList.size()<10){//���а�����û��
    			rankDAO.insertNewRankTimeGame(game, context);
    			return 1;
    		}    		
    		for(int i = 0;i<gameList.size();i++){//����ܷ�������а�
				Game g = gameList.get(i);
				if(game.getGameRightTime() > g.getGameRightTime()){
					Log.i(TAG, "�ɹ��������а�...");
					if(gameList.size()>=10){//������а��¼����10��    	
						Log.i(TAG, "���а��¼����10�����һ������...");
						//1.ɾ�����һ���ü�¼//2.��ѯ�����һ�����(����dao)
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

	//���ع̶��������а�
	public List<Game> loadRankCountList(Context context) {		
		return rankDAO.findAllRankCountGame(context);
	}

	//���ع̶�ʱ�����а�
	public List<Game> loadRankTimeList(Context context) {		
		return rankDAO.findAllRankTimeGame(context);
	}

}
