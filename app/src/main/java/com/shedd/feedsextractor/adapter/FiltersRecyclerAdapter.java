package com.shedd.feedsextractor.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shedd.feedsextractor.R;
import com.shedd.feedsextractor.data.model.cache.Filter;

import java.util.ArrayList;

public class FiltersRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Filter> mObjects;
    private Context mContext;
    private ActionPerformedListener IActionPerformedListener;
    private LongClickPerformedListener ILongClickPerformedListener;

    public FiltersRecyclerAdapter(Context context, ArrayList<Filter> objects) {
        this.mContext = context;
        this.mObjects = objects;
    }

    public ArrayList<Filter> getItems() {
        return mObjects;
    }

    public static class FilterHolder extends RecyclerView.ViewHolder {
        public CardView container;
        public TextView name;

        public FilterHolder(View root) {
            super(root);
            container = (CardView) root.findViewById(R.id.row_container);
            name = (TextView) root.findViewById(R.id.row_filter_name);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.row_adapter_filter, parent, false);
        return new FilterHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindTweetsHolder((FilterHolder) holder, position);
    }

    private void onBindTweetsHolder(final FilterHolder holder, final int position) {
        Filter filter = mObjects.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            holder.name.setText(Html.fromHtml(filter.getName(), 0));
        else
            holder.name.setText(Html.fromHtml(filter.getName()));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IActionPerformedListener != null) {
                    IActionPerformedListener.OnFilterClicked(position);
                }
            }
        });
        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ILongClickPerformedListener != null) {
                    ILongClickPerformedListener.OnRemoveFilter(position);
                    return false;
                }
                return true;
            }
        });
    }

    public Filter getItem(int position) {
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
        void OnFilterClicked(int position);
    }

    public interface LongClickPerformedListener {
        void OnRemoveFilter(int position);
    }

    public void setActionPerformedListener(ActionPerformedListener IActionPerformedListener) {
        this.IActionPerformedListener = IActionPerformedListener;
    }

    public void setLongClickPerformedListener(LongClickPerformedListener ILongClickPerformedListener) {
        this.ILongClickPerformedListener = ILongClickPerformedListener;
    }

}
