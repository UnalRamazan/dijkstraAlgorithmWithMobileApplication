package com.example.dijkstraalgorithm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dijkstraalgorithm.Adapter.FromPointAdapter;
import com.example.dijkstraalgorithm.Adapter.ResultAdapter;
import com.example.dijkstraalgorithm.Adapter.ToPointAdapter;
import com.example.dijkstraalgorithm.DataBase.NodeDataBase;
import com.example.dijkstraalgorithm.Entity.LinkedListNodeForPoint;
import com.example.dijkstraalgorithm.Entity.Node;
import com.example.dijkstraalgorithm.LinkedListOperation.LinkedListOperationsForPoint;
import com.example.dijkstraalgorithm.PageViewModel.PageViewModel;
import com.example.dijkstraalgorithm.R;
import com.example.dijkstraalgorithm.Utility.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private ArrayList<Node> nodeArrayList = new ArrayList<>();//Node(point-edge-point) için arraylist
    private HashMap<String, Integer> map = new HashMap<>();//key-value
    private Graph graph;//graph
    LinkedListOperationsForPoint pointList = new LinkedListOperationsForPoint();//points' attributes list

    private LinearLayout search_linear_layout;
    private TextView from_text_view;
    private RecyclerView from_recycler_view;
    private TextView to_text_view;
    private RecyclerView to_recycler_view;
    private LinearLayout result_linear_layout;
    private RecyclerView result_recycler_view;
    private TextView current_route_text_view;
    private TextView total_distance_text_view;
    private ImageView map_image_view;
    private LinearLayout error_linear_layout;

    PageViewModel pageViewModel;
    private String fromValue = "";
    private String toValue = "";

    //arraylist for recycler view
    private final ArrayList<String> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        search_linear_layout = findViewById(R.id.search_linear_layout);
        from_text_view = findViewById(R.id.from_text_view);
        from_text_view.setSelected(true);
        from_recycler_view = findViewById(R.id.from_recycler_view);
        to_text_view = findViewById(R.id.to_text_view);
        to_text_view.setSelected(true);
        to_recycler_view = findViewById(R.id.to_recycler_view);
        Button search_button = findViewById(R.id.search_button);
        result_linear_layout = findViewById(R.id.result_linear_layout);
        ImageButton add_node_image_button = findViewById(R.id.add_node_image_button);
        result_recycler_view = findViewById(R.id.result_recycler_view);
        current_route_text_view = findViewById(R.id.current_route_text_view);
        current_route_text_view.setSelected(true);
        total_distance_text_view = findViewById(R.id.total_distance_text_view);
        total_distance_text_view.setSelected(true);
        map_image_view = findViewById(R.id.map_image_view);
        error_linear_layout = findViewById(R.id.error_linear_layout);

        //Gone
        search_linear_layout.setVisibility(View.GONE);
        result_linear_layout.setVisibility(View.GONE);
        map_image_view.setVisibility(View.GONE);

        pageViewModel.getFromInput().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                fromValue = s;
                String str = getResources().getString(R.string.from) + " " + s;
                from_text_view.setText(str);
            }
        });

        pageViewModel.getToInput().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toValue = s;
                String str = getResources().getString(R.string.to) + " " + s;
                to_text_view.setText(str);
            }
        });

        add_node_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateNodeActivity.class);
                startActivity(intent);
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!fromValue.equals("") && !toValue.equals("")) {
                    dijkstraAlgorithm();
                } else {

                    if (fromValue.equals("") && toValue.equals("")) {
                        printErrorMessage("Please select the starting and end point.(From and To Point)!");
                    } else if (fromValue.equals("")) {
                        printErrorMessage("Please select the starting point.(From Point)!");
                    } else {
                        printErrorMessage("Please select the end point.(To point)!");
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        controlLayout();
    }

    //print error message
    private void printErrorMessage(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    private void controlLayout() {

        //NodeDataBase
        NodeDataBase nodeDataBase = NodeDataBase.getInstance(this);
        nodeArrayList = nodeDataBase.getNodeList();//take list for nodes from NodeDataBase

        //NodeDataBase is not empty
        if (!nodeArrayList.isEmpty()) {
            error_linear_layout.setVisibility(View.GONE);
            search_linear_layout.setVisibility(View.VISIBLE);
            map_image_view.setVisibility(View.VISIBLE);

            createGraph();//create graph
        } else {
            search_linear_layout.setVisibility(View.GONE);
            result_linear_layout.setVisibility(View.GONE);
            map_image_view.setVisibility(View.GONE);
            error_linear_layout.setVisibility(View.VISIBLE);
        }
    }

    //create hashmap
    private int createHashMap() {

        int size = 0;//for size of hashmap
        map.clear();

        for (Node node : nodeArrayList) {

            if (!map.containsKey(node.getFrom()) || map.isEmpty()) {
                map.put(node.getFrom(), size);
                points.add(node.getFrom());
                size++;
            }

            if (!map.containsKey(node.getTo()) || map.isEmpty()) {
                map.put(node.getTo(), size);
                points.add(node.getTo());
                size++;
            }
        }

        return size;
    }

    //create graph
    private void createGraph() {

        graph = new Graph((createHashMap() + 1));

        for (Node node : nodeArrayList) {

            int from = map.get(node.getFrom());
            int to = map.get(node.getTo());
            graph.addEdge(from, to);//add value to graph

            //create linkedList for points' attributes
            pointList.add(from);
            pointList.add(to);
        }

        //show to user points
        setAdapterForFromPoint(points);
        setAdapterForToPoint(points);
    }

    private void dijkstraAlgorithm() {

        String strOutput = "";

        //NodeDataBase
        NodeDataBase nodeDataBase = NodeDataBase.getInstance(this);

        int counter = 0;

        //take value from user
        int selectedValue = map.get(fromValue);
        pointList.selectStartPoint(selectedValue);//for first point

        //for first state with dijkstra's algorithm
        String str = graph.neighbors(selectedValue);//take neighbors from the graph
        String[] strSplit = str.split(" ");//divide neighbors

        for (String s : strSplit) {
            //point(next point)--point(selected point)--distance(starting point --> point(next point))
            pointList.makeDetectedPoint(Integer.parseInt(s), selectedValue, (nodeDataBase.getDistance(getKey(selectedValue), getKey(Integer.parseInt(s))) + pointList.distancePoint(selectedValue)));
        }
        strOutput += String.valueOf(selectedValue) + " ";
        counter++;

        //for while loop
        while (counter != map.size()) {

            int point = pointList.getMinDistanceFromDetectedPoints();//tespit edilmiş noktalar arasından en küçük olanı bulmak için

            if (point >= 0) {

                str = graph.neighbors(point);//komşu vertexleri buldum
                strSplit = str.split(" ");

                for (String s : strSplit) {
                    pointList.makeDetectedPoint(Integer.parseInt(s), point, (nodeDataBase.getDistance(getKey(point), getKey(Integer.parseInt(s))) + pointList.distancePoint(point)));
                }

                strOutput += String.valueOf(point) + " ";
                counter++;
            }
        }

        int toPointData = map.get(toValue);
        String result = getResultValue(selectedValue, toPointData);
        String currentRoute = getCurrentRoute(result);


        map_image_view.setVisibility(View.GONE);
        result_linear_layout.setVisibility(View.VISIBLE);
        current_route_text_view.setText(currentRoute);//print current route
        total_distance_text_view.setText(String.valueOf(getTotalDistance(toPointData)));//print total distance
        setAdapterForAllRoutes();

        //clear data in pointList
        pointList.clearData();
    }

    private int getTotalDistance(int point) {

        int totalDistance = 0;
        LinkedListNodeForPoint walk = pointList.getHead();

        while (walk != null) {

            if (walk.getPoint() == point) {

                totalDistance = walk.getDistance();
                break;
            }

            walk = walk.getNext();
        }

        return totalDistance;
    }

    //get key from hashmap
    private String getKey(int value) {

        String key = "";

        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            if (entry.getValue() == value) {
                key = entry.getKey();

                break;
            }
        }

        return key;
    }

    //get route
    private String getResultValue(int fromPoint, int toPoint) {

        String str = "";

        LinkedListNodeForPoint walk = pointList.getHead();
        while (walk != null) {

            if (fromPoint == toPoint) {
                return String.valueOf(fromPoint);
            }

            if (walk.getPoint() == toPoint) {

                str = walk.getPoint() + " " + getResultValue(fromPoint, walk.getEdgeTo());
                break;
            }

            walk = walk.getNext();
        }

        return str;
    }

    private String getCurrentRoute(String route) {
        String routeValue = "";
        String[] routeSplit = route.split(" ");

        for (int i = routeSplit.length - 1; i >= 0; i--) {

            if (i == 0) {
                routeValue += getKey(Integer.parseInt(routeSplit[i]));
            } else {
                routeValue += getKey(Integer.parseInt(routeSplit[i])) + "-> ";
            }
        }

        return routeValue;
    }

    private void setAdapterForFromPoint(ArrayList<String> arrayList) {

        FromPointAdapter fromPointAdapter = new FromPointAdapter(arrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        from_recycler_view.setLayoutManager(mLayoutManager);
        from_recycler_view.setAdapter(fromPointAdapter);

        fromPointAdapter.SetPointArrayList(arrayList);
    }

    private void setAdapterForToPoint(ArrayList<String> arrayList) {

        ToPointAdapter toPointAdapter = new ToPointAdapter(arrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        to_recycler_view.setLayoutManager(mLayoutManager);
        to_recycler_view.setAdapter(toPointAdapter);

        toPointAdapter.SetPointArrayList(arrayList);
    }

    private void setAdapterForAllRoutes() {

        ArrayList<Node> nodeArrayList = new ArrayList<>();

        LinkedListNodeForPoint walk = pointList.getHead();
        while (walk != null) {

            if (walk.getDistance() > 0) {
                Node node = new Node(0, fromValue, getKey(walk.getPoint()), walk.getDistance());

                nodeArrayList.add(node);
            }

            walk = walk.getNext();
        }

        ResultAdapter resultAdapter = new ResultAdapter(nodeArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        result_recycler_view.setLayoutManager(mLayoutManager);
        result_recycler_view.setAdapter(resultAdapter);
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public ArrayList<Node> getNodeArrayList() {
        return nodeArrayList;
    }

    public void setNodeArrayList(ArrayList<Node> nodeArrayList) {
        this.nodeArrayList = nodeArrayList;
    }

    public HashMap<String, Integer> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Integer> map) {
        this.map = map;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public String getFromValue() {
        return fromValue;
    }

    public void setFromValue(String fromValue) {
        this.fromValue = fromValue;
    }

    public String getToValue() {
        return toValue;
    }

    public void setToValue(String toValue) {
        this.toValue = toValue;
    }

    public ArrayList<String> getPoints() {
        return points;
    }
}