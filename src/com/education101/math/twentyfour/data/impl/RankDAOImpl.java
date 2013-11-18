package com.education101.math.twentyfour.data.impl;

import java.util.ArrayList;
import java.util.List;

import com.education101.math.twentyfour.data.RankDAO;
import com.education101.math.twentyfour.domain.Game;
import com.education101.math.twentyfour.util.DatabaseHelper;
import com.education101.math.twentyfour.util.DateUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RankDAOImpl implements RankDAO{
	private final static String TAG = "NO.24";
	
	//�̶�����ģʽ,��ȡ�������а��¼
	public List<Game> findAllRankCountGame(Context context) {
		List<Game> rankGames = new ArrayList<Game>();
		//ȡ���������м�¼		
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getReadableDatabase();	
		String [] columns = {"id","create_date","max_count","waste_time"};
		Cursor result = db.query(DatabaseHelper.RANK_COUNT_TABLE_NAME,
				columns, null, null, null, null, "waste_time asc");
		Log.i(TAG, "���ݿ��CountĿǰ�м�¼��>>"+result.getCount());
		Log.i(TAG, "��ʼ������Count��¼>>...");
		result.moveToFirst();
		while (!result.isAfterLast()) { 
	        int id=result.getInt(0); 
	        String createdate=result.getString(1); 
	        Integer max_count = result.getInt(2); 
	        Integer waste_time = result.getInt(3);
	        rankGames.add(new Game(id,createdate,waste_time));	        
	        result.moveToNext(); 
	        Log.i(TAG, "��¼>>���>"+id+"-MAX_COUNT>"+max_count+"-TIME>"+waste_time+"-DATE>"+createdate);
	    } 
		result.close();
		db.close();
		Log.i(TAG, "������Count��¼����>>...");
		return rankGames;
	}

	//�̶�����ģʽ,����һ���µ����а��¼
	public void insertNewRankCountGame(Game game, Context context) {
		Log.i(TAG, "׼������COUNT�����¼...");
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues conVals = new ContentValues();
		conVals.put("create_date", DateUtil.dateToString(game.getGameDate()));
		conVals.put("max_count",  Game.MAX_COUNT);
		conVals.put("waste_time", game.getGameTime());
		db = dbHelper.getWritableDatabase();
		db.insert(DatabaseHelper.RANK_COUNT_TABLE_NAME, null, conVals);					
		db.close();
		Log.i(TAG, "׼������COUNT�����¼���...");    
	}

	//�̶�����ģʽ,���ݼ�¼���ɾ��һ�����а��¼
	public void deleteRankCountGame(Integer gameId, Context context) {
		Log.i(TAG, "׼������COUNTɾ����¼,��¼ID:"+gameId+"...");
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(DatabaseHelper.RANK_COUNT_TABLE_NAME, "id=?", new String[]{String.valueOf(gameId)});
		db.close();
		Log.i(TAG, "����COUNTɾ����¼�ɹ�,��¼ID:"+gameId+"...");
	}
	
	//�̶�ʱ��ģʽ��,��ȡ�������а��¼
	public List<Game> findAllRankTimeGame(Context context) {
		List<Game> rankGames = new ArrayList<Game>();
		//ȡ���������м�¼		
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getReadableDatabase();	
		String [] columns = {"id","create_date","max_time","sub_count"};
		Cursor result = db.query(DatabaseHelper.RANK_TIME_TABLE_NAME,
				columns, null, null, null, null, "sub_count desc");
		Log.i(TAG, "���ݿ��TimeĿǰ�м�¼��>>"+result.getCount());
		Log.i(TAG, "��ʼ������Time��¼>>...");
		result.moveToFirst();
		while (!result.isAfterLast()) { 
	        int id = result.getInt(0); 
	        String  createdate = result.getString(1); 
	        Integer max_time   = result.getInt(2); 
	        Integer sub_count  = result.getInt(3);	        
	        Game game = new Game();
	        game.setGameId(id);
	        game.setGameRightTime(sub_count);
	        game.setGameDate(DateUtil.stringToDate(createdate));
	        rankGames.add(game);	        
	        result.moveToNext(); 
	        Log.i(TAG, "��¼>>���>"+id+"-MAX_TIME>"+max_time+"-SUB_COUNT>"+sub_count+"-DATE>"+createdate);
	    } 
		result.close();
		db.close();
		Log.i(TAG, "������Time��¼����>>...");
		return rankGames;
	}

	//�̶�ʱ��ģʽ��,����һ���µ����а��¼
	public void insertNewRankTimeGame(Game game, Context context) {
		Log.i(TAG, "׼������TIME�����¼...");
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues conVals = new ContentValues();
		conVals.put("create_date", DateUtil.dateToString(game.getGameDate()));
		conVals.put("max_time",  Game.MAX_TIME);
		conVals.put("sub_count", game.getGameRightTime());
		db = dbHelper.getWritableDatabase();
		db.insert(DatabaseHelper.RANK_TIME_TABLE_NAME, null, conVals);					
		db.close();
		Log.i(TAG, "׼������TIME�����¼���...");   
	}

	//�̶�ʱ��ģʽ��,���ݱ��ɾ��һ�����а��¼
	public void deleteRankTimeGame(Integer gameId, Context context) {
		Log.i(TAG, "׼������TIMEɾ����¼,��¼ID:"+gameId+"...");
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(DatabaseHelper.RANK_TIME_TABLE_NAME, "id=?", new String[]{String.valueOf(gameId)});
		db.close();
		Log.i(TAG, "����TIMEɾ����¼�ɹ�,��¼ID:"+gameId+"...");
	}
}
