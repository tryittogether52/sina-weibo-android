// Static wrapper library around AsyncHttpClient

package com.zzz.open;

import com.zzz.http.AsyncHttpClient;
import com.zzz.http.AsyncHttpResponseHandler;
import com.zzz.http.RequestParams;

public class TwitterRestClient {
//    private static final String BASE_URL = "http://api.twitter.com/1/";
	 private static final String BASE_URL ="http://webservice.wisesz.com/service/getPowerinfo.asmx/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}