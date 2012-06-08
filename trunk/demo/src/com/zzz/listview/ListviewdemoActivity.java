package com.zzz.listview;


import adam.test.MyTest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class ListviewdemoActivity extends Activity {
    /** Called when the activity is first created. */
	 ListView mlist;
	 private Listadapter mAdapter;
	 private String[] mdata;
	 private int mcom=-1;
	 private int m_iFirstItem = 0;
	 private int m_nScroll = 0;
   
    
	    private OnTouchListener mListTouch = new OnTouchListener() {
			int mY = -1;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mY = (int) event.getY();
//					Toast.makeText(ListviewdemoActivity.this, "onTouch:"+mY+" action_down", Toast.LENGTH_SHORT);
//					System.out.println("onTouch:"+mY+" action_down");
					break;
				case MotionEvent.ACTION_MOVE:
					m_nScroll = (int) (event.getY() - mY);
//					Toast.makeText(ListviewdemoActivity.this, "onTouch:"+m_nScroll+" action_move", Toast.LENGTH_SHORT);
//					System.out.println("onTouch:"+m_nScroll+" action_move");
					break;
				case MotionEvent.ACTION_UP:
//					Toast.makeText(ListviewdemoActivity.this, " ACTION_UP", Toast.LENGTH_SHORT);
//					System.out.println("onTouch:"+mY+" action_up");
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
				System.out.println("OnScrollListener!");
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
	    	
	    };
	 
	 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mlist=(ListView)findViewById(R.id.listview1);
        mdata=new String[20];
        for(int i=0;i<20;i++){
        	mdata[i]="lsitviwe"+(++mcom);
        }

        mAdapter=new Listadapter(this);
        mlist.setAdapter(mAdapter);

        mlist.setOnScrollListener(mListViewScroll);
        mlist.setOnTouchListener(mListTouch);
//      mlist.setOnClickListener(mClickListerner);
        System.out.println("Activity onCreat!");
    }
    
    public void onResume(){
    	super.onResume();
    	Log.i("Activity", "onResume");
    }
    public void onStart(){
    	super.onStart();
    	Log.i("Activity", "onStart");
    }
    public void onPause(){
    	super.onPause();
    	Log.i("Activity", "onPause");
    }
    public void onRestart(){
    	super.onRestart();
    	Log.i("Activity", "onRestart");
    }
    public void onStop(){
    	super.onStop();
    	Log.i("Activity", "onStop");
    }
    public void onDestroy(){
    	super.onDestroy();
    	Log.i("Activity", "onDestroy");
    }
    
  
    class Listadapter extends BaseAdapter{
    	LayoutInflater inflater;

		public Listadapter(Context context) {
			// TODO Auto-generated constructor stub
			inflater=LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			System.out.println("listAdapter:Count:"+mdata.length);
			return mdata.length;
		}

		@Override
		public Object getItem(int position) {
			System.out.println("listAdapter-position:"+position);
			return mdata[position];
		}

		@Override
		public long getItemId(int position) {
			System.out.println("listAdapter-getItemId:"+position);
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
				holder.btn1 = (Button)convertView.findViewById(R.id.btn);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text1.setText(mdata[position]);
			holder.image1.setImageResource(R.drawable.ic_launcher);
			holder.btn1.setText("Button2");
			setButtonListen(holder.btn1);
			System.out.println("getView!");
			return convertView;
		}
    	
    }
    public void setButtonListen(Button btn){
    	if(btn == null){return ;}
    	   btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("setButtonListen!"+v.getId());
				Toast.makeText(ListviewdemoActivity.this,"Now,you click the button!"+v.getId(), Toast.LENGTH_SHORT).show();
			}
		});
    	System.out.println("setButtonListen!"+btn.getId());
    }
    
    public void onListItemClick(){
    	System.out.println("µ÷ÓÃonListItemClick£¡");
    }
    
    static class ViewHolder{
    	TextView text1;
    	ImageView image1;
    	Button btn1;
    }
}