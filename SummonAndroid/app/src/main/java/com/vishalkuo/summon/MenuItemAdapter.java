package com.vishalkuo.summon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vishalkuo on 15-07-01.
 */
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.viewHolder> {
    private List<menuPostInfo> resultList;

    public MenuItemAdapter(List<menuPostInfo> resultList) {
        this.resultList = resultList;
    }

    @Override
    public MenuItemAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent
        , false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MenuItemAdapter.viewHolder holder, int position) {
        menuPostInfo itemInfo = resultList.get(position);
        holder.item.setText(itemInfo.name);
        holder.description.setText(itemInfo.description);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        private static TextView item;
        private static TextView description;

        public viewHolder(View itemView) {
            super(itemView);
            item = (TextView)itemView.findViewById(R.id.title);
            description = (TextView)itemView.findViewById(R.id.description);
        }
    }
}
