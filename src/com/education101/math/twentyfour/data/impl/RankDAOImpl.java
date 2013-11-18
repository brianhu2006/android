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
	
	//固定题数模式,获取所有排行榜记录
	public List<Game> findAllRankCountGame(Context context) {
		List<Game> rankGames = new ArrayList<Game>();
		//取出所有已有记录		
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getReadableDatabase();	
		String [] columns = {"id","create_date","max_count","waste_time"};
		Cursor result = db.query(DatabaseHelper.RANK_COUNT_TABLE_NAME,
				columns, null, null, null, null, "waste_time asc");
		Log.i(TAG, "数据库表Count目前有记录数>>"+result.getCount());
		Log.i(TAG, "开始遍历表Count记录>>...");
		result.moveToFirst();
		while (!result.isAfterLast()) { 
	        int id=result.getInt(0); 
	        String createdate=result.getString(1); 
	        Integer max_count = result.getInt(2); 
	        Integer waste_time = result.getInt(3);
	        rankGames.add(new Game(id,createdate,waste_time));	        
	        result.moveToNext(); 
	        Log.i(TAG, "记录>>编号>"+id+"-MAX_COUNT>"+max_count+"-TIME>"+waste_time+"-DATE>"+createdate);
	    } 
		result.close();
		db.close();
		Log.i(TAG, "遍历表Count记录结束>>...");
		return rankGames;
	}

	//固定题数模式,插入一条新的排行榜记录
	public void insertNewRankCountGame(Game game, Context context) {
		Log.i(TAG, "准备往表COUNT插入记录...");
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues conVals = new ContentValues();
		conVals.put("create_date", DateUtil.dateToString(game.getGameDate()));
		conVals.put("max_count",  Game.MAX_COUNT);
		conVals.put("waste_time", game.getGameTime());
		db = dbHelper.getWritableDatabase();
		db.insert(DatabaseHelper.RANK_COUNT_TABLE_NAME, null, conVals);					
		db.close();
		Log.i(TAG, "准备往表COUNT插入记录完成...");    
	}

	//固定题数模式,根据记录编号删除一条排行榜记录
	public void deleteRankCountGame(Integer gameId, Context context) {
		Log.i(TAG, "准备往表COUNT删除记录,记录ID:"+gameId+"...");
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(DatabaseHelper.RANK_COUNT_TABLE_NAME, "id=?", new String[]{String.valueOf(gameId)});
		db.close();
		Log.i(TAG, "往表COUNT删除记录成功,记录ID:"+gameId+"...");
	}
	
	//固定时间模式下,获取所有排行榜记录
	public List<Game> findAllRankTimeGame(Context context) {
		List<Game> rankGames = new ArrayList<Game>();
		//取出所有已有记录		
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getReadableDatabase();	
		String [] columns = {"id","create_date","max_time","sub_count"};
		Cursor result = db.query(DatabaseHelper.RANK_TIME_TABLE_NAME,
				columns, null, null, null, null, "sub_count desc");
		Log.i(TAG, "数据库表Time目前有记录数>>"+result.getCount());
		Log.i(TAG, "开始遍历表Time记录>>...");
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
	        Log.i(TAG, "记录>>编号>"+id+"-MAX_TIME>"+max_time+"-SUB_COUNT>"+sub_count+"-DATE>"+createdate);
	    } 
		result.close();
		db.close();
		Log.i(TAG, "遍历表Time记录结束>>...");
		return rankGames;
	}

	//固定时间模式下,插入一跳新的排行榜记录
	public void insertNewRankTimeGame(Game game, Context context) {
		Log.i(TAG, "准备往表TIME插入记录...");
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues conVals = new ContentValues();
		conVals.put("create_date", DateUtil.dateToString(game.getGameDate()));
		conVals.put("max_time",  Game.MAX_TIME);
		conVals.put("sub_count", game.getGameRightTime());
		db = dbHelper.getWritableDatabase();
		db.insert(DatabaseHelper.RANK_TIME_TABLE_NAME, null, conVals);					
		db.close();
		Log.i(TAG, "准备往表TIME插入记录完成...");   
	}

	//固定时间模式下,根据编号删除一条排行榜记录
	public void deleteRankTimeGame(Integer gameId, Context context) {
		Log.i(TAG, "准备往表TIME删除记录,记录ID:"+gameId+"...");
		DatabaseHelper dbHelper = new DatabaseHelper(context);	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(DatabaseHelper.RANK_TIME_TABLE_NAME, "id=?", new String[]{String.valueOf(gameId)});
		db.close();
		Log.i(TAG, "往表TIME删除记录成功,记录ID:"+gameId+"...");
	}
}
