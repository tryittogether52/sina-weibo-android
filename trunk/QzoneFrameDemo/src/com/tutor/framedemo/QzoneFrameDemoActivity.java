package com.tutor.framedemo;

import com.tutor.frame.QZLeftNavBar;
import com.tutor.frame.QZRightWindow1;
import com.tutor.frame.QZRightWindow2;
import com.tutor.frame.QZRightWindow3;
import com.tutor.frame.QZRightWindow4;
import com.tutor.frame.QZRightWindowBase;
import com.tutor.frame.QZRightWindowContainer;
import com.tutor.frame.QZRightWindowManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class QzoneFrameDemoActivity extends Activity implements OnClickListener{
    
	private QZRightWindow1 mQzRightWindow1;
	
	private QZRightWindow2 mQzRightWindow2;
	
	private QZRightWindow3 mQzRightWindow3;
	
	private QZRightWindow4 mQzRightWindow4;
	
	private QZLeftNavBar mQzLeftNavBar;
	
	private QZRightWindowContainer mQzRightWindowContainer;
	
	private QZRightWindowManager mQzRightWindowManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setupViews();
    }
    
    private void setupViews(){
    	mQzRightWindowManager = new QZRightWindowManager();
    	
    	mQzLeftNavBar = (QZLeftNavBar)findViewById(R.id.navbar);
    	
    	mQzLeftNavBar.findViewById(R.id.rw1).setOnClickListener(this);
    	mQzLeftNavBar.findViewById(R.id.rw2).setOnClickListener(this);
    	mQzLeftNavBar.findViewById(R.id.rw3).setOnClickListener(this);
    	mQzLeftNavBar.findViewById(R.id.rw4).setOnClickListener(this);
    	
    	mQzRightWindow1 = (QZRightWindow1)findViewById(R.id.qzrw1);
    	
    	mQzRightWindowContainer = (QZRightWindowContainer)findViewById(R.id.container);
    	mQzRightWindowManager.setmContainer(mQzRightWindowContainer);
    }

    private void showRightWindow(int num,QZRightWindowBase mQzRightWindowBase){
    	mQzRightWindowManager.showRightWindow(num, mQzRightWindowBase);
    }
    
	@Override
	public void onClick(View v) {		
		int id = v.getId();
		switch (id) {
		case R.id.rw1:
			showRightWindow(QZRightWindowManager.FRIEND_TRENDS_WINDOW, mQzRightWindow1);
			break;
		case R.id.rw2:
			if(mQzRightWindow2 == null){
				mQzRightWindow2 = new QZRightWindow2(this);
			}
			showRightWindow(QZRightWindowManager.HOME_PAGE_WINDOW, mQzRightWindow2);
			break;
		case R.id.rw3:
			if(mQzRightWindow3 == null){
				mQzRightWindow3 = new QZRightWindow3(this);
			}
			showRightWindow(QZRightWindowManager.FRIEND_LIST_WINDOW, mQzRightWindow3);
			break;
		case R.id.rw4:
			if(mQzRightWindow4 == null){
				mQzRightWindow4 = new QZRightWindow4(this);
			}
			showRightWindow(QZRightWindowManager.APP_CENTER_WINDOW, mQzRightWindow4);
			break;
		default:
			break;
		}
	}
}