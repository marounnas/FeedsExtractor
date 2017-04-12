package com.shedd.feedsextractor.data.api.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shedd.feedsextractor.R;
import com.shedd.feedsextractor.data.api.TwitterAuth;
import com.shedd.feedsextractor.data.model.Tweet;
import com.shedd.feedsextractor.listener.OnTaskCompletedListener;
import com.shedd.feedsextractor.utils.ConnectionDetector;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 4/11/2017.
 */

public class GetTweetsTask extends AsyncTask<String, Void, Void> {

    public static String TAG = "AsyncTaskGetTweets";
    private Context mContext;
    private OnTaskCompletedListener mListener;
    private ConnectionDetector cd;
    private boolean noConnection = false;
    private ArrayList<Tweet> response;

    public GetTweetsTask(Context context, HashMap<String, String> params, OnTaskCompletedListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.cd = new ConnectionDetector(mContext);
        params = (params == null) ? new HashMap<String, String>() : params;
        execute(params.get("screen_name"));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... param) {

        response = new ArrayList<>();
        HttpURLConnection conn = null;
        BufferedReader br;
        if (cd.isConnectedToInternet()) {

            try {
                URL url = new URL("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=" + param[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                String auth = TwitterAuth.autenticateRequest();
                JSONObject jsonAccess = new JSONObject(auth);
                String token = jsonAccess.getString("token_type") + " " +
                        jsonAccess.getString("access_token");

                conn.setRequestProperty("Authorization", token);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder resp = new StringBuilder();

                while ((line = br.readLine()) != null)
                    resp.append(line);

                response = new GsonBuilder().create().fromJson(resp.toString(), new TypeToken<List<Tweet>>() {
                }.getType());

            } catch (Exception e) {
                Log.e("Error GET: ", Log.getStackTraceString(e));
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
        } else
            noConnection = true;
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (noConnection) {
            mListener.onError(TAG, mContext.getString(R.string.error_no_connection));
            return;
        }
        if (response.isEmpty()) {
            mListener.onEmptyResponse(TAG, mContext.getString(R.string.no_data));
            return;
        }
        mListener.onSuccess(TAG, response);
        super.onPostExecute(aVoid);
    }
}
