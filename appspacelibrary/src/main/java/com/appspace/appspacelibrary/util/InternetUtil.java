package com.appspace.appspacelibrary.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by siwaweswongcharoen on 3/28/2016 AD.
 */
public class InternetUtil {
    public interface CheckInternetResult {
        void checkInternetResult(boolean isInternetAvailable);
        void checkInternetResult(boolean isInternetAvailable, String message);
    }

    public static void checkInternet(AsyncHttpClient client, String apiUrl, final CheckInternetResult callback) {
        client.get(apiUrl, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
                    String str = new String(response, "UTF-8");
                    if (!str.contains("internet ok")) {
                        callback.checkInternetResult(false);
                    }
                    callback.checkInternetResult(true);
                } catch (UnsupportedEncodingException e) {
                    callback.checkInternetResult(false, e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                callback.checkInternetResult(false);
            }

            @Override
            public void onRetry(int retryNo) {
                if (retryNo >= 3) {
                    callback.checkInternetResult(false);
                }

            }
        });
    }
}
