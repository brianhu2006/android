package com.education101.math.twentyfour.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * ���ݿ���ʹ�����
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
		Log.i(TAG, "�������ݿ�,��ʼ������:"+RANK_TIME_TABLE_NAME);
		StringBuffer sqlCreateTimeTb = new StringBuffer();
		sqlCreateTimeTb.append("create table ").append(RANK_TIME_TABLE_NAME)
		   .append("(id integer primary key autoincrement,")		   
		   .append(" create_date  TIMESTAMP,")//����ʱ��
		   .append(" max_time integer,")      //ģʽʱ��
		   .append(" sub_count integer);");	  //�������
		db.execSQL(sqlCreateTimeTb.toString());
		Log.i(TAG, "��"+RANK_TIME_TABLE_NAME+"�������");
		Log.i(TAG, "�������ݿ�,��ʼ������:"+RANK_COUNT_TABLE_NAME);
		StringBuffer sqlCreateCountTb = new StringBuffer();
		sqlCreateCountTb.append("create table ").append(RANK_COUNT_TABLE_NAME)
		   .append("(id integer primary key autoincrement,")		   
		   .append(" create_date  TIMESTAMP,") //����ʱ��
		   .append(" max_count integer,")      //�������
		   .append(" waste_time integer);");   //����ʱ��   
		db.execSQL(sqlCreateCountTb.toString());
		Log.i(TAG, "��"+RANK_COUNT_TABLE_NAME+"�������");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("drop table "+RANK_TIME_TABLE_NAME);
		//db.execSQL("drop table "+RANK_COUNT_TABLE_NAME);
	}	
	
}
