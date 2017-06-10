package com.example.rapido.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rapido.R;
import com.example.rapido.model.DirectionsInfo;

import java.util.List;

/**
 * Created by Khushhal on 6/10/2017.
 */

public class DirectionsRecyclerViewAdapter extends RecyclerView.Adapter<DirectionsRecyclerViewAdapter.ViewHolder> {
    private List<DirectionsInfo> directionsInfos;
    private Context context;

    public DirectionsRecyclerViewAdapter(List<DirectionsInfo> directionsInfos, Context context) {
        this.directionsInfos = directionsInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_directions, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DirectionsInfo directionsInfo = directionsInfos.get(position);
        holder.textViewTitle.setText(directionsInfo.getTitle());
        holder.textViewDistance.setText(directionsInfo.getDistance());
        holder.imageView.setImageResource(directionsInfo.getIconId());
    }

    @Override
    public int getItemCount() {
        return directionsInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDistance;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivDirectionsIcon);
            textViewTitle = (TextView) itemView.findViewById(R.id.tvDirectionsTitle);
            textViewDistance = (TextView) itemView.findViewById(R.id.tvDirectionsDistance);
        }
    }
}
