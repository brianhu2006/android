package com.education101.math.twentyfour.data;

import java.util.List;

import com.education101.math.twentyfour.domain.Game;

import android.content.Context;

public interface RankDAO {
	/**
	 * 固定模式下 ,获取所有排行榜记录
	 * @param context
	 * @return
	 */
	List<Game> findAllRankCountGame(Context context);
	
	/**
	 * 固定模式下 ,插入一条新的排行榜记录
	 * @param game
	 * @param context
	 */
	void insertNewRankCountGame(Game game,Context context);
	
	/**
	 * 固定模式下 ,根据记录编号删除一条排行榜记录
	 * @param gameId
	 * @param context
	 */
	void deleteRankCountGame(Integer gameId, Context context);
	
	/**
	 * 固定时间模式下,获取所有排行榜记录
	 * @param context
	 * @return
	 */
	List<Game> findAllRankTimeGame(Context context);
	
	/**
	 * 固定时间模式下,插入一跳新的排行榜记录
	 * @param game
	 * @param context
	 */
	void insertNewRankTimeGame(Game game,Context context);

	/**
	 * 固定时间模式下,根据编号删除一条排行榜记录
	 * @param delId
	 * @param context
	 */
	void deleteRankTimeGame(Integer delId, Context context);
	
	//Integer findLastRankIdByGameModel(GameModelEnum model,Context context);
}
