package com.zzz.deail;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity implements  myScrollView.ScrollViewListener,
            View.OnClickListener {

	
	private myScrollView scroll;
	private RelativeLayout  toprelative;
	private ImageView  imageview;
	 View pinView1;
	  View pinView1Parent;
	  View pinView2;
	  private RelativeLayout headlayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scroll=(myScrollView)findViewById(R.id.fillsocrollview);
        toprelative=(RelativeLayout)findViewById(R.id.headlinear);
        imageview=(ImageView)findViewById(R.id.scroll_image);
        this.scroll.setScrollViewListener(this);
        this.scroll.setInsideScrollViewId(R.id.photo_container);
        this.pinView1=findViewById(R.id.price);
        headlayout=(RelativeLayout)findViewById(R.id.headlinear);
//        this.pinView2=findViewById(R.id.)
//        isscrolview.scrollTo(100, 0);
//       isscrolview.seto
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onScrollChanged(View paramView, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4) {
		// TODO Auto-generated method stub
		 int i = this.imageview.getTop() - this.scroll.getScrollY();
		 if(i<-imageview.getMeasuredHeight())
			 headlayout.setVisibility(View.VISIBLE);
		 else{
			 headlayout.setVisibility(View.GONE);
		 }
//		 Log.e("test","...."+i);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

   
    
}
