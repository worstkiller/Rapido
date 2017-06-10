package com.example.rapido.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rapido.R;
import com.example.rapido.interfaces.ClickListener;
import com.example.rapido.model.RouteInfo;

import java.util.List;

/**
 * Created by Khushhal on 6/10/2017.
 */

public class RouteRecyclerViewAdapter extends RecyclerView.Adapter<RouteRecyclerViewAdapter.ViewHolder> {
    private List<RouteInfo> routeInfoList;
    private ClickListener clickListener;

    public RouteRecyclerViewAdapter(List<RouteInfo> routeInfoList, ClickListener clickListener) {
        this.routeInfoList = routeInfoList;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RouteInfo routeInfo = routeInfoList.get(position);
        holder.textViewDistance.setText(routeInfo.getDistance());
        holder.textViewTime.setText(routeInfo.getTime());
        holder.textViewTitle.setText(routeInfo.getRouteTitle());
    }

    @Override
    public int getItemCount() {
        return routeInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewTitle, textViewDistance, textViewTime;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewDistance = (TextView) itemView.findViewById(R.id.tvRouteDistance);
            textViewTime = (TextView) itemView.findViewById(R.id.tvRouteTime);
            textViewTitle = (TextView) itemView.findViewById(R.id.tvRouteTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition());
        }
    }
}
