package com.education101.math.twentyfour.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.education101.math.twentyfour.domain.Game;
import com.education101.math.twentyfour.service.GameService;
import com.education101.math.twentyfour.service.impl.GameServiceImpl;
import com.education101.math.twentyfour.util.DateUtil;

import  com.education101.math.twentyfour.R;
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

public class RankCountActivity extends ListActivity {
	private final static String TAG = "NO.24";	
	private ProgressDialog progressDialog;
	
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		new LoadDataTask().execute(0);
	}
	//菜单
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();	    
	    inflater.inflate(R.menu.rank_menu, menu);
	    return true;
	}
	
	//选中菜单处理
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "排行榜固定题数模式,菜单Item:"+item.getItemId()+"-"+item.getTitle());
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
	
	//退出应用程序
	private void closeApp(){
		//关闭应用程序
		Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
        System.exit(0);
	}
	
	//加载数据任务
	class LoadDataTask extends AsyncTask<Integer,Integer,Integer>{
		private GameService gameService = new GameServiceImpl();
		private SimpleAdapter listItemAdapter  = null;

		protected Integer doInBackground(Integer... params) {
			List<Game> gameList = gameService.loadRankCountList(RankCountActivity.this);
			//生成动态数组，加入数据  
	        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	        //列表标题
	        HashMap<String, Object> titleMap = new HashMap<String, Object>();
	        titleMap.put("rank_id","名次");
	        titleMap.put("rank_time_or_count", "答题用时");  
	        titleMap.put("rank_date", "创建时间");
	        listItem.add(titleMap);
	        for(int i=1;i<=gameList.size();i++){  
	        	Game game = gameList.get(i-1);
	        	HashMap<String, Object> map = new HashMap<String, Object>();  
	            map.put("rank_id","第"+i+"名");
	            map.put("rank_time_or_count", game.getGameTime()+"秒");  
	            map.put("rank_date", DateUtil.dateToString(game.getGameDate()));  
	            listItem.add(map);  
	        } 	        
	        //生成适配器的Item和动态数组对应的元素  
	        listItemAdapter = new SimpleAdapter(RankCountActivity.this,listItem,//数据源   
	            R.layout.rank_time_or_count,//ListItem的XML实现  
	            new String[] {"rank_id","rank_time_or_count", "rank_date"},
	            new int[] {R.id.rank_id,R.id.rank_time_or_count,R.id.rank_date}  
	        );
	        if(gameList.size()<=0)//如果没有记录
				return -1;
			return 1;
		}
		
		protected void onPreExecute() {
			Log.i(TAG, "准备加载固定题数排行榜数据,稍后...");
			progressDialog = new ProgressDialog(RankCountActivity.this); 
			progressDialog.setMessage("正在努力拉取数据中...");  
			progressDialog.show(); 
			super.onPreExecute();
		}
		
		protected void onPostExecute(Integer flag) {
			if(flag == -1)
				Toast.makeText(RankCountActivity.this, "还没有人上榜哦.", Toast.LENGTH_LONG).show();
			RankCountActivity.this.setListAdapter(listItemAdapter);
			if(progressDialog != null)  
	            progressDialog.dismiss();
			super.onPostExecute(flag);
		}
	}	
}
