package com.example.dijkstraalgorithm.Entity;

import android.os.Parcelable;

public class Node {

    private int id;
    private String from;
    private String to;
    private int distance;

    public final static Parcelable.Creator<Node> CREATOR = new Parcelable.Creator<Node>() {

        @SuppressWarnings({
                "unchecked"
        })

        public Node createFromParcel(android.os.Parcel in) {
            return new Node(in);
        }

        public Node[] newArray(int size) {
            return (new Node[size]);
        }
    };

    public Node() {

    }

    protected Node(android.os.Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.from = ((String) in.readValue((String.class.getClassLoader())));
        this.to = ((String) in.readValue((String.class.getClassLoader())));
        this.distance = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Node(int id, String from, String to, int distance){
        super();
        this.id = id;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(from);
        dest.writeValue(to);
        dest.writeValue(distance);
    }

    public int describeContents() {
        return 0;
    }
}