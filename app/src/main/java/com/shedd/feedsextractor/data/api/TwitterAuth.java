package com.shedd.feedsextractor.data.api;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 4/11/2017.
 */

public class TwitterAuth {

    private static final String URL_AUTH = "https://api.twitter.com/oauth2/token";

    private static final String CONSUMER_KEY = "VR8xO38AF7galKco7Jl5D5ZUp";
    private static final String CONSUMER_SECRET = "FzVwOViNXFcEjfPy6vO3EUOo3gZlptZRenw0AA9uzLlD9SDZ41";

    public static String autenticateRequest() {

        HttpURLConnection conn = null;
        OutputStream os;
        BufferedReader br;
        StringBuilder response = new StringBuilder("");

        try {
            URL url = new URL(URL_AUTH);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String credenciaisAcesso = CONSUMER_KEY + ":" + CONSUMER_SECRET;
            String autorizacao = "Basic " + Base64.encodeToString(credenciaisAcesso.getBytes(), Base64.NO_WRAP);
            String parametro = "grant_type=client_credentials";

            conn.addRequestProperty("Authorization", autorizacao);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.connect();

            os = conn.getOutputStream();
            os.write(parametro.getBytes());
            os.flush();
            os.close();

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            response = new StringBuilder();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            Log.d("Response code POST", String.valueOf(conn.getResponseCode()));
            Log.d("Response JSON", response.toString());

        } catch (Exception e) {
            Log.e("Error POST", Log.getStackTraceString(e));

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response.toString();
    }
}
