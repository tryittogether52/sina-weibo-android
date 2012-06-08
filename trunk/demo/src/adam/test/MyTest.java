package adam.test;

import com.zzz.listview.ListviewdemoActivity;
import com.zzz.listview.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyTest extends ListActivity {
	
	private String[] strList= {"ListView1","ListView2"};
	
    @Override
	public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	
    	
          setContentView(R.layout.list_main);
//          setListAdapter(new ArrayAdapter<String>(this,
//        		  android.R.layout.simple_list_item_1,  strList));
          
    }
    
    protected void onListItemClick (ListView l, View v, int position, long id){
    	super.onListItemClick(l, v, position, id);
    	int row = (int)id;
    	switch(row){
    	case 0:
    		turnTheActivity(this,new Intent(this, ListviewdemoActivity.class));
    		break;
    	case 1:
    		break;
    	}
    	Log.i("ListItemClick", "row"+id);
    }
    
    public static void turnTheActivity(Activity curPage,Intent intent){
    	curPage.startActivity(intent);
    }
}
