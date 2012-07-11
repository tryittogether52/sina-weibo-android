package com.tutor.frame;

import com.tutor.framedemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class QZLeftNavBar extends FrameLayout {

	public QZLeftNavBar(Context context){
		super(context);
		setupViews();
	}
	
	public QZLeftNavBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}
	
	private void setupViews(){
		final LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
		View v = mLayoutInflater.inflate(R.layout.nav_bar, null);
		addView(v);
	}

}
