package com.zzz.open;

import java.util.List;
import com.google.gson.reflect.TypeToken;
import com.zzz.http.AsyncHttpResponseHandler;
import com.zzz.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class HttpopenActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView textview=(TextView)findViewById(R.id.name);
//        textview.setText(ExampleUsage.makeRequest());
//        ExampleUsage.makeRequest();
        RequestParams params=new RequestParams();
        params.put("city", "苏州");
//        AsyncHttpResponseHandler client = new AsyncHttpResponseHandler();
        TwitterRestClient.get("getArea", params, new AsyncHttpResponseHandler(){
        	 public void onSuccess(String response) {
        		 Log.e("onSuccess", "onSuccess");
                 System.out.println(response);
                 String temp01 = response.substring(76);
     			String temp02 = temp01.substring(0,temp01.length()-9);
                 try {
					List<AreaImp> list = JSONUtils.fromJson(temp02, new TypeToken<List<AreaImp>>(){});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

             }
        	 public void onStart(){
        		 Log.e("onStart", "onStart");
        	 }
        	  public void onFailure(Throwable e, String response) {
        		  Log.e("onFailure", response);
        	  }
        	  public void onFinish() {
        		Log.e("onFinish", "onFinish");
             }
        });
        
    }
}