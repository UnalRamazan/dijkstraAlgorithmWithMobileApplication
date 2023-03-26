package com.example.dijkstraalgorithm.Utility;

public class Graph {

    private int numberOfVertices = 0;
    private int numberOfEdges = 0;
    private int[][] adjMatrix = null;

    public Graph(int size) {
        adjMatrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                adjMatrix[i][j] = 0;
            }
        }

        numberOfVertices = size;
    }

    public void addEdge(int from, int to) {
        adjMatrix[from][to] = 1;
        numberOfEdges++;
    }

    public int degree(int vertexNo) {
        int degree = 0;
        for (int i = 0; i < numberOfVertices; i++) {

            if (adjMatrix[vertexNo][i] == 1) {
                degree++;
            }
        }

        return degree;
    }

    public boolean isEdge(int v, int w) {
        boolean result = false;

        if (adjMatrix[v][w] == 1) {
            result = true;
        }

        return result;
    }

    public String neighbors(int v) {
        String str = "";
        for (int i = 0; i < numberOfVertices; i++) {

            if (adjMatrix[v][i] == 1) {
                str += i + " ";
            }
        }

        return str;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void setNumberOfVertices(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public void setNumberOfEdges(int numberOfEdges) {
        this.numberOfEdges = numberOfEdges;
    }

    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    public void setAdjMatrix(int[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
    }
}