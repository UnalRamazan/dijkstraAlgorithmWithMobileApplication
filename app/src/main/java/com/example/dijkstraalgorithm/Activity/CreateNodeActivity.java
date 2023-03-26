package com.example.dijkstraalgorithm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dijkstraalgorithm.Adapter.RouteAdapter;
import com.example.dijkstraalgorithm.DataBase.NodeDataBase;
import com.example.dijkstraalgorithm.Entity.Node;
import com.example.dijkstraalgorithm.PageViewModel.PageViewModel;
import com.example.dijkstraalgorithm.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CreateNodeActivity extends AppCompatActivity {

    private String TAG = "CreateNodeActivity";
    private boolean check = false;
    private TextInputLayout from_edit_text_layout;
    private TextInputEditText from_edit_text;
    private TextInputLayout to_edit_text_layout;
    private TextInputEditText to_edit_text;
    private TextInputLayout distance_edit_text_layout;
    private TextInputEditText distance_edit_text;
    private ImageButton selected_two_way_button;
    private LinearLayout result_route_linear_layout;
    private RecyclerView routes_recycler_view;
    private LinearLayout error_no_route_linear_layout;
    PageViewModel pageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_node);

        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        from_edit_text_layout = findViewById(R.id.from_edit_text_layout);
        from_edit_text = findViewById(R.id.from_edit_text);
        to_edit_text_layout = findViewById(R.id.to_edit_text_layout);
        to_edit_text = findViewById(R.id.to_edit_text);
        distance_edit_text_layout = findViewById(R.id.distance_edit_text_layout);
        distance_edit_text = findViewById(R.id.distance_edit_text);
        selected_two_way_button = findViewById(R.id.selected_two_way_button);
        Button add_new_node_button = findViewById(R.id.add_new_node_button);
        ImageButton add_new_nodes_from_file = findViewById(R.id.add_new_nodes_from_file);
        result_route_linear_layout = findViewById(R.id.result_route_linear_layout);
        routes_recycler_view = findViewById(R.id.routes_recycler_view);
        ImageButton delete_all_nodes = findViewById(R.id.delete_all_nodes);
        error_no_route_linear_layout = findViewById(R.id.error_no_route_linear_layout);

        //Gone
        result_route_linear_layout.setVisibility(View.GONE);

        pageViewModel.getIsDelete().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                printRoute();
            }
        });

        selected_two_way_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTwoWayButton();
            }
        });

        add_new_node_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNode();
            }
        });

        add_new_nodes_from_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showErrorMessage();
            }
        });

        delete_all_nodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showErrorMessage2();
            }
        });

        printRoute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        printRoute();
    }

    private void printRoute() {

        NodeDataBase nodeDataBase = NodeDataBase.getInstance(this);
        ArrayList<Node> nodeArrayList = nodeDataBase.getNodeList();

        if (!nodeArrayList.isEmpty()) {

            error_no_route_linear_layout.setVisibility(View.GONE);
            result_route_linear_layout.setVisibility(View.VISIBLE);
            setAdapterForRoute(nodeArrayList);
        } else {
            result_route_linear_layout.setVisibility(View.GONE);
            error_no_route_linear_layout.setVisibility(View.VISIBLE);
        }
    }

    private void checkTwoWayButton() {

        if (check) {

            check = false;
            selected_two_way_button.setImageResource(R.drawable.ic_baseline_radio_button_unchecked);
        } else {

            check = true;
            selected_two_way_button.setImageResource(R.drawable.ic_baseline_radio_button_checked);
        }
    }

    private void addNewNode() {

        String from;
        String to;
        String distance;

        from = from_edit_text.getText().toString();
        to = to_edit_text.getText().toString();
        distance = distance_edit_text.getText().toString();

        if (!from.equals("") && !to.equals("") && !distance.equals("")) {

            if (from_edit_text_layout.isHelperTextEnabled()) {
                from_edit_text_layout.setHelperText("");
            }

            if (to_edit_text_layout.isHelperTextEnabled()) {
                to_edit_text_layout.setHelperText("");
            }

            if (distance_edit_text_layout.isHelperTextEnabled()) {
                distance_edit_text_layout.setHelperText("");
            }

            //add new node to data base
            NodeDataBase nodeDataBase = NodeDataBase.getInstance(this);

            if(!nodeDataBase.checkRoute(from, to)){
                nodeDataBase.addNode(from, to, Integer.parseInt(distance));
                String str = "The route has been registered";
                Toast.makeText(this, str, Toast.LENGTH_LONG).show();
            }else{

                Toast.makeText(this, getResources().getString(R.string.same_existing_route), Toast.LENGTH_LONG).show();
            }

            if (check) {

                if(!nodeDataBase.checkRoute(to, from)){
                    nodeDataBase.addNode(to, from, Integer.parseInt(distance));
                    String str = "(For two-way street) the route has been registered";
                    Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(this, getResources().getString(R.string.same_existing_route2), Toast.LENGTH_LONG).show();
                }
            }

            printRoute();
        } else {

            if (from.equals("")) {
                from_edit_text_layout.setHelperText(getResources().getString(R.string.empty_from));
            }

            if (to.equals("")) {
                to_edit_text_layout.setHelperText(getResources().getString(R.string.empty_to));
            }

            if (distance.equals("")) {
                distance_edit_text_layout.setHelperText(getResources().getString(R.string.empty_distance));
            }
        }
    }

    private void setAdapterForRoute(ArrayList<Node> nodeArrayList) {
        RouteAdapter adapter = new RouteAdapter(nodeArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        routes_recycler_view.setLayoutManager(mLayoutManager);
        routes_recycler_view.setAdapter(adapter);
    }

    private void readDhikrTxtFile() {

        try {
            String fileName = "map.txt";

            String line = "";

            InputStream inputStream = getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {

                String[] arraySplit = line.split("-");

                if (arraySplit.length == 3) {

                    com.example.dijkstraalgorithm.Entity.Node node = new com.example.dijkstraalgorithm.Entity.Node(
                            0,
                            arraySplit[0],
                            arraySplit[1],
                            Integer.parseInt(arraySplit[2])
                    );
                    addProductToDB(node);
                }
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addProductToDB(@NonNull com.example.dijkstraalgorithm.Entity.Node node){

         NodeDataBase nodeDataBase = NodeDataBase.getInstance(this);

        if(!nodeDataBase.checkRoute(node.getFrom(), node.getTo())){
            nodeDataBase.addNode(node.getFrom(), node.getTo(), node.getDistance());
        }
    }

    //show error message
    private void showErrorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateNodeActivity.this);
        builder.setTitle(getResources().getString(R.string.error_message_title));
        builder.setMessage(getResources().getString(R.string.bring_existing_route));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setNegativeButton(getResources().getString(R.string.cancel_button_name), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {

                //İptal butonuna basılınca yapılacaklar. Sadece kapanması isteniyorsa boş bırakılacak
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.done_button_name), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                readDhikrTxtFile();
                printRoute();
            }
        });

        builder.show();
    }

    //show error message
    private void showErrorMessage2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateNodeActivity.this);
        builder.setTitle(getResources().getString(R.string.error_message_title2));
        builder.setMessage(getResources().getString(R.string.delete_all_routes));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setNegativeButton(getResources().getString(R.string.cancel_button_name), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {

                //İptal butonuna basılınca yapılacaklar. Sadece kapanması isteniyorsa boş bırakılacak
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.done_button_name), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                allNode();
            }
        });

        builder.show();
    }

    private void allNode(){

        NodeDataBase nodeDataBase = NodeDataBase.getInstance(this);
        nodeDataBase.allDelete();
        printRoute();
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}