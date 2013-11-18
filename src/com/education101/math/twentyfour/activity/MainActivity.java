package com.education101.math.twentyfour.activity;


import java.util.Date;
import java.util.List;

import com.education101.math.twentyfour.domain.Game;
import com.education101.math.twentyfour.domain.Subject;
import com.education101.math.twentyfour.service.GameService;
import com.education101.math.twentyfour.service.impl.GameServiceImpl;
import com.education101.math.twentyfour.util.AdvancedCountdownTimer;
import com.education101.math.twentyfour.util.ComputeUtil;
import com.education101.math.twentyfour.util.GameModelEnum;
import com.education101.math.twentyfour.util.GameStatusEnum;
import com.education101.math.twentyfour.util.KeyEnum;
import com.education101.math.twentyfour.util.KeyManager;
import com.education101.math.twentyfour.util.SubStatusEnum;
import com.education101.math.twentyfour.util.TwentyFour;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import  com.education101.math.twentyfour.R;


public class MainActivity extends Activity implements OnClickListener{
	
	private final static String TAG = "NO.24";
	private GameService gameService = new GameServiceImpl();
	private Game   game;                    //运行中的游戏
	//private boolean changedCard = false;
	//private CalculateThread calThread;    //计算题目答案线程		
	private AnswerTime ansTimeThread;       //计时线程
	private ControlCountDownTimer cdTimer;  //倒计时器
	private InitTask  initGameTask;         //初始化游戏任务
	private CalculateTask calTask;          //计算答案任务
	
/*	private ImageView firstCard; //第一张卡
	private ImageView secondCard;//第二张卡
	private ImageView thirdCard; //第三张卡
	private ImageView fourCard;  //第四张卡
*/	private Button btnClear;    //清空
	private Button btnSubmit;   //确定
	private Button btnAnswer;   //查看答案
	private Button btnNoAnswer; //无答案
	private Button btnNextSub;  //下一题
	private Button btnFirstNum; //第一个随机数
	private Button btnSecondNum;//第二个随机数	
	private Button btnThirdNum; //第三个随机数
	private Button btnFourNum;  //第四个随机数
	private Button btnLeftBracket;   //运算符左括号
	private Button btnRightBracket;  //运算符右括号
	private Button btnAdd;           //运算符加号
	private Button btnSubtract;      //运算符减号
	private Button btnMultiply;      //运算符乘号
	private Button btnDivide;        //运算符除号
	private TableRow trRemainTime;   //剩余时间行(控制其是否可见)
	private TextView remainTime;     //时间模式倒计时
	private TableRow trRemainSub;    //剩余题目行(控制其是否可见)
	private TextView remainSubject;  //题目模式剩余题数	
	private TextView wasteTime;      //耗时
	private TextView rightSubject;   //做对题数
	private TextView errorSubject;   //做错题数
	private EditText inputExp;       //表达式输入框
		
	private ProgressDialog progressDialog;//等待进度对话框
	
	//--------------------------------------------------------------------------//
	//-------------------         Activity事件处理                          ------------------------//
	//--------------------------------------------------------------------------//
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        //初始化组件
        initComponent(); 
        //禁用软键盘
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
		KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(getPackageName());
        keyguardLock.disableKeyguard();
    }

	protected void onResume() {		
		super.onResume();
		Log.i(TAG, "MainActivity onResume...");
		//执行初始化游戏任务
        this.startInitGameTask();
	}
	
	//界面不可见
	protected void onPause() {		
		super.onPause();
		Log.i(TAG, "MainActivity onPause,界面不可见...");
		//停掉在本Activity开启的线程和任务
		this.stopInitGameTask();
		this.stopAnswerTimeThread();
		this.stopCalSubThread();
		this.stopControlSubTimer();
	}
    
	//菜单
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();	    
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	//选中菜单处理
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "MainActivity,菜单Item:"+item.getItemId()+"-"+item.getTitle());
		switch (item.getItemId()) {
		case R.id.newGame:
			this.startInitGameTask();//开始初始化游戏
			break;
		case R.id.about:
			showAbout();
			break;
		case R.id.pause:
			if(game.getGameStatus().equals(GameStatusEnum.START)){
				item.setIcon(R.drawable.menu_play);
				item.setTitle(R.string.play);
			}else if(game.getGameStatus().equals(GameStatusEnum.PAUSE)){
				item.setIcon(R.drawable.menu_pause);
				item.setTitle(R.string.pause);
			}
			pauseGame();//暂停游戏
			break;
		case R.id.quit:
			closeApp();//退出程序
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	//--------------------------------------------------------------------------//
	//-------------------         界面组件声明/注册回调函数/    ------------------------//
	//--------------------------------------------------------------------------//
	
	//初始化组件
	private void initComponent(){
		//卡片
		/*firstCard = (ImageView)findViewById(R.id.firstCard);
		secondCard = (ImageView)findViewById(R.id.secondCard);
		thirdCard = (ImageView)findViewById(R.id.thirdCard);
		fourCard = (ImageView)findViewById(R.id.fourCard);*/
		//获取按钮
        btnClear = (Button)findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnAnswer = (Button)findViewById(R.id.btnAnswer);
        btnAnswer.setOnClickListener(this);
        btnNoAnswer = (Button)findViewById(R.id.btnNoAnswer);       
        btnNoAnswer.setOnClickListener(this);
        btnNextSub = (Button)findViewById(R.id.btnNextSub);       
        btnNextSub.setOnClickListener(this);
        btnFirstNum = (Button)findViewById(R.id.btnFirstNum);
        btnFirstNum.setOnClickListener(this);
        btnSecondNum = (Button)findViewById(R.id.btnSecondNum);
        btnSecondNum.setOnClickListener(this);
        btnThirdNum = (Button)findViewById(R.id.btnThirdNum);
        btnThirdNum.setOnClickListener(this);
        btnFourNum = (Button)findViewById(R.id.btnFourNum);
        btnFourNum.setOnClickListener(this);
        btnLeftBracket = (Button)findViewById(R.id.btnLeftBracket);
        btnLeftBracket.setOnClickListener(this);
        btnRightBracket = (Button)findViewById(R.id.btnRightBracket);       
        btnRightBracket.setOnClickListener(this);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnSubtract = (Button)findViewById(R.id.btnSubtract);
        btnSubtract.setOnClickListener(this);
        btnMultiply = (Button)findViewById(R.id.btnMultiply);
        btnMultiply.setOnClickListener(this);
        btnDivide = (Button)findViewById(R.id.btnDivide);
        btnDivide.setOnClickListener(this);
        //获取文本对象
        trRemainTime = (TableRow)findViewById(R.id.trRemainTime);
        remainTime = (TextView)findViewById(R.id.remainTime);
        trRemainSub = (TableRow)findViewById(R.id.trRemainSub);
        remainSubject = (TextView)findViewById(R.id.remainSub); 
        wasteTime = (TextView)findViewById(R.id.wasteTime); 
        rightSubject = (TextView)findViewById(R.id.rightSubject);        
        errorSubject = (TextView)findViewById(R.id.errorSubject); 
        inputExp = (EditText)findViewById(R.id.inputExp);        
	}
	
	//按钮单击事件处理
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnFirstNum://第一个数
			KeyManager.setEditText(inputExp, KeyEnum.DIGIT,btnFirstNum);
			break;
		case R.id.btnSecondNum://第二个数
			KeyManager.setEditText(inputExp, KeyEnum.DIGIT,btnSecondNum);
			break;
		case R.id.btnThirdNum://第三个数
			KeyManager.setEditText(inputExp, KeyEnum.DIGIT,btnThirdNum);
			break;
		case R.id.btnFourNum://第四个数
			KeyManager.setEditText(inputExp, KeyEnum.DIGIT,btnFourNum);
			break;
		case R.id.btnAdd://加
			KeyManager.setEditText(inputExp, KeyEnum.OPERATOR,btnAdd);
			break;
		case R.id.btnSubtract://减	
			KeyManager.setEditText(inputExp, KeyEnum.OPERATOR,btnSubtract);
			break;
		case R.id.btnMultiply://乘
			KeyManager.setEditText(inputExp, KeyEnum.OPERATOR,btnMultiply);
			break;
		case R.id.btnDivide://除
			KeyManager.setEditText(inputExp, KeyEnum.OPERATOR,btnDivide);
			break;
		case R.id.btnLeftBracket://左括号
			KeyManager.setEditText(inputExp, KeyEnum.LEFT_BRACKET,btnLeftBracket);
			break;
		case R.id.btnRightBracket://右括号
			KeyManager.setEditText(inputExp, KeyEnum.RIGHT_BRACKET,btnRightBracket);
			break;	
		case R.id.btnSubmit: //提交答案
			submitAnswer(inputExp.getText());
			break;
		case R.id.btnClear: //清空输入
			inputExp.setText("");
			btnFirstNum.setEnabled(true);
			btnSecondNum.setEnabled(true);
			btnThirdNum.setEnabled(true);
			btnFourNum.setEnabled(true);
			break;		
		case R.id.btnAnswer:  //查看答案
			showAnswer();
			break;			
		case R.id.btnNoAnswer://无答案
			noAnswer();
			break;
		case R.id.btnNextSub: //下一题
			nextSub();
			break;
		default:
			break;
		}
	}
	
	//--------------------------------------------------------------------------//
	//-------------------         功能处理方法定义                                 ------------------------//
	//--------------------------------------------------------------------------//
	
	//下一题
	private void nextSub() {
		Integer i = game.getCurrentSubIndex();
		Subject cur = game.getGameSub().get(game.getCurrentSubIndex()-1);
		if(game.getGameStatus().equals(GameStatusEnum.STOP)){
			Toast.makeText(getApplicationContext(), "游戏已结束...",Toast.LENGTH_SHORT).show();	
			return;
		}else if(game.getGameStatus().equals(GameStatusEnum.PAUSE)){
			Toast.makeText(getApplicationContext(), "游戏已暂停...",Toast.LENGTH_SHORT).show();	
			return;
		}else {//游戏正在进行
			Toast.makeText(getApplicationContext(), "Jump Subject"+(i+1)+"",Toast.LENGTH_SHORT).show();
			if(cur.getSubStatus().equals(SubStatusEnum.UNANSWER)){//更新跳过题数
				game.setGameSkipTime(game.getGameSkipTime()+1);
			}
		}
		if(game.getGameModel().equals(GameModelEnum.PLAY_IN_COUNT)){
			//获取当前剩余题量
			Integer count = Integer.parseInt(remainSubject.getText().toString());
			if(count == 0){ //没有剩余题目
				Log.i(TAG, "在固定题数模式下结束答题..");
                MainActivity.this.btnNextSub.setText("下一题");
				//停止计时
				MainActivity.this.stopAnswerTimeThread();
				game.setGameDate(new Date());//设置游戏完成时间
				finishGame();	
				return;
			}
			if(count >= 1){	//		
				if(count == 1)
					btnNextSub.setText("Complete");
				remainSubject.setText(String.valueOf(count-1));
			}			
		}
		//新题目
		Subject sub = new Subject();
		game.getGameSub().add(sub);
		updateRandomNums(sub);				
		//更新题目索引		
		game.setCurrentSubIndex(game.getCurrentSubIndex()+1);			
		//清空输入框并设置数字按钮可用
		inputExp.setText("");
		btnFirstNum.setEnabled(true);
		btnSecondNum.setEnabled(true);
		btnThirdNum.setEnabled(true);
		btnFourNum.setEnabled(true);
		//开启新线程开始计算当前题目答案
        MainActivity.this.startCalSubThread();//开始计算  
	}

	//无答案
	private void noAnswer() {
		Log.i(TAG, "用户选择题目>>"+game.getCurrentSubIndex()+"为无答案");
		//游戏状态检查
		if(game.getGameStatus().equals(GameStatusEnum.STOP)){
			Toast.makeText(getApplicationContext(), "Game Over...",Toast.LENGTH_SHORT).show();	
			return;
		}else if(game.getGameStatus().equals(GameStatusEnum.PAUSE)){
			Toast.makeText(getApplicationContext(), "Game Suspend...",Toast.LENGTH_SHORT).show();	
			return;
		}
		Subject correntSub = game.getGameSub().get(game.getCurrentSubIndex()-1);
		if(!correntSub.isSubCalculated()){
			Log.i(TAG, "题目还在计算中,请稍后...");
			Toast.makeText(getApplicationContext(), "It is under calculation, please wait...",Toast.LENGTH_SHORT).show();	
		}else{	
			List<String> exps = correntSub.getSubRightExp();
			if(exps.size() == 0){//答对
				Toast.makeText(getApplicationContext(), "Congratulation，The Answer is correct...",Toast.LENGTH_SHORT).show();
				if(correntSub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
					correntSub.setSubStatus(SubStatusEnum.RIGHT);//该题答对
					game.setGameRightTime(game.getGameRightTime()+1);
					rightSubject.setText(String.valueOf(game.getGameRightTime()));					
				}
			}else{
				Toast.makeText(getApplicationContext(), "I am sorry. The answer is wrong...",Toast.LENGTH_SHORT).show();
				if(correntSub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
					correntSub.setSubStatus(SubStatusEnum.ERROR);//该题答错
					game.setGameErrorTime(game.getGameErrorTime()+1);
					errorSubject.setText(String.valueOf(game.getGameErrorTime()));					
				}
			}			
		}
	}

	//查看答案
	private void showAnswer() {
		Log.i(TAG, "用户选择查看答案...");
		//游戏状态检查
		if(game.getGameStatus().equals(GameStatusEnum.STOP)){
			Toast.makeText(getApplicationContext(), "Game Over...",Toast.LENGTH_SHORT).show();	
			return;
		}else if(game.getGameStatus().equals(GameStatusEnum.PAUSE)){
			Toast.makeText(getApplicationContext(), "Game Suspend...",Toast.LENGTH_SHORT).show();	
			return;
		}
		Subject correntSub = game.getGameSub().get(game.getCurrentSubIndex()-1);
		boolean calculated = correntSub.isSubCalculated();
		if(!calculated){ //如果系统还未计算完成
			Log.i(TAG, "题目还在计算中,请稍后...");
			Toast.makeText(getApplicationContext(), "It is under calculation, please wait a moment...",Toast.LENGTH_SHORT).show();	
		}else{		
			StringBuffer result = new StringBuffer();			
			List<String> exps = correntSub.getSubRightExp();
			if(exps.size() ==0) 
				result.append("No ansewr for this subject");	
			else{
				result.append("For Example:").append(exps.get(0));
			}
			new AlertDialog.Builder(this) 
		    .setTitle("Correct Answer") 
		    .setMessage(result)
		    .setPositiveButton("I Got it", new DialogInterface.OnClickListener(){ 
                public void onClick(DialogInterface dialoginterface, int i){ }
            }).show();	
			//未作答就查看答案视为答错
			if(correntSub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
				correntSub.setSubStatus(SubStatusEnum.ERROR);//该题答错
				game.setGameErrorTime(game.getGameErrorTime()+1);
				errorSubject.setText(String.valueOf(game.getGameErrorTime()));	
			}			
		}	
	}

	
	//提交答案
	private void submitAnswer(CharSequence text) {
		Log.i(TAG, "用户已提交答案...");
		//游戏状态检查
		if(game.getGameStatus().equals(GameStatusEnum.STOP)){
			Toast.makeText(getApplicationContext(), "Game Over...",Toast.LENGTH_SHORT).show();	
			return;
		}else if(game.getGameStatus().equals(GameStatusEnum.PAUSE)){
			Toast.makeText(getApplicationContext(), "Game Suspend...",Toast.LENGTH_SHORT).show();	
			return;
		}
		if(text == null || text.length()==0){
			Toast.makeText(getApplicationContext(), "Please input your answer...",Toast.LENGTH_SHORT).show();	
			return;
		}
		//取得当前计算题目
		Subject correntSub = game.getGameSub().get(game.getCurrentSubIndex()-1);
		String exp = inputExp.getText().toString();	//用户输入的表达式(输入表达式正确且不为空)
		boolean cmpFlag = false;
		try {
			cmpFlag = ComputeUtil.equalNum(exp, 24);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
			Toast.makeText(getApplicationContext(), "很遗憾,表达式有误...",Toast.LENGTH_SHORT).show();
			return;
		}
		if(cmpFlag){//计算正确
			Log.i(TAG, "用户输入的表达式与系统答案匹配成功>>题目:"+game.getCurrentSubIndex());
			Toast.makeText(getApplicationContext(), "Congratulation，回答正确...",Toast.LENGTH_SHORT).show();
			if(correntSub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
				correntSub.setSubStatus(SubStatusEnum.RIGHT);//该题答对
				game.setGameRightTime(game.getGameRightTime()+1);
				rightSubject.setText(String.valueOf(game.getGameRightTime()));		
			}					
		}else{
			Toast.makeText(getApplicationContext(), "很遗憾，答案不正确...",Toast.LENGTH_SHORT).show();
			if(correntSub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
				correntSub.setSubStatus(SubStatusEnum.ERROR);//该题答错
				game.setGameErrorTime(game.getGameErrorTime()+1);
				errorSubject.setText(String.valueOf(game.getGameErrorTime()));	
			}
		}
	} 
	
	//打开英雄榜
	private void openRankList(){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(MainActivity.this, RankActivity.class);	
		MainActivity.this.startActivity(intent);
	}
	
	//退出应用程序
	private void closeApp(){
		Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
        System.exit(0);
	}
	
	//打开游侠关于
	private void showAbout() {
		new AlertDialog.Builder(MainActivity.this)
    	.setTitle("About")
	    .setMessage(R.string.aboutInfo)
	    .setPositiveButton("Confirm", null).show();
	}
	
	//暂停游戏
	private void pauseGame() {
		if(game.getGameStatus().equals(GameStatusEnum.START)){
			game.setGameStatus(GameStatusEnum.PAUSE);
			//暂停计时//停止计算
			if(game.getGameModel().equals(GameModelEnum.PLAY_IN_COUNT)){
				this.pauseAnswerTimeThread();//暂停计时
				this.stopCalSubThread();//停止计算题目
			}else if(game.getGameModel().equals(GameModelEnum.PLAY_IN_TIME)){				
				this.pauseControlSubTimer();//暂停倒计时
				this.stopCalSubThread();//停止计算题目
			}
		}else if(game.getGameStatus().equals(GameStatusEnum.PAUSE)){
			game.setGameStatus(GameStatusEnum.START);
			if(game.getGameModel().equals(GameModelEnum.PLAY_IN_COUNT)){
				this.restartAnswerTimeThread();	
				this.nextSub();
			}else if(game.getGameModel().equals(GameModelEnum.PLAY_IN_TIME)){
				this.restartControlSubTimer();//恢复倒计时
				this.nextSub();//进入下一题
			}
		}
	}
	
	//更新随机数显示及按钮和卡片	
	private void updateRandomNums(Subject sub){		
	/*	firstCard.setImageResource(R.drawable.card_back);
		secondCard.setImageResource(R.drawable.card_back);
		thirdCard.setImageResource(R.drawable.card_back);
		fourCard.setImageResource(R.drawable.card_back);*/
		Integer[] nums = sub.getSubNums();
		btnFirstNum.setText(String.valueOf(nums[0]));
		btnSecondNum.setText(String.valueOf(nums[1]));	
		btnThirdNum.setText(String.valueOf(nums[2]));
		btnFourNum.setText(String.valueOf(nums[3]));
		new ChangeCardThread(sub).start();//开始改变卡片
	}
	
	
	//完成、结束游戏
	private void finishGame(){    	
    	//保存记录
    	int flag = gameService.saveRecord(this,game);    	
    	//准备弹出游戏结果
    	StringBuffer sb = new StringBuffer();    	
    	sb.append("答对:").append(game.getGameRightTime()).append("\n")
    	  .append("答错:").append(game.getGameErrorTime()).append("\n")
    	  .append("跳过:").append(game.getGameSkipTime()).append("\n");
    	if(flag == 1){
    		sb.append("成功进入排行榜.");
    	}else if(flag == -1){
    		sb.append("未能进入排行榜.");
    	}
    	new AlertDialog.Builder(MainActivity.this)
    	.setTitle("本轮游戏详情")
	    .setMessage(sb.toString())
	    .setPositiveButton("不玩了", new DialogInterface.OnClickListener(){ 
            public void onClick(DialogInterface dialoginterface, int i){
            	 //关闭应用程序
            	 MainActivity.this.closeApp();
            } 
	    })
	    .setNegativeButton("再玩一次", new DialogInterface.OnClickListener(){ 
            public void onClick(DialogInterface dialoginterface, int i){
            	//执行初始化游戏任务
                MainActivity.this.startInitGameTask();
            } 
	    })
	    .setNeutralButton("英雄榜", new DialogInterface.OnClickListener(){ 
            public void onClick(DialogInterface dialoginterface, int i){ 
            	MainActivity.this.openRankList();
            } 
	    }).show();
    	MainActivity.this.game.setGameStatus(GameStatusEnum.STOP);	
	}
	
		
	//--------------------------------------------------------------------------//
	//-------------------     调用线程/任务方法定义                                  ------------------------//
	//--------------------------------------------------------------------------//
	
	//开启新线程重新倒计时(固定时间模式)	
	private  void startControlSubTimer(){
		this.stopControlSubTimer();
		cdTimer = new ControlCountDownTimer(Game.MAX_TIME*1000,1000);
		cdTimer.start();
	}
	
	//关闭倒计时器(固定时间模式)
	private  void stopControlSubTimer(){
		if(cdTimer != null)
			cdTimer.cancel();
	}
	
	//暂停倒计时器(固定时间模式)
	private void pauseControlSubTimer(){
		if(cdTimer != null){
			cdTimer.pause();
		}
	}
	
	//恢复倒计时器(固定时间模式)
	private void restartControlSubTimer(){
		if(cdTimer != null){
			cdTimer.resume();
		}
	}
	
	//开始新线程计算题目
	private  void startCalSubThread(){
		//this.stopCalSubThread();
		//calThread = new CalculateThread(game.getCurrentSubIndex()-1);
		//calThread.start();		
		//已有计算任务则取消该计算任务
		if(calTask != null)
			calTask.cancel(true);
		calTask = new CalculateTask();
		calTask.execute(0);		
	}
	
	//关闭正在计算题目的线程
	private  void stopCalSubThread(){		
		//if(calThread != null)
		//	calThread.cancel(true);
		if(calTask != null)
			calTask.cancel(true);
	}
	
	//开启任务初始化游戏
	private  void startInitGameTask(){		
		this.stopInitGameTask();
		initGameTask = new InitTask();
		initGameTask.execute(0);
	}
	
	//关闭初始化游戏任务
	private  void stopInitGameTask(){
		if(initGameTask != null){
			initGameTask.cancel(true);			
		}
	}
	
	//开启新线程进行计时计算答题时间(固定题数模式下)
	private  void startAnswerTimeThread(){
		this.stopAnswerTimeThread();
		ansTimeThread = new AnswerTime();
		ansTimeThread.start();
	}
	
	//关闭已开始的计时线程(固定题数模式下)
	private void stopAnswerTimeThread(){
		if(ansTimeThread != null)
		  ansTimeThread.cancel();
	}
	
	//暂停答题计时(固定题数模式下)
	private void pauseAnswerTimeThread(){
		if(ansTimeThread != null)
		  ansTimeThread.pause();
	}
	
	//回复答题计时(固定题数模式下)
	private void restartAnswerTimeThread(){
		if(ansTimeThread != null)
		  ansTimeThread.restart();
	}
	//--------------------------------------------------------------------------//
	//-------------------         定义任务和线程处理                             ------------------------//
	//--------------------------------------------------------------------------//
	//初始化任务
 	class InitTask extends AsyncTask<Integer,Integer,Subject>{

		protected Subject doInBackground(Integer... params) {
			Log.i(TAG, "开始初始化游戏...");
			//获取用户选择的游戏模式
			Bundle bundle = getIntent().getExtras();
	        String gameModel = (String)bundle.get("gameModel");	
	        //创建新游戏
	        game = gameService.createGame(gameModel);
	        //开启新线程开始计算当前题目答案
	        MainActivity.this.startCalSubThread();//开始计算
			Subject sub = game.getGameSub().get(0);
			return sub;
		}

		protected void onPostExecute(Subject result) {
			Log.i(TAG, "初始化游戏完毕,开始更新UI...");
			//更新随机数及其显示
		    MainActivity.this.updateRandomNums(game.getGameSub().get(0));   
	        //设置显示剩余时间还是剩余题数
	        if(game.getGameModel().equals(GameModelEnum.PLAY_IN_TIME)){
	        	remainTime.setText(String.valueOf(Game.MAX_TIME));
	        	trRemainTime.setVisibility(View.VISIBLE);
	        	MainActivity.this.startControlSubTimer();//开启新线程开始倒计时并设置显示
	        }else if(game.getGameModel().equals(GameModelEnum.PLAY_IN_COUNT)){
	        	remainSubject.setText(String.valueOf(Game.MAX_COUNT-1));
	        	trRemainSub.setVisibility(View.VISIBLE);
	        	MainActivity.this.startAnswerTimeThread();//计时开始
	        }
	        rightSubject.setText(String.valueOf(game.getGameRightTime()));
	        errorSubject.setText(String.valueOf(game.getGameErrorTime()));
	        //情况输入框并设置数字按钮可用
	        inputExp.setText("");
			btnFirstNum.setEnabled(true);
			btnSecondNum.setEnabled(true);
			btnThirdNum.setEnabled(true);
			btnFourNum.setEnabled(true);
	        if(progressDialog != null)  
                progressDialog.dismiss();
			super.onPostExecute(result);
		}
		
		protected void onPreExecute() {
			Log.i(TAG, "准备初始化游戏,提示用户稍后...");
			progressDialog = new ProgressDialog(MainActivity.this); 
			progressDialog.setMessage("游戏初始化中...");  
			progressDialog.show();  
			super.onPreExecute();
		}
		
		protected void onCancelled() {
			Log.i(TAG, "初始化游戏任务被取消...");
			super.onCancelled();
		}
	}

 	
	//在时间模式下控制答题时间线程类CountDownTimer
	class ControlCountDownTimer extends AdvancedCountdownTimer{
		private long countDownTime;//间隔时间单位ms
		private long totalTime; //总时间单位s
		private long currentTime;//当前倒计时到第几秒
		private long wasteTimes;//当前消耗时间单位s
		
		public ControlCountDownTimer(long millisInFuture, long countDownInterval) {			
			super(millisInFuture, countDownInterval);			
			this.countDownTime = countDownInterval;
			this.totalTime = millisInFuture/countDownInterval;
		}

		public void onFinish() {
			Log.i(TAG, "时间模式下倒计时>>onFinish>>结束倒计时,答题结束...");			
        	remainTime.setText(String.valueOf(currentTime-1));
        	wasteTime.setText(String.valueOf(wasteTimes+1));
        	game.setGameDate(new Date());//设置游戏完成时间
        	finishGame();  
		}

		public void onTick(long millisUntilFinished, int percent) {
			currentTime = millisUntilFinished/this.countDownTime;
			Log.i(TAG, "时间模式下倒计时>>onTick>>"+currentTime);			
        	remainTime.setText(String.valueOf(currentTime));
        	wasteTimes = totalTime - currentTime;
        	wasteTime.setText(String.valueOf(wasteTimes));
		}
		
	}	
	
	//固定题数模式下答题耗费时间线程
	class AnswerTime extends Thread{
		private int status = 2;//状态(暂停0，恢复1,运行2，停止3)
		private int time = 0;
		private int pauseTime = 0;

		public void run() {			
			if(status == 1) time = pauseTime;
			if(status == 3){
				ansTimeHandler.removeCallbacks(this);
				return;
			}
			while(status != 3){ //非停止
				if(status != 0){ //非暂停
					Message msg = new Message();
					msg.arg1 = time++;					
					ansTimeHandler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}					
				}
			}
			super.run();
		}		
		public void restart(){
			status = 1;
		}				
		public void pause() {
			pauseTime = time;
			status = 0;
		}
		public void cancel() {
			status = 3;
		}				
	}
	
	//在固定题数模式下控制答题时间Handler
	private Handler ansTimeHandler = new Handler(){
		public void handleMessage(Message msg) {
			Log.i(TAG, "固定题数模式耗时:"+msg.arg1+"...");
			wasteTime.setText(String.valueOf(msg.arg1));
			game.setGameTime(msg.arg1);//设置游戏耗时
		}
	};
		
	//换卡片线程
	class ChangeCardThread extends Thread{
		private Subject sub;
		public ChangeCardThread(Subject sub){
			this.sub = sub;
		}
		public void run() {
			//MainActivity.this.changedCard = false;
			for(int i=0;i<sub.getSubNums().length;i++){
				try {
					Thread.sleep(500);
					Message msg = new Message();
					msg.obj = sub;
					msg.arg1 = i;
					//changeCardHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//MainActivity.this.changedCard = true;
			super.run();
		}
	}
	
	//很卡片更新UI
/*	private Handler changeCardHandler = new Handler(){
		public void handleMessage(Message msg) {
			Subject sub = (Subject)msg.obj;
			Integer [] nums = sub.getSubNums();
			Integer [] numTypes = sub.getNumTypes();
			switch (msg.arg1) {
			case 0:
				firstCard.setImageResource(setCard(nums[0],numTypes[0]));				
				break;
			case 1:
				secondCard.setImageResource(setCard(nums[1],numTypes[1]));				
				break;
			case 2:
				thirdCard.setImageResource(setCard(nums[2],numTypes[2]));				
				break;
			case 3:
				fourCard.setImageResource(setCard(nums[3],numTypes[3]));				
				break;
			default:
				break;
			}			
		};
	};*/
	
	/*//计算题目答案
	class CalculateThread extends Thread{		
		private Integer subIndex;
		private boolean stop = false;
		
		public CalculateThread(Integer subIndex){
			this.subIndex = subIndex;
			Log.i(TAG, "开启一个新的计算题目答案线程,线程ID:"+this.getId()+"题目:"+(this.subIndex+1)+"...");
		}
		
		public void run() {
			if(stop){
				Log.i(TAG, "计算题目答案线程开始计算答案>>题目:"+(this.subIndex+1));			
				Subject sub = MainActivity.this.game.getGameSub().get(this.subIndex);
				Integer [] nums = sub.getSubNums();
				List<String> exps = TwentyFour.getExpression(nums);
				sub.setSubRightExp(exps);
				sub.setSubCalculated(true);
				Log.i(TAG, "计算题目答案线程计算答案完毕>>题目:"+(this.subIndex+1));				
			}
			super.run();
		}
		
		//控制线程
		public void cancel(boolean stop) {
			this.stop = stop;
		}
	}*/
	
	
	//计算题目答案 -- 便于手动停止(未使用)
	class CalculateTask extends AsyncTask<Integer,Integer,Integer>{		
		private Integer calSubIndex;//被计算的题目索引	
			
		protected Integer doInBackground(Integer... params) {
			calSubIndex = game.getCurrentSubIndex();
			Log.i(TAG, "计算题目答案任务开始计算答案>>题目:"+calSubIndex);
			//当前题目
			Subject sub = MainActivity.this.game.getGameSub().get(calSubIndex-1);
			Integer [] nums = sub.getSubNums();
			Log.i(TAG, "计算随机数---->"+nums[0]+"-"+nums[1]+"-"+nums[2]+"-"+nums[3]);
			List<String> exps = TwentyFour.getExpression(nums);
			sub.setSubRightExp(exps);
			sub.setSubCalculated(true);
			Log.i(TAG, "计算题目答案任务计算答案完毕>>题目:"+calSubIndex);
			return 1;//计算结束
		}
				
		protected void onCancelled() {			
			Log.i(TAG, "计算题目答案任务临时被取消了..,被取消题目:"+calSubIndex);
			super.onCancelled();
		}
	}
	
	//------------------更新牌------------------------------------------//
	private int setCard(int randomNum,int type){
		Log.i(TAG, "开始更新牌>点数>"+randomNum+"-花色>"+type);
		String imageName = "";
		switch (type) {
		case 1:
			imageName +="dnd_";
			break;
		case 2:
			imageName +="clb_";
			break;
		case 3:
			imageName +="hrt_";
			break;
		case 4:
			imageName +="spd_";
			break;
		default:
			break;
		}
		if(randomNum<10) 
			imageName += "0";
		imageName +=randomNum;
		Log.i(TAG, "随机产生的图片:"+imageName);
		return this.getResources().getIdentifier(imageName,
				"drawable", "com.education101.math.twentyfour");
	}
}
