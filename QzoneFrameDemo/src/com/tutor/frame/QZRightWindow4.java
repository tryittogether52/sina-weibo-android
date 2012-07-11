package com.tutor.frame;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class QZRightWindow4 extends QZRightWindowBase {
	
	public QZRightWindow4(Context context){
		super(context);
		setupViews();
	}
	public QZRightWindow4(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}
	
	private void setupViews(){
		mContentTextView.setText("应用中心");
		mContentTextView.setBackgroundColor(Color.BLUE);
		addView(mContentTextView);
	}

	@Override
	public void dosomething() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dosomething2() {
		// TODO Auto-generated method stub
		
	}
}
