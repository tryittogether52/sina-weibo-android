package com.zzz.toupiao;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VoteResultActivity extends Activity{
	 TextView result_one;
	 TextView result_two;
	 TextView result_three;
	 TextView result_three_number;
	 TextView result_two_number;
	 TextView result_one_number;
	 Handler handler;
//	 = new Handler(); 
	protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vote_result_dialog);
        Button close_button=(Button)findViewById(R.id. vote_result_colse_button);//¹Ø±Õ
        close_button.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VoteResultActivity.this.finish();
			}
        	
        });
        result_one=(TextView)findViewById(R.id.text_pre_one);
        result_one.setBackgroundColor(Color.RED);
//        result_one.setWidth(79*2);
         result_one_number=(TextView)findViewById(R.id.text_pre_one_number);
        result_one_number.setText("79%");
        result_two=(TextView)findViewById(R.id.text_pre_two);
        result_two.setBackgroundColor(Color.GREEN);
//        result_two.setWidth(15*2);
         result_two_number=(TextView)findViewById(R.id.text_pre_two_number);
        result_two_number.setText("15%");
        result_three=(TextView)findViewById(R.id.text_pre_three);
        result_three.setBackgroundColor(Color.BLUE);
//        result_three.setWidth(6*2);
         result_three_number=(TextView)findViewById(R.id.text_pre_three_number);
//        result_three_number.setText("6%");
//        result_three_number.
        handler=new MyHandler();
        RunnerVote m_runnerVoet= new RunnerVote( );
        Thread t = new Thread(m_runnerVoet); 
        t.start();
        
	 }
	
	   class MyHandler extends Handler {
	        public MyHandler() {
	        }

	        public MyHandler(Looper L) {
	            super(L);
	        }

	        @Override
	        public void handleMessage(Message msg) {
	            // TODO Auto-generated method stub
	            Log.d("MyHandler", "handleMessage......");
	            super.handleMessage(msg);
	            int tmp1=0,tmp2=0,tmp3=0;
	            result_one.setWidth( (int) Math.ceil((79/100.0)*(msg.what+1)*3));
	            result_two.setWidth((int)Math.ceil((15/100.0)*(msg.what+1)*3)); 
				result_three.setWidth((int) Math.ceil((6/100.0)*(msg.what+1)*3));
	            if(msg.what%10==9)
	            {   tmp1=(int)(79/100.0*(msg.what+1));
		            result_one_number.setText(tmp1+"%");
		            tmp2=(int)(15/100.0*(msg.what+1));
					 result_two_number.setText(tmp2+"%");
					 tmp3=(int)(6/100.0*(msg.what+1));
					 result_three_number.setText(tmp3+"%");
	            }
				
				
				

	        }
	    }
	class RunnerVote implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for(int i=0;i<100;i++){
//				 result_one.setWidth(79/7*(i+1)*2);
//				 result_two.setWidth(15/7*(i+1)*2);
//				 result_three.setWidth(6/7*(i+1)*2);
				 Message msg = new Message();
				 msg.what=i;
				 VoteResultActivity.this.handler.sendMessage(msg); 
				Log.e("...","...");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	

}
