package com.example.dijkstraalgorithm.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dijkstraalgorithm.Entity.Node;

import java.util.ArrayList;

public class NodeDataBase extends SQLiteOpenHelper {

    private static NodeDataBase dataBaseInstance = null;
    private final static String databaseName = "NodeDB";
    private final static int databaseVersionNumber = 1;
    private String NODE_TABLE = "Node";

    //singleton
    public synchronized static NodeDataBase getInstance(Context context) {
        if (dataBaseInstance == null) {
            dataBaseInstance = new NodeDataBase(context.getApplicationContext());
        }
        return dataBaseInstance;
    }

    public NodeDataBase(Context context) {
        super(context, databaseName, null, databaseVersionNumber);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createQuery = "CREATE TABLE " + NODE_TABLE + " ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " fromNode TEXT,"
                + " toNode TEXT,"
                + " distance INTEGER"
                + " )";

        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("ALTER TABLE " + NODE_TABLE + " ADD COLUMN age INTEGER");
    }

    @SuppressLint("Range")
    public Node getNode(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                NODE_TABLE,
                null,
                "id= ? ",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        Node node = null;

        if (cursor.moveToFirst()) {
            node = new Node(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("fromNode")),
                    cursor.getString(cursor.getColumnIndex("toNode")),
                    cursor.getInt(cursor.getColumnIndex("distance"))
            );
        }

        cursor.close();
        sqLiteDatabase.close();
        return node;
    }

    @SuppressLint("Range")
    public ArrayList<Node> getNodeList() {

        ArrayList<Node> nodeArrayList = new ArrayList<Node>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                NODE_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Node nodeList;

        if (cursor.moveToFirst()) {
            do {
                nodeArrayList.add(
                        new Node(
                                cursor.getInt(cursor.getColumnIndex("id")),
                                cursor.getString(cursor.getColumnIndex("fromNode")),
                                cursor.getString(cursor.getColumnIndex("toNode")),
                                cursor.getInt(cursor.getColumnIndex("distance"))
                        )
                );
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return nodeArrayList;
    }

    public void updateNode(int id, String from, String to, int distance) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("fromNode", from);
        contentValues.put("toNode", to);
        contentValues.put("distance", distance);

        sqLiteDatabase.update(
                NODE_TABLE,
                contentValues,
                "id= ? ",
                new String[]{
                        String.valueOf(id)
                }
        );

        sqLiteDatabase.close();
    }

    public void addNode(String from, String to, int distance) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("fromNode", from);
        contentValues.put("toNode", to);
        contentValues.put("distance", distance);
        sqLiteDatabase.insertOrThrow(NODE_TABLE, null, contentValues);

        sqLiteDatabase.close();
    }

    public boolean checkRoute(String from, String to) {
        boolean result = false;

        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Node", null);

            int fromData = cursor.getColumnIndex("fromNode");
            int toData = cursor.getColumnIndex("toNode");

            while (cursor.moveToNext()) {
                if (cursor.getString(fromData).equals(from) && cursor.getString(toData).equals(to)) {
                    result = true;
                    break;
                }
            }

            cursor.close();
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void delete(int idValue) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(
                NODE_TABLE,
                "id= ? ",
                new String[]{
                        String.valueOf(idValue)
                }
        );

        sqLiteDatabase.close();
    }

    public void allDelete() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM Node");
        sqLiteDatabase.close();
    }

    public int getDistance(String from, String to) {
        int result = 0;

        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Node", null);

            int fromData = cursor.getColumnIndex("fromNode");
            int toData = cursor.getColumnIndex("toNode");
            int distance = cursor.getColumnIndex("distance");

            while (cursor.moveToNext()) {

                if (cursor.getString(fromData).equals(from) && cursor.getString(toData).equals(to)) {
                    result = Integer.parseInt(cursor.getString(distance));
                    break;
                }
            }

            cursor.close();
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getNODE_TABLE() {
        return NODE_TABLE;
    }

    public void setNODE_TABLE(String NODE_TABLE) {
        this.NODE_TABLE = NODE_TABLE;
    }
}