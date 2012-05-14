package com.zzz.toupiao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;

public class VoteActvity extends Activity {

	String m_check=null;//记录选择的结果
	Handler endhandler;
	AlertDialog dlg=null; //投票成功提示对话款 
	private Runnable endRunnable;
	protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vote_dialog);
        Button m_vote=(Button)findViewById(R.id.dialog_vote_button);
        Button m_close=(Button)findViewById(R.id.dialog_colse_button);
        m_vote.setOnClickListener(voteButton);
        m_close.setOnClickListener(voteButton);
        endhandler=new Handler();
        RadioGroup m_radio=(RadioGroup)findViewById(R.id.radiogroup_vote_dialog);
        m_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {// 选择对话投票结果
				// TODO Auto-generated method stub
				if(checkedId==R.id.radioBtn1){
					m_check="0";
				}else if(checkedId==R.id.radioBtn2){
					m_check="1";
				}else if(checkedId==R.id.radioBtn3){
					m_check="2";
				}
			}
        	
        });
        
        endRunnable=new Runnable() {//投票延时
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				dlg.cancel();// 关闭对话框
				Intent intent=new Intent();
				intent.setClass(VoteActvity.this, VoteResultActivity.class);
				startActivity(intent);
				VoteActvity.this.finish();
			}
		};
       
    }
	
	
  Button.OnClickListener voteButton = new Button.OnClickListener()
  {
    public void onClick(View view)
    {

    	 if (view.getId() == R.id.dialog_colse_button) {
    		 VoteActvity.this.finish();
         }else if(view.getId() == R.id.dialog_vote_button){
        	 if(m_check!=null){
        		 AlertDialog.Builder voetbuidler = new AlertDialog.Builder(VoteActvity.this);
        		 voetbuidler.setMessage("投票成功+1!");
        		 voetbuidler.setTitle("提示");
        		 dlg = voetbuidler.create();
        		 dlg.show();
       		     endhandler.removeCallbacks(endRunnable);
       		     endhandler.postDelayed(endRunnable, 1000);     // handler 异步延时处理  		
        	 }else{
        		 AlertDialog.Builder builder = new Builder(VoteActvity.this);
        		  builder.setMessage("你没有选中任何选项!");
        		  builder.setTitle("提示");
        		  builder.setNegativeButton("确定", null);
        		  builder.create().show();
        	 }
         }
    }
  };
 
}
