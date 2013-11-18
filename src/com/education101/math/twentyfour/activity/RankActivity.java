package com.education101.math.twentyfour.activity;

import  com.education101.math.twentyfour.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;


public class RankActivity extends TabActivity  {
		
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);			
		Resources res = getResources();
		TabHost tabHost = getTabHost();  
	    TabHost.TabSpec spec;  
	    Intent intent; 
	   
	    intent = new Intent().setClass(this, RankTimeActivity.class);
	    spec = tabHost.newTabSpec("rankTime").setIndicator("争分夺秒",
                res.getDrawable(R.drawable.tab_rank_time)).setContent(intent);
	   
	    tabHost.addTab(spec);
	   
	    intent = new Intent().setClass(this, RankCountActivity.class);
	    spec = tabHost.newTabSpec("rankCount").setIndicator("题海无边",
                res.getDrawable(R.drawable.tab_rank_count)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    tabHost.setCurrentTab(0);
	}
}
