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
	private Game   game;                    //�����е���Ϸ
	//private boolean changedCard = false;
	//private CalculateThread calThread;    //������Ŀ���߳�		
	private AnswerTime ansTimeThread;       //��ʱ�߳�
	private ControlCountDownTimer cdTimer;  //����ʱ��
	private InitTask  initGameTask;         //��ʼ����Ϸ����
	private CalculateTask calTask;          //���������
	
/*	private ImageView firstCard; //��һ�ſ�
	private ImageView secondCard;//�ڶ��ſ�
	private ImageView thirdCard; //�����ſ�
	private ImageView fourCard;  //�����ſ�
*/	private Button btnClear;    //���
	private Button btnSubmit;   //ȷ��
	private Button btnAnswer;   //�鿴��
	private Button btnNoAnswer; //�޴�
	private Button btnNextSub;  //��һ��
	private Button btnFirstNum; //��һ�������
	private Button btnSecondNum;//�ڶ��������	
	private Button btnThirdNum; //�����������
	private Button btnFourNum;  //���ĸ������
	private Button btnLeftBracket;   //�����������
	private Button btnRightBracket;  //�����������
	private Button btnAdd;           //������Ӻ�
	private Button btnSubtract;      //���������
	private Button btnMultiply;      //������˺�
	private Button btnDivide;        //���������
	private TableRow trRemainTime;   //ʣ��ʱ����(�������Ƿ�ɼ�)
	private TextView remainTime;     //ʱ��ģʽ����ʱ
	private TableRow trRemainSub;    //ʣ����Ŀ��(�������Ƿ�ɼ�)
	private TextView remainSubject;  //��Ŀģʽʣ������	
	private TextView wasteTime;      //��ʱ
	private TextView rightSubject;   //��������
	private TextView errorSubject;   //��������
	private EditText inputExp;       //���ʽ�����
		
	private ProgressDialog progressDialog;//�ȴ����ȶԻ���
	
	//--------------------------------------------------------------------------//
	//-------------------         Activity�¼�����                          ------------------------//
	//--------------------------------------------------------------------------//
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        //��ʼ�����
        initComponent(); 
        //���������
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
		KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(getPackageName());
        keyguardLock.disableKeyguard();
    }

	protected void onResume() {		
		super.onResume();
		Log.i(TAG, "MainActivity onResume...");
		//ִ�г�ʼ����Ϸ����
        this.startInitGameTask();
	}
	
	//���治�ɼ�
	protected void onPause() {		
		super.onPause();
		Log.i(TAG, "MainActivity onPause,���治�ɼ�...");
		//ͣ���ڱ�Activity�������̺߳�����
		this.stopInitGameTask();
		this.stopAnswerTimeThread();
		this.stopCalSubThread();
		this.stopControlSubTimer();
	}
    
	//�˵�
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();	    
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	//ѡ�в˵�����
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "MainActivity,�˵�Item:"+item.getItemId()+"-"+item.getTitle());
		switch (item.getItemId()) {
		case R.id.newGame:
			this.startInitGameTask();//��ʼ��ʼ����Ϸ
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
			pauseGame();//��ͣ��Ϸ
			break;
		case R.id.quit:
			closeApp();//�˳�����
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	//--------------------------------------------------------------------------//
	//-------------------         �����������/ע��ص�����/    ------------------------//
	//--------------------------------------------------------------------------//
	
	//��ʼ�����
	private void initComponent(){
		//��Ƭ
		/*firstCard = (ImageView)findViewById(R.id.firstCard);
		secondCard = (ImageView)findViewById(R.id.secondCard);
		thirdCard = (ImageView)findViewById(R.id.thirdCard);
		fourCard = (ImageView)findViewById(R.id.fourCard);*/
		//��ȡ��ť
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
        //��ȡ�ı�����
        trRemainTime = (TableRow)findViewById(R.id.trRemainTime);
        remainTime = (TextView)findViewById(R.id.remainTime);
        trRemainSub = (TableRow)findViewById(R.id.trRemainSub);
        remainSubject = (TextView)findViewById(R.id.remainSub); 
        wasteTime = (TextView)findViewById(R.id.wasteTime); 
        rightSubject = (TextView)findViewById(R.id.rightSubject);        
        errorSubject = (TextView)findViewById(R.id.errorSubject); 
        inputExp = (EditText)findViewById(R.id.inputExp);        
	}
	
	//��ť�����¼�����
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnFirstNum://��һ����
			KeyManager.setEditText(inputExp, KeyEnum.DIGIT,btnFirstNum);
			break;
		case R.id.btnSecondNum://�ڶ�����
			KeyManager.setEditText(inputExp, KeyEnum.DIGIT,btnSecondNum);
			break;
		case R.id.btnThirdNum://��������
			KeyManager.setEditText(inputExp, KeyEnum.DIGIT,btnThirdNum);
			break;
		case R.id.btnFourNum://���ĸ���
			KeyManager.setEditText(inputExp, KeyEnum.DIGIT,btnFourNum);
			break;
		case R.id.btnAdd://��
			KeyManager.setEditText(inputExp, KeyEnum.OPERATOR,btnAdd);
			break;
		case R.id.btnSubtract://��	
			KeyManager.setEditText(inputExp, KeyEnum.OPERATOR,btnSubtract);
			break;
		case R.id.btnMultiply://��
			KeyManager.setEditText(inputExp, KeyEnum.OPERATOR,btnMultiply);
			break;
		case R.id.btnDivide://��
			KeyManager.setEditText(inputExp, KeyEnum.OPERATOR,btnDivide);
			break;
		case R.id.btnLeftBracket://������
			KeyManager.setEditText(inputExp, KeyEnum.LEFT_BRACKET,btnLeftBracket);
			break;
		case R.id.btnRightBracket://������
			KeyManager.setEditText(inputExp, KeyEnum.RIGHT_BRACKET,btnRightBracket);
			break;	
		case R.id.btnSubmit: //�ύ��
			submitAnswer(inputExp.getText());
			break;
		case R.id.btnClear: //�������
			inputExp.setText("");
			btnFirstNum.setEnabled(true);
			btnSecondNum.setEnabled(true);
			btnThirdNum.setEnabled(true);
			btnFourNum.setEnabled(true);
			break;		
		case R.id.btnAnswer:  //�鿴��
			showAnswer();
			break;			
		case R.id.btnNoAnswer://�޴�
			noAnswer();
			break;
		case R.id.btnNextSub: //��һ��
			nextSub();
			break;
		default:
			break;
		}
	}
	
	//--------------------------------------------------------------------------//
	//-------------------         ���ܴ���������                                 ------------------------//
	//--------------------------------------------------------------------------//
	
	//��һ��
	private void nextSub() {
		Integer i = game.getCurrentSubIndex();
		Subject cur = game.getGameSub().get(game.getCurrentSubIndex()-1);
		if(game.getGameStatus().equals(GameStatusEnum.STOP)){
			Toast.makeText(getApplicationContext(), "��Ϸ�ѽ���...",Toast.LENGTH_SHORT).show();	
			return;
		}else if(game.getGameStatus().equals(GameStatusEnum.PAUSE)){
			Toast.makeText(getApplicationContext(), "��Ϸ����ͣ...",Toast.LENGTH_SHORT).show();	
			return;
		}else {//��Ϸ���ڽ���
			Toast.makeText(getApplicationContext(), "Jump Subject"+(i+1)+"",Toast.LENGTH_SHORT).show();
			if(cur.getSubStatus().equals(SubStatusEnum.UNANSWER)){//������������
				game.setGameSkipTime(game.getGameSkipTime()+1);
			}
		}
		if(game.getGameModel().equals(GameModelEnum.PLAY_IN_COUNT)){
			//��ȡ��ǰʣ������
			Integer count = Integer.parseInt(remainSubject.getText().toString());
			if(count == 0){ //û��ʣ����Ŀ
				Log.i(TAG, "�ڹ̶�����ģʽ�½�������..");
                MainActivity.this.btnNextSub.setText("��һ��");
				//ֹͣ��ʱ
				MainActivity.this.stopAnswerTimeThread();
				game.setGameDate(new Date());//������Ϸ���ʱ��
				finishGame();	
				return;
			}
			if(count >= 1){	//		
				if(count == 1)
					btnNextSub.setText("Complete");
				remainSubject.setText(String.valueOf(count-1));
			}			
		}
		//����Ŀ
		Subject sub = new Subject();
		game.getGameSub().add(sub);
		updateRandomNums(sub);				
		//������Ŀ����		
		game.setCurrentSubIndex(game.getCurrentSubIndex()+1);			
		//���������������ְ�ť����
		inputExp.setText("");
		btnFirstNum.setEnabled(true);
		btnSecondNum.setEnabled(true);
		btnThirdNum.setEnabled(true);
		btnFourNum.setEnabled(true);
		//�������߳̿�ʼ���㵱ǰ��Ŀ��
        MainActivity.this.startCalSubThread();//��ʼ����  
	}

	//�޴�
	private void noAnswer() {
		Log.i(TAG, "�û�ѡ����Ŀ>>"+game.getCurrentSubIndex()+"Ϊ�޴�");
		//��Ϸ״̬���
		if(game.getGameStatus().equals(GameStatusEnum.STOP)){
			Toast.makeText(getApplicationContext(), "Game Over...",Toast.LENGTH_SHORT).show();	
			return;
		}else if(game.getGameStatus().equals(GameStatusEnum.PAUSE)){
			Toast.makeText(getApplicationContext(), "Game Suspend...",Toast.LENGTH_SHORT).show();	
			return;
		}
		Subject correntSub = game.getGameSub().get(game.getCurrentSubIndex()-1);
		if(!correntSub.isSubCalculated()){
			Log.i(TAG, "��Ŀ���ڼ�����,���Ժ�...");
			Toast.makeText(getApplicationContext(), "It is under calculation, please wait...",Toast.LENGTH_SHORT).show();	
		}else{	
			List<String> exps = correntSub.getSubRightExp();
			if(exps.size() == 0){//���
				Toast.makeText(getApplicationContext(), "Congratulation��The Answer is correct...",Toast.LENGTH_SHORT).show();
				if(correntSub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
					correntSub.setSubStatus(SubStatusEnum.RIGHT);//������
					game.setGameRightTime(game.getGameRightTime()+1);
					rightSubject.setText(String.valueOf(game.getGameRightTime()));					
				}
			}else{
				Toast.makeText(getApplicationContext(), "I am sorry. The answer is wrong...",Toast.LENGTH_SHORT).show();
				if(correntSub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
					correntSub.setSubStatus(SubStatusEnum.ERROR);//������
					game.setGameErrorTime(game.getGameErrorTime()+1);
					errorSubject.setText(String.valueOf(game.getGameErrorTime()));					
				}
			}			
		}
	}

	//�鿴��
	private void showAnswer() {
		Log.i(TAG, "�û�ѡ��鿴��...");
		//��Ϸ״̬���
		if(game.getGameStatus().equals(GameStatusEnum.STOP)){
			Toast.makeText(getApplicationContext(), "Game Over...",Toast.LENGTH_SHORT).show();	
			return;
		}else if(game.getGameStatus().equals(GameStatusEnum.PAUSE)){
			Toast.makeText(getApplicationContext(), "Game Suspend...",Toast.LENGTH_SHORT).show();	
			return;
		}
		Subject correntSub = game.getGameSub().get(game.getCurrentSubIndex()-1);
		boolean calculated = correntSub.isSubCalculated();
		if(!calculated){ //���ϵͳ��δ�������
			Log.i(TAG, "��Ŀ���ڼ�����,���Ժ�...");
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
			//δ����Ͳ鿴����Ϊ���
			if(correntSub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
				correntSub.setSubStatus(SubStatusEnum.ERROR);//������
				game.setGameErrorTime(game.getGameErrorTime()+1);
				errorSubject.setText(String.valueOf(game.getGameErrorTime()));	
			}			
		}	
	}

	
	//�ύ��
	private void submitAnswer(CharSequence text) {
		Log.i(TAG, "�û����ύ��...");
		//��Ϸ״̬���
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
		//ȡ�õ�ǰ������Ŀ
		Subject correntSub = game.getGameSub().get(game.getCurrentSubIndex()-1);
		String exp = inputExp.getText().toString();	//�û�����ı��ʽ(������ʽ��ȷ�Ҳ�Ϊ��)
		boolean cmpFlag = false;
		try {
			cmpFlag = ComputeUtil.equalNum(exp, 24);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
			Toast.makeText(getApplicationContext(), "���ź�,���ʽ����...",Toast.LENGTH_SHORT).show();
			return;
		}
		if(cmpFlag){//������ȷ
			Log.i(TAG, "�û�����ı��ʽ��ϵͳ��ƥ��ɹ�>>��Ŀ:"+game.getCurrentSubIndex());
			Toast.makeText(getApplicationContext(), "Congratulation���ش���ȷ...",Toast.LENGTH_SHORT).show();
			if(correntSub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
				correntSub.setSubStatus(SubStatusEnum.RIGHT);//������
				game.setGameRightTime(game.getGameRightTime()+1);
				rightSubject.setText(String.valueOf(game.getGameRightTime()));		
			}					
		}else{
			Toast.makeText(getApplicationContext(), "���ź����𰸲���ȷ...",Toast.LENGTH_SHORT).show();
			if(correntSub.getSubStatus().equals(SubStatusEnum.UNANSWER)){
				correntSub.setSubStatus(SubStatusEnum.ERROR);//������
				game.setGameErrorTime(game.getGameErrorTime()+1);
				errorSubject.setText(String.valueOf(game.getGameErrorTime()));	
			}
		}
	} 
	
	//��Ӣ�۰�
	private void openRankList(){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(MainActivity.this, RankActivity.class);	
		MainActivity.this.startActivity(intent);
	}
	
	//�˳�Ӧ�ó���
	private void closeApp(){
		Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
        System.exit(0);
	}
	
	//����������
	private void showAbout() {
		new AlertDialog.Builder(MainActivity.this)
    	.setTitle("About")
	    .setMessage(R.string.aboutInfo)
	    .setPositiveButton("Confirm", null).show();
	}
	
	//��ͣ��Ϸ
	private void pauseGame() {
		if(game.getGameStatus().equals(GameStatusEnum.START)){
			game.setGameStatus(GameStatusEnum.PAUSE);
			//��ͣ��ʱ//ֹͣ����
			if(game.getGameModel().equals(GameModelEnum.PLAY_IN_COUNT)){
				this.pauseAnswerTimeThread();//��ͣ��ʱ
				this.stopCalSubThread();//ֹͣ������Ŀ
			}else if(game.getGameModel().equals(GameModelEnum.PLAY_IN_TIME)){				
				this.pauseControlSubTimer();//��ͣ����ʱ
				this.stopCalSubThread();//ֹͣ������Ŀ
			}
		}else if(game.getGameStatus().equals(GameStatusEnum.PAUSE)){
			game.setGameStatus(GameStatusEnum.START);
			if(game.getGameModel().equals(GameModelEnum.PLAY_IN_COUNT)){
				this.restartAnswerTimeThread();	
				this.nextSub();
			}else if(game.getGameModel().equals(GameModelEnum.PLAY_IN_TIME)){
				this.restartControlSubTimer();//�ָ�����ʱ
				this.nextSub();//������һ��
			}
		}
	}
	
	//�����������ʾ����ť�Ϳ�Ƭ	
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
		new ChangeCardThread(sub).start();//��ʼ�ı俨Ƭ
	}
	
	
	//��ɡ�������Ϸ
	private void finishGame(){    	
    	//�����¼
    	int flag = gameService.saveRecord(this,game);    	
    	//׼��������Ϸ���
    	StringBuffer sb = new StringBuffer();    	
    	sb.append("���:").append(game.getGameRightTime()).append("\n")
    	  .append("���:").append(game.getGameErrorTime()).append("\n")
    	  .append("����:").append(game.getGameSkipTime()).append("\n");
    	if(flag == 1){
    		sb.append("�ɹ��������а�.");
    	}else if(flag == -1){
    		sb.append("δ�ܽ������а�.");
    	}
    	new AlertDialog.Builder(MainActivity.this)
    	.setTitle("������Ϸ����")
	    .setMessage(sb.toString())
	    .setPositiveButton("������", new DialogInterface.OnClickListener(){ 
            public void onClick(DialogInterface dialoginterface, int i){
            	 //�ر�Ӧ�ó���
            	 MainActivity.this.closeApp();
            } 
	    })
	    .setNegativeButton("����һ��", new DialogInterface.OnClickListener(){ 
            public void onClick(DialogInterface dialoginterface, int i){
            	//ִ�г�ʼ����Ϸ����
                MainActivity.this.startInitGameTask();
            } 
	    })
	    .setNeutralButton("Ӣ�۰�", new DialogInterface.OnClickListener(){ 
            public void onClick(DialogInterface dialoginterface, int i){ 
            	MainActivity.this.openRankList();
            } 
	    }).show();
    	MainActivity.this.game.setGameStatus(GameStatusEnum.STOP);	
	}
	
		
	//--------------------------------------------------------------------------//
	//-------------------     �����߳�/���񷽷�����                                  ------------------------//
	//--------------------------------------------------------------------------//
	
	//�������߳����µ���ʱ(�̶�ʱ��ģʽ)	
	private  void startControlSubTimer(){
		this.stopControlSubTimer();
		cdTimer = new ControlCountDownTimer(Game.MAX_TIME*1000,1000);
		cdTimer.start();
	}
	
	//�رյ���ʱ��(�̶�ʱ��ģʽ)
	private  void stopControlSubTimer(){
		if(cdTimer != null)
			cdTimer.cancel();
	}
	
	//��ͣ����ʱ��(�̶�ʱ��ģʽ)
	private void pauseControlSubTimer(){
		if(cdTimer != null){
			cdTimer.pause();
		}
	}
	
	//�ָ�����ʱ��(�̶�ʱ��ģʽ)
	private void restartControlSubTimer(){
		if(cdTimer != null){
			cdTimer.resume();
		}
	}
	
	//��ʼ���̼߳�����Ŀ
	private  void startCalSubThread(){
		//this.stopCalSubThread();
		//calThread = new CalculateThread(game.getCurrentSubIndex()-1);
		//calThread.start();		
		//���м���������ȡ���ü�������
		if(calTask != null)
			calTask.cancel(true);
		calTask = new CalculateTask();
		calTask.execute(0);		
	}
	
	//�ر����ڼ�����Ŀ���߳�
	private  void stopCalSubThread(){		
		//if(calThread != null)
		//	calThread.cancel(true);
		if(calTask != null)
			calTask.cancel(true);
	}
	
	//���������ʼ����Ϸ
	private  void startInitGameTask(){		
		this.stopInitGameTask();
		initGameTask = new InitTask();
		initGameTask.execute(0);
	}
	
	//�رճ�ʼ����Ϸ����
	private  void stopInitGameTask(){
		if(initGameTask != null){
			initGameTask.cancel(true);			
		}
	}
	
	//�������߳̽��м�ʱ�������ʱ��(�̶�����ģʽ��)
	private  void startAnswerTimeThread(){
		this.stopAnswerTimeThread();
		ansTimeThread = new AnswerTime();
		ansTimeThread.start();
	}
	
	//�ر��ѿ�ʼ�ļ�ʱ�߳�(�̶�����ģʽ��)
	private void stopAnswerTimeThread(){
		if(ansTimeThread != null)
		  ansTimeThread.cancel();
	}
	
	//��ͣ�����ʱ(�̶�����ģʽ��)
	private void pauseAnswerTimeThread(){
		if(ansTimeThread != null)
		  ansTimeThread.pause();
	}
	
	//�ظ������ʱ(�̶�����ģʽ��)
	private void restartAnswerTimeThread(){
		if(ansTimeThread != null)
		  ansTimeThread.restart();
	}
	//--------------------------------------------------------------------------//
	//-------------------         ����������̴߳���                             ------------------------//
	//--------------------------------------------------------------------------//
	//��ʼ������
 	class InitTask extends AsyncTask<Integer,Integer,Subject>{

		protected Subject doInBackground(Integer... params) {
			Log.i(TAG, "��ʼ��ʼ����Ϸ...");
			//��ȡ�û�ѡ�����Ϸģʽ
			Bundle bundle = getIntent().getExtras();
	        String gameModel = (String)bundle.get("gameModel");	
	        //��������Ϸ
	        game = gameService.createGame(gameModel);
	        //�������߳̿�ʼ���㵱ǰ��Ŀ��
	        MainActivity.this.startCalSubThread();//��ʼ����
			Subject sub = game.getGameSub().get(0);
			return sub;
		}

		protected void onPostExecute(Subject result) {
			Log.i(TAG, "��ʼ����Ϸ���,��ʼ����UI...");
			//���������������ʾ
		    MainActivity.this.updateRandomNums(game.getGameSub().get(0));   
	        //������ʾʣ��ʱ�仹��ʣ������
	        if(game.getGameModel().equals(GameModelEnum.PLAY_IN_TIME)){
	        	remainTime.setText(String.valueOf(Game.MAX_TIME));
	        	trRemainTime.setVisibility(View.VISIBLE);
	        	MainActivity.this.startControlSubTimer();//�������߳̿�ʼ����ʱ��������ʾ
	        }else if(game.getGameModel().equals(GameModelEnum.PLAY_IN_COUNT)){
	        	remainSubject.setText(String.valueOf(Game.MAX_COUNT-1));
	        	trRemainSub.setVisibility(View.VISIBLE);
	        	MainActivity.this.startAnswerTimeThread();//��ʱ��ʼ
	        }
	        rightSubject.setText(String.valueOf(game.getGameRightTime()));
	        errorSubject.setText(String.valueOf(game.getGameErrorTime()));
	        //���������������ְ�ť����
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
			Log.i(TAG, "׼����ʼ����Ϸ,��ʾ�û��Ժ�...");
			progressDialog = new ProgressDialog(MainActivity.this); 
			progressDialog.setMessage("��Ϸ��ʼ����...");  
			progressDialog.show();  
			super.onPreExecute();
		}
		
		protected void onCancelled() {
			Log.i(TAG, "��ʼ����Ϸ����ȡ��...");
			super.onCancelled();
		}
	}

 	
	//��ʱ��ģʽ�¿��ƴ���ʱ���߳���CountDownTimer
	class ControlCountDownTimer extends AdvancedCountdownTimer{
		private long countDownTime;//���ʱ�䵥λms
		private long totalTime; //��ʱ�䵥λs
		private long currentTime;//��ǰ����ʱ���ڼ���
		private long wasteTimes;//��ǰ����ʱ�䵥λs
		
		public ControlCountDownTimer(long millisInFuture, long countDownInterval) {			
			super(millisInFuture, countDownInterval);			
			this.countDownTime = countDownInterval;
			this.totalTime = millisInFuture/countDownInterval;
		}

		public void onFinish() {
			Log.i(TAG, "ʱ��ģʽ�µ���ʱ>>onFinish>>��������ʱ,�������...");			
        	remainTime.setText(String.valueOf(currentTime-1));
        	wasteTime.setText(String.valueOf(wasteTimes+1));
        	game.setGameDate(new Date());//������Ϸ���ʱ��
        	finishGame();  
		}

		public void onTick(long millisUntilFinished, int percent) {
			currentTime = millisUntilFinished/this.countDownTime;
			Log.i(TAG, "ʱ��ģʽ�µ���ʱ>>onTick>>"+currentTime);			
        	remainTime.setText(String.valueOf(currentTime));
        	wasteTimes = totalTime - currentTime;
        	wasteTime.setText(String.valueOf(wasteTimes));
		}
		
	}	
	
	//�̶�����ģʽ�´���ķ�ʱ���߳�
	class AnswerTime extends Thread{
		private int status = 2;//״̬(��ͣ0���ָ�1,����2��ֹͣ3)
		private int time = 0;
		private int pauseTime = 0;

		public void run() {			
			if(status == 1) time = pauseTime;
			if(status == 3){
				ansTimeHandler.removeCallbacks(this);
				return;
			}
			while(status != 3){ //��ֹͣ
				if(status != 0){ //����ͣ
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
	
	//�ڹ̶�����ģʽ�¿��ƴ���ʱ��Handler
	private Handler ansTimeHandler = new Handler(){
		public void handleMessage(Message msg) {
			Log.i(TAG, "�̶�����ģʽ��ʱ:"+msg.arg1+"...");
			wasteTime.setText(String.valueOf(msg.arg1));
			game.setGameTime(msg.arg1);//������Ϸ��ʱ
		}
	};
		
	//����Ƭ�߳�
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
	
	//�ܿ�Ƭ����UI
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
	
	/*//������Ŀ��
	class CalculateThread extends Thread{		
		private Integer subIndex;
		private boolean stop = false;
		
		public CalculateThread(Integer subIndex){
			this.subIndex = subIndex;
			Log.i(TAG, "����һ���µļ�����Ŀ���߳�,�߳�ID:"+this.getId()+"��Ŀ:"+(this.subIndex+1)+"...");
		}
		
		public void run() {
			if(stop){
				Log.i(TAG, "������Ŀ���߳̿�ʼ�����>>��Ŀ:"+(this.subIndex+1));			
				Subject sub = MainActivity.this.game.getGameSub().get(this.subIndex);
				Integer [] nums = sub.getSubNums();
				List<String> exps = TwentyFour.getExpression(nums);
				sub.setSubRightExp(exps);
				sub.setSubCalculated(true);
				Log.i(TAG, "������Ŀ���̼߳�������>>��Ŀ:"+(this.subIndex+1));				
			}
			super.run();
		}
		
		//�����߳�
		public void cancel(boolean stop) {
			this.stop = stop;
		}
	}*/
	
	
	//������Ŀ�� -- �����ֶ�ֹͣ(δʹ��)
	class CalculateTask extends AsyncTask<Integer,Integer,Integer>{		
		private Integer calSubIndex;//���������Ŀ����	
			
		protected Integer doInBackground(Integer... params) {
			calSubIndex = game.getCurrentSubIndex();
			Log.i(TAG, "������Ŀ������ʼ�����>>��Ŀ:"+calSubIndex);
			//��ǰ��Ŀ
			Subject sub = MainActivity.this.game.getGameSub().get(calSubIndex-1);
			Integer [] nums = sub.getSubNums();
			Log.i(TAG, "���������---->"+nums[0]+"-"+nums[1]+"-"+nums[2]+"-"+nums[3]);
			List<String> exps = TwentyFour.getExpression(nums);
			sub.setSubRightExp(exps);
			sub.setSubCalculated(true);
			Log.i(TAG, "������Ŀ�������������>>��Ŀ:"+calSubIndex);
			return 1;//�������
		}
				
		protected void onCancelled() {			
			Log.i(TAG, "������Ŀ��������ʱ��ȡ����..,��ȡ����Ŀ:"+calSubIndex);
			super.onCancelled();
		}
	}
	
	//------------------������------------------------------------------//
	private int setCard(int randomNum,int type){
		Log.i(TAG, "��ʼ������>����>"+randomNum+"-��ɫ>"+type);
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
		Log.i(TAG, "���������ͼƬ:"+imageName);
		return this.getResources().getIdentifier(imageName,
				"drawable", "com.education101.math.twentyfour");
	}
}
