package com.example.dijkstraalgorithm.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dijkstraalgorithm.DataBase.NodeDataBase;
import com.example.dijkstraalgorithm.Entity.Node;
import com.example.dijkstraalgorithm.PageViewModel.PageViewModel;
import com.example.dijkstraalgorithm.R;

import java.util.ArrayList;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private ArrayList<Node> nodeDataArrayList;
    PageViewModel pageViewModel;
    private Dialog dialog;

    public RouteAdapter(ArrayList<Node> nodeDataArrayList) {
        this.nodeDataArrayList = nodeDataArrayList;
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_route, viewGroup, false);
        RouteAdapter.ViewHolder holder = new RouteAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RouteAdapter.ViewHolder holder, final int position) {

        pageViewModel = new ViewModelProvider((ViewModelStoreOwner) holder.itemView.getContext()).get(PageViewModel.class);

        String unStr = holder.itemView.getContext().getResources().getString(R.string.un_str);

        Node nodeData = nodeDataArrayList.get(position);

        String fromData = "";
        if (nodeData.getFrom().equals("") || nodeData.getFrom() == null) {
            fromData = unStr;
        } else {
            fromData = nodeData.getFrom();
        }
        holder.from_route_text_view.setText(fromData);

        String toData = "";
        if (nodeData.getTo().equals("") || nodeData.getTo() == null) {
            toData = unStr;
        } else {
            toData = nodeData.getTo();
        }
        holder.to_route_text_view.setText(toData);

        holder.distance_for_route_text_view.setText(String.valueOf(nodeData.getDistance()));

        holder.update_route_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu(v, nodeData.getId(), nodeData.getFrom(), nodeData.getTo());
            }
        });

        holder.delete_route_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showErrorMessage(holder, v, nodeData.getId());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return nodeDataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView from_route_text_view;
        private final TextView distance_for_route_text_view;
        private final TextView to_route_text_view;
        private final Button update_route_button;
        private final Button delete_route_button;

        public ViewHolder(View v) {
            super(v);

            from_route_text_view = v.findViewById(R.id.from_route_text_view);
            from_route_text_view.setSelected(true);
            distance_for_route_text_view = v.findViewById(R.id.distance_for_route_text_view);
            distance_for_route_text_view.setSelected(true);
            to_route_text_view = v.findViewById(R.id.to_route_text_view);
            to_route_text_view.setSelected(true);
            update_route_button = v.findViewById(R.id.update_route_button);
            delete_route_button = v.findViewById(R.id.delete_route_button);
        }
    }

    //show error message
    private void showErrorMessage(ViewHolder holder, View view, int idData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(holder.itemView.getContext().getResources().getString(R.string.error_message_title2));
        builder.setMessage(holder.itemView.getContext().getResources().getString(R.string.delete_route));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setNegativeButton(holder.itemView.getContext().getResources().getString(R.string.cancel_button_name), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //İptal butonuna basılınca yapılacaklar. Sadece kapanması isteniyorsa boş bırakılacak
            }
        });

        builder.setPositiveButton(holder.itemView.getContext().getResources().getString(R.string.done_button_name), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                NodeDataBase nodeDataBase = NodeDataBase.getInstance(view.getContext());
                nodeDataBase.delete(idData);
                pageViewModel.setIsDelete("Delete");
            }
        });

        builder.show();
    }

    public void popupMenu(View view, int id, String from, String to) {

        dialog = new Dialog(view.getContext(), R.style.dialog);
        dialog.setContentView(R.layout.popup_menu);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);

        TextView popup_menu_edit_text = dialog.findViewById(R.id.popup_menu_edit_text);
        TextView popup_menu_information_box = dialog.findViewById(R.id.popup_menu_information_box);

        Button popup_menu_save_button = dialog.findViewById(R.id.popup_menu_save_button);

        popup_menu_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String distance = popup_menu_edit_text.getText().toString();

                if (!distance.equals("")) {

                    NodeDataBase nodeDataBase = NodeDataBase.getInstance(view.getContext());
                    nodeDataBase.updateNode(id, from, to, Integer.parseInt(distance));
                    dialog.dismiss();

                    pageViewModel.setIsDelete("Delete");
                    String str = "The route has been updated";
                    Toast.makeText(view.getContext(), str, Toast.LENGTH_LONG).show();

                } else {
                    String str = "Distance cannot be empty. Please try again.";
                    popup_menu_information_box.setText(str);
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });

        // Geri tuşuyla kapatılması engelleniyor
        //loginDialog.setCancelable(false);

        // Popup dışına tıklanarak kapatılması engelleniyor
        //loginDialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }

    public ArrayList<Node> getNodeDataArrayList() {
        return nodeDataArrayList;
    }

    public void setNodeDataArrayList(ArrayList<Node> nodeDataArrayList) {
        this.nodeDataArrayList = nodeDataArrayList;
    }
}