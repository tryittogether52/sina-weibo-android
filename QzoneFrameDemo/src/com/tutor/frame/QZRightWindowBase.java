package com.tutor.frame;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

public abstract class QZRightWindowBase extends FrameLayout {

	public TextView mContentTextView;
	
	private LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
			LayoutParams.FILL_PARENT);
		
	public QZRightWindowBase(Context context){
		super(context);
		setupViews();
	}
	
	public QZRightWindowBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}
	
	private void setupViews(){
		mContentTextView = new TextView(getContext());
		mContentTextView.setLayoutParams(params);
	}
	
	//做些事为了扩展举例而已
	public abstract void dosomething();
	//做些事2
	public abstract void dosomething2();

}
