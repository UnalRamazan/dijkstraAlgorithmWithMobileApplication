package com.example.dijkstraalgorithm.Entity;

public class LinkedListNodeForPoint {

    private int point;//noktanın ismi
    private int edgeTo;//geldiği noktanın ismi
    private int distance;//Başlanğınç noktası ile arasındaki mesafe
    private boolean known;//biliniyor mu
    private boolean detected;//tespit edildi mi
    private LinkedListNodeForPoint next;

    public LinkedListNodeForPoint(int point) {
        this.point = point;
        this.edgeTo = point;
        this.distance = -1;
        this.known = false;
        this.detected = false;
        this.next = null;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getEdgeTo() {
        return edgeTo;
    }

    public void setEdgeTo(int edgeTo) {
        this.edgeTo = edgeTo;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    public boolean isDetected() {
        return detected;
    }

    public void setDetected(boolean detected) {
        this.detected = detected;
    }

    public LinkedListNodeForPoint getNext() {
        return next;
    }

    public void setNext(LinkedListNodeForPoint next) {
        this.next = next;
    }
}