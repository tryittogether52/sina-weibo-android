package com.zzz.toupiao;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;

public class VoteActvity extends Activity {

	String m_check=null;//记录选择的结果
	protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.vote_dialog);
        Button m_vote=(Button)findViewById(R.id.dialog_vote_button);
        Button m_close=(Button)findViewById(R.id.dialog_colse_button);
        m_vote.setOnClickListener(voteButton);
        m_close.setOnClickListener(voteButton);
        RadioGroup m_radio=(RadioGroup)findViewById(R.id.radiogroup_vote_dialog);
        m_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
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
       
    }
	
  Button.OnClickListener voteButton = new Button.OnClickListener()
  {
    public void onClick(View view)
    {
//    	switch()
    	 if (view.getId() == R.id.dialog_colse_button) {
    		 VoteActvity.this.finish();
         }else if(view.getId() == R.id.dialog_vote_button){
        	 if(m_check!=null){
        		Intent intent=new Intent();
      			intent.setClass(VoteActvity.this, VoteResultActivity.class);
      			startActivity(intent);
      			VoteActvity.this.finish();
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
