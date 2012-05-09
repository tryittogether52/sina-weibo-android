package com.zzz.toupiao;



import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class VoteActvity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);
        
//        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        
        // See assets/res/any/layout/dialog_activity.xml for this
        // view layout definition, which is being set here as
        // the content of our screen.
        setContentView(R.layout.vote_dialog);
        
       
    }
}
