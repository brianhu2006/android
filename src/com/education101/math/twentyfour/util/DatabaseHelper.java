package com.education101.math.twentyfour.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库访问工具类
 * @author Tonlin
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private final static String TAG = "NO.24";	
	public   static   final  String DATABASE_NAME =  "com.education101.math.twentyfour.db";   
	public   static   final  int    DATABASE_VERSION =  1 ;   
	public   static   final  String RANK_TIME_TABLE_NAME =  "rank_time_list" ; 
	public   static   final  String RANK_COUNT_TABLE_NAME =  "rank_count_list" ; 
	
	public DatabaseHelper(Context context) {  
		super (context, DATABASE_NAME,  null , DATABASE_VERSION);   
	} 
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "创建数据库,开始创建表:"+RANK_TIME_TABLE_NAME);
		StringBuffer sqlCreateTimeTb = new StringBuffer();
		sqlCreateTimeTb.append("create table ").append(RANK_TIME_TABLE_NAME)
		   .append("(id integer primary key autoincrement,")		   
		   .append(" create_date  TIMESTAMP,")//创建时间
		   .append(" max_time integer,")      //模式时间
		   .append(" sub_count integer);");	  //答对题数
		db.execSQL(sqlCreateTimeTb.toString());
		Log.i(TAG, "表"+RANK_TIME_TABLE_NAME+"创建完毕");
		Log.i(TAG, "创建数据库,开始创建表:"+RANK_COUNT_TABLE_NAME);
		StringBuffer sqlCreateCountTb = new StringBuffer();
		sqlCreateCountTb.append("create table ").append(RANK_COUNT_TABLE_NAME)
		   .append("(id integer primary key autoincrement,")		   
		   .append(" create_date  TIMESTAMP,") //创建时间
		   .append(" max_count integer,")      //最大题数
		   .append(" waste_time integer);");   //花费时间   
		db.execSQL(sqlCreateCountTb.toString());
		Log.i(TAG, "表"+RANK_COUNT_TABLE_NAME+"创建完毕");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("drop table "+RANK_TIME_TABLE_NAME);
		//db.execSQL("drop table "+RANK_COUNT_TABLE_NAME);
	}	
	
}
