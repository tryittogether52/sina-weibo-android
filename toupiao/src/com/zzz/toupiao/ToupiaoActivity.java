package com.zzz.toupiao;

import com.zzz.image.ImageThreadLoader;
import com.zzz.toupiao.VoteResultActivity.MyHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
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
import android.widget.ListView;
import android.widget.TextView;

public class ToupiaoActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String LOG_TAG = ToupiaoActivity.class.getName();
    private ViewFlow viewFlow ;
	private String[] mdata={"ces","ces" ,"ces" ,"ces","ces"};
	Context m_context; 
	Button m_vote_button;
	Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toupiao_layout);
        m_context=this;
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
		public View getView(final int position, View convertView,final ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder ;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.main, null);
				holder = new ViewHolder();
				holder.text_content=(TextView)convertView.findViewById(R.id.ui_vote_content);
				holder.text_title=(TextView)convertView.findViewById(R.id.ui_titile_content);
				holder.imageview=(ImageView)convertView.findViewById(R.id.ui_image_gallery);
				holder.text_state=(TextView)convertView.findViewById(R.id.ui_vote_status);
				holder.button_vote=(Button)convertView.findViewById(R.id.ui_vote_button);
				holder.text_time=(TextView)convertView.findViewById(R.id.ui_vote_time);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			String imageUrl = null;
			imageUrl="http://media.npr.org/assets/img/2012/05/11/brooks_sq.jpg?t=1336748245";
			if(imageUrl!=null){
				Drawable cachedImage = imageLoader.loadImage(imageUrl,new ImageLoadListener(position, (AdapterView) parent));
				holder.imageview.setImageDrawable(cachedImage);
			}
			holder.text_content.setText("今日，关于。。。。。。。");
			holder.text_time.setText("2012-5-10");
			holder.text_state.setText("投票进行中");
			holder.button_vote.setText("投票进行中");
			holder.text_title.setText("投票内容");
			holder.button_vote.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					 VoteDialog dialog=new VoteDialog(getApplicationContext());
//					dialog.show();
//					dialog();
//					Log.e("TEST","TEST"+position);
				}
				
			});
			
			return convertView;
			
		}
		
//		public void dialog() {
//			Intent intent=new Intent();
//			intent.setClass(ToupiaoActivity.this, VoteActvity.class);
//			startActivity(intent);
//
//			 }
    	
    }

    static class ViewHolder {
    	ImageView imageview;
        TextView text_content;
        TextView text_title;
        TextView text_time;
        TextView text_state;
        Button   button_vote;
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