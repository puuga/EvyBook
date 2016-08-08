package com.appspace.appspacelibrary.manager;

import com.appspace.appspacelibrary.helper.ApiUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by siwaweswongcharoen on 1/26/2016 AD.
 */
public class LoginManager {
    public static RequestParams getParamsByFacebookGraph(JSONObject object, String facebookToken, String appId) throws JSONException {
        RequestParams params = new RequestParams();

        String newBirthDay;
        String birthDay;
        String[] temp;
        try {
            birthDay = object.getString("birthday");
            temp = birthDay.split("/");
            newBirthDay = temp[2] + "-" + temp[0] + "-" + temp[1];
        } catch (JSONException | NullPointerException e) {
            newBirthDay = "0000-00-00";
        }

        params.add("firstname", object.getString("first_name"));
        params.add("lastname", object.getString("last_name"));
        params.add("email", object.getString("email"));
        params.add("facebook_id", object.getString("id"));
        params.add("birthday", newBirthDay);
        params.add("gender", object.getString("gender"));
        params.add("facebook_token", facebookToken);
        params.add("app_id", appId);


        return params;
    }

    public interface LoginCityResult {
        void loginCityResult(int statusCode, JSONObject response);
        void loginCityResult(int statusCode, String responseString);
    }

    public static void loginCity(AsyncHttpClient client, RequestParams params, final LoginCityResult callback) {
        client.post(ApiUrl.API_LOGIN,
                params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        callback.loginCityResult(statusCode, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        callback.loginCityResult(statusCode, responseString);
                    }
                });
    }
}
