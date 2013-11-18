package com.education101.math.twentyfour.service;


import java.util.List;

import com.education101.math.twentyfour.domain.Game;

import android.content.Context;

public interface GameService {

	/**
	 * ����һ������Ϸ
	 * @param gameModel
	 * @return
	 */
	public Game createGame(String gameModel);

	
	/**
	 * ������Ϸ���
	 * @param context
	 * @param game
	 * @return 1 �ɹ��������а�-1δ�ܽ������а�
	 */
	public int saveRecord(Context context,Game game);

	/**
	 * ���ع̶��������а�
	 * @return
	 */
	public List<Game> loadRankCountList(Context context);

	/**
	 * ���ع̶�ʱ�����а�
	 * @return
	 */
	public List<Game> loadRankTimeList(Context context);
}
