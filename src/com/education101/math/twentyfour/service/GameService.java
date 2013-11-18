package com.education101.math.twentyfour.service;


import java.util.List;

import com.education101.math.twentyfour.domain.Game;

import android.content.Context;

public interface GameService {

	/**
	 * 创建一个新游戏
	 * @param gameModel
	 * @return
	 */
	public Game createGame(String gameModel);

	
	/**
	 * 保存游戏结果
	 * @param context
	 * @param game
	 * @return 1 成功进入排行榜，-1未能进入排行榜
	 */
	public int saveRecord(Context context,Game game);

	/**
	 * 加载固定题量排行榜
	 * @return
	 */
	public List<Game> loadRankCountList(Context context);

	/**
	 * 加载固定时间排行榜
	 * @return
	 */
	public List<Game> loadRankTimeList(Context context);
}
