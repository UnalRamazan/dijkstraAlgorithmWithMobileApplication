package com.example.dijkstraalgorithm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dijkstraalgorithm.Entity.Node;
import com.example.dijkstraalgorithm.R;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private ArrayList<Node> pointsArrayList;

    public ResultAdapter(ArrayList<Node> pointsArrayList) {
        this.pointsArrayList = pointsArrayList;
    }

    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_result, viewGroup, false);
        ResultAdapter.ViewHolder holder = new ResultAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ResultAdapter.ViewHolder holder, final int position) {

        String unStr = holder.itemView.getContext().getResources().getString(R.string.un_str);

        Node pointData = pointsArrayList.get(position);

        String fromData = "";
        if (pointData.getFrom().equals("") || pointData.getFrom() == null) {
            fromData = unStr;
        } else {
            fromData = pointData.getFrom();
        }
        holder.from_point_text_view.setText(fromData);

        String toData = "";
        if (pointData.getTo().equals("") || pointData.getTo() == null) {
            toData = unStr;
        } else {
            toData = pointData.getTo();
        }
        holder.to_point_text_view.setText(toData);

        holder.distance_text_view.setText(String.valueOf(pointData.getDistance()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return pointsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView from_point_text_view;
        private final TextView distance_text_view;
        private final TextView to_point_text_view;

        public ViewHolder(View v) {
            super(v);

            from_point_text_view = v.findViewById(R.id.from_point_text_view);
            distance_text_view = v.findViewById(R.id.distance_text_view);
            to_point_text_view = v.findViewById(R.id.to_point_text_view);
        }
    }

    public ArrayList<Node> getPointsArrayList() {
        return pointsArrayList;
    }

    public void setPointsArrayList(ArrayList<Node> pointsArrayList) {
        this.pointsArrayList = pointsArrayList;
    }
}