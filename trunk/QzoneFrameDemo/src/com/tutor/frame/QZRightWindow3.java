package com.tutor.frame;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class QZRightWindow3 extends QZRightWindowBase{
	
	public QZRightWindow3(Context context){
		super(context);
		setupViews();
	}
	public QZRightWindow3(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}
	
	private void setupViews(){
		mContentTextView.setText("∫√”—¡–±Ì");
		mContentTextView.setBackgroundColor(Color.GREEN);
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
