package com.example.dijkstraalgorithm.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dijkstraalgorithm.PageViewModel.PageViewModel;
import com.example.dijkstraalgorithm.R;

import java.util.ArrayList;

public class FromPointAdapter extends RecyclerView.Adapter<FromPointAdapter.PointViewHolder> {

    private final Context context;
    private ArrayList<String> pointArrayList;
    private int checkedPosition = -1;
    PageViewModel pageViewModel;

    public FromPointAdapter(ArrayList<String> pointArrayList, Context context) {
        this.context = context;
        this.pointArrayList = pointArrayList;
    }

    public void SetPointArrayList(ArrayList<String> pointArrayList) {
        this.pointArrayList = new ArrayList<>();
        this.pointArrayList = pointArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_point, parent, false);
        return new PointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointViewHolder holder, int position) {
        pageViewModel = new ViewModelProvider((ViewModelStoreOwner) holder.itemView.getContext()).get(PageViewModel.class);
        holder.bind(pointArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return pointArrayList.size();
    }

    class PointViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout point_edge_linear_layout;
        private final LinearLayout point_linear_layout;
        private final TextView point_text_view;

        public PointViewHolder(@NonNull View itemView) {
            super(itemView);

            point_edge_linear_layout = itemView.findViewById(R.id.point_edge_linear_layout);
            point_linear_layout = itemView.findViewById(R.id.point_linear_layout);
            point_text_view = itemView.findViewById(R.id.point_text_view);
        }

        void bind(final String point) {

            if (checkedPosition == -1) {

                point_edge_linear_layout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                point_linear_layout.setBackgroundColor(Color.parseColor("#2196f3"));
                point_text_view.setTextColor(Color.parseColor("#FFFFFFFF"));
            } else {

                if (checkedPosition == getAdapterPosition()) {
                    point_edge_linear_layout.setBackgroundColor(Color.parseColor("#2196f3"));
                    point_linear_layout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    point_text_view.setTextColor(Color.parseColor("#2196f3"));
                } else {
                    point_edge_linear_layout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    point_linear_layout.setBackgroundColor(Color.parseColor("#2196f3"));
                    point_text_view.setTextColor(Color.parseColor("#FFFFFFFF"));
                }
            }

            point_text_view.setText(point);

            point_linear_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    point_edge_linear_layout.setBackgroundColor(Color.parseColor("#2196f3"));
                    point_linear_layout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    point_text_view.setTextColor(Color.parseColor("#2196f3"));

                    pageViewModel.setFromInput(point);

                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }
}