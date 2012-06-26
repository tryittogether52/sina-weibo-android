package com.zzz.open;
import com.zzz.http.AsyncHttpClient;
import com.zzz.http.AsyncHttpResponseHandler;

public class ExampleUsage {
	
    public static void makeRequest() {
       
    	AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.google.com", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                System.out.println(response);
//                return response;
            }
        });
    }
}