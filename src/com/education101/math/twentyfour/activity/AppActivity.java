package com.education101.math.twentyfour.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import  com.education101.math.twentyfour.R;
/**
 * 
 * @author hubing
 *
 */

public class AppActivity extends Activity implements OnClickListener{
    
	private final static String TAG = "NO.24";
	
	private Button  btnStartGame;  //start Game
	private Button  btnRankList;   //Player List
	private Spinner spiGameModel;  //game model
	private String  gameModel;     //game model
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);
        //Add listener for start game
        btnStartGame = (Button)findViewById(R.id.btnStartGame);
        btnStartGame.setOnClickListener(this);
     
        //Add listener for player
        btnRankList = (Button)findViewById(R.id.btnRankList);
        btnRankList.setOnClickListener(this);
        
        //game model selection
        spiGameModel = (Spinner)findViewById(R.id.spiGameModel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.gameModel, android.R.layout.simple_spinner_item);       
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiGameModel.setAdapter(adapter);
        spiGameModel.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {				
				//Retrieve game model list		
				gameModel = spiGameModel.getSelectedItem().toString();
				Log.i(TAG, "Use choose game model["+gameModel+"]");
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				
			}        	
        });
    }

    //Button Handling
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStartGame:
			startGame();
			break;		
		case R.id.btnRankList:
			viewRank();
			break;
		default:
			break;
		}		
	}
	
	//Play List
	private void viewRank() {
		Intent intent = new Intent();
		//If we haven't setup this parameter, we can't close this activity
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(AppActivity.this, RankActivity.class);	
		this.startActivity(intent);
	}

	//Start Game
	private void startGame(){		
		Log.i(TAG,"start new game...");		
		Log.i(TAG, "selected game model>> "+gameModel);
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(AppActivity.this, MainActivity.class);		
		intent.putExtra("gameModel", gameModel);
		this.startActivity(intent);
		
	}
}