package com.balkarsinghdandiwal.personalorganisersystem;

/**
 * Created by User on 14/09/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper {

    public static final String IMAGE_ID = "id";
    public static final String IMAGE = "image";
    // public static final String Name = "name";
    private Context mContext;
    public DatabaseHelper mDbHelper;
    public SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Imagess";
    private static final int DATABASE_VERSION = 2;

    private static final String IMAGES_TABLE = "ImagesTable";

    private static final String CREATE_IMAGES_TABLE =
            "CREATE TABLE ImagesTable (id INTEGER PRIMARY KEY, name TEXT, image BLOB NOT NULL );";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_IMAGES_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // db.execSQL("DROP TABLE IF EXISTS " + CREATE_IMAGES_TABLE);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public DBHelper(Context ctx) {
        this.mContext = ctx;
        mDbHelper = new DatabaseHelper(mContext);
        this.mDb = mDbHelper.getWritableDatabase();
    }

    public DBHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    public void clearRecords()
    {
        mDb = mDbHelper.getWritableDatabase();
        mDb.delete(IMAGES_TABLE, null, null);
        // db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DB_TABLE + "'");
    }
    public void close() {
        mDbHelper.close();
    }

    // Insert the image to the Sqlite DB
    public void insertImage(byte[] imageBytes, String m) {
        ContentValues cv = new ContentValues();
        cv.put(IMAGE, imageBytes);
        cv.put("name", m);
        mDb.insert(IMAGES_TABLE, null, cv);
    }


    public ArrayList<String> retreivenameFromDB() {
        ArrayList<String> frndRows = new ArrayList<String>();
        String[] columns = new String[] {"name"};
        Cursor cursor = mDb.query(IMAGES_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            frndRows.add(cursor.getString(0));
            // productRows.add(Integer.toString(cursor.getInt(3)));
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return frndRows;
    }
    // Get the image from SQLite DB
    // We will just get the last image we just saved for convenience...
    public byte[] retreiveImageFromDB(String idd) {
        Cursor cur = mDb.rawQuery("SELECT image FROM ImagesTable WHERE id =" +idd , null);
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }
}