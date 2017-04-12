package com.shedd.feedsextractor.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shedd.feedsextractor.R;
import com.shedd.feedsextractor.data.model.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by eddy on 3/18/2016.
 */
public class TweetsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Tweet> mObjects;
    private Context mContext;
    private ActionPerformedListener IActionPerformedListener;

    public TweetsRecyclerAdapter(Context context, ArrayList<Tweet> objects) {
        mContext = context;
        mObjects = objects;
    }

    public static class TweetHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView image;
        public TextView description;
        public TextView date;
        public CardView container;

        public TweetHolder(View root) {
            super(root);
            image = (SimpleDraweeView) root.findViewById(R.id.row_image);
            description = (TextView) root.findViewById(R.id.row_description);
            date = (TextView) root.findViewById(R.id.row_date);
            container = (CardView) root.findViewById(R.id.row_container);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.row_adapter_tweet, parent, false);
        return new TweetHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindTweetsHolder((TweetHolder) holder, position);
    }

    private void onBindTweetsHolder(final TweetHolder holder, final int position) {
        Tweet tweet = mObjects.get(position);


        try {
            final String TWITTER_DATE = "EEE MMM dd HH:mm:ss Z yyyy";
            String strCurrentDate = tweet.getDate();
            SimpleDateFormat format = new SimpleDateFormat(TWITTER_DATE);
            Date newDate = format.parse(strCurrentDate);
            format = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
            String date = format.format(newDate);
            holder.date.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.description.setText(Html.fromHtml(tweet.getMessage(), 0));

        } else {
            holder.description.setText(Html.fromHtml(tweet.getMessage()));
        }
        holder.image.setImageURI(tweet.getUser() != null ? Uri.parse(tweet.getUser().getImageUrl()) : Uri.parse(""));



        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IActionPerformedListener != null)
                    IActionPerformedListener.OnTweetClicked(position);
            }
        });
    }

    public Tweet getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public int getItemCount() {
        if (mObjects != null)
            return mObjects.size();
        else
            return 0;
    }

    public interface ActionPerformedListener {
        public void OnTweetClicked(int position);
    }


    public void setActionPerformedListener(ActionPerformedListener IActionPerformedListener) {
        this.IActionPerformedListener = IActionPerformedListener;
    }
}
