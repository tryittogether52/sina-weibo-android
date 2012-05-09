package com.zzz.toupiao;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;

public class VoteDialog extends Dialog{

	public VoteDialog(Context context) {
		super(context);
		initialize(context);
		// TODO Auto-generated constructor stub
	}
	
	public VoteDialog(Context context, int theme) {
		super(context, theme);
		initialize(context);
	}
	
	public VoteDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initialize(context);
	}

	private void initialize(final Context context) {
		// TODO Auto-generated method stub
		setContentView(R.layout.toupiao_layout);
		
	}

}
