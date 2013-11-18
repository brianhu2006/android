package com.education101.math.twentyfour.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.education101.math.twentyfour.domain.Game;
import com.education101.math.twentyfour.service.GameService;
import com.education101.math.twentyfour.service.impl.GameServiceImpl;
import com.education101.math.twentyfour.util.DateUtil;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import  com.education101.math.twentyfour.R;


public class RankTimeActivity extends ListActivity {
	private final static String TAG = "NO.24";	
	private ProgressDialog progressDialog;
	
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		new LoadDataTask().execute(0);		
	}
	
	//�˵�
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();	    
	    inflater.inflate(R.menu.rank_menu, menu);
	    return true;
	}
	
	//ѡ�в˵�����
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "���а�̶�ʱ��ģʽ,�˵�Item:"+item.getItemId()+"-"+item.getTitle());
		switch (item.getItemId()) {
		case R.id.reload:
			new LoadDataTask().execute(0);
			break;
		case R.id.quit:
			closeApp();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
		
	//�˳�Ӧ�ó���
	private void closeApp(){
		//�ر�Ӧ�ó���
		Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
        System.exit(0);
	}
	
	
	//������������
	class LoadDataTask extends AsyncTask<Integer,Integer,Integer>{
		private GameService gameService = new GameServiceImpl();
		private SimpleAdapter listItemAdapter  = null;
		
		protected Integer doInBackground(Integer... params) {
			List<Game> gameList = this.gameService.loadRankTimeList(RankTimeActivity.this);	
			//���ɶ�̬���飬��������  
	        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	        //�б����
	        HashMap<String, Object> titleMap = new HashMap<String, Object>();
	        titleMap.put("rank_id","����");
	        titleMap.put("rank_time_or_count", "��ȷ����");  
	        titleMap.put("rank_date", "����ʱ��");
	        listItem.add(titleMap);
	        for(int i=1;i<=gameList.size();i++){  
	        	Game game = gameList.get(i-1);
	        	HashMap<String, Object> map = new HashMap<String, Object>();  
	            map.put("rank_id","��"+i+"��");
	            map.put("rank_time_or_count", game.getGameRightTime()+"��");  
	            map.put("rank_date", DateUtil.dateToString(game.getGameDate()));  
	            listItem.add(map);  
	        } 
	        //������������Item�Ͷ�̬�����Ӧ��Ԫ��  
	        listItemAdapter = new SimpleAdapter(RankTimeActivity.this,listItem,//����Դ   
	            R.layout.rank_time_or_count,//ListItem��XMLʵ��  
	            new String[] {"rank_id","rank_time_or_count", "rank_date"},
	            new int[] {R.id.rank_id,R.id.rank_time_or_count,R.id.rank_date}  
	        );
	        if(gameList.size()<=0) 
	        	return -1;
			return 1;
		}
		
		protected void onPreExecute() {
			Log.i(TAG, "׼�����ع̶�ʱ�����а�����,�Ժ�...");
			progressDialog = new ProgressDialog(RankTimeActivity.this); 
			progressDialog.setMessage("����Ŭ����ȡ������...");  
			progressDialog.show(); 
			super.onPreExecute();
		}
		
		protected void onPostExecute(Integer result) {			
			if(result == -1)
				Toast.makeText(RankTimeActivity.this, "��û�����ϰ�Ŷ.", Toast.LENGTH_LONG).show();
			RankTimeActivity.this.setListAdapter(listItemAdapter);
			if(progressDialog != null)  
	            progressDialog.dismiss();
			super.onPostExecute(result);
		}
	}	
}
