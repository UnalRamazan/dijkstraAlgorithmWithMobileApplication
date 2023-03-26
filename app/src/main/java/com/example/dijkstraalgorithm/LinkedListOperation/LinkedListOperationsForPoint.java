package com.example.dijkstraalgorithm.LinkedListOperation;

import com.example.dijkstraalgorithm.Entity.LinkedListNodeForPoint;

public class LinkedListOperationsForPoint {

    public LinkedListNodeForPoint head;

    public LinkedListOperationsForPoint() {
        head = null;
    }

    public LinkedListNodeForPoint getHead() {
        return head;
    }

    public int add(int point) {

        LinkedListNodeForPoint newNode = new LinkedListNodeForPoint(point);
        int check = -100;

        if (comparison(point)) {

            if (head == null) {
                head = newNode;
                head.setNext(null);

                check = point;
            } else {
                LinkedListNodeForPoint walk = head;

                while (walk.getNext() != null) {
                    walk = walk.getNext();
                }

                check = point;

                walk.setNext(newNode);
                newNode.setNext(null);
            }
        }

        return check;
    }

    private boolean comparison(int point) {

        LinkedListNodeForPoint walk = head;
        boolean control = true;

        while (walk != null) {

            if (walk.getPoint() == point) {
                control = false;
                break;
            }

            walk = walk.getNext();
        }
        return control;
    }

    public void selectStartPoint(int point) {
        LinkedListNodeForPoint walk = head;

        while (walk != null) {

            if (walk.getPoint() == point) {
                walk.setEdgeTo(point);
                walk.setDistance(0);
                walk.setDetected(true);
                walk.setKnown(true);
                break;
            }
            walk = walk.getNext();
        }
    }

    public void makeDetectedPoint(int point, int edgePoint, int distance) {
        LinkedListNodeForPoint walk = head;

        while (walk != null) {

            if (walk.getPoint() == point && !walk.isDetected()) {
                walk.setEdgeTo(edgePoint);
                walk.setDistance(distance);
                walk.setDetected(true);
                break;
            }
            walk = walk.getNext();
        }
    }

    public int distancePoint(int point) {
        int distance = 0;
        LinkedListNodeForPoint walk = head;

        while (walk != null) {

            if (walk.getPoint() == point) {
                distance = walk.getDistance();
                break;
            }
            walk = walk.getNext();
        }

        return distance;
    }

    public int getMinDistanceFromDetectedPoints() {
        int distance = Integer.MAX_VALUE;
        int point = -1;

        LinkedListNodeForPoint walk = head;
        while (walk != null) {

            if (!walk.isKnown() && walk.isDetected() && distance > walk.getDistance()) {

                point = walk.getPoint();
                distance = walk.getDistance();
            }

            walk = walk.getNext();
        }

        makeKnownPoint(point);

        return point;
    }

    private void makeKnownPoint(int point) {
        LinkedListNodeForPoint walk = head;
        System.out.println("makeKnownPoint: " + point);

        while (walk != null) {

            if (walk.getPoint() == point) {
                walk.setKnown(true);
                break;
            }
            walk = walk.getNext();
        }
    }

    public void clearData(){

        LinkedListNodeForPoint walk = head;

        while(walk != null){

            walk.setEdgeTo(walk.getPoint());
            walk.setDistance(-1);
            walk.setKnown(false);
            walk.setDetected(false);
            walk = walk.getNext();
        }
    }
}