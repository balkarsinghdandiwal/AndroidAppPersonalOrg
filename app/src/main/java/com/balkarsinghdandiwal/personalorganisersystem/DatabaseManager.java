package com.balkarsinghdandiwal.personalorganisersystem;

/**
 * Created by User on 3/09/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseManager  {
    public static final String DB_NAME = "shopping";
    public static final String DB_TABLE = "products";
    public static final int DB_VERSION = 2;
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (id INTEGER PRIMARY KEY,fname Text,lname TEXT,dob TEXT,sex TEXT,address TEXT);";
    private SQLHelper helper;
    public SQLiteDatabase db;
    private Context context;



    public DatabaseManager(Context c) {
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();

    }

    public DatabaseManager openReadable() throws android.database.SQLException {
        helper = new SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public boolean addRow(String c, String d, String b, String f, String g) {
        synchronized(this.db) {

            ContentValues newProduct = new ContentValues();
            newProduct.put("fname", c);
            newProduct.put("lname", d);
            newProduct.put("dob", b);
            newProduct.put("sex", f);
            newProduct.put("address", g);
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

    public boolean updateData(String c, String d, String b, String f, String g, String i) {
        synchronized(this.db) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("fname", c);
            contentValues.put("lname", d);
            contentValues.put("dob", b);
            contentValues.put("sex", f);
            contentValues.put("address", g);
            db.update(DB_TABLE, contentValues, "id = ?", new String[]{i});
            return true;
        }
    }

    public boolean deleteData (String c, String d, String b, String f, String g, String i) {
        // int mx=-1;
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE, "id = ?", new String[] {i});
        // db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DB_TABLE + "'");
        return true;
    }

    public ArrayList<String> retrieveRows() {
        ArrayList<String> productRows = new ArrayList<String>();
        String[] columns = new String[] {"fname", "lname", "dob", "sex", "address"};
        Cursor cursor = db.query(DB_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            productRows.add(cursor.getString(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3)+ ", " + cursor.getString(4));
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
        String[] columns = new String[] {"fname"};
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
            Log.w("Products table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TALBE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}




