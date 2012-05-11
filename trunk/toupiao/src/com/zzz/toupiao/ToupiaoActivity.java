package com.zzz.toupiao;
import com.zzz.image.ImageThreadLoader;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ToupiaoActivity extends Activity {
    /** Called when the activity is first created. */
	
	private ViewFlow viewFlow;
	private String[] mdata={"ces","ces" ,"ces" ,"ces","ces"};
	Context m_context; 
	Button m_vote_button;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toupiao_layout);
        m_context=this;
        viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		
		final VoteAdapter voteAdapter=new VoteAdapter(this);
		viewFlow.setAdapter(voteAdapter, 0);
		m_vote_button=(Button)findViewById(R.id.framelayout_vote_button);
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
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
    
    public void dialog() {
		Intent intent=new Intent();
		intent.setClass(ToupiaoActivity.this, VoteActvity.class);
		startActivity(intent);

		 }
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		viewFlow.onConfigurationChanged(newConfig);
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
		public View getView(final int position, View convertView, ViewGroup parent) {
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
			if(imageUrl!=null){
				
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
    
    private class ImageLoadListener implements ImageThreadLoader.ImageLoadedListener {

		@Override
		public void imageLoaded(Drawable imageBitmap) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}