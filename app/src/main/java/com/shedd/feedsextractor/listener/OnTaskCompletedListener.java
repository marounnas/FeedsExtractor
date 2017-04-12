package com.shedd.feedsextractor.listener;

import com.shedd.feedsextractor.data.model.Tweet;

import java.util.ArrayList;

/**
 * Created by Admin on 4/11/2017.
 */

public interface OnTaskCompletedListener {

    public void onError(String tag, String message);

    public void onEmptyResponse(String tag, String message);

    public void onSuccess(String tag, ArrayList<Tweet> response);
}
