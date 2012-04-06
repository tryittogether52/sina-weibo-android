package com.zzz.listview;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class ListviewdemoActivity extends Activity {
    /** Called when the activity is first created. */
	 ListView mlist;
	 private lsitadapter mAdapter;
	 private String[] mdata;
	 private int mcom=-1;
	 private int m_iFirstItem = 0;
	 private int m_nScroll = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mlist=(ListView)findViewById(R.id.listview1);
        mdata=new String[20];
        for(int i=0;i<20;i++){
        	mdata[i]="lsitviwe"+(++mcom);
        	
        }

        mAdapter=new lsitadapter(this);
        mlist.setAdapter(mAdapter);
//        mlist.setOnClickListener(l)
        mlist.setOnScrollListener(mListViewScroll);
        mlist.setOnTouchListener(mListTouch);
    }
    private OnTouchListener mListTouch = new OnTouchListener() {
		int mY = -1;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mY = (int) event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				m_nScroll = (int) (event.getY() - mY);
				break;
			case MotionEvent.ACTION_UP:
				break;
			}
			return false;
		}

	};
    private OnScrollListener mListViewScroll = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			switch(scrollState){
			case OnScrollListener.SCROLL_STATE_IDLE:
				if(view.getFirstVisiblePosition() == 0&& m_iFirstItem == 0
						&& m_nScroll >= 0){
					Log.e("mcom", "shang"+mcom);
					if(mcom>=19){
						
						for(int i=0;i<20;i++){
				        	mdata[i]="lsitviwe"+(mcom--);
				        	
				        	
				        	
				        }
						
					}
					
				}
				else if(m_iFirstItem == view.getFirstVisiblePosition()
						&& m_iFirstItem + view.getVerticalFadingEdgeLength() >= view
								.getChildCount() && m_nScroll <= 0){
					Log.e("mcom", "xia"+mcom);
					if(mcom<=979){
						for(int i=0;i<20;i++){
				        	mdata[i]="lsitviwe"+(mcom++);
				        	
				        }
					}
					
				}
				m_iFirstItem = view.getFirstVisiblePosition();
				mAdapter.notifyDataSetChanged();
				break;
			case OnScrollListener.SCROLL_STATE_FLING:
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				break;
				
			
			}
			
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}
    	
    };
    class lsitadapter extends BaseAdapter{
    	LayoutInflater inflater;

		public lsitadapter(Context context) {
			// TODO Auto-generated constructor stub
			inflater=LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mdata.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mdata[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder ;
			if(convertView==null){
				convertView=inflater.inflate(R.layout.lsitview, null);
				holder = new ViewHolder();
				holder.image1=(ImageView)convertView.findViewById(R.id.icon);
				holder.text1=(TextView)convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text1.setText(mdata[position]);
			holder.image1.setImageResource(R.drawable.ic_launcher);
			return convertView;
		}
    	
    }
    static class ViewHolder{
    	TextView text1;
    	ImageView image1;
    }
}