package com.vishalkuo.summon;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vishalkuo.summon.R;
import com.vishalkuo.summon.rInfo;

import java.util.List;

/**
 * Created by vishalkuo on 15-06-29.
 */
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.viewHolder> {

    private List<rInfo> resultList;
    private static Context c;

    public MenuItemAdapter(List<rInfo> foodList, Context c) {
        this.resultList = foodList;
        this.c = c;
    }

    @Override
    public MenuItemAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,
                parent, false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MenuItemAdapter.viewHolder holder, int position) {
        rInfo itemInfo = resultList.get(position);
        holder.item.setText(itemInfo.name);
        holder.description.setText(itemInfo.description);

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder{
        private TextView item;
        private TextView description;


        public viewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.description);
            item = (TextView)itemView.findViewById(R.id.title);
        }
    }
}
