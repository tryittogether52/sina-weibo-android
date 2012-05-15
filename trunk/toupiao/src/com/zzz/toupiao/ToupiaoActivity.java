package com.zzz.toupiao;

import com.zzz.image.ImageThreadLoader;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ToupiaoActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String LOG_TAG = ToupiaoActivity.class.getName();
    private ViewFlow viewFlow ;
	private String[] mdata;
	Button m_vote_button;
	Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toupiao_layout);
        mdata=new String[5];
		mdata[0]="ce";
		mdata[1]="ce";
        viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		final VoteAdapter voteAdapter=new VoteAdapter(this);
		viewFlow.setAdapter(voteAdapter, 0,this);
		
		m_vote_button=(Button)findViewById(R.id.framelayout_vote_button);
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
		String IMEI=getImei();
//		 handler=new MyHandler();
		m_vote_button.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				 VoteDialog dialog=new VoteDialog(getApplicationContext());
//				dialog.show();
				dialog();
				Log.e("TEST","TEST"+viewFlow.getSelectedItemPosition());
			}
			
		});
		
    }
    
  //获取设备的唯一标示
    private  String getImei(){
    	 TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
    	  String imei= tm.getDeviceId();
    	  return imei;
    }
    
    public void setButton(int mCurrentBufferIndex) {
		// TODO Auto-generated method stub
		Log.e(LOG_TAG, "setButton");
//		 Message msg = new Message();
//		 msg.what=mCurrentBufferIndex;
//		 ToupiaoActivity.this.handler.sendMessage(msg);
    	m_vote_button.setText("已投票");
	}
    
    public void dialog() {
		Intent intent=new Intent();
		intent.setClass(ToupiaoActivity.this, VoteActvity.class);
		startActivity(intent);

		 }
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.e(LOG_TAG, "onConfigurationChanged");
		viewFlow.onConfigurationChanged(newConfig);
	}
    

	public   class MyHandler extends Handler {
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
	            m_vote_button.setText("已投票");

	        }
	    }
    
    
    public class VoteAdapter extends BaseAdapter{
    	private LayoutInflater mInflater;
    	 private final ImageThreadLoader imageLoader;
    	public VoteAdapter(Context context) {
    		   mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		   imageLoader = ImageThreadLoader.getOnDiskInstance(context);

    	}
    	
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mdata.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return  mdata[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView( int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			return drawView(position,convertView, parent);
		}
		
		private View drawView(int position, View convertView,final ViewGroup parent) {
			
			ViewHolder holder ;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.main, null);
				holder = new ViewHolder();
				holder.text_content=(TextView)convertView.findViewById(R.id.ui_vote_content);
				holder.text_title=(TextView)convertView.findViewById(R.id.ui_titile_content);
				holder.imageview=(ImageView)convertView.findViewById(R.id.ui_image_gallery);
				holder.text_state=(TextView)convertView.findViewById(R.id.ui_vote_status);
//				holder.button_vote=(Button)convertView.findViewById(R.id.ui_vote_button);
				holder.text_promble=(TextView)convertView.findViewById(R.id.ui_vote_proble);
				holder.text_time=(TextView)convertView.findViewById(R.id.ui_vote_time);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			final String o = (String) getItem(position);
			if(o!=null){
				String imageUrl = null;
//				imageUrl="http://media.npr.org/assets/img/2012/05/11/brooks_sq.jpg?t=1336748245";
				imageUrl="http://lh5.ggpht.com/_mrb7w4gF8Ds/TCpetKSqM1I/AAAAAAAAD2c/Qef6Gsqf12Y/s144-c/_DSC4374%20copy.jpg";
				if(imageUrl!=null){
					Drawable cachedImage = imageLoader.loadImage(imageUrl,new ImageLoadListener(position, (AdapterView) parent));
					holder.imageview.setImageDrawable(cachedImage);
				}
				holder.text_content.setVisibility(View.VISIBLE);
				holder.text_time.setVisibility(View.VISIBLE);
				holder.text_state.setVisibility(View.VISIBLE);
				holder.text_promble.setVisibility(View.VISIBLE);
				holder.text_title.setVisibility(View.VISIBLE);
				holder.text_content.setText("今日，关于。。。。。。。");
				holder.text_time.setText("2012-5-10");
				holder.text_state.setText("投票进行中");
//				holder.button_vote.setText("投票进行中");
				holder.text_title.setText("投票内容");
			}else{
				holder.text_content.setVisibility(View.INVISIBLE);
				holder.text_time.setVisibility(View.INVISIBLE);
				holder.text_state.setVisibility(View.INVISIBLE);
				holder.text_promble.setVisibility(View.INVISIBLE);
				holder.text_title.setVisibility(View.INVISIBLE);
				new LoadContentTask().execute(position, convertView,parent);
			}
//			holder.button_vote.setOnClickListener(new Button.OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
////					 VoteDialog dialog=new VoteDialog(getApplicationContext());
////					dialog.show();
////					dialog();
////					Log.e("TEST","TEST"+position);
//				}
//				
//			});
			
			return convertView;
			
			
		}
		
		private class LoadContentTask extends AsyncTask<Object, Object, Object> {
			
			private Integer position;
			private View view;
			private ViewGroup  parent;
			@Override
			protected void onPreExecute() {
				// 在doInBackground执行之前触发,显示进度条
//				if(!isNetworkAvailable()){
//					setNetWork();
//					return;
//				}
				super.onPreExecute();
			}
			@Override
			protected Object doInBackground(Object... arg) {
				position = (Integer) arg[0];
				view = (View) arg[1];
				parent=(ViewGroup)arg[2];
	// long-term task is here 			
				try {
					Thread.sleep(10000); // do nothing for 3000 miliseconds (3 second)
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				return null;
			}

			protected void onPostExecute(Object result) {
	// process result    	 
//				mdata[position] = (String) result;
				mdata[position]="ce";
		    	drawView(position, view,parent);

		    	view.postInvalidate();
		     }

		}	
    }

    static class ViewHolder {
    	ImageView imageview;
        TextView text_content;
        TextView text_title;
        TextView text_time;
        TextView text_state;
        TextView text_promble;
//        Button   button_vote;
      } 
    private class ButtonListener implements ViewFlow.ViewSwitchListener{

		@Override
		public void onSwitched(View view, int position) {
			// TODO Auto-generated method stub
			  Log.w(LOG_TAG, "get view item at position " +
			            position);
		}
    	
    }
    private class ImageLoadListener implements ImageThreadLoader.ImageLoadedListener {

    	 private int position;
    	 private AdapterView parent;
    	
    	 public ImageLoadListener(int position, AdapterView parent) {
    	      this.position = position;
    	      this.parent = parent;
    	    }
    	 
		@Override
		public void imageLoaded(Drawable imageBitmap) {//接口回调 更新ui
			// TODO Auto-generated method stub
			 View itemView = parent.getChildAt(position -
			          parent.getFirstVisiblePosition());
			      if (itemView == null) {
			        Log.w(LOG_TAG, "Could not find list item at position " +
			            position);
			        return;
			      }
			      ImageView img = (ImageView)
			          itemView.findViewById(R.id.ui_image_gallery);
			      if (img == null) {
			        Log.w(LOG_TAG, "Could not find image for list item at " +
			            "position " + position);
			        return;
			      }
			      Log.d(LOG_TAG, "Drawing image at position " + position);
			      img.setImageDrawable(imageBitmap);
		}
    	
    }


}