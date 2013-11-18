package com.education101.math.twentyfour.data;

import java.util.List;

import com.education101.math.twentyfour.domain.Game;

import android.content.Context;

public interface RankDAO {
	/**
	 * �̶�ģʽ�� ,��ȡ�������а��¼
	 * @param context
	 * @return
	 */
	List<Game> findAllRankCountGame(Context context);
	
	/**
	 * �̶�ģʽ�� ,����һ���µ����а��¼
	 * @param game
	 * @param context
	 */
	void insertNewRankCountGame(Game game,Context context);
	
	/**
	 * �̶�ģʽ�� ,���ݼ�¼���ɾ��һ�����а��¼
	 * @param gameId
	 * @param context
	 */
	void deleteRankCountGame(Integer gameId, Context context);
	
	/**
	 * �̶�ʱ��ģʽ��,��ȡ�������а��¼
	 * @param context
	 * @return
	 */
	List<Game> findAllRankTimeGame(Context context);
	
	/**
	 * �̶�ʱ��ģʽ��,����һ���µ����а��¼
	 * @param game
	 * @param context
	 */
	void insertNewRankTimeGame(Game game,Context context);

	/**
	 * �̶�ʱ��ģʽ��,���ݱ��ɾ��һ�����а��¼
	 * @param delId
	 * @param context
	 */
	void deleteRankTimeGame(Integer delId, Context context);
	
	//Integer findLastRankIdByGameModel(GameModelEnum model,Context context);
}
