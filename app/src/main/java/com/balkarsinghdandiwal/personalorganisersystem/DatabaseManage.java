package com.balkarsinghdandiwal.personalorganisersystem;

/**
 * Created by User on 13/09/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseManage  {
    public static final String DB_NAME = "shopping";
    public static final String DB_TABLE = "event";
    public static final int DB_VERSION = 2;
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (id INTEGER PRIMARY KEY,name Text,datee TEXT,timee TEXT,address TEXT);";
    private SQLHelper helper;
    public SQLiteDatabase db;
    private Context context;



    public DatabaseManage(Context c) {
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();

    }

    public DatabaseManage openReadable() throws android.database.SQLException {
        helper = new SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public boolean addRow(String c, String d, String p, String n) {
        synchronized(this.db) {

            ContentValues newProduct = new ContentValues();
            newProduct.put("name", c);
            newProduct.put("datee", d);
            newProduct.put("timee", p);
            newProduct.put("address", n);
            try {
                db.insertOrThrow(DB_TABLE, null, newProduct);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            //db.close();
            return true;
        }
    }

    public boolean updateData(String c, String n, String p, String g, String i) {
        synchronized(this.db) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", c);
            contentValues.put("datee", n);
            contentValues.put("timee", p);
            contentValues.put("address", g);
            db.update(DB_TABLE, contentValues, "id = ?", new String[]{i});
            return true;
        }
    }

    public boolean deleteData (String c, String n, String p, String g, String i) {
        // int mx=-1;
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE, "id = ?", new String[] {i});
        // db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DB_TABLE + "'");
        return true;
    }

    public ArrayList<String> retrieveRows() {
        ArrayList<String> productRows = new ArrayList<String>();
        String[] columns = new String[] {"name", "datee", "timee", "address"};
        Cursor cursor = db.query(DB_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            productRows.add(cursor.getString(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3));
            // productRows.add(Integer.toString(cursor.getInt(3)));
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return productRows;
    }

    public ArrayList<String> retrieveRow() {
        ArrayList<String> productRows = new ArrayList<String>();
        String[] columns = new String[] {"datee"};
        Cursor cursor = db.query(DB_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            productRows.add(cursor.getString(0));
            // productRows.add(Integer.toString(cursor.getInt(3)));
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return productRows;
    }

    public void clearRecords()
    {
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE, null, null);
        // db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DB_TABLE + "'");
    }

    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper (Context c) {
            super(c, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("event table", "Upgrading database i.e. dropping table and re-creating it");
            //db.execSQL("DROP TALBE products");
            //db.execSQL("DROP TALBE IF EXISTS " + DB_TABLE);

            onCreate(db);
        }
    }
}





