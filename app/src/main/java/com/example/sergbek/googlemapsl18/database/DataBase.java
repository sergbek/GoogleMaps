package com.example.sergbek.googlemapsl18.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.sergbek.googlemapsl18.model.MarkerEntity;

import java.util.ArrayList;
import java.util.List;


public class DataBase extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TABLE = "marker";

    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_MARKER_TITLE = "marker_title";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_PHOTO = "photo";

    private static final String DATABASE_CREATE_SCRIPT = "create table IF NOT EXISTS "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + COLUMN_MARKER_TITLE
            + " text not null, " + COLUMN_LATITUDE + " integer, " + COLUMN_LONGITUDE + " integer, "
            + COLUMN_PHOTO + " text);";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addMarker(MarkerEntity marker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MARKER_TITLE, marker.getTitle());
        values.put(COLUMN_LATITUDE, marker.getLatitude());
        values.put(COLUMN_LONGITUDE, marker.getLongitude());
        values.put(COLUMN_PHOTO, marker.getPhoto());

        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    public List<MarkerEntity> getAllMarker() {
        List<MarkerEntity> markerList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MarkerEntity marker = new MarkerEntity();
                marker.setId(cursor.getString(0));
                marker.setTitle(cursor.getString(1));
                marker.setLatitude(cursor.getString(2));
                marker.setLongitude(cursor.getString(3));
                marker.setPhoto(cursor.getString(4));
                markerList.add(marker);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return markerList;
    }

    public int getMarkerCount() {

        String countQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public void deleteMarker(MarkerEntity marker) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, COLUMN_ID + " = ?",
                new String[] { String.valueOf(marker.getId()) });
        db.close();
    }
}
