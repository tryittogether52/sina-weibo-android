package com.zzz.deail;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class myScrollView extends ScrollView {

	 boolean mHasTouchDownInside;
	 View mInsideScroll;
	  boolean mInsideScrollNeedUp;
	  private ScrollViewListener scrollViewListener;
	public myScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public myScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public myScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
//	 public void scrollTo (int x, int y){
//		   super.scrollTo(x, y);
//		   Log.e("scrollTo","scrollTo");
//	    }
//
//	 protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//	        super.onSizeChanged(w, h, oldw, oldh);
//	        Log.e("onSizeChanged","onSizeChanged");
//	 }
	
	
	 public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
	  {
	  
		    int i = 0;
		    if (this.mInsideScroll == null);
		    try
		    {
		      boolean bool2 = super.dispatchTouchEvent(paramMotionEvent);
//		      i = bool2;
//		      label19: 
		    	  return bool2;
		    }
		    catch (Exception localException2)
		    {
		      while (true)
		      {
//		        break label19:
		        if (this.mInsideScroll.getVisibility() == 0)
		        {
		          int j = this.mInsideScroll.getTop() - getScrollY();
		          int k = j + this.mInsideScroll.getHeight();
		          float f = paramMotionEvent.getY();
		          if ((f > j) && (f < k))
		          {
		            if (paramMotionEvent.getAction() != 1)
		              i = 1;
//		            this.mInsideScrollNeedUp = i;
		            this.mInsideScrollNeedUp = true;
		            this.mInsideScroll.dispatchTouchEvent(paramMotionEvent);
		            i = 1;
		          }
		          if ((this.mInsideScrollNeedUp) && (paramMotionEvent.getAction() == 1))
		          {
		            this.mInsideScrollNeedUp = false;
		            this.mInsideScroll.dispatchTouchEvent(paramMotionEvent);
		          }
		        }
		        if (paramMotionEvent.getAction() == 0)
		          this.mHasTouchDownInside = true;
		        if (!this.mHasTouchDownInside)
		          i = 1;
		        if (paramMotionEvent.getAction() == 1)
		          this.mHasTouchDownInside = false;
		        try
		        {
		          boolean bool1 = super.dispatchTouchEvent(paramMotionEvent);
//		          i = bool1;
		        }
		        catch (Exception localException1)
		        {
		        }
		      }
		    }
	  }
	 
	 
	 protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
	  {
	    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
	    if (this.scrollViewListener == null)
	      return;
	    this.scrollViewListener.onScrollChanged(this, paramInt1, paramInt2, paramInt3, paramInt4);
	  }

	  public void setInsideScrollViewId(int paramInt)
	  {
	    this.mInsideScroll = findViewById(paramInt);
	  }

	  public void setScrollViewListener(ScrollViewListener paramScrollViewListener)
	  {
	    this.scrollViewListener = paramScrollViewListener;
	  }
	 
	 
	 public static abstract interface ScrollViewListener
	  {
	    public abstract void onScrollChanged(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
	  }
}
